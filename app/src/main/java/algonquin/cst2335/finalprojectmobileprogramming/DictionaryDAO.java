package algonquin.cst2335.finalprojectmobileprogramming;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO to take care of inserting, updating, creating and deleting.
 * Data Access Object, which is responsible for running the sql commands
 */
@Dao
public interface DictionaryDAO {
    @Insert
    public  void insertWord(DictionaryData w);
    @Query("Select * from DictionaryData")
    public List<DictionaryData> getAllWords();
    @Query("SELECT * FROM DictionaryData WHERE id = :id")
    DictionaryData getWordById(long id);

    @Query("SELECT * FROM DictionaryData WHERE searchTerm = :searchTerm")
    List<DictionaryData> getAllWordsWithSearchTerm(String searchTerm);


    @Delete
    void deleteWord(DictionaryData m);
}
