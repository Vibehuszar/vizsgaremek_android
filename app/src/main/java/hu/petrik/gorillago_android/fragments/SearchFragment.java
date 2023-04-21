package hu.petrik.gorillago_android.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.petrik.gorillago_android.R;
import hu.petrik.gorillago_android.RequestHandler;
import hu.petrik.gorillago_android.Response;
import hu.petrik.gorillago_android.classes.MenuItem;
import hu.petrik.gorillago_android.classes.Restaurant;


public class SearchFragment extends Fragment {

    private SearchView searchView;
    private ListView listViewSearch;
    private List<Restaurant> restaurants = new ArrayList<>();
    private String url = "http://10.0.2.2:3000/restaurants/search";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        init(view);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RequestTask task = new RequestTask(url + "/" + query, "GET");
                task.execute();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String  query) {
                /*if (query.equals("")){
                    restaurants.clear();
                }
                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                RequestTask task = new RequestTask(url + "/" + query, "GET");
                task.execute();*/
                return true;
            }
        });
        return view;
    }
    public void init(View view){
        searchView = view.findViewById(R.id.searchView);
        listViewSearch = view.findViewById(R.id.listViewSearch);
    }
    private void initListView() {
        SearchRestaurantAdapter adapter = new SearchRestaurantAdapter();
        listViewSearch.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    private class SearchRestaurantAdapter extends ArrayAdapter<Restaurant> {
        public SearchRestaurantAdapter() {
            super(listViewSearch.getContext(), R.layout.searchrestaurantadapter, restaurants);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.searchrestaurantadapter, parent, false);
            }
            Restaurant actualRestaurant = restaurants.get(position);
            MaterialCardView cardViewSearchRestaurant = convertView.findViewById(R.id.cardViewSearchRestaurant);
            ImageView imageViewSearchRestaurant = convertView.findViewById(R.id.imageViewSearchRestaurant);
            TextView textViewSearchRestaurant = convertView.findViewById(R.id.textViewSearchRestaurant);
            //TextView textViewSearchDescription = convertView.findViewById(R.id.textViewSearchDescription);
            Picasso.get().load(actualRestaurant.getUrl()).into(imageViewSearchRestaurant);
            textViewSearchRestaurant.setText(actualRestaurant.getName());
            //textViewSearchDescription.setText(actualRestaurant.getDes());

            return convertView;
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
                        Restaurant[] restaurantsArray = converter.fromJson(response.getContent(), Restaurant[].class);
                        restaurants.clear();
                        restaurants.addAll(Arrays.asList(restaurantsArray));
                        initListView();
                        break;
                }
            }
        }
    }
}