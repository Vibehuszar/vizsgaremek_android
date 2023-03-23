package hu.petrik.gorillago_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GorillaGoActivity extends AppCompatActivity {

    private ListView listViewRestaurants;
    private List<Restaurant> restaurants = new ArrayList<>();
    private String url = "http://10.0.2.2:3000/restaurants";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_gorilla_go);
        init();
        RequestTask task = new RequestTask(url, "GET");
        task.execute();
    }

    private void init() {
        listViewRestaurants = findViewById(R.id.listViewRestaurants);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        listViewRestaurants.setAdapter(new RestaurantAdapter());
    }

    private class RestaurantAdapter extends ArrayAdapter<Restaurant> {
        public RestaurantAdapter() {
            super(GorillaGoActivity.this, R.layout.restaurantadapter, restaurants);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.restaurantadapter, null, false);
            Restaurant actualRestaurant = restaurants.get(position);
            TextView textViewRecommended = view.findViewById(R.id.textViewRecommended);
            ImageView imageViewRecommended = view.findViewById(R.id.imageViewRecommended);
            textViewRecommended.setText(actualRestaurant.getName());
            Picasso.get().load(actualRestaurant.getUrl()).into(imageViewRecommended);
            return view;
        }
    }

    private class RequestTask extends AsyncTask<Void, Void, Response> {

        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTask(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }

        public RequestTask(String requestUrl, String requestType) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                switch (requestType) {
                    case "GET":
                        response = RequestHandler.get(requestUrl);
                        break;
                    case "POST":
                        response = RequestHandler.post(requestUrl, requestParams);
                        break;
                }
            } catch (IOException e) {
                Toast.makeText(GorillaGoActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response == null || response.getResponseCode() >= 400) {
                Toast.makeText(GorillaGoActivity.this, "Hiba a Bejelentkezés során", Toast.LENGTH_SHORT).show();
            } else {
                switch (requestType) {
                    case "GET":
                        Restaurant[] restaurantsArray = converter.fromJson(response.getContent(), Restaurant[].class);
                        restaurants.clear();
                        restaurants.addAll(Arrays.asList(restaurantsArray));
                        break;
                }
            }
        }
    }
}