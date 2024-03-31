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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.finalprojectmobileprogramming.R;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter.SongAdapter;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Song;

/**
 * Displays a list of songs for a selected artist. Users can view song details and navigate
 * to additional functionalities such as viewing favorite songs. This activity receives a list
 * of songs from {@link ArtistListActivity} and displays them using a {@link RecyclerView}.
 *
 * @author Joel Esteban Velasquez Rodriguez
 * @labSection 031
 * @creationDate (please insert the creation date here, e.g., "April 16, 2023")
 */
public class SongListActivity extends AppCompatActivity {
    private SongAdapter adapter;

    /**
     * Initializes the activity, sets up the toolbar, and prepares the RecyclerView
     * for displaying the list of songs.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously
     *                           being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle).
     */
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

    /**
     * Inflates the options menu when the activity is created.
     *
     * @param menu The options menu in which items are placed.
     * @return True for the menu to be displayed; false if it should not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tabmenu, menu);
        return true;
    }

    /**
     * Handles action bar item clicks here.
     *
     * @param item The menu item that was selected.
     * @return False to allow normal menu processing to proceed, true to consume it here.
     */
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
            Intent intent = new Intent(SongListActivity.this, FavoriteSongsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Displays a help dialog with instructions on how to use the song list feature.
     * The dialog lists instructions defined in the resources.
     */
    private void showHelpDialog() {
        // Obtén la lista de instrucciones desde los recursos
        String[] instructionsArray = getResources().getStringArray(R.array.help_instructions_song);

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
