package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalprojectmobileprogramming.R;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter.ArtistAdapter;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Artist;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Song;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.service.TopTracksResponse;

public class ArtistListActivity extends AppCompatActivity implements ArtistAdapter.OnViewSongButtonClickListener {

    private RecyclerView recyclerView;
    private ArtistAdapter adapter;
    private ArrayList<Artist> artists;

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

    private void fetchTopTracks(final int artistId, final String artistName) {
        String url = "https://api.deezer.com/artist/" + artistId + "/top?limit=50";

        // Crear una solicitud de Volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Aquí necesitas parsear la respuesta JSON a objetos Java
                        Gson gson = new Gson();
                        TopTracksResponse topTracksResponse = gson.fromJson(response, TopTracksResponse.class);
                        List<TopTracksResponse.Track> tracks = topTracksResponse.getTracks();
                        ArrayList<Song> songs = new ArrayList<>();
                        for (TopTracksResponse.Track track : tracks) {
                            long songId = track.getId();
                            String title = track.getTitle();
                            int duration = track.getDuration();
                            String albumName = track.getAlbum().getTitle();
                            String albumCoverUrl = track.getAlbum().getCoverUrl();
                            songs.add(new Song(songId, artistName, title, duration, albumName, albumCoverUrl));
                        }
                        Intent intent = new Intent(ArtistListActivity.this, SongListActivity.class);
                        intent.putParcelableArrayListExtra("songs", songs);
                        intent.putExtra("artistName", artistName);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ArtistListActivity.this, "Error fetching top tracks: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Asegúrate de que tienes una instancia de RequestQueue y añade la solicitud
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
