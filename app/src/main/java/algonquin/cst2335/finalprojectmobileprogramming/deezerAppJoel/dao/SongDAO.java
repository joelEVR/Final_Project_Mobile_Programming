package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Song;
 @Dao
 public interface SongDAO {

        @Insert
        long insertSong(Song song);

        @Query(value = "SELECT * FROM songs")
        List<Song> getAllSongs();

        @Delete
        void deleteSong(Song m);

        @Query("DELETE FROM songs")
        void deleteAllSongs();

}

