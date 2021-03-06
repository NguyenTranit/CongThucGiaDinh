package com.nhatnguyen.congthucgiadinh.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.ui.Main.MainActivity;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 123;
    List<AuthUI.IdpConfig> providers;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        providers = Arrays.asList(

                new AuthUI.IdpConfig.FacebookBuilder().build() ,
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        showSignInOptions();
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .setLogo(R.drawable.title2)
                        .setIsSmartLockEnabled(false)
                        .setTosAndPrivacyPolicyUrls(
                "https://sites.google.com/view/chnh-sch-bo-mt-ca-ng-dng-3-ba-/trang-ch%E1%BB%A7?fbclid=IwAR3trYSX5Rbj_GpGshVq3DX3koSQRKoEXBqLyci6bH9bzcaz3TtRHAmm4Pc",
                "https://sites.google.com/view/chnh-sch-bo-mt-ca-ng-dng-3-ba-/trang-ch%E1%BB%A7?fbclid=IwAR3trYSX5Rbj_GpGshVq3DX3koSQRKoEXBqLyci6bH9bzcaz3TtRHAmm4Pc")
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                onBackPressed();
                Toast.makeText(this, "Ch??o m???ng :  " + user.getDisplayName()+". H??y l??u th???t nhi???u m??n ??n b???n y??u th??ch nh?? !", Toast.LENGTH_LONG).show();
                finish();
                return;

            } else {
                // Sign in failed
                if (response == null) {
                    Toast.makeText(this, "H???y ????ng nh???p !", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Kh??ng c?? k???t n???i Internet", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Kh??ng r?? l???i", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
            Toast.makeText(this, "Ph???n h???i ????ng nh???p kh??ng x??c ?????nh", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        // ?????t resultCode l?? Activity.RESULT_CANCELED th??? hi???n
        // ???? th???t b???i khi ng?????i d??ng click v??o n??t Back.
        // Khi n??y s??? kh??ng tr??? v??? data.
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}