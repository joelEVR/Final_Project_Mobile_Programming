package algonquin.cst2335.finalprojectmobileprogramming;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import algonquin.cst2335.finalprojectmobileprogramming.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String locationName; // CLASS variable !!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbar); // Activate the Toolbar

        // Load last search query
        loadLastSearch();

        // Set the click listener for the lookup button
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

        // Set the click listener for the save button
        binding.buttonSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLocation();
            }
        });
    }

    private void performSunriseSunsetLookup() {
        String latitude = binding.editTextLatitude.getText().toString().trim();
        String longitude = binding.editTextLongitude.getText().toString().trim();

        // Provide the latitude and longitude are not empty
        if (latitude.isEmpty() || longitude.isEmpty()) {
            Toast.makeText(MainActivity.this, getString(R.string.enter_latitude_longitude),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate and display the request result
        String message = "Looking up the latitude" + latitude + "，longitude：" + longitude
                + "whose time of sunrise and sunset are ... ";
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

        // Save the current search query
        saveLastSearch(latitude, longitude);

        // create request to URL
        String url = "https://api.sunrisesunset.io/json?lat=" + latitude + "&lng=" + longitude
                + "&timezone=UTC&date=today";

        // Launch request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Firstly check status String (首先，检查状态码是否为OK)
                            String status = response.getString("status");
                            if ("OK".equals(status)) {
                                // Retrieve result object (获取results对象)
                                JSONObject results = response.getJSONObject("results");

                                // Retrieve sunset and sunrise time from object (从results对象中获取日出和日落时间)
                                String sunrise = results.getString("sunrise");
                                String sunset = results.getString("sunset");

                                // Transfer time to local time Style (将时间转换为本地时间（因为API返回的是UTC时间）)
                                String localSunrise = convertUTCToLocalTime(sunrise);
                                String localSunset = convertUTCToLocalTime(sunset);

                                // Update UI (更新UI（确保这部分代码在主线程中执行）)
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //                                        String resultText = getString(R.string.sunrise_time, localSunrise)
                                        //                                                + "\n" + getString(R.string.sunset_time, localSunset);
                                        //                                        String resultText = "Sunrise Time: " + localSunrise + "\nSunset Time: " + localSunset;
                                        //                                        binding.textViewResult.setText(resultText);
                                        String resultFormat = "Location Name: %s\nLatitude: %s, " +
                                                "Longitude: %s\nSunrise: %s\nSunset: %s";
                                        String displayText = String.format(resultFormat, locationName,
                                                latitude, longitude, localSunrise, localSunset);
                                        binding.textViewResult.setText(displayText);


                                    }
                                });
                            } else {
                                // Handel errors when status =! OK
                                Toast.makeText(MainActivity.this, "Retrieve Data Fail.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Phase Data Error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log the error or inspect it
                        Log.e("SunriseSunsetLookup", "Network request error: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e("SunriseSunsetLookup", "Status Code: " + error.networkResponse.statusCode);
                        }
                        Toast.makeText(MainActivity.this, "Request Fail", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add request into request queue (将请求添加到请求队列)
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private String convertUTCToLocalTime(String utcTime) {
        try {
            // Parse the time format returned by the API (解析API返回的时间格式)
            SimpleDateFormat utcFormat = new SimpleDateFormat("h:mm:ss a", Locale.US);
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = utcFormat.parse(utcTime);

            // Convert to local time format (转换为本地时间格式)
            SimpleDateFormat localFormat = new SimpleDateFormat("h:mm:ss a", Locale.US);
            localFormat.setTimeZone(TimeZone.getDefault());
            return localFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return utcTime; // When parsing fails, the original UTC time is returned(解析失败时，返回原始UTC时间)
        }
    }


    // Load the last search query from SharedPreferences
    private void loadLastSearch() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String latitude = prefs.getString("latitude", "");
        String longitude = prefs.getString("longitude", "");
        binding.editTextLatitude.setText(latitude);
        binding.editTextLongitude.setText(longitude);
    }

    // Save the current search query to SharedPreferences
    private void saveLastSearch(String latitude, String longitude) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("latitude", latitude);
        editor.putString("longitude", longitude);
        editor.apply();
    }

    // etSupportActionBar(),Inflate the Menu in Your Activity
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
            refreshQuery();
        }else if (id == R.id.action_help){
            showHelpToast();
        }else {
            throw new IllegalStateException(getString(R.string.error));
        }
        return super.onOptionsItemSelected(item);
    }

    // Display the instruction by Toast Message
    private void showHelpToast() {
        String helpText = getString(R.string.MainActivity_Instruction);
        Toast.makeText(this, helpText, Toast.LENGTH_LONG).show();
    }

    private void refreshQuery() {
        // redo the Lookup method
        performSunriseSunsetLookup();
    }

    private void saveLocation() {
//            NO need to get the locationName here! Use the class level variable locationName
//            String locationName = binding.editTextLocationName.getText().toString().trim();
        double latitude = Double.parseDouble(binding.editTextLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(binding.editTextLongitude.getText().toString().trim());

        // 检查位置名称是否为空
        if(locationName.isEmpty()) {
            Toast.makeText(MainActivity.this, "位置名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建Location对象并保存到数据库
        LocationItem location = new LocationItem(latitude, longitude, locationName);
        //        location.name = locationName;
        //        location.latitude = latitude;
        //        location.longitude = longitude;

        // 假设你有一个数据库实例db
        // db.locationDao().insert(location);
        // OR
        // Assuming you have a method to save the LocationItem to your database
        // saveLocationToDatabase(location);

        Toast.makeText(MainActivity.this, "位置已保存", Toast.LENGTH_SHORT).show();
    }


}
