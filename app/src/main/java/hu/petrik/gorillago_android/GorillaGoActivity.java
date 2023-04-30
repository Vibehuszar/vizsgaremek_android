package hu.petrik.gorillago_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.petrik.gorillago_android.classes.AllRestaurantAdapter;
import hu.petrik.gorillago_android.classes.Restaurant;
import hu.petrik.gorillago_android.classes.RestaurantAdapter;
import hu.petrik.gorillago_android.fragments.AccountFragment;
import hu.petrik.gorillago_android.fragments.CartFragment;
import hu.petrik.gorillago_android.fragments.RestaurantFragment;
import hu.petrik.gorillago_android.fragments.SearchFragment;

public class GorillaGoActivity extends AppCompatActivity {
    private AlertDialog.Builder alert;
    private String url = "http://10.0.2.2:3000/restaurants";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private FrameLayout frameLayoutAccount, frameLayoutSearch, frameLayoutRestaurant, frameLayoutCart;
    private NavigationView navigationView;
    private TextView textViewRestaurants, textViewMarkets;
    private RecyclerView recyclerViewRestaurants, recyclerViewAllRestaurant;
    private RestaurantAdapter restaurantAdapter;
    private AllRestaurantAdapter allRestaurantAdapter;
    private List<Restaurant> restaurants = new ArrayList<>();
    private Toast exitToast;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorilla_go);
        RequestTask task = new RequestTask(url, "GET");
        task.execute();
        init();
        getMenuInflater().inflate(R.menu.menu_toolbar, toolbar.getMenu());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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
                        frameLayoutCart.setVisibility(View.GONE);
                        recyclerViewRestaurants.setVisibility(View.VISIBLE);
                        recyclerViewAllRestaurant.setVisibility(View.VISIBLE);
                        textViewRestaurants.setVisibility(View.VISIBLE);
                        textViewMarkets.setVisibility(View.VISIBLE);
                        break;
                    case R.id.account:
                        frameLayoutAccount.setVisibility(View.VISIBLE);
                        frameLayoutSearch.setVisibility(View.GONE);
                        frameLayoutRestaurant.setVisibility(View.GONE);
                        frameLayoutCart.setVisibility(View.GONE);
                        recyclerViewRestaurants.setVisibility(View.GONE);
                        recyclerViewAllRestaurant.setVisibility(View.GONE);
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
                        frameLayoutCart.setVisibility(View.GONE);
                        recyclerViewRestaurants.setVisibility(View.GONE);
                        recyclerViewAllRestaurant.setVisibility(View.GONE);
                        textViewRestaurants.setVisibility(View.GONE);
                        textViewMarkets.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_search, new SearchFragment()).commit();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_cart:
                        frameLayoutAccount.setVisibility(View.GONE);
                        frameLayoutSearch.setVisibility(View.GONE);
                        frameLayoutRestaurant.setVisibility(View.GONE);
                        frameLayoutCart.setVisibility(View.VISIBLE);
                        recyclerViewRestaurants.setVisibility(View.GONE);
                        recyclerViewAllRestaurant.setVisibility(View.GONE);
                        textViewRestaurants.setVisibility(View.GONE);
                        textViewMarkets.setVisibility(View.GONE);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container_cart, new CartFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            exitToast.cancel();
            super.onBackPressed();
            return;
        } else {
            exitToast = Toast.makeText(this, "Nyomja meg mégegyszer a kilépéshez", Toast.LENGTH_SHORT);
            exitToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    private void initRecyclerView() {
        restaurantAdapter = new RestaurantAdapter(restaurants);
        allRestaurantAdapter = new AllRestaurantAdapter(restaurants);
        recyclerViewRestaurants.setAdapter(restaurantAdapter);
        recyclerViewAllRestaurant.setAdapter(allRestaurantAdapter);
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
                frameLayoutCart.setVisibility(View.GONE);
                recyclerViewRestaurants.setVisibility(View.GONE);
                recyclerViewAllRestaurant.setVisibility(View.GONE);
                textViewRestaurants.setVisibility(View.GONE);
                textViewMarkets.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_restaurant, new RestaurantFragment()).commit();
            }
        });
        allRestaurantAdapter.setAllRestaurantClickListener(new AllRestaurantAdapter.AllRestaurantClickListener() {
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
                frameLayoutCart.setVisibility(View.GONE);
                recyclerViewRestaurants.setVisibility(View.GONE);
                recyclerViewAllRestaurant.setVisibility(View.GONE);
                textViewRestaurants.setVisibility(View.GONE);
                textViewMarkets.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_restaurant, new RestaurantFragment()).commit();
            }
        });
    }
    private void init() {
        recyclerViewRestaurants = findViewById(R.id.recyclerViewRestaurants);
        recyclerViewAllRestaurant = findViewById(R.id.recyclerViewAllRestaurants);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolBar);
        frameLayoutAccount = findViewById(R.id.fragment_container_account);
        frameLayoutSearch = findViewById(R.id.fragment_container_search);
        frameLayoutRestaurant = findViewById(R.id.fragment_container_restaurant);
        frameLayoutCart = findViewById(R.id.fragment_container_cart);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        textViewRestaurants = findViewById(R.id.textViewRestaurants);
        textViewMarkets = findViewById(R.id.textViewAllRestaurant);
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
                        editor.clear();
                        editor.apply();
                        SharedPreferences sharedPreferencesCart = getSharedPreferences("shopping_cart", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorCart = sharedPreferencesCart.edit();
                        editorCart.clear();
                        editorCart.apply();
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