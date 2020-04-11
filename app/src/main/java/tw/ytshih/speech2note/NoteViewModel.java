package tw.ytshih.speech2note;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository mRepository;

    private LiveData<List<Note>> mAllNote;

    public NoteViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNote = mRepository.getAllNote();
    }

    LiveData<List<Note>> getAllNote() {
        return mAllNote;
    }

    LiveData<Note> getNoteById(int id) {
        return mRepository.getNoteById(id);
    }

    MutableLiveData<Long> insert(Note note) {
        return mRepository.insert(note);
    }

    void update(Note note) {
        mRepository.update(note);
    }

    void delete(Note note) {
        mRepository.delete(note);
    }
}
