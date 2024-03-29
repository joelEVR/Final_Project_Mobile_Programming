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

    public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

        private static final String TAG = "SongAdapter";

        private Context context;
        private ArrayList<Song> songs;
        private SongDatabase songDatabase;
        private AlertDialog dialog;
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
                titleTextView = itemView.findViewById(R.id.titleTextView);
                albumNameTextView = itemView.findViewById(R.id.albumNameTextView);
                durationTextView = itemView.findViewById(R.id.durationTextView);
                favoriteButton = itemView.findViewById(R.id.favoriteButton);
            }
        }

        private String formatDuration(int duration) {
            int minutes = duration / 60;
            int seconds = duration % 60;
            return String.format("%d:%02d", minutes, seconds);
        }

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

        private void updateFavoriteButtonIcon(ImageButton favoriteButton, Song song) {
            if (isSongFavorite(song)) {
                favoriteButton.setImageResource(R.drawable.ic_delete); // Set filled favorite icon
            } else {
                favoriteButton.setImageResource(R.drawable.ic_fav); // Set empty favorite icon
            }
        }

        private void addSongToDatabase(Song song, ImageButton favoriteButton) {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                long id = songDatabase.songDAO().insertSong(song);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (id != -1) {
                        Toast.makeText(context, song.getTitle()+" added to favorites", Toast.LENGTH_SHORT).show();
                        updateFavoriteButtonIcon(favoriteButton, song);
                    } else {
                        Toast.makeText(context, "Failed to add song to favorites", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        private void deleteSongFromDatabase(Song song, ImageButton favoriteButton) {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                songDatabase.songDAO().deleteSong(song);
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(context, song.getTitle()+" removed from favorites", Toast.LENGTH_SHORT).show();
                    updateFavoriteButtonIcon(favoriteButton, song);
                });
            });
        }

        private void showSongDetailPopup(Song song) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.song_detail_popup, null);
            builder.setView(dialogView);

            TextView titleTextView = dialogView.findViewById(R.id.titleTextView);
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
