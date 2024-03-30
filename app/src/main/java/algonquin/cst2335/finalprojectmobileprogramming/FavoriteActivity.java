package algonquin.cst2335.finalprojectmobileprogramming;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class FavoriteActivity extends AppCompatActivity {

    ActivityFavoriteBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private List<LocationItem> locationItems = new ArrayList<>();

    private final Executor executor = Executors.newSingleThreadExecutor();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_favorite);
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
                LocationItem item = locationItems.get(position); // 假设这是你的数据源中的项
                holder.bind(item, position);
            }

            @Override
            public int getItemCount() {
                return locationItems.size(); // 假设这是你的数据源大小
            }
        });

        // Load the location in the database
        loadLocationsFromDatabase();

        // Set the text change listener for the search box
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


    // Define loadLocationsFromDatabase calling in onCreate()
    private void loadLocationsFromDatabase() {
        new Thread(() -> {
            // To avoid creating multiple database instances, use the singleton mode
            LocationDatabase db = LocationDatabase.getDatabase(getApplicationContext());
            List<LocationItem> items = db.locationItemDao().getAllLocations(); // 假设你有这样的方法
            runOnUiThread(() -> {
                locationItems.clear();
                locationItems.addAll(items);
                binding.favoritesRecyclerView.getAdapter().notifyDataSetChanged();
            });
        }).start();
    }

    // VERY IMPORTANT interface!!! USING IN MyViewHolder
    public interface OnLocationItemDeleted {
        void onItemDeleted(int position);
    }

    // Key Components -- MyViewHolder!!!!
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final LocationItemBinding binding;
        private final OnLocationItemDeleted deleteCallback;

        public MyViewHolder(LocationItemBinding binding, OnLocationItemDeleted deleteCallback) {
            super(binding.getRoot());
            this.binding = binding;
            this.deleteCallback = deleteCallback;
        }

        void bind(LocationItem item, int position) {
            binding.name.setText(item.getName());
            binding.latitude.setText(String.valueOf(item.getLatitude()));
            binding.longitude.setText(String.valueOf(item.getLongitude()));
            // Set the click listener for the delete button
            binding.deleteButton.setOnClickListener(v -> {
                // Call delete callback !!!!
                deleteCallback.onItemDeleted(position);
            });
        }
    }


    // USELESS!!Defines an interface to handle the deletion of location items
    public interface LocationItemListener {
        void onDeleteLocation(LocationItem item);
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
    private void showConfirmDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除所有收藏的位置吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 删除所有位置的逻辑
                        deleteAllLocations();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    private void deleteAllLocations() {
        new Thread(() -> {
            LocationDatabase db = LocationDatabase.getDatabase(getApplicationContext());
            db.locationItemDao().deleteAllLocations(); // 假设你有这样的方法
            // 从列表中移除所有项目，并在UI线程上通知适配器更改
            runOnUiThread(() -> {
                locationItems.clear();
                binding.favoritesRecyclerView.getAdapter().notifyDataSetChanged();
            });
        }).start();
    }


    // filterLocations + updateAdapterData to realize KeywordSearch
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
    private void updateAdapterData(List<LocationItem> filteredList) {
        locationItems.clear();
        locationItems.addAll(filteredList);
        binding.favoritesRecyclerView.getAdapter().notifyDataSetChanged();
    }

}