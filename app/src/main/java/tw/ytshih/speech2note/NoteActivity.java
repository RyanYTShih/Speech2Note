package tw.ytshih.speech2note;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;

import java.sql.Date;
import java.text.DateFormat;
import java.util.concurrent.Future;

public class NoteActivity extends AppCompatActivity {

    private static final String voice_name = "Microsoft Server Speech Text to Speech Voice (zh-TW, Yating, Apollo)";
    private static String speechSubscriptionKey = "your_subscription_key";
    private static String serviceRegion = "southeastasia";
//    private static final String voice_name = "Microsoft Server Speech Text to Speech Voice (zh-CN, XiaoxiaoNeural)";
    private SpeechConfig config;

    private SpeechRecognizer recognizer;

//    private SpeechSynthesizer synthesizer;

    private String lang = "zh-TW";

    private Note mNote;

    private TextView title;
    private TextView content;
    private TextView date;

    private NoteViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


        int requestCode = 5;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET}, requestCode);
        config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
        recognizer = new SpeechRecognizer(config, "zh-TW");

        title = findViewById(R.id.et_title);
        content = findViewById(R.id.et_content);
        date = findViewById(R.id.date);

        Intent intent = this.getIntent();

        int nid = intent.getIntExtra("nid", -1);
        if (nid == -1)
            finish();

        final DateFormat longFormat =
                DateFormat.getDateTimeInstance(
                        DateFormat.MEDIUM, DateFormat.MEDIUM);

        model = new ViewModelProvider(this).get(NoteViewModel.class);
        LiveData<Note> noteLiveData = model.getNoteById(nid);
        noteLiveData.observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                if (note == null) return;
                mNote = note;
                title.setText(note.title);
                content.setText(note.content);
                date.setText("建立日期：" + longFormat.format(note.createTime) + "\n最後編輯：" + longFormat.format(note.editTime));
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab_speech);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Future<SpeechRecognitionResult> task = recognizer.recognizeOnceAsync();

                    SpeechRecognitionResult result = task.get();

                    if (result.getReason() == ResultReason.RecognizedSpeech) {
                        content.setText(content.getEditableText() + result.getText());
//                        String xml_body = XmlDom.createDom(voice_name, result.getText().substring(0, Math.min(result.getText().length(), 1000)));
//                        TextToSpeech(xml_body);
                    } else {
//                        txt.setText("Error recognizing.");
                    }

                    result.close();
                } catch (Exception e) {
                    Log.e("SpeechSDKDemo", e.getMessage() + "");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        onSave(null);
        super.onDestroy();
    }

    public void onSave(View view) {
        mNote.title = title.getEditableText().toString();
        mNote.content = content.getEditableText().toString();
        mNote.editTime = new Date(System.currentTimeMillis());
        model.update(mNote);
    }

    public void onDelete(View view) {
        model.delete(mNote);
        finish();
    }
}
