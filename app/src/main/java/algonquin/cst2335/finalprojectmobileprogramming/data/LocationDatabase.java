package algonquin.cst2335.finalprojectmobileprogramming.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {LocationItem.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract LocationItemDAO locationItemDao();

    // Enforced method!!
    // Add the static method to ensures that there is only one database instance globally
    private static volatile LocationDatabase INSTANCE;

    public static LocationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    LocationDatabase.class, "location_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
