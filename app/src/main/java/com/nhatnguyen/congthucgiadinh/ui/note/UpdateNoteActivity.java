package com.nhatnguyen.congthucgiadinh.ui.note;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.model.NoteModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UpdateNoteActivity extends AppCompatActivity {
    TextView timer, id;
    EditText body, edit_title;
    Button hoantatUpdate;
    DatabaseReference refNote;
    Toolbar toolbar;
    private NoteModel noteModel = null;
    private ImageView imgRecord_note,imgRecord_title;
    protected static final int RESULT_SPEECH_TITLE = 1;
    protected static final int RESULT_SPEECH_NOTE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note2);
        hoantatUpdate = findViewById(R.id.hoantatUpdate);
        timer = findViewById(R.id.timer);
        id = findViewById(R.id.id);
        body = findViewById(R.id.edit_body);
        edit_title = findViewById(R.id.edit_title);
        imgRecord_note=findViewById(R.id.imgRecord_note);
        imgRecord_title=findViewById(R.id.imgRecord_title);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh:mm:ss a");

        String dataTime = simpleDateFormat.format(calendar.getTime());
        timer.setText(dataTime);
        timer.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        timer.setSelected(true);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.arrowleft2);
        upArrow.setColorFilter(Color.parseColor("#850505"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Object object = getIntent().getSerializableExtra("Note");
        if (object instanceof NoteModel) {
            noteModel = (NoteModel) object;
        }

        if (noteModel != null) {
            hoantatUpdate.setVisibility(View.VISIBLE);
            id.setText(noteModel.getId());
            body.setText(noteModel.getNote());
            edit_title.setText(noteModel.getTitle());
        }
        hoantatUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNoteUpdate();
            }
        });
        imgRecord_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VI");
                try {
                    startActivityForResult(intent, RESULT_SPEECH_TITLE);
                   // edit_title.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Thi???t b??? c???a b???n kh??ng h??? tr??? chuy???n l???i n??i th??nh v??n b???n", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        imgRecord_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VI");
                try {
                    startActivityForResult(intent, RESULT_SPEECH_NOTE);
                    //body.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Thi???t b??? c???a b???n kh??ng h??? tr??? chuy???n l???i n??i th??nh v??n b???n", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        refNote = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Note");


    }

    private void AddNoteUpdate() {
        String idNote = id.getText().toString().trim();
        String title = edit_title.getText().toString().trim();
        String time = timer.getText().toString().trim();
        String namebody = body.getText().toString().trim();
        if (TextUtils.isEmpty(title)){
            edit_title.setError("B???n ch??a ??i???n ti??u ????? ");
        }
        if (TextUtils.isEmpty(namebody)){
            body.setError("B???n ch??a ??i???n ghi ch?? ");
        }
        if ( ! TextUtils.isEmpty(namebody) &&  ! TextUtils.isEmpty(title)){
            NoteModel addNoteModel =new NoteModel(idNote,namebody,time,title);
            refNote.child(idNote).setValue(addNoteModel);
            Toast.makeText(this, "???? l??u ghi ch??", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(UpdateNoteActivity.this, NoteFragment.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH_TITLE:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edit_title.setText(text.get(0));
                }
                break;
            case RESULT_SPEECH_NOTE:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    body.setText(text.get(0));
                }
                break;
        }
    }
}