package com.example.workoutapplication;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExerciseManagementAdapter extends RecyclerView.Adapter<ExerciseManagementAdapter.ViewHolder> {

    private String[] localDataSet;

    // Providing a reference to type of views being used
    // (custom ViewHolder)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        // ViewHolder constructed using a given view (which it will hold)
        public ViewHolder(View view) {
            super(view);

            // NOTE: could define click listener here

            // in the view, find the element with id textView
            textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the adapte
     *
     * @param dataSet String[] containing the data to populate views
     *                to be used by RecyclerView
     */
    public ExerciseManagementAdapter(String[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (will be invoked by the layout manager
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false); //TODO add a layout item for this

        return new ViewHolder(view);
    }

    // Replace the contents of a view (will be invoked by layout manager)
    // Alternatively: Adds/changes data in the ViewHolder (different views!)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and
        // replace the contents of the view with that element
        viewHolder.getTextView().setText(localDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

}
