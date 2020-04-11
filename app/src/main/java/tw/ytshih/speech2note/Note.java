package tw.ytshih.speech2note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "notes")
class Note {
    @PrimaryKey(autoGenerate = true)
    Integer nid;

    @NonNull
    String title;

    @NonNull
    String content;

    @NonNull
    Date createTime;

    @NonNull
    Date editTime;

    Note(@Nullable Integer nid,
         @NonNull String title,
         @NonNull String content,
         @NonNull Date createTime,
         @NonNull Date editTime) {
        this.nid = nid;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.editTime = editTime;
    }
}
