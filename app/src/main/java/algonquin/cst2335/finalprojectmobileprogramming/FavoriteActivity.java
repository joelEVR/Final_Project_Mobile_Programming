/**
 * Student Name: Ting Cheng
 * Professor: Samira Ouaaz
 * Due Date: Apr. 1,2024
 * Description:  CST2355-031 FinalAssignment
 * Section: 031
 * Modify Date: Mar. 31,2024
 */
package algonquin.cst2335.finalprojectmobileprogramming;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectmobileprogramming.data.LocationDatabase;
import algonquin.cst2335.finalprojectmobileprogramming.data.LocationItem;
import algonquin.cst2335.finalprojectmobileprogramming.databinding.ActivityFavoriteBinding;
import algonquin.cst2335.finalprojectmobileprogramming.databinding.ActivityMainBinding;
import algonquin.cst2335.finalprojectmobileprogramming.databinding.LocationItemBinding;

/**
 * This activity represents the favorites screen of the application, where users can view,
 * search, and delete their saved locations. It utilizes a RecyclerView to list the saved locations,
 * and allows deletion of individual items or all items at once. Users can also return to the main screen
 * by selecting a location, which will populate the search fields with that location's data.
 *
 * @author Ting Cheng
 * @version 3.0
 * @since 2024-03-22
 */
public class FavoriteActivity extends AppCompatActivity {
    /**
     * Binding instance for interacting with the FavoriteActivity layout views.
     */
    ActivityFavoriteBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    /**
     * The adapter for the RecyclerView, responsible for binding data to views.
     */
    private List<LocationItem> locationItems = new ArrayList<>();
    /**
     * A list holding the saved locations displayed in the RecyclerView.
     */
    private final Executor executor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize View Binding
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // create Toolbar
        setSupportActionBar(binding.toolbarLayout.toolbar);
        // The layout manager specifies RecyclerView items is arranged in a vertical column.
        binding.favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // The adapter is set up for RecyclerView, and is responsible for binding the data
        // from the data source to each entry of RecyclerView.
        binding.favoritesRecyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Use View Binding to create a view for each item
                LocationItemBinding itemBinding = LocationItemBinding.inflate
                        (LayoutInflater.from(parent.getContext()), parent, false);
                return  new MyViewHolder(itemBinding, position -> {
                    LocationItem itemToRemove = locationItems.get(position);
                    // The database is deleted asynchronously
                    executor.execute(() -> {
                        LocationDatabase db = LocationDatabase.getDatabase(getApplicationContext());
                        db.locationItemDao().deleteLocation(itemToRemove);
                        // Run on the UI thread to update the UI
                        runOnUiThread(() -> {
                            // Removes the list data source and notifies the adapter of the update
                            locationItems.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, locationItems.size());
                        });
                    });
                });
            }
            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                // Gets the LocationItem object for the current location
                LocationItem item = locationItems.get(position);
                holder.bind(item, position);
            }
            @Override
            public int getItemCount() {
                return locationItems.size();
            }
        });

        // Load the location in the database
        loadLocationsFromDatabase();

        // Set the text change listener for the search box。 Brand-new using TextWatcher!!!!
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterLocations(s.toString());
            }
        });
    }

    /**
     * Loads the saved locations from the database and updates the RecyclerView which called in onCreate()
     */
    private void loadLocationsFromDatabase() {
        new Thread(() -> {
            // To avoid creating multiple database instances, use the singleton mode
            LocationDatabase db = LocationDatabase.getDatabase(getApplicationContext());
            List<LocationItem> items = db.locationItemDao().getAllLocations();
            runOnUiThread(() -> {
                locationItems.clear();
                locationItems.addAll(items);
                binding.favoritesRecyclerView.getAdapter().notifyDataSetChanged();
            });
        }).start();
    }

    /**
     * An interface defining a callback method to be invoked when a location item is deleted.
     * This allows communication between the {@link MyViewHolder}
     * and the containing {@link FavoriteActivity}
     * to handle the deletion of a location item from the RecyclerView and database.
     */
    // VERY IMPORTANT interface!!! USING IN MyViewHolder
    public interface OnLocationItemDeleted {
        /**
         * Callback method to be invoked when a location item is deleted.
         *
         * @param position The position of the item in the adapter's data set that was deleted.
         */
        void onItemDeleted(int position);
    }

    // Key Components -- MyViewHolder!!!!
    /**
     * {@link RecyclerView.ViewHolder} subclass used for displaying individual location items
     * within a {@link RecyclerView} in {@link FavoriteActivity}. This class binds location data to the views
     * defined in the {@link LocationItemBinding} layout and sets up click listeners for interactions,
     * such as clicking on a location item to navigate back to the {@link MainActivity} with the item's data.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        /**
         * The binding for accessing the layout's views for a single location item.
         * It allows direct interaction with the views defined in the corresponding XML layout file,
         * enabling the setting of text, onClickListeners, and other properties.
         */
        private final LocationItemBinding binding;
        /**
         * A callback interface instance for handling deletion events of location items.
         * This interface method is invoked when the delete button within a location item view is clicked,
         * signaling to the containing activity or fragment that the item should be removed from both the
         * RecyclerView adapter's dataset and the underlying data source, such as a database or a remote server.
         */
        private final OnLocationItemDeleted deleteCallback;

        /**
         * Constructs a new {@link MyViewHolder} instance.
         *
         * @param binding        The {@link LocationItemBinding} for the location item layout.
         * @param deleteCallback The callback to be invoked when the delete button is clicked.
         */
        public MyViewHolder(LocationItemBinding binding, OnLocationItemDeleted deleteCallback) {
            super(binding.getRoot());
            this.binding = binding;
            this.deleteCallback = deleteCallback;
        }

        // VERY Special Handler!!!!!!!!!
        /**
         * Binds a {@link LocationItem} to the view holder, setting the location's name, latitude,
         * and longitude to the respective TextViews. Also sets up a click listener on the delete button
         * to handle the deletion of the location item.
         *
         * @param item     The {@link LocationItem} to be displayed.
         * @param position The position of the item in the data set.
         */
        void bind(LocationItem item, int position) {
            binding.name.setText(item.getName());
            binding.latitude.setText(String.valueOf(item.getLatitude()));
            binding.longitude.setText(String.valueOf(item.getLongitude()));
            // Set the click listener for the entire entry
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(binding.getRoot().getContext(), MainActivity.class);
                intent.putExtra(getString(R.string.latitude), item.getLatitude());
                intent.putExtra(getString(R.string.longitude), item.getLongitude());
                intent.putExtra(getString(R.string.name), item.getName());
                binding.getRoot().getContext().startActivity(intent);
            });
            // Set the click listener for the delete button
            binding.deleteButton.setOnClickListener(v -> {
                // Call delete callback !!!!
                deleteCallback.onItemDeleted(position);
            });
        }
    }


    // onCreateOptionsMenu + onOptionsItemSelected to handle Menu events
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.all_delete) {
            showConfirmDeleteDialog();
        } else if (itemId == R.id.location_instruction) {
                Toast.makeText(this, getString(R.string.FavoriteActivity_Instruction), Toast.LENGTH_SHORT).show();
        } else {
                throw new IllegalStateException(getString(R.string.error) + itemId);
        }
        return true;
    }

    // ShowConfirmDeleteDialog + deleteAllLocations to realize DeleteAll events
    /**
     * Shows a dialog to confirm the deletion of all saved locations.
     */
    private void showConfirmDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.clear_locations))
                .setMessage(getString(R.string.sure_to_delete_all))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllLocations();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }
    /**
     * Deletes all saved locations from the database and updates the UI.
     */
    private void deleteAllLocations() {
        new Thread(() -> {
            LocationDatabase db = LocationDatabase.getDatabase(getApplicationContext());
            db.locationItemDao().deleteAllLocations();
            // Remove all items from the list and notify the adapter of the change on the UI thread
            runOnUiThread(() -> {
                locationItems.clear();
                binding.favoritesRecyclerView.getAdapter().notifyDataSetChanged();
            });
        }).start();
    }


    // filterLocations + updateAdapterData to realize KeywordSearch
    /**
     * Filters the displayed locations based on the given query string.
     *
     * @param query The search query to filter the locations.
     */
    private void filterLocations(String query) {
        List<LocationItem> filteredList = new ArrayList<>();
        for (LocationItem item : locationItems) {
            // Assume that the location name is in the name attribute of the LocationItem
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        // Update adapter data and refresh RecyclerView
        updateAdapterData(filteredList);
    }
    /**
     * Updates the data set of the adapter and refreshes the RecyclerView.
     *
     * @param filteredList The list of locations that match the search query.
     */
    private void updateAdapterData(List<LocationItem> filteredList) {
        locationItems.clear();
        locationItems.addAll(filteredList);
        binding.favoritesRecyclerView.getAdapter().notifyDataSetChanged();
    }

}