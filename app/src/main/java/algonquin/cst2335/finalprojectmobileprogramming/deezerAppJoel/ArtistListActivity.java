package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
        Toolbar toolbar = findViewById(R.id.songListToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.artist_list_toolbar));

        // Initialize RecyclerView and artists list
        recyclerView = findViewById(R.id.recyclerArtistView);
        artists = getIntent().getParcelableArrayListExtra("artists");
        if (artists != null && !artists.isEmpty()) {
            adapter = new ArtistAdapter(this, artists, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Toast.makeText(this, getString(R.string.no_artist_found), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_home) {
            // Agrega aquí tu nueva acción para la opción "Home"
            Intent intent = new Intent(this, DeezerMainActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_help) {
            showHelpDialog();
            return true;
        } else if (item.getItemId() == R.id.fav_list) {
            Intent intent = new Intent(ArtistListActivity.this, FavoriteSongsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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
                Toast.makeText(ArtistListActivity.this,getString(R.string.error_fetching_top_tracks) + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // Asegúrate de que tienes una instancia de RequestQueue y añade la solicitud
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tabmenu, menu);
        return true;
    }

    private void showHelpDialog() {
        // Obtén la lista de instrucciones desde los recursos
        String[] instructionsArray = getResources().getStringArray(R.array.help_instructions_artist);

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
