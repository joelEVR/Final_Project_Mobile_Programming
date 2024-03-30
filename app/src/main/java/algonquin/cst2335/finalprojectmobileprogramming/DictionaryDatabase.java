package algonquin.cst2335.finalprojectmobileprogramming;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Extends Room. This Database class is meant for storing DictionaryData objects, and uses the DictionaryDAO class for querying data.
 */
@Database(entities = {DictionaryData.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class DictionaryDatabase extends RoomDatabase {
    public abstract DictionaryDAO stDAO();
}
