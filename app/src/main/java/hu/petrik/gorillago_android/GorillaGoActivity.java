package hu.petrik.gorillago_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.petrik.gorillago_android.classes.Restaurant;
import hu.petrik.gorillago_android.classes.RestaurantAdapter;
import hu.petrik.gorillago_android.fragments.AccountFragment;
import hu.petrik.gorillago_android.fragments.RestaurantFragment;
import hu.petrik.gorillago_android.fragments.SearchFragment;

public class GorillaGoActivity extends AppCompatActivity {
    private TextView textViewFirstName;
    private AlertDialog.Builder alert;
    private String url = "http://10.0.2.2:3000/restaurants";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private FrameLayout frameLayoutAccount, frameLayoutSearch, frameLayoutRestaurant;
    private NavigationView navigationView;
    private View headerView;
    private TextView textViewRestaurants, textViewMarkets;

    private RecyclerView recyclerViewRestaurants, recyclerViewMarkets;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorilla_go);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String firstName = sharedPreferences.getString("firstName", "");
        //System.out.println(firstName);
        RequestTask task = new RequestTask(url, "GET");
        task.execute();
        System.out.println("cadaudhadhas" +restaurants.size());
        init();
        textViewFirstName.setText(firstName);
        System.out.println(restaurants.size());
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        frameLayoutAccount.setVisibility(View.GONE);
                        frameLayoutSearch.setVisibility(View.GONE);
                        frameLayoutRestaurant.setVisibility(View.GONE);
                        recyclerViewRestaurants.setVisibility(View.VISIBLE);
                        recyclerViewMarkets.setVisibility(View.VISIBLE);
                        textViewRestaurants.setVisibility(View.VISIBLE);
                        textViewMarkets.setVisibility(View.VISIBLE);
                        break;
                    case R.id.account:
                        frameLayoutAccount.setVisibility(View.VISIBLE);
                        frameLayoutSearch.setVisibility(View.GONE);
                        frameLayoutRestaurant.setVisibility(View.GONE);
                        recyclerViewRestaurants.setVisibility(View.GONE);
                        recyclerViewMarkets.setVisibility(View.GONE);
                        textViewRestaurants.setVisibility(View.GONE);
                        textViewMarkets.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_account, new AccountFragment()).commit();
                        break;
                    case R.id.logout:
                        alert.show();
                        break;
                    case R.id.search:
                        frameLayoutAccount.setVisibility(View.GONE);
                        frameLayoutSearch.setVisibility(View.VISIBLE);
                        frameLayoutRestaurant.setVisibility(View.GONE);
                        recyclerViewRestaurants.setVisibility(View.GONE);
                        recyclerViewMarkets.setVisibility(View.GONE);
                        textViewRestaurants.setVisibility(View.GONE);
                        textViewMarkets.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_search, new SearchFragment()).commit();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    private void initRecyclerView() {
        restaurantAdapter = new RestaurantAdapter(restaurants);
        recyclerViewRestaurants.setAdapter(restaurantAdapter);

        restaurantAdapter.setRestaurantClickListener(new RestaurantAdapter.RestaurantClickListener() {
            @Override
            public void onRestaurantClick(int id) {
                Log.d("RestaurantAdapter", "Clicked on restaurant with ID: " + id);
                int restaurantId = id;
                SharedPreferences sharedPreferences=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("restaurantId", restaurantId);
                editor.commit();
                System.out.println(restaurantId);
                frameLayoutAccount.setVisibility(View.GONE);
                frameLayoutSearch.setVisibility(View.GONE);
                frameLayoutRestaurant.setVisibility(View.VISIBLE);
                recyclerViewRestaurants.setVisibility(View.GONE);
                recyclerViewMarkets.setVisibility(View.GONE);
                textViewRestaurants.setVisibility(View.GONE);
                textViewMarkets.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_restaurant, new RestaurantFragment()).commit();
            }
        });
    }
    private void init() {
        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);
        recyclerViewMarkets = findViewById(R.id.recyclerViewMarkets);


        navigationView = findViewById(R.id.navigationView);
        headerView = navigationView.getHeaderView(0);
        textViewFirstName = headerView.findViewById(R.id.textViewFirstName);
        toolbar = findViewById(R.id.toolBar);
        frameLayoutAccount = findViewById(R.id.fragment_container_account);
        frameLayoutSearch = findViewById(R.id.fragment_container_search);
        frameLayoutRestaurant = findViewById(R.id.fragment_container_restaurant);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        textViewRestaurants = findViewById(R.id.textViewRestaurants);
        textViewMarkets = findViewById(R.id.textViewMarkets);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        alert = new AlertDialog.Builder(GorillaGoActivity.this);
        alert.setMessage("Biztos ki akar jelentkezni?")
                .setPositiveButton("Nem", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                    }
                })
                .setNegativeButton("Igen", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("token");
                        editor.remove("userId");
                        editor.remove("firstName");
                        editor.commit();
                        startActivity(new Intent(GorillaGoActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .setCancelable(false)
                .create();
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
                Toast.makeText(GorillaGoActivity.this, "Hiba a lekérdezés során", Toast.LENGTH_SHORT).show();
            } else {
                switch (requestType) {
                    case "GET":
                        Restaurant[] restaurantsArray = converter.fromJson(response.getContent(), Restaurant[].class);
                        restaurants.clear();
                        restaurants.addAll(Arrays.asList(restaurantsArray));
                        System.out.println(restaurants.size());
                        initRecyclerView();
                        break;
                }
            }
        }
    }
}