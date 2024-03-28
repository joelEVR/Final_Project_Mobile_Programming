package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalprojectmobileprogramming.R;
public class PreviousSearchAdapter extends RecyclerView.Adapter<PreviousSearchAdapter.ViewHolder> {

    private ArrayList<String> searchTerms;
    private OnDeleteClickListener onDeleteClickListener; // Add interface reference
    private OnClickListener onClickListener;

    public PreviousSearchAdapter(ArrayList<String> searchTerms) {
        this.searchTerms = searchTerms;
    }

    // Define interface for delete click listener
    public interface OnDeleteClickListener {
        void onDeleteClick(String searchTerm);
    }

    // Set delete click listener
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }

    // Define interface for item click listener
    public interface OnClickListener {
        void onItemClick(String searchTerm);
    }

    // Set item click listener
    public void setOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_previous_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String searchTerm = searchTerms.get(position);
        holder.textViewPreviousSearch.setText(searchTerm);

        // Set click listener for the entire item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onItemClick(searchTerm);
                }
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(searchTerm);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchTerms.size();
    }

    public void setData(List<String> newSearchTerms) {
        searchTerms.clear();
        searchTerms.addAll(newSearchTerms);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPreviousSearch;
        ImageView imageViewDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPreviousSearch = itemView.findViewById(R.id.textViewPreviousSearch);
            imageViewDelete = itemView.findViewById(R.id.imageButtonDelete);
        }
    }
}