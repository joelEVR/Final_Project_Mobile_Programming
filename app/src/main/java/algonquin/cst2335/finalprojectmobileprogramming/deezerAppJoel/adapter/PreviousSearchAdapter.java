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
/**
 * Adapter for displaying a list of previous search terms in a RecyclerView.
 * Allows users to interact with search terms by clicking on an item to re-execute the search
 * or by clicking a delete icon to remove the search term from the list.
 *
 * Includes interfaces for delete and item click listeners to allow the calling activity
 * or fragment to define specific behaviors for these actions.
 *
 * @author Joel Esteban Velasquez Rodriguez
 * @labSection 031
 * @creationDate Insert the creation date here (e.g., "April 17, 2023")
 */
public class PreviousSearchAdapter extends RecyclerView.Adapter<PreviousSearchAdapter.ViewHolder> {

    /**
     * List of search terms to be displayed.
     */
    private ArrayList<String> searchTerms;

    /**
     * Listener for delete button click events.
     */
    private OnDeleteClickListener onDeleteClickListener;

    /**
     * Listener for item click events.
     */
    private OnClickListener onClickListener;

    /**
     * Constructs a new adapter with the specified list of search terms.
     *
     * @param searchTerms The list of search terms to display.
     */
    public PreviousSearchAdapter(ArrayList<String> searchTerms) {
        this.searchTerms = searchTerms;
    }

    /**
     * Interface definition for a callback to be invoked when the delete button is clicked.
     */
    public interface OnDeleteClickListener {
        void onDeleteClick(String searchTerm);
    }

    /**
     * Sets the listener for delete button click events.
     *
     * @param listener The listener to set.
     */
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when an item is clicked.
     */
    public interface OnClickListener {
        void onItemClick(String searchTerm);
    }

    /**
     * Sets the listener for item click events.
     *
     * @param listener The listener to set.
     */
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

    /**
     * Updates the list of search terms displayed by the adapter.
     *
     * @param newSearchTerms The new list of search terms to display.
     */
    public void setData(List<String> newSearchTerms) {
        searchTerms.clear();
        searchTerms.addAll(newSearchTerms);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for previous search terms items.
     * Holds references to the text view and delete image view for each item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPreviousSearch;
        ImageView imageViewDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPreviousSearch = itemView.findViewById(R.id.textViewPreviousSearch);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }
}