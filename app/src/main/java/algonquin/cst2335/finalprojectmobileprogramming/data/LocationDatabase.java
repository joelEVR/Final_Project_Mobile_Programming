/**
 * Student Name: Ting Cheng
 * Professor: Samira Ouaaz
 * Due Date: Apr. 1,2024
 * Description:  CST2355-031 FinalAssignment
 * Section: 031
 * Modify Date: Mar. 31,2024
 */
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
    // To avoid creating multiple database instances, you are advised to use the singleton mode
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
