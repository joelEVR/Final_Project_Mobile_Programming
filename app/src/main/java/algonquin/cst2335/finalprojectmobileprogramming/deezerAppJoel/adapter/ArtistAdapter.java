package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import algonquin.cst2335.finalprojectmobileprogramming.R;
import algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.models.Artist;

/**
 * Adapter class for displaying a list of artists in a RecyclerView.
 * Each item in the list shows the artist's name and image, and includes a button to view the artist's top songs.
 * Uses Picasso for efficient image loading and caching.
 *
 * This adapter also defines a custom listener interface to handle button clicks for viewing an artist's songs.
 *
 * @author Joan Esteban Velasquez Rodriguez
 * labSection 031
 * creationDate March 31 2023
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Artist> artists;
    private OnViewSongButtonClickListener onViewSongButtonClickListener;

    /**
     * Constructs a new ArtistAdapter.
     *
     * @param context The current context.
     * @param artists A list of Artist objects to be displayed.
     * @param onViewSongButtonClickListener A listener for click events on the "view songs" button.
     */
    public ArtistAdapter(Context context, ArrayList<Artist> artists, OnViewSongButtonClickListener onViewSongButtonClickListener) {
        this.context = context;
        this.artists = artists;
        this.onViewSongButtonClickListener = onViewSongButtonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ViewHolder(view, onViewSongButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Artist artist = artists.get(position);
        holder.artistNameTextView.setText(artist.getName());
        Picasso.get().load(artist.getPictureSmall()).into(holder.artistImageView);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    /**
     * ViewHolder class for artist items. It also implements the View.OnClickListener to handle click events.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView artistImageView;
        TextView artistNameTextView;
        ImageView viewSongButtonImageView;
        OnViewSongButtonClickListener onViewSongButtonClickListener;

        /**
         * Constructs a ViewHolder for artist items.
         *
         * @param itemView The view inflated in onCreateViewHolder method.
         * @param onViewSongButtonClickListener The click listener passed from the adapter.
         */
        public ViewHolder(@NonNull View itemView, OnViewSongButtonClickListener onViewSongButtonClickListener) {
            super(itemView);
            artistImageView = itemView.findViewById(R.id.pictureArtist);
            artistNameTextView = itemView.findViewById(R.id.nameTextView);
            viewSongButtonImageView = itemView.findViewById(R.id.viewSongButtonImageView);
            this.onViewSongButtonClickListener = onViewSongButtonClickListener;

            viewSongButtonImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.viewSongButtonImageView) {
                onViewSongButtonClickListener.onViewSongButtonClick(getAdapterPosition());
            }
        }
    }
    /**
     * Interface for handling click events on the "view songs" button within an artist item.
     */
    public interface OnViewSongButtonClickListener {
        /**
         * Callback method to be invoked when the "view songs" button is clicked.
         *
         * @param position The adapter position of the clicked artist item.
         */
        void onViewSongButtonClick(int position);
    }
}
