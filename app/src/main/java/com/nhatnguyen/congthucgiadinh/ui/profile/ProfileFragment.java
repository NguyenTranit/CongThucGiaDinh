package com.nhatnguyen.congthucgiadinh.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.ui.Main.MainActivity;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private TextView MyEmail, MyName, txt;
    private CircleImageView image;
    private ProfilePictureView imageFace;
    private Button signOut;
    private FirebaseUser firebaseUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        signOut = root.findViewById(R.id.signOut);
        txt = root.findViewById(R.id.txt);
        MyName = root.findViewById(R.id.MyName);
        MyEmail = root.findViewById(R.id.MyEmail);
        image = root.findViewById(R.id.image);
        imageFace=root.findViewById(R.id.imageFace);
        display();
        CheckInternet();
        //
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(getContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getContext(), "Bạn đã đăng xuất tài khoản !", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), MainActivity.class));
                                // finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return root;

    }

    private void CheckInternet() {
        if (!CheckConnect.haveNetworkConnection(getActivity())) {
            CheckConnect.WarningCheckInternet(getActivity());
        }
    }


    private void GoogleInfo() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (signInAccount != null) {
              MyName.setText(signInAccount.getDisplayName());
              MyEmail.setText(signInAccount.getEmail());
            Glide.with(getContext()).load(signInAccount.getPhotoUrl()).into(image);
            image.setVisibility(View.VISIBLE);
            imageFace.setVisibility(View.GONE);
        }
    }

    private void display() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            txt.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);
            facebookInfo();
            GoogleInfo();
        } else {

            txt.setVisibility(View.VISIBLE);
            image.setImageResource(R.mipmap.slogan);
            image.setVisibility(View.VISIBLE);
            imageFace.setVisibility(View.GONE);
            MyName.setVisibility(View.GONE);
            MyEmail.setVisibility(View.GONE);
            signOut.setVisibility(View.GONE);
        }
    }

    private void facebookInfo() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                try {
                   String name = object.getString("name");
                   String email = object.getString("email");
                    MyName.setText(name);
                    MyEmail.setText(email);
                    imageFace.setProfileId(Profile.getCurrentProfile().getId());
                    imageFace.setVisibility(View.VISIBLE);
                    image.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "name,email");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

    }
}