package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Song;

import java.util.List;
 @Dao
    public interface SongDAO {

        @Insert
        public long insertSong(Song song);

        @Query(value = "SELECT * FROM songs")
        public List<Song> getAllSongs();

        @Delete
        public void deleteSong(Song m);

        @Query("DELETE FROM songs")
        public void deleteAllSongs();

}

