package hu.petrik.gorillago_android.fragments;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.petrik.gorillago_android.GorillaGoActivity;
import hu.petrik.gorillago_android.R;
import hu.petrik.gorillago_android.RequestHandler;
import hu.petrik.gorillago_android.Response;
import hu.petrik.gorillago_android.classes.MenuItem;
import hu.petrik.gorillago_android.classes.Restaurant;

public class RestaurantFragment extends Fragment {

    private ListView listViewMenu;
    private ImageView imageViewRestaurantImage;
    private TextView textViewRestaurantName;
    private List<MenuItem> menuItems = new ArrayList<>();
    private String url = "http://10.0.2.2:3000/restaurants";
    private String url_two = "http://10.0.2.2:3000/restaurant";
    private String urlSelector;
    private int restaurantId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        restaurantId = sharedPreferences.getInt("restaurantId",0);
        RequestTaskRestaurant task = new RequestTaskRestaurant(url_two + "/" + restaurantId, "GET");
        urlSelector = url + "/" + restaurantId;
        task.execute();
        RequestTask task_two = new RequestTask(url + "/" + restaurantId + "/menus", "GET");
        urlSelector = url + "/" + restaurantId + "/menus";
        task_two.execute();
        init(view);
        return view;
    }

    private void init(View view){
        imageViewRestaurantImage = view.findViewById(R.id.imageViewRestaurantImage);
        textViewRestaurantName = view.findViewById(R.id.textViewRestaurantName);
        listViewMenu = view.findViewById(R.id.listViewMenu);
        listViewMenu.setAdapter(new MenuAdapter());
    }
    private void initListView() {
        MenuAdapter adapter = new MenuAdapter();
        listViewMenu.setAdapter(adapter);
    }

    private class MenuAdapter extends ArrayAdapter<MenuItem> {
        public MenuAdapter() {
            super(listViewMenu.getContext(), R.layout.menuadapter, menuItems);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.menuadapter, parent, false);
            }
            MenuItem actualMenu = menuItems.get(position);
            ImageView imageViewItem = convertView.findViewById(R.id.item_image);
            TextView textViewItemName = convertView.findViewById(R.id.item_name);
            TextView textViewItemDescription = convertView.findViewById(R.id.item_description);
            TextView textViewItemPrice = convertView.findViewById(R.id.item_price);
            Picasso.get().load(actualMenu.getUrl()).into(imageViewItem);
            textViewItemName.setText(actualMenu.getName());
            textViewItemDescription.setText(actualMenu.getDescription());
            textViewItemPrice.setText(String.valueOf(actualMenu.getPrice()));
            Log.d("MenuAdapter", "getView called for position " + position);
            Log.d("MenuAdapter", "Item added");
            return convertView;
        }
    }
    private class RequestTaskRestaurant extends AsyncTask<Void, Void, Response> {
        String requestUrl;
        String requestType;
        String requestParams;

        public RequestTaskRestaurant(String requestUrl, String requestType, String requestParams) {
            this.requestUrl = requestUrl;
            this.requestType = requestType;
            this.requestParams = requestParams;
        }

        public RequestTaskRestaurant(String requestUrl, String requestType) {
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
                Toast.makeText(getActivity(),
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response == null || response.getResponseCode() >= 400) {
                Toast.makeText(getActivity(), "Hiba a lekérdezés során", Toast.LENGTH_SHORT).show();
            } else {
                switch (requestType) {
                    case "GET":
                        Restaurant restaurant = converter.fromJson(response.getContent(), Restaurant.class);
                        textViewRestaurantName.setText(restaurant.getName());
                        Picasso.get().load(restaurant.getUrl()).into(imageViewRestaurantImage);
                        break;
                }
            }
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
                Toast.makeText(getActivity(),
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response == null || response.getResponseCode() >= 400) {
                Toast.makeText(getActivity(), "Hiba a lekérdezés során", Toast.LENGTH_SHORT).show();
            } else {
                switch (requestType) {
                    case "GET":
                                MenuItem[] menuItemsArray = converter.fromJson(response.getContent(), MenuItem[].class);
                                menuItems.clear();
                                menuItems.addAll(Arrays.asList(menuItemsArray));
                                initListView();
                        break;
                }
            }
        }
    }
}