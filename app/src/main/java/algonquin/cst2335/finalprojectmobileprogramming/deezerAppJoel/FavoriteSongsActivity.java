package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectmobileprogramming.R;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter.FavoriteSongAdapter;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.dao.SongDatabase;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Song;
public class FavoriteSongsActivity extends AppCompatActivity {

    private SongDatabase songDatabase;
    private FavoriteSongAdapter adapter; // Declare adapter field

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_songs);

        // Initialize the database instance
        songDatabase = SongDatabase.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Favorite Songs");

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve favorite songs from the database in a background thread
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            ArrayList<Song> songs = new ArrayList<>(songDatabase.songDAO().getAllSongs());

            // Update UI on the main thread
            runOnUiThread(() -> {
                // Assign adapter field instead of creating a new local variable
                adapter = new FavoriteSongAdapter(this, songs);
                recyclerView.setAdapter(adapter);
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
