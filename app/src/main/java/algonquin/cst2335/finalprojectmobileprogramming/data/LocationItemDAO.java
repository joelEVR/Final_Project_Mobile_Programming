/**
 * Student Name: Ting Cheng
 * Professor: Samira Ouaaz
 * Due Date: Apr. 1,2024
 * Description:  CST2355-031 FinalAssignment
 * Section: 031
 * Modify Date: Mar. 31,2024
 */
package algonquin.cst2335.finalprojectmobileprogramming.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationItemDAO {
    @Insert
    void insertLocation(LocationItem locationItem);
    @Query("SELECT * FROM LocationItem")
    public List<LocationItem> getAllLocations();
    @Delete
    void deleteLocation(LocationItem locationItem);
    @Query("DELETE FROM LocationItem")
    void deleteAllLocations();
}

