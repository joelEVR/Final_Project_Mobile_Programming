package algonquin.cst2335.finalprojectmobileprogramming;

import androidx.room.TypeConverter;
import java.util.ArrayList;

/**
 * Specifies how Room should convert my custom data type (ArrayList<String>) to a type that can be stored in the database.
 */
public class Converters {
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        // Split the string by comma to convert it back to ArrayList<String>
        String[] array = value.split(",");
        ArrayList<String> arrayList = new ArrayList<>();
        for (String item : array) {
            arrayList.add(item);
        }
        return arrayList;
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        // Convert ArrayList<String> to comma-separated String
        StringBuilder value = new StringBuilder();
        for (String item : list) {
            value.append(item);
            value.append(",");
        }
        return value.toString();
    }
}
