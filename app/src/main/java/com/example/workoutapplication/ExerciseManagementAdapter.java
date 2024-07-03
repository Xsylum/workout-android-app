package com.example.workoutapplication;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ExerciseManagementAdapter extends RecyclerView.Adapter<ExerciseManagementAdapter.ExerciseViewHolder> {

    private Exercise[] localDataSet;

    // Providing a reference to type of views being used
    // (custom ViewHolder)
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final ImageView exerciseThumbnail;
        private final TextView exerciseName;
        private final TextView exerciseDescription;

        // ViewHolder constructed using a given view (which it will hold)
        public ExerciseViewHolder(View view) {
            super(view);

            // NOTE: could define click listener here

            // in the view, find the element with id textView
            exerciseThumbnail = (ImageView) view.findViewById(R.id.iv_exercise_thumbnail);
            exerciseName = (TextView) view.findViewById(R.id.tv_exercise_name);
            exerciseDescription = (TextView) view.findViewById(R.id.tv_exercise_description);
        }

        public ImageView getExerciseThumbnailView() {
            return exerciseThumbnail;
        }
        public TextView getExerciseNameView() {return exerciseName;}
        public TextView getExerciseDescription() {return exerciseDescription;}
    }

    /**
     * Initialize the dataset of the adapter
     *
     * @param dataSet String[] containing the data to populate views
     *                to be used by RecyclerView
     */
    public ExerciseManagementAdapter(Exercise[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (will be invoked by the layout manager
    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.exercise_row_item, viewGroup, false);

        return new ExerciseViewHolder(view);
    }

    // Replace the contents of a view (will be invoked by layout manager)
    // Alternatively: Adds/changes data in the ViewHolder (different views!)
    @Override
    public void onBindViewHolder(ExerciseViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and
        // replace the contents of the view with that element
        viewHolder.getExerciseNameView().setText(localDataSet[position].getName());
        viewHolder.getExerciseDescription().setText(localDataSet[position].getDescription());
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

}
