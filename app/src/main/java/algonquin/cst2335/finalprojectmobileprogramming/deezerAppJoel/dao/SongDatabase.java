package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Song;

@Database(entities = {Song.class}, version = 1)
public abstract class SongDatabase extends RoomDatabase {
    public abstract SongDAO songDAO();

    private static SongDatabase instance;

    public static synchronized SongDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            SongDatabase.class, "song_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
