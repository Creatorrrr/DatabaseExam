package com.example.kosta.databaseexam;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditActivity extends Activity {

    private EditText titleText;
    private EditText bodyText;
    private Long rowId;

    private NoteDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = new NoteDB(this);
        db.open();

        titleText = (EditText)findViewById(R.id.title);
        bodyText = (EditText)findViewById(R.id.body);

        if(rowId == null) {
            Bundle extras = getIntent().getExtras();

            if(extras != null) {
                rowId = extras.getLong(NoteDB.KEY_ROWID);
            } else {
                rowId = null;
            }
        }

        populateFields();

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void populateFields() {
        if(rowId != null) {
            Cursor note = db.fetchNote(rowId);
            String title = note.getString(note.getColumnIndexOrThrow(NoteDB.KEY_TITLE));
            String body = note.getString(note.getColumnIndexOrThrow(NoteDB.KEY_BODY));

            titleText.setText(title);
            bodyText.setText(body);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveState();
    }

    private void saveState() {
        String title = titleText.getText().toString();
        String body = bodyText.getText().toString();

        if(rowId == null) {
            long id = db.createNote(title, body);
            if(id > 0) {
                rowId = id;
            }
        } else {
            db.updateNote(rowId, title, body);
        }
    }
}
