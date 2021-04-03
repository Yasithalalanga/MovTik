package com.livecodex.movtik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieRatingAdapter extends RecyclerView.Adapter<MovieRatingAdapter.ViewHolder> {

    MovieRating[] movieRatings;
    Context context;

    public MovieRatingAdapter(MovieRating[] movieRatings, Context context) {
        this.movieRatings = movieRatings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rating_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MovieRating movieRating = movieRatings[position];
        holder.movieNameView.setText(movieRating.getMovieName());
        holder.movieRatingView.setText(movieRating.getMovieRating());
        holder.movieImageView.setImageResource(movieRating.getMovieImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, movieRating.getMovieName(), Toast.LENGTH_SHORT).show();

                if(holder.movieImageView.getVisibility() == View.VISIBLE){
                    holder.movieImageView.setVisibility(View.GONE);
                }else{
                    holder.movieImageView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieRatings.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView movieNameView;
        TextView movieRatingView;
        ImageView movieImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            movieNameView = itemView.findViewById(R.id.cardMovieName);
            movieRatingView = itemView.findViewById(R.id.cardRating);
            movieImageView = itemView.findViewById(R.id.cardImageView);

        }
    }
}
