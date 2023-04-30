package hu.petrik.gorillago_android.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

import hu.petrik.gorillago_android.R;

public class AllRestaurantAdapter extends RecyclerView.Adapter<AllRestaurantAdapter.ViewHolder> {
    private List<Restaurant> restaurants;
    private AllRestaurantClickListener allRestaurantClickListener;
    public interface AllRestaurantClickListener {
        void onRestaurantClick(int id);
    }

    public AllRestaurantAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewRestaurant;
        private TextView textViewRestaurant;
        private MaterialCardView cardViewRestaurant;



        public ViewHolder(View itemView) {
            super(itemView);
            imageViewRestaurant = itemView.findViewById(R.id.imageViewRestaurant);
            textViewRestaurant = itemView.findViewById(R.id.textViewRestaurant);
            cardViewRestaurant = itemView.findViewById(R.id.cardViewRestaurant);
            cardViewRestaurant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && allRestaurantClickListener != null) {
                        Restaurant restaurant = restaurants.get(position);
                        int id = restaurant.getId();
                        allRestaurantClickListener.onRestaurantClick(id);
                    }
                }
            });
        }
    }
    public void setAllRestaurantClickListener(AllRestaurantClickListener listener) {
        this.allRestaurantClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurantadapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant actualRestaurant = restaurants.get(position);
        Picasso.get().load(actualRestaurant.getUrl()).into(holder.imageViewRestaurant);
        holder.textViewRestaurant.setText(actualRestaurant.getName());

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
