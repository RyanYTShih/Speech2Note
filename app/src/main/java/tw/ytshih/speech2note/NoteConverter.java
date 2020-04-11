package tw.ytshih.speech2note;

import androidx.room.TypeConverter;

import java.sql.Date;

public class NoteConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
