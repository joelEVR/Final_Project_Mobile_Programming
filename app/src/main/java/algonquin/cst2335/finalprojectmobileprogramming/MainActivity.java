package algonquin.cst2335.finalprojectmobileprogramming;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.DeezerMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece land_page.xml como el layout de esta actividad
        setContentView(R.layout.land_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_land_page, menu);
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
        } else if (item.getItemId() == R.id.samuel) {
            // Agrega aquí tu nueva acción para la opción "Home"
            Intent intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.joel) {
            Intent intent = new Intent(MainActivity.this, DeezerMainActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.ting) {
            // Agrega aquí tu nueva acción para la opción "Home"
            Intent intent = new Intent(this, MainActivityTing.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
