package tw.ytshih.speech2note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class NoteAdapter extends ArrayAdapter<Note> {

    NoteAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_adapter, parent, false);
        }

        Note note = (Note) getItem(position);

        TextView title = convertView.findViewById(R.id.title);
        TextView content = convertView.findViewById(R.id.content);

        assert note != null;
        title.setText(note.title);
        content.setText(note.content);

        return convertView;
    }
}
