package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.finalprojectmobileprogramming.R;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter.SongAdapter;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Song;

public class SongListActivity extends AppCompatActivity {
    private SongAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        // Retrieve list of songs passed from ArtistListActivity
        ArrayList<Song> songs = getIntent().getParcelableArrayListExtra("songs");

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.songListToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String artistName = getIntent().getStringExtra("artistName");
        getSupportActionBar().setTitle(artistName+ getString(R.string.song_list));

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set adapter
        adapter = new SongAdapter(this, songs);
        recyclerView.setAdapter(adapter);

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
