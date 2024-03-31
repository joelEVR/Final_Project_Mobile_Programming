package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter;

import android.content.Context;
import android.media.MediaPlayer;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalprojectmobileprogramming.R;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.dao.SongDatabase;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Song;
public class FavoriteSongAdapter extends RecyclerView.Adapter<FavoriteSongAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Song> songs;
    private MediaPlayer mediaPlayer;
    private boolean[] isPlaying;
    private SongDatabase songDatabase;
    private AlertDialog dialog; // Declare dialog as a class-level variable
    private int currentPlayingPosition = -1;
    private FavoriteSongAdapter.ViewHolder currentPlayingViewHolder;
    public FavoriteSongAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
        this.isPlaying = new boolean[songs.size()];
        this.songDatabase = SongDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(holder.getAdapterPosition());
        holder.titleTextView.setText(song.getTitle());
        holder.albumNameTextView.setText(song.getAlbumName());
        holder.durationTextView.setText(formatDuration(song.getDuration()));
        Picasso.get().load(song.getAlbumCoverUrl()).into(holder.albumCoverImageView);

        // Set click listener for deleteButton button

        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog(holder.getAdapterPosition());

        });

        // Update favorite button icon based on song's favorite status
        // updateFavoriteButtonIcon(holder.favoriteButton, song);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSongDetailPopup(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView albumCoverImageView;
        TextView titleTextView, albumNameTextView, durationTextView;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumCoverImageView = itemView.findViewById(R.id.albumCoverImageView);
            titleTextView = itemView.findViewById(R.id.titleSongTextView);
            albumNameTextView = itemView.findViewById(R.id.albumNameTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            deleteButton = itemView.findViewById(R.id.deleteFavButton);
        }
    }

    private String formatDuration(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format(context.getString(R.string.duration_format), minutes, seconds);
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        builder.setTitle(context.getString(R.string.confirm_deletion))
                .setMessage(context.getString(R.string.confirm_deletion_message))
                .setPositiveButton(context.getString(R.string.delete), (dialog, which) -> deleteSong(position))
                .setNegativeButton(context.getString(R.string.cancel), null)
                .show();
    }



    public void deleteSong(int position) {
        Song song = songs.get(position);

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            songDatabase.songDAO().deleteSong(song);
            ((AppCompatActivity) context).runOnUiThread(() -> {
                songs.remove(position);
                notifyItemRemoved(position);
            });
        });
        Toast.makeText(context, song.getTitle()+" removed from favorites", Toast.LENGTH_SHORT).show();
    }

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
