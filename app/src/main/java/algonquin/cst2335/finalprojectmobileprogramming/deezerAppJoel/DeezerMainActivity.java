package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


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

import algonquin.cst2335.finalprojectmobileprogramming.MainActivity;
import algonquin.cst2335.finalprojectmobileprogramming.R;
import algonquin.cst2335.finalprojectmobileprogramming.databinding.ActivitySearchBinding;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter.PreviousSearchAdapter;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Artist;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.service.SearchResponse;

/**
 * Main activity for the Deezer App, responsible for handling search operations,
 * displaying previous search terms, and navigating to artist details.
 * This class integrates with the Deezer API to fetch artist data based on user searches.
 * It also manages user preferences to store and display previous search terms.
 *
 * @author Joel Esteban Velasquez Rodriguez
 * @labSection 031
 * @creationDate (Insert your creation date here, e.g., "April 10, 2023")
 */
public class DeezerMainActivity extends AppCompatActivity implements PreviousSearchAdapter.OnDeleteClickListener,PreviousSearchAdapter.OnClickListener  {

    /**
     * Binding instance for accessing the activity's views.
     */
    private ActivitySearchBinding binding;

    /**
     * Shared preferences to store and retrieve user preferences such as previous search terms.
     */
    private SharedPreferences preferences;

    /**
     * List to hold previous search terms entered by the user.
     */
    private ArrayList<String> previousSearchTerms = new ArrayList<>();

    /**
     * Adapter for managing and displaying previous search terms in a RecyclerView.
     */
    private PreviousSearchAdapter adapter;

    /**
     * RequestQueue instance for managing network requests via Volley library.
     */
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar customToolbar = findViewById(R.id.deezerMainToolbar);
        setSupportActionBar(binding.deezerMainToolbar);

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
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
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

    /**
     * Retrieves search terms from SharedPreferences.
     *
     * @return A list of previously searched terms.
     */
    private List<String> getSearchTermsFromPreferences() {
        String searchTermsJson = preferences.getString("searchTerms", "[]");
        Type type = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(searchTermsJson, type);
    }

    /**
     * Saves the current list of search terms into SharedPreferences.
     *
     * @param searchTerms The list of search terms to be saved.
     */
    private void saveSearchTermsToPreferences(List<String> searchTerms) {
        String searchTermsJson = new Gson().toJson(searchTerms);
        preferences.edit().putString(getString(R.string.search_terms_key), searchTermsJson).apply();
    }


    /**
     * Displays the list of previous search terms using the adapter.
     */
    private void displaySearchTerms() {
        List<String> searchTerms = getSearchTermsFromPreferences();
        adapter.setData(searchTerms);
    }

    /**
     * Handles the event when a user clicks the delete button next to a previous search term.
     * This method removes the specified search term from the list and updates the shared preferences.
     *
     * @param searchTerm The search term to be deleted.
     */
    @Override
    public void onDeleteClick(String searchTerm) {
        previousSearchTerms.remove(searchTerm);
        saveSearchTermsToPreferences(previousSearchTerms);
        adapter.notifyDataSetChanged();
    }

    /**
     * Handles the event when a user clicks on a previous search term.
     * This method initiates a new search operation using the clicked search term.
     *
     * @param searchTerm The search term selected by the user.
     */
    @Override
    public void onItemClick(String searchTerm) {
        // Handle item click event here
        // For example, you can perform a search with the clicked search term
        performSearch(searchTerm);
    }

    /**
     * Inflates the menu options from the XML resource.
     *
     * @param menu The options menu in which you place your items.
     * @return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tabmenu, menu);
        return true;
    }

    /**
     * Handles action bar item clicks. The action bar will automatically handle clicks on the
     * Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            showHelpDialog();
            return true;
        } else if (item.getItemId() == R.id.action_home) {
            // Agrega aquí tu nueva acción para la opción "Home"
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.fav_list) {
            Intent intent = new Intent(DeezerMainActivity.this, FavoriteSongsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Performs a search operation by making a network request to the Deezer API.
     * Fetches artists based on the user's query and navigates to the ArtistListActivity if successful.
     * Displays a toast message if no artists are found or if there is an error.
     *
     * @param searchTerm The search term used to query the Deezer API.
     */
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
                            Intent intent = new Intent(DeezerMainActivity.this, ArtistListActivity.class);
                            intent.putParcelableArrayListExtra("artists", new ArrayList<>(artists));
                            startActivity(intent);
                        } else {
                            Toast.makeText(DeezerMainActivity.this, getString(R.string.no_artist_found), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DeezerMainActivity.this, getString(R.string.error) + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    /**
     * Displays a custom help dialog to the user.
     * This dialog contains a list of instructions or information about how to use the application.
     */
    private void showHelpDialog() {
        // Obtén la lista de instrucciones desde los recursos
        String[] instructionsArray = getResources().getStringArray(R.array.help_instructions_main);

        // Infla el diseño personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.custom_help_dialog, null);

        // Obtiene las referencias a los elementos del diseño
        TextView textTitle = dialogView.findViewById(R.id.text_title);
        ListView listInstructions = dialogView.findViewById(R.id.list_instructions);
        Button buttonOk = dialogView.findViewById(R.id.button_ok);

        // Configura el título del diálogo
        textTitle.setText(R.string.help_dialog_title);

        // Configura un adaptador para la lista de instrucciones
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, instructionsArray);
        listInstructions.setAdapter(adapter);

        // Construye el AlertDialog con el diseño personalizado
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Crea y muestra el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();

        // Configura el clic del botón "OK" para cerrar el diálogo
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
