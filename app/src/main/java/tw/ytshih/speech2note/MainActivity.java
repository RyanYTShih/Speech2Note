package tw.ytshih.speech2note;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.text.DateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private NoteViewModel model;
    private LiveData<List<Note>> notes;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new ViewModelProvider(this).get(NoteViewModel.class);
        notes = model.getAllNote();


        ListView noteList = findViewById(R.id.noteList);
        noteAdapter = new NoteAdapter(this, R.layout.note_adapter);

        noteList.setAdapter(noteAdapter);

        notes.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> data) {
                Log.d(TAG, "note list updated");
                if (notes == null)
                    return;
                try {
                    MainActivity.this.notes = model.getAllNote();
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
                noteAdapter.clear();
                for (Note note : data) {

                    try {
                        noteAdapter.add(note);
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
        });

        final FloatingActionButton fab = findViewById(R.id.fab_new);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setEnabled(false);
                Note note = new Note(null, "", "", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
                MutableLiveData<Long> nid = model.insert(note);
                nid.observe(MainActivity.this, new Observer<Long>() {
                    @Override
                    public void onChanged(Long insertId) {
                        Log.d(TAG, "new Note, nid = " + insertId);
                        Intent intent = new Intent(MainActivity.this, NoteActivity.class)
                                .putExtra("nid", insertId.intValue());

                        startActivity(intent);
                        fab.setEnabled(true);
                    }
                });
            }
        });

        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) noteAdapter.getItem(position);
                DateFormat longFormat =
                        DateFormat.getDateTimeInstance(
                                DateFormat.MEDIUM, DateFormat.MEDIUM);
                assert note != null;
                Intent intent = new Intent(MainActivity.this, NoteActivity.class)
                        .putExtra("nid", note.nid);

                startActivity(intent);
            }
        });
    }
}
