package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalprojectmobileprogramming.R;
import algonquin.cst2335.finalprojectmobileprogramming.databinding.ActivitySearchBinding;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter.PreviousSearchAdapter;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Artist;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.service.SearchResponse;

public class SearchActivity extends AppCompatActivity implements PreviousSearchAdapter.OnDeleteClickListener,PreviousSearchAdapter.OnClickListener  {

    private ActivitySearchBinding binding;
    private SharedPreferences preferences;
    private ArrayList<String> previousSearchTerms = new ArrayList<>();
    private PreviousSearchAdapter adapter;
    private RequestQueue requestQueue; // Add this line


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);

        // Set up RecyclerView
        adapter = new PreviousSearchAdapter(previousSearchTerms);
        adapter.setOnDeleteClickListener(this);
        adapter.setOnClickListener(this); // Set item click listener
        binding.recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewResults.setAdapter(adapter);

        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        requestQueue = Volley.newRequestQueue(this);

        // Set OnEditorActionListener for the search EditText
        binding.editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchTerm = binding.editTextSearch.getText().toString();

                    // Add the search term to the list
                    previousSearchTerms.add(searchTerm);

                    // Notify the adapter of the data change
                    adapter.notifyDataSetChanged();

                    // Save the list to SharedPreferences
                    saveSearchTermsToPreferences(previousSearchTerms);

                    // Perform the search operation
                    performSearch(searchTerm);

                    return true;
                }
                return false;
            }
        });

        // Display the list of previous search terms
        displaySearchTerms();
    }

    private List<String> getSearchTermsFromPreferences() {
        String searchTermsJson = preferences.getString("searchTerms", "[]");
        Type type = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(searchTermsJson, type);
    }

    private void saveSearchTermsToPreferences(List<String> searchTerms) {
        String searchTermsJson = new Gson().toJson(searchTerms);
        preferences.edit().putString("searchTerms", searchTermsJson).apply();
    }

    private void displaySearchTerms() {
        List<String> searchTerms = getSearchTermsFromPreferences();
        adapter.setData(searchTerms);
    }

    @Override
    public void onDeleteClick(String searchTerm) {
        previousSearchTerms.remove(searchTerm);
        saveSearchTermsToPreferences(previousSearchTerms);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(String searchTerm) {
        // Handle item click event here
        // For example, you can perform a search with the clicked search term
        performSearch(searchTerm);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tabmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.fav_list){
            // Start the FavoriteSongsActivity
            Intent intent = new Intent(SearchActivity.this, FavoriteSongsActivity.class);
            // Pass the list of favorite songs to the FavoriteSongsActivity
            //   intent.putStringArrayListExtra("favoriteSongs", favoriteSongs); // Replace 'favoriteSongs' with your list
            startActivity(intent);
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }


    }
    private void performSearch(final String searchTerm) {
        String url = "https://api.deezer.com/search/artist?q=" + Uri.encode(searchTerm);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Convertir respuesta JSON a objetos Java usando Gson
                        Gson gson = new Gson();
                        SearchResponse searchResponse = gson.fromJson(response, SearchResponse.class);
                        List<Artist> artists = searchResponse.getArtists();
                        if (artists != null && !artists.isEmpty()) {
                            Intent intent = new Intent(SearchActivity.this, ArtistListActivity.class);
                            intent.putParcelableArrayListExtra("artists", new ArrayList<>(artists));
                            startActivity(intent);
                        } else {
                            Toast.makeText(SearchActivity.this, "No artists found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }
}
