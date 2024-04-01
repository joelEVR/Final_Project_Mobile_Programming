/**
 * Student Name: Ting Cheng
 * Professor: Samira Ouaaz
 * Due Date: Apr. 1,2024
 * Description:  CST2355-031 FinalAssignment
 * Section: 031
 * Modify Date: Mar. 31,2024
 */
package algonquin.cst2335.finalprojectmobileprogramming;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import algonquin.cst2335.finalprojectmobileprogramming.data.LocationDatabase;
import algonquin.cst2335.finalprojectmobileprogramming.data.LocationItem;
import algonquin.cst2335.finalprojectmobileprogramming.databinding.ActivityMainBinding;

/**
 * MainActivity of the application, handling the user interface for sunrise and sunset lookups.
 * <p>
 * This class provides functionalities for users to input latitude and longitude,
 * perform lookup for sunrise and sunset times, save locations to favorites, and
 * view saved locations. It utilizes the Volley library for network requests and
 * SharedPreferences for storing the user's last search.
 * </p>
 *
 * @author Ting Chneg
 * @version 3.0
 * @since 2024-03-22
 */
public class MainActivityTing extends AppCompatActivity {
    /**
     * Binding instance for interacting with the MainActivity layout views.
     */
    private ActivityMainBinding binding;
    /**
     * Variable to store the name of the location. This is used to display the location name
     * in the UI and save it along with latitude and longitude in the database.
     */
    private String locationName; // CLASS variable !!!!

    /**
     * Called when the activity is first created. Initializes the UI and sets up event listeners.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comment this database instantiation which moved in LocationDatabase ensure singleton mode
        // LocationDatabase db = Room.databaseBuilder(getApplicationContext(), LocationDatabase.class, "location_database").build();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Activate the Toolbar
        setSupportActionBar(binding.toolbar);

        // Checks whether the Intent contains specific data and updates the UI accordingly
        if (getIntent() != null && getIntent().hasExtra(getString(R.string.latitude))
                && getIntent().hasExtra(getString(R.string.longitude))
                && getIntent().hasExtra(getString(R.string.name))) {
            double latitude = getIntent().getDoubleExtra(getString(R.string.latitude), 0);
            double longitude = getIntent().getDoubleExtra(getString(R.string.longitude), 0);
            String name = getIntent().getStringExtra(getString(R.string.name));

            binding.editTextLatitude.setText(String.valueOf(latitude));
            binding.editTextLongitude.setText(String.valueOf(longitude));
            binding.editTextLocationName.setText(name);
        }else{
            // Load last search query
            loadLastSearch();
        }


        // Click listener for the Lookup button to CALL performSunriseSunsetLookup()
        binding.btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Key step of tricky logic that
                // Allows the user to enter the location name before entering the latitude
                // and longitude and the query.
                // Magic thing is I add String locationName = ... then result will output as null
                // because here locationName will considered as LOCAL variable!!!!
                locationName = binding.editTextLocationName.getText().toString().trim();
                performSunriseSunsetLookup();
            }
        });
        // Click listener for the saveLocation button
        binding.buttonSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation();
            }
        });
        // Click listener for the showFavorites button
        binding.btnShowFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityTing.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Performs the lookup for sunrise and sunset times.
     * This method constructs the request URL, makes the network request, and handles the response data.
     */
    // KEY STEP!!! CONNECT network to retrieve Lookup result
    private void performSunriseSunsetLookup() {
        String latitude = binding.editTextLatitude.getText().toString().trim();
        String longitude = binding.editTextLongitude.getText().toString().trim();
        // Provide the latitude and longitude are not empty
        if (latitude.isEmpty() || longitude.isEmpty()) {
            Toast.makeText(MainActivityTing.this, getString(R.string.enter_latitude_longitude),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // Save the current search query
        saveLastSearch(latitude, longitude,locationName);
        // create request to URL
        String url = getString(R.string.url_part1)+ latitude + getString(R.string.url_part2) + longitude
                + getString(R.string.url_part3);
        // Launch request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Firstly check status String (首先，检查状态码是否为OK)
                            String status = response.getString(getString(R.string.status));
                            if (getString(R.string.OK).equals(status)) {
                                // Retrieve result object (获取results对象)
                                JSONObject results = response.getJSONObject(getString(R.string.results));

                                // Retrieve sunset and sunrise time from object (从results对象中获取日出和日落时间)
                                String sunrise = results.getString(getString(R.string.sunrise));
                                String sunset = results.getString(getString(R.string.sunset));

                                // !!! Transfer time to local time Style (将时间转换为本地时间（因为API返回的是UTC时间）)
                                String localSunrise = convertUTCToLocalTime(sunrise);
                                String localSunset = convertUTCToLocalTime(sunset);

                                // Update UI (更新UI（确保这部分代码在主线程中执行）)
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String resultFormat = getString(R.string.result_format);
                                                String displayText = String.format(resultFormat, locationName,
                                                latitude, longitude, localSunrise, localSunset);
                                        binding.textViewResult.setText(displayText);
                                    }
                                });
                            } else {
                                // Handel errors when status =! OK
                                Toast.makeText(MainActivityTing.this, getString(R.string.retrieve_data_fail), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivityTing.this, getString(R.string.phase_data_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    // Various network error message. Whether need ???
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log the error or inspect it
                        Log.e("SunriseSunsetLookup", "Network request error: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e("SunriseSunsetLookup", "Status Code: " + error.networkResponse.statusCode);
                        }
                        Toast.makeText(MainActivityTing.this, getString(R.string.request_fail), Toast.LENGTH_SHORT).show();
                    }
                });
        // Add request into request queue (将请求添加到请求队列) !!!!
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    /**
     * Converts UTC time to local time format.
     *
     * @param utcTime The UTC time string to be converted.
     * @return The converted local time string.
     */
    // Whether to need this conversion ????
    private String convertUTCToLocalTime(String utcTime) {
        try {
            // Parse the time format returned by the API (解析API返回的时间格式)
            SimpleDateFormat utcFormat = new SimpleDateFormat(getString(R.string.time_format), Locale.CANADA);
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = utcFormat.parse(utcTime);

            // Convert to local time format (转换为本地时间格式)
            SimpleDateFormat localFormat = new SimpleDateFormat(getString(R.string.time_format), Locale.CANADA);
            localFormat.setTimeZone(TimeZone.getDefault());
            return localFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return utcTime; // When parsing fails, the original UTC time is returned(解析失败时，返回原始UTC时间)
        }
    }

    /**
     * Saves the current query to SharedPreferences.
     *
     * @param latitude The latitude string.
     * @param longitude The longitude string.
     * @param locationName The name of the location.
     */
    private void saveLastSearch(String latitude, String longitude,String locationName) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.latitude), latitude);
        editor.putString(getString(R.string.longitude), longitude);
        editor.putString(getString(R.string.location_name), locationName);
        editor.apply();
    }
    /**
     * Loads the last search from SharedPreferences.
     */
    private void loadLastSearch() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String latitude = prefs.getString(getString(R.string.latitude), "");
        String longitude = prefs.getString(getString(R.string.longitude), "");
        binding.editTextLatitude.setText(latitude);
        binding.editTextLongitude.setText(longitude);
        String locationName = prefs.getString(getString(R.string.location_name), "");
        binding.editTextLocationName.setText(locationName);
    }


    // setSupportActionBar() to Inflate the Menu(ToolBar)
    // onCreateOptionsMenu + onOptionsItemSelected + showHelpToast to realize WHOLE Menu event
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            binding.editTextLatitude.setText("");
            binding.editTextLongitude.setText("");
            binding.editTextLocationName.setText("");
            binding.textViewResult.setText("");
            Toast.makeText(this, getString(R.string.content_clear), Toast.LENGTH_SHORT).show();
        }else if (id == R.id.main_instruction){
            showHelpToast();
        }else {
            throw new IllegalStateException(getString(R.string.error));
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Display the instruction by Toast Message.
     */
    private void showHelpToast() {
        String helpText = getString(R.string.MainActivity_Instruction);
        Toast.makeText(this, helpText, Toast.LENGTH_LONG).show();
    }

    /**
     * Saves the location information to the database.
     */
    // KEY STEP!!! Save Location to transfer to Database
    private void saveLocation() {
        // NO need to get the locationName here! Use the class level variable locationName
        // String locationName = binding.editTextLocationName.getText().toString().trim();
        double latitude = Double.parseDouble(binding.editTextLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(binding.editTextLongitude.getText().toString().trim());
        // Check whether the location name is empty
        if(locationName.isEmpty()) {
            Snackbar.make(findViewById(R.id.editTextLocationName), getString(R.string.location_not_null), Snackbar.LENGTH_SHORT).show();
            return;
        }
        // create Location object and store in database
        LocationItem location = new LocationItem(latitude, longitude, locationName);
        new Thread(() -> {
            // To avoid creating multiple database instances, use the singleton mode
            LocationDatabase db = LocationDatabase.getDatabase(getApplicationContext());
            db.locationItemDao().insertLocation(location);
            runOnUiThread(() -> {
                Toast.makeText(MainActivityTing.this, getString(R.string.location_saved), Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
}
