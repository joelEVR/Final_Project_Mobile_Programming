package algonquin.cst2335.finalprojectmobileprogramming.deezerAppJoel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.finalprojectmobileprogramming.R;

public class PreviousSearchAdapter extends RecyclerView.Adapter<PreviousSearchAdapter.ViewHolder> {

    private List<String> previousSearchTerms;
    private OnItemClickListener onItemClickListener;

    public PreviousSearchAdapter(List<String> searchTerms, OnItemClickListener listener) {
        this.previousSearchTerms = searchTerms;
        this.onItemClickListener = listener;
    }

    // Define interface for click listeners (both delete and item click)
    public interface OnItemClickListener {
        void onDeleteClick(String searchTerm);
        void onItemClick(String searchTerm);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(previousSearchTerms.get(position));
    }

    @Override
    public int getItemCount() {
        return previousSearchTerms.size();
    }

    public void setData(List<String> newSearchTerms) {
        previousSearchTerms.clear();
        previousSearchTerms.addAll(newSearchTerms);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPreviousSearch;
        ImageView imageViewDelete;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewPreviousSearch = itemView.findViewById(R.id.textViewPreviousSearch);
            imageViewDelete = itemView.findViewById(R.id.imageButtonDelete);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(textViewPreviousSearch.getText().toString());
                }
            });

            imageViewDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(textViewPreviousSearch.getText().toString());
                }
            });
        }

        public void bind(String searchTerm) {
            textViewPreviousSearch.setText(searchTerm);
        }
    }
}
