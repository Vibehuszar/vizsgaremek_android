package hu.petrik.gorillago_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import hu.petrik.gorillago_android.fragments.AccountFragment;
import hu.petrik.gorillago_android.fragments.RestaurantFragment;
import hu.petrik.gorillago_android.fragments.SearchFragment;

public class GorillaGoActivity extends AppCompatActivity {
    private TextView textViewRestaurant1, textViewRestaurant2, textViewRestaurant3, textViewRestaurant4,
            textviewRestaurantDescription1, textviewRestaurantDescription2, textviewRestaurantDescription3, textviewRestaurantDescription4,
            textViewMarket1, textViewMarket2, textViewMarket3, textViewMarket4,
            textviewMarketDescription1, textviewMarketDescription2, textviewMarketDescription3, textviewMarketDescription4;
    private ImageView imageViewRestaurant1, imageViewRestaurant2, imageViewRestaurant3, imageViewRestaurant4,
            imageViewMarket1, imageViewMarket2, imageViewMarket3, imageViewMarket4;
    private HorizontalScrollView horizontalScrollViewRestaurants, horizontalScrollViewMarkets ;
    private List<Restaurant> restaurants = new ArrayList<>();
    private Restaurant[] restaurantsArray = new Restaurant[5];
    private AlertDialog.Builder alert;
    private String url = "http://10.0.2.2:3000/restaurants";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private FrameLayout frameLayoutAccount, frameLayoutSearch, frameLayoutRestaurant;
    private NavigationView navigationView;
    private TextView textViewRestaurants, textViewMarkets;
    private MaterialCardView cardRestaurant1, cardRestaurant2, cardRestaurant3, cardRestaurant4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorilla_go);
        RequestTask task = new RequestTask(url, "GET");
        task.execute();
        init();
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
                        horizontalScrollViewRestaurants.setVisibility(View.VISIBLE);
                        horizontalScrollViewMarkets.setVisibility(View.VISIBLE);
                        textViewRestaurants.setVisibility(View.VISIBLE);
                        textViewMarkets.setVisibility(View.VISIBLE);
                        break;
                    case R.id.account:
                        frameLayoutAccount.setVisibility(View.VISIBLE);
                        frameLayoutSearch.setVisibility(View.GONE);
                        frameLayoutRestaurant.setVisibility(View.GONE);
                        horizontalScrollViewRestaurants.setVisibility(View.GONE);
                        horizontalScrollViewMarkets.setVisibility(View.GONE);
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
                        horizontalScrollViewRestaurants.setVisibility(View.GONE);
                        horizontalScrollViewMarkets.setVisibility(View.GONE);
                        textViewRestaurants.setVisibility(View.GONE);
                        textViewMarkets.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_search, new SearchFragment()).commit();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        cardRestaurant1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int restaurantId = restaurantsArray[0].getId();
                SharedPreferences sharedPreferences=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("restaurantId", restaurantId);
                editor.commit();
                System.out.println(restaurantId);
                frameLayoutAccount.setVisibility(View.GONE);
                frameLayoutSearch.setVisibility(View.GONE);
                frameLayoutRestaurant.setVisibility(View.VISIBLE);
                horizontalScrollViewRestaurants.setVisibility(View.GONE);
                horizontalScrollViewMarkets.setVisibility(View.GONE);
                textViewRestaurants.setVisibility(View.GONE);
                textViewMarkets.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_restaurant, new RestaurantFragment()).commit();
            }
        });
    }
    private void init() {
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolBar);
        frameLayoutAccount = findViewById(R.id.fragment_container_account);
        frameLayoutSearch = findViewById(R.id.fragment_container_search);
        frameLayoutRestaurant = findViewById(R.id.fragment_container_restaurant);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        textViewRestaurants = findViewById(R.id.textViewRestaurants);
        cardRestaurant1 = findViewById(R.id.cardRestaurant1);
        cardRestaurant2 = findViewById(R.id.cardRestaurant2);
        cardRestaurant3 = findViewById(R.id.cardRestaurant3);
        cardRestaurant4 = findViewById(R.id.cardRestaurant4);
        textViewMarkets = findViewById(R.id.textViewMarkets);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        textViewRestaurant1 = findViewById(R.id.textViewRestaurant1);
        textViewRestaurant2 = findViewById(R.id.textViewRestaurant2);
        textViewRestaurant3 = findViewById(R.id.textViewRestaurant3);
        textViewRestaurant4 = findViewById(R.id.textViewRestaurant4);
        textviewRestaurantDescription1 = findViewById(R.id.textviewRestaurantDescription1);
        textviewRestaurantDescription2 = findViewById(R.id.textviewRestaurantDescription2);
        textviewRestaurantDescription3 = findViewById(R.id.textviewRestaurantDescription3);
        textviewRestaurantDescription4 = findViewById(R.id.textviewRestaurantDescription4);
        imageViewRestaurant1 = findViewById(R.id.imageViewRestaurant1);
        imageViewRestaurant2 = findViewById(R.id.imageViewRestaurant2);
        imageViewRestaurant3 = findViewById(R.id.imageViewRestaurant3);
        imageViewRestaurant4 = findViewById(R.id.imageViewRestaurant4);
        horizontalScrollViewRestaurants = findViewById(R.id.horizontalScrollViewRestaurants);
        horizontalScrollViewMarkets = findViewById(R.id.horizontalScrollViewMarkets);
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
                        restaurantsArray = converter.fromJson(response.getContent(), Restaurant[].class);
                        restaurants.clear();
                        restaurants.addAll(Arrays.asList(restaurantsArray));

                        textViewRestaurant1.setText(restaurantsArray[0].getName());
                        Picasso.get().load(restaurantsArray[0].getUrl()).into(imageViewRestaurant1);
                        textViewRestaurant2.setText(restaurantsArray[1].getName());
                        Picasso.get().load(restaurantsArray[1].getUrl()).into(imageViewRestaurant2);
                        textViewRestaurant3.setText(restaurantsArray[2].getName());
                        Picasso.get().load(restaurantsArray[2].getUrl()).into(imageViewRestaurant3);
                        textViewRestaurant4.setText(restaurantsArray[3].getName());
                        Picasso.get().load(restaurantsArray[3].getUrl()).into(imageViewRestaurant4);
                        break;
                }
            }
        }
    }
}