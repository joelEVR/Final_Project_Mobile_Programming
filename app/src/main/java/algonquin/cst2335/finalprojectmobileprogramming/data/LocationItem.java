/**
 * Student Name: Ting Cheng
 * Professor: Samira Ouaaz
 * Due Date: Apr. 1,2024
 * Description:  CST2355-031 FinalAssignment
 * Section: 031
 * Modify Date: Mar. 31,2024
 */
package algonquin.cst2335.finalprojectmobileprogramming.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Location database DTO
@Entity
public class LocationItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public int id;
    @ColumnInfo(name = "Latitude")
    private double latitude;
    @ColumnInfo(name = "Longitude")
    private double longitude;
    @ColumnInfo(name = "LocationName")
    private String name;

    public LocationItem(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
}

