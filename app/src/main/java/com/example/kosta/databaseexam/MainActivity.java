package com.example.kosta.databaseexam;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class MainActivity extends ListActivity {

    private static final int CREATE = 0;
    private static final int EDIT = 1;

    private NoteDB db;

    private Spinner searchBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBy = (Spinner)findViewById(R.id.searchBy);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_item, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchBy.setAdapter(adapter);

        db = new NoteDB(this);
        db.open();

        fillData();

        findViewById(R.id.searchNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBy.getSelectedItem().toString().equals("제목")) {
                    fillSearchedData("title", ((EditText)findViewById(R.id.keywordForSearch)).getText().toString());
                } else if(searchBy.getSelectedItem().toString().equals("내용")) {
                    fillSearchedData("body", ((EditText)findViewById(R.id.keywordForSearch)).getText().toString());
                }
            }
        });

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, CREATE);
            }
        });

        registerForContextMenu(getListView());
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(NoteDB.KEY_ROWID, id);
        startActivityForResult(intent, EDIT);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(Menu.NONE, 1, Menu.NONE, "DELETE");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        db.deleteNote(info.id);
        fillData();

        return super.onContextItemSelected(item);
    }

    private void fillSearchedData(String searchBy, String keyword) {
        Cursor cursor = null;

        if(searchBy.equals("title")) {
            cursor = db.fetchNotesByTitle(keyword);
        } else if (searchBy.equals("body")) {
            cursor = db.fetchNotesByBody(keyword);
        }

        SimpleCursorAdapter adapter = (SimpleCursorAdapter)getListAdapter();

        adapter.swapCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    private void fillData() {
        Cursor cursor = db.fetchAllNotes();

        String[] from = {NoteDB.KEY_TITLE};
        int[] to = {R.id.item};

        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(
                        this,
                        R.layout.list_item,
                        cursor,
                        from,
                        to,
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        this.setListAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fillData();
    }
}
