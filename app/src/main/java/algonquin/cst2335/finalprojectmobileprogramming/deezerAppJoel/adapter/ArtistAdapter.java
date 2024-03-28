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

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Artist> artists;
    private OnViewSongButtonClickListener onViewSongButtonClickListener;

    public ArtistAdapter(Context context, ArrayList<Artist> artists, OnViewSongButtonClickListener onViewSongButtonClickListener) {
        this.context = context;
        this.artists = artists;
        this.onViewSongButtonClickListener = onViewSongButtonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_artist, parent, false);
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView artistImageView;
        TextView artistNameTextView;
        ImageView viewSongButtonImageView;
        OnViewSongButtonClickListener onViewSongButtonClickListener;

        public ViewHolder(@NonNull View itemView, OnViewSongButtonClickListener onViewSongButtonClickListener) {
            super(itemView);
            artistImageView = itemView.findViewById(R.id.pictureImageView);
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

    public interface OnViewSongButtonClickListener {
        void onViewSongButtonClick(int position);
    }
}
