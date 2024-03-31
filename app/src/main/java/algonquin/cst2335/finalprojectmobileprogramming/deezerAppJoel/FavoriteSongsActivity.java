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
/**
 * Activity for displaying and managing the user's favorite songs.
 * Users can view their list of favorite songs saved in the local database.
 * This activity utilizes a {@link RecyclerView} to list the favorite songs and provides
 * functionality to interact with these songs, including viewing detailed information.
 *
 * @author Joel Esteban Velasquez Rodriguez
 * @labSection 031
 * @creationDate (please insert the creation date here, e.g., "April 15, 2023")
 */
public class FavoriteSongsActivity extends AppCompatActivity {

    /**
     * Database instance for accessing favorite songs stored in the local database.
     */
    private SongDatabase songDatabase;
    /**
     * Adapter for the RecyclerView that displays the list of favorite songs.
     */
    private FavoriteSongAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_songs);

        // Initialize the database instance
        songDatabase = SongDatabase.getInstance(this);

        Toolbar toolbar = findViewById(R.id.songListToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.favorite_songs));

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

    /**
     * Inflates the options menu.
     *
     * @param menu The options menu in which menu items are placed.
     * @return True for the menu to be displayed; false will not show the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tabmenu, menu);
        return true;
    }

    /**
     * Handles action bar item clicks.
     *
     * @param item The menu item that was selected.
     * @return Boolean Return false to allow normal menu processing to proceed, true to consume it here.
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
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Displays a help dialog to the user.
     * The dialog contains instructions on how to use the favorite songs functionality.
     */
    private void showHelpDialog() {
        // Obtén la lista de instrucciones desde los recursos
        String[] instructionsArray = getResources().getStringArray(R.array.help_instructions_favorites);

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
