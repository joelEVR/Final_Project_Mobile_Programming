package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import algonquin.cst2335.finalprojectmobileprogramming.R;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.dao.SongDatabase;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Song;

/**
 * Adapter for displaying a list of songs in a RecyclerView.
 * This adapter supports operations such as displaying song details, toggling a song's favorite status,
 * and showing a detailed popup for each song. It utilizes Picasso for image loading and manages
 * song favorites within a local database.
 *
 * @author Joel Esteban Velasquez Rodriguez
 * labSection 031
 * creationDate March 31 2023
 */
    public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

        private static final String TAG = "SongAdapter";

        private Context context;
        private ArrayList<Song> songs;
        private SongDatabase songDatabase;
        private AlertDialog dialog;

    /**
     * Constructs a new SongAdapter.
     *
     * @param context Context for accessing resources and performing UI updates.
     * @param songs List of Song objects to be displayed by the adapter.
     */
        public SongAdapter(Context context, ArrayList<Song> songs) {
            this.context = context;
            this.songs = songs;
            this.songDatabase = SongDatabase.getInstance(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Song song = songs.get(holder.getAdapterPosition());
            holder.titleTextView.setText(song.getTitle());
            holder.albumNameTextView.setText(song.getAlbumName());
            holder.durationTextView.setText(formatDuration(song.getDuration()));
            Picasso.get().load(song.getAlbumCoverUrl()).into(holder.albumCoverImageView);

            // Set click listener for favorite button
            holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFavorite(song, holder.favoriteButton);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSongDetailPopup(song);
                }
            });

            // Update favorite button icon based on song's favorite status
            updateFavoriteButtonIcon(holder.favoriteButton, song);

        }

        @Override
        public int getItemCount() {
            return songs.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView albumCoverImageView;
            TextView titleTextView, albumNameTextView, durationTextView;
            ImageButton favoriteButton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                albumCoverImageView = itemView.findViewById(R.id.albumCoverImageView);
                titleTextView = itemView.findViewById(R.id.titleSongTextView);
                albumNameTextView = itemView.findViewById(R.id.albumNameTextView);
                durationTextView = itemView.findViewById(R.id.durationTextView);
                favoriteButton = itemView.findViewById(R.id.favoriteButton);
            }
        }

    /**
     * Formats a song duration from seconds to a mm:ss string.
     *
     * @param duration The song's duration in seconds.
     * @return A formatted string representing the duration.
     */
        private String formatDuration(int duration) {
            int minutes = duration / 60;
            int seconds = duration % 60;
            return String.format("%d:%02d", minutes, seconds);
        }

    /**
     * Toggles the favorite status of a song.
     *
     * @param song The song whose favorite status is to be toggled.
     * @param favoriteButton The ImageButton representing the favorite toggle button.
     */
        private void toggleFavorite(Song song, ImageButton favoriteButton) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                boolean isFavorite = isSongFavorite(song);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (isFavorite) {
                        deleteSongFromDatabase(song, favoriteButton);
                    } else {
                        addSongToDatabase(song, favoriteButton);
                    }
                });
            });
            executor.shutdown();
        }

    /**
     * Checks if a song is marked as favorite in the database.
     *
     * @param song The song to check.
     * @return true if the song is a favorite, false otherwise.
     */
        private boolean isSongFavorite(Song song) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Boolean> future = executor.submit(() -> {
                List<Song> songsInDatabase = songDatabase.songDAO().getAllSongs();
                for (Song dbSong : songsInDatabase) {
                    if (dbSong.getId() == song.getId()) {
                        return true;
                    }
                }
                return false;
            });
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return false;
            } finally {
                executor.shutdown();
            }
        }

    /**
     * Updates the favorite button icon based on the song's favorite status.
     *
     * @param favoriteButton The ImageButton representing the favorite toggle button.
     * @param song The song whose favorite status determines the icon.
     */
        private void updateFavoriteButtonIcon(ImageButton favoriteButton, Song song) {
            if (isSongFavorite(song)) {
                favoriteButton.setImageResource(R.drawable.ic_unfav); // Set filled favorite icon
            } else {
                favoriteButton.setImageResource(R.drawable.ic_fav); // Set empty favorite icon
            }
        }

    /**
     * Adds a song to the favorites in the database.
     *
     * @param song The song to be added to favorites.
     * @param favoriteButton The ImageButton representing the favorite toggle button.
     */
        private void addSongToDatabase(Song song, ImageButton favoriteButton) {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                long id = songDatabase.songDAO().insertSong(song);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (id != -1) {
                        String messageAddSong = context.getString(R.string.added_to_favorites, song.getTitle());
                        Snackbar.make(favoriteButton, messageAddSong, Snackbar.LENGTH_SHORT).show();
                        updateFavoriteButtonIcon(favoriteButton, song);
                    } else {
                        Snackbar.make(favoriteButton, R.string.failed_to_add_song_to_favorites, Snackbar.LENGTH_SHORT).show();
                    }
                });
            });
        }

    /**
     * Deletes a song from the favorites in the database.
     *
     * @param song The song to be removed from favorites.
     * @param favoriteButton The ImageButton representing the favorite toggle button.
     */
        private void deleteSongFromDatabase(Song song, ImageButton favoriteButton) {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                songDatabase.songDAO().deleteSong(song);
                new Handler(Looper.getMainLooper()).post(() -> {
                    String messageDeleteSong = context.getString(R.string.deleted_from_favorites, song.getTitle());
                    Toast.makeText(context, messageDeleteSong, Toast.LENGTH_SHORT).show();
                    updateFavoriteButtonIcon(favoriteButton, song);
                });
            });
        }

    /**
     * Shows a popup dialog with detailed information about a song.
     *
     * @param song The song for which details are to be displayed.
     */
        private void showSongDetailPopup(Song song) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.song_detail_popup, null);
            builder.setView(dialogView);

            TextView titleTextView = dialogView.findViewById(R.id.titleSongTextView);
            TextView artistTextView = dialogView.findViewById(R.id.artistTextView);
            TextView durationTextView = dialogView.findViewById(R.id.durationTextView);
            TextView albumNameTextView = dialogView.findViewById(R.id.albumNameTextView);
            Button closeButton = dialogView.findViewById(R.id.closeButton);
            ImageView albumCoverImageView = dialogView.findViewById(R.id.albumCoverImageView);

            // Set song details
            titleTextView.setText(song.getTitle());
            artistTextView.setText(song.getArtist());
            durationTextView.setText(formatDuration(song.getDuration()));
            albumNameTextView.setText(song.getAlbumName());

            // Load album cover image using Picasso
            Picasso.get().load(song.getAlbumCoverUrl()).into(albumCoverImageView);

            // Set close button click listener
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog = builder.create(); // Assign dialog here
            dialog.show();
        }

    }
