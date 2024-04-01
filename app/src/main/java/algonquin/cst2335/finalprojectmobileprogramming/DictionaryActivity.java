package algonquin.cst2335.finalprojectmobileprogramming;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectmobileprogramming.databinding.ActivityDictionaryBinding;

/**
 * This class runs the Dictionary Application where a word is entered and on click of search
 * It redirects the user to an API and retrieves the matching definitions of that word
 * @author Samuel Olotu
 *
 */
public class DictionaryActivity extends AppCompatActivity {
    private static final String TAG = "DictionaryActivity";
    /**
     * Viewbinding variable
     */
    ActivityDictionaryBinding binding;
    /**
     * ViewModel class object of DictonaryActivity
     */
    private DictionaryViewModel model;
    /**
     * SharedPreferences File name static variable
     */
    private static final String SHARED_PREFS_FILE = "MyData";
    /**
     * Array to store definitions of a word
     */
    private ArrayList<DictionaryData> wordAndDefinitions = new ArrayList<>();
    /**
     * RecycleView Adapter
     */
    private RecyclerView.Adapter myAdapter;
    /**
     * Client Request to server
     */
    RequestQueue requestQueue;

    /**
     * DAO for DictionaryDatabase
     */
    DictionaryDAO mDAO;
    /**
     * DTO
     */
    DictionaryData data;

    /**
     * On create function to load the Activities layout inclusing widgets
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("DictionaryActivity", "In OnCreate()-widget");

        //ViewModel for SeachText
        model = new ViewModelProvider(this).get(DictionaryViewModel.class);

        binding = ActivityDictionaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Setting Toolbar
        Toolbar toolbar = binding.myToolbar;
        setSupportActionBar(toolbar);

        //Setting Layout for the recycleView
        binding.dictionaryView.setLayoutManager(new LinearLayoutManager(this));

        //Open the Database
        DictionaryDatabase db = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, "database-name").build();
        mDAO = db.stDAO();

        //Shared Preferences for Search Term in edit text
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        String lastSearchTerm = prefs.getString("last_search_term", "");
        binding.searchText.setText(lastSearchTerm);

        //OnClick listener for search button
        binding.searchButton.setOnClickListener(click -> {
            model.searchText.postValue(binding.searchText.getText().toString());


            String word = binding.searchText.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("last_search_term", word);
            editor.apply();

            fetchDefinitions(word);

        });

        //OnClick listener for save button
        binding.save.setOnClickListener(click -> {
            // Iterate over the list of wordAndDefinitions to save each entry
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                // Save the data object
                mDAO.insertWord(data);

                runOnUiThread(() -> Toast.makeText(DictionaryActivity.this, "Word and Definitions have been saved", Toast.LENGTH_SHORT).show());
            });
        });


        binding.savedButton.setOnClickListener(click -> {
            // Retrieve all saved search terms from the database
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                List<DictionaryData> savedWords = mDAO.getAllWords();
                List<String> savedSearchTerms = new ArrayList<>();
                for (DictionaryData word : savedWords) {
                    savedSearchTerms.add(word.getSearchTerm());
                }
                runOnUiThread(() -> {
                    // Show a dialog or list to allow the user to select a saved search term
                    AlertDialog.Builder builder = new AlertDialog.Builder(DictionaryActivity.this);
                    builder.setTitle("Saved Search Terms");
                    builder.setItems(savedSearchTerms.toArray(new String[0]), (dialog, which) -> {
                        // When a search term is selected, display its definitions
                        String selectedTerm = savedSearchTerms.get(which);
                        showDefinitionsForSavedTerm(selectedTerm);
                    });
                    builder.create().show();
                });
            });
        });


        binding.dictionaryView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            /**
             * This function creates a ViewHolder object. It represents a single row in the list
             * @param parent   The ViewGroup into which the new View will be added after it is bound to
             *                 an adapter position.
             * @param viewType The view type of the new View.
             * @return MyRowHolder
             */
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_definition, parent, false);
                return new MyRowHolder(itemView);
            }

            /**
             * This initializes a ViewHolder to go at the row specified by the position parameter.
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                DictionaryData currentTerm = wordAndDefinitions.get(position);
                holder.searchTermText.setText(currentTerm.getSearchTerm());

                holder.definitionText.setText(formatDefinitions(currentTerm.getDefinitionsOfTerm()));

            }

            /**
             * This function just returns an int specifying how many items to draw.
             * @return int
             */
            @Override
            public int getItemCount() {
                return wordAndDefinitions.size();
            }

            public int getItemViewType(int position) {
                return 0;
            }
        });


    }

    private void showDefinitionsForSavedTerm(String selectedTerm) {
        // Retrieve definitions for the saved term from the database
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            List<DictionaryData> savedDefinitions = mDAO.getAllWordsWithSearchTerm(selectedTerm);
            runOnUiThread(() -> {
                // Display definitions to the user
                if (!savedDefinitions.isEmpty()) {
                    // Show a dialog or list to display definitions
                    AlertDialog.Builder builder = new AlertDialog.Builder(DictionaryActivity.this);
                    builder.setTitle("Definitions for " + selectedTerm);
                    builder.setItems(savedDefinitions.toArray(new String[0]), (dialog, which) -> {
                        // When a definition is selected, ask for confirmation to delete it
                        confirmDeleteDefinition(savedDefinitions.get(which));
                    });
                    builder.create().show();
                } else {
                    Toast.makeText(DictionaryActivity.this, "No definitions found for " + selectedTerm, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void confirmDeleteDefinition(DictionaryData dictionaryData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this definition?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Delete the definition from the database
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                mDAO.deleteWord(dictionaryData);
                runOnUiThread(() -> Toast.makeText(DictionaryActivity.this, "Definition deleted", Toast.LENGTH_SHORT).show());
            });
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }


    /**
     * Sends request to Dictionary API and fetches the definitions related to the word entered by user
     *
     * @param word Word entered by user
     */
    public void fetchDefinitions(String word) {
        String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response -> {
                    ArrayList<DictionaryData> definitionsList = new ArrayList<>();

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String searchTerm = jsonObject.getString("word");
                            JSONArray meanings = jsonObject.getJSONArray("meanings");

                            // List to store definitions of the current term
                            ArrayList<String> definitions = new ArrayList<>();

                            for (int j = 0; j < meanings.length(); j++) {
                                JSONObject meaning = meanings.getJSONObject(j);
                                JSONArray definitionsArray = meaning.getJSONArray("definitions");
                                for (int k = 0; k < definitionsArray.length(); k++) {
                                    JSONObject definitionObj = definitionsArray.getJSONObject(k);
                                    String definition = definitionObj.getString("definition");
                                    definitions.add(definition);
                                }
                            }

                            // Create a DictionaryData object and add it to the list
                            data = new DictionaryData();
                            data.setSearchTerm(searchTerm);
                            data.setDefinitionsOfTerm(definitions);
                            definitionsList.add(data);
                        }

                        // Update RecyclerView adapter with the new data
                        wordAndDefinitions.clear();
                        wordAndDefinitions.addAll(definitionsList);
                        myAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                    }

                },
                error -> {
                    Log.e(TAG, "Error fetching data", error);
                }
        );

        requestQueue.add(jsonArrayRequest);
    }


    /**
     * an object for representing everything that goes on a row in the list
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView searchTermText;
        TextView definitionText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            searchTermText = itemView.findViewById(R.id.word);
            definitionText = itemView.findViewById(R.id.definitionText);
        }
    }

    private String formatDefinitions(ArrayList<String> definitions) {
        StringBuilder formattedDefinitions = new StringBuilder();
        for (String definition : definitions) {
            formattedDefinitions.append("\u2022 ").append(definition).append("\n"); // Unicode for bullet point
        }
        return formattedDefinitions.toString();
    }

    //Toolbar Menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mainIcon) {
            Intent mainPage = new Intent(DictionaryActivity.this, MainActivity.class);
            startActivity(mainPage);
            return true;
        }
         else if (item.getItemId() == R.id.help_Icon) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Help");
            builder.setMessage("Instructions for using the Dictionary API:\n\n" +
                    "1. Enter a word to look up the definition.\n" +
                    "2. Definitions will be displayed in a RecyclerView.\n" +
                    "3. You can save search terms and definitions locally.\n" +
                    "4. Use the 'View Saved Terms' button to view saved terms.\n" +
                    "5. SharedPreferences will save your last search term.");
            builder.setPositiveButton("OK", ((dialog, click) ->{} )).create().show();
            return true;
        }else {

        }
        return false;
    }
}
