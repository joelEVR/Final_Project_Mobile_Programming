package algonquin.cst2335.finalprojectmobileprogramming.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {LocationItem.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract LocationItemDAO locationItemDao();
}
