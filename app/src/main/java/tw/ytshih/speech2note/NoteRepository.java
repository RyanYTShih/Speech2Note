package tw.ytshih.speech2note;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> mAllNote;

    NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        noteDao = db.noteDao();
        mAllNote = noteDao.getNotes();
    }

    LiveData<List<Note>> getAllNote() {
        return mAllNote;
    }

    LiveData<Note> getNoteById(int id) {
        return noteDao.getNoteById(id);
    }

    void update(Note note) {
        new updateAsyncTask(noteDao).execute(note);
    }

    void delete(Note note) {
        new deleteAsyncTask(noteDao).execute(note);
    }

    MutableLiveData<Long> insert(Note note) {
        MutableLiveData<Long> liveData = new MutableLiveData<>();
        new NoteRepository.insertAsyncTask(liveData, noteDao).execute(note);
        return liveData;
    }

    private static class updateAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mDao;

        updateAsyncTask(NoteDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mDao.updateNote(notes[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mDao;

        deleteAsyncTask(NoteDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mDao.deleteNote(notes[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Note, Void, Long> {
        private MutableLiveData<Long> mLiveData;
        private NoteDao mAsyncTaskDao;

        insertAsyncTask(MutableLiveData<Long> liveData, NoteDao dao) {
            mLiveData = liveData;
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(Note... notes) {
            return mAsyncTaskDao.insertNote(notes[0]);
        }

        @Override
        protected void onPostExecute(Long insertId) {
            mLiveData.postValue(insertId);
            super.onPostExecute(insertId);
        }

        @Override
        protected void onCancelled() {
            mLiveData.postValue((long) 0);
            super.onCancelled();
        }
    }
}
