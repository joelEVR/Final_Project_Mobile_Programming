package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalprojectmobileprogramming.R;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter.ArtistAdapter;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Artist;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Song;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.service.DeezerService;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.service.DeezerServiceFactory;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.service.TopTracksResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistListActivity extends AppCompatActivity implements ArtistAdapter.OnViewSongButtonClickListener {

    private RecyclerView recyclerView;
    private ArtistAdapter adapter;
    private ArrayList<Artist> artists;
    private DeezerService deezerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Artist List");

        // Initialize RecyclerView and artists list
        recyclerView = findViewById(R.id.recyclerArtistView);
        artists = getIntent().getParcelableArrayListExtra("artists");
        if (artists != null && !artists.isEmpty()) {
            adapter = new ArtistAdapter(this, artists, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Toast.makeText(this, "No artists found", Toast.LENGTH_SHORT).show();
        }

        // Set up Retrofit
        deezerService = DeezerServiceFactory.create();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Navigate` back to the previous activity (search page)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewSongButtonClick(int position) {
        Artist artist = artists.get(position);
        int artistId = artist.getId();
        String artistName = artist.getName();
        fetchTopTracks(artistId,artistName);
    }

    private void fetchTopTracks(int artistId,String artistName) {
        // Make API call to fetch top tracks of the artist using DeezerService
        Call<TopTracksResponse> call = deezerService.getTopTracks(artistId, 50);
        call.enqueue(new Callback<TopTracksResponse>() {
            @Override
            public void onResponse(Call<TopTracksResponse> call, Response<TopTracksResponse> response) {
                if (response.isSuccessful()) {
                    TopTracksResponse topTracksResponse = response.body();
                    if (topTracksResponse != null) {
                        // Handle API response
                        List<TopTracksResponse.Track> tracks = topTracksResponse.getTracks();
                        // Process the tracks and start SongListActivity
                        ArrayList<Song> songs = new ArrayList<>();
                        for (TopTracksResponse.Track track : tracks) {
                            long songId = track.getId();
                            String title = track.getTitle();
                            int duration = track.getDuration();
                            String albumName = track.getAlbum().getTitle();
                            String albumCoverUrl = track.getAlbum().getCoverUrl();
                            String previewSongUrl = track.getPreview();
                            songs.add(new Song(songId,artistName,title, duration, albumName, albumCoverUrl,previewSongUrl));
                        }
                        Intent intent = new Intent(ArtistListActivity.this, SongListActivity.class);
                        intent.putParcelableArrayListExtra("songs", songs);
                        intent.putExtra("artistName", artistName);

                        startActivity(intent);
                    }
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(ArtistListActivity.this, "Error fetching top tracks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TopTracksResponse> call, Throwable t) {
                // Handle failure
                Toast.makeText(ArtistListActivity.this, "Error fetching top tracks: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
