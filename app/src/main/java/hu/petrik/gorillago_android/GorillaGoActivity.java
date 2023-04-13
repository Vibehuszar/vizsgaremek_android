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

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.petrik.gorillago_android.classes.Restaurant;

public class GorillaGoActivity extends AppCompatActivity {

    private ListView listViewRestaurants;
    private TextView textViewRecommended1, textViewRecommended2, textViewRecommended3, textViewRecommended4;

    private ImageView imageViewRecommended1, imageViewRecommended2, imageViewRecommended3, imageViewRecommended4;

    private HorizontalScrollView horizontalScrollViewRestaurants, horizontalScrollViewMarkets ;
    private List<Restaurant> restaurants = new ArrayList<>();

    private Restaurant[] restaurantArray = new Restaurant[5];
    private AlertDialog.Builder alert;
    private String url = "http://10.0.2.2:3000/restaurants";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Toolbar toolbar;

    private FrameLayout frameLayoutAccount, frameLayoutSearch;

    private NavigationView navigationView;
    private TextView textViewRestaurants, textViewMarkets;

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
                        horizontalScrollViewRestaurants.setVisibility(View.VISIBLE);
                        horizontalScrollViewMarkets.setVisibility(View.VISIBLE);
                        textViewRestaurants.setVisibility(View.VISIBLE);
                        textViewMarkets.setVisibility(View.VISIBLE);
                        break;
                    case R.id.account:
                        frameLayoutAccount.setVisibility(View.VISIBLE);
                        frameLayoutSearch.setVisibility(View.GONE);
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
    }
    private void init() {
        //listViewRestaurants = findViewById(R.id.listViewRestaurants);
       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        //listViewRestaurants.setAdapter(new RestaurantAdapter());
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolBar);
        frameLayoutAccount = findViewById(R.id.fragment_container_account);
        frameLayoutSearch = findViewById(R.id.fragment_container_search);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        textViewRestaurants = findViewById(R.id.textViewRestaurants);
        textViewMarkets = findViewById(R.id.textViewMarkets);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        textViewRecommended1 = findViewById(R.id.textViewRecommended1);
        textViewRecommended2 = findViewById(R.id.textViewRecommended2);
        textViewRecommended3 = findViewById(R.id.textViewRecommended3);
        textViewRecommended4 = findViewById(R.id.textViewRecommended4);
        imageViewRecommended1 = findViewById(R.id.imageViewRecommended1);
        imageViewRecommended2 = findViewById(R.id.imageViewRecommended2);
        imageViewRecommended3 = findViewById(R.id.imageViewRecommended3);
        imageViewRecommended4 = findViewById(R.id.imageViewRecommended4);
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
    /*private class RestaurantAdapter extends ArrayAdapter<Restaurant> {
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
            System.out.println(actualRestaurant.getName());
            System.out.println(actualRestaurant);
            return view;
        }
    }*/
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
                        for (int i = 0; i < restaurantsArray.length; i++) {
                            restaurantArray[i] = restaurantsArray[i];
                            textViewRecommended1.setText(restaurantArray[i].getName());
                            Picasso.get().load(restaurantArray[i].getUrl()).into(imageViewRecommended1);
                        }
                        textViewRecommended1.setText(restaurantArray[0].getName());
                        Picasso.get().load(restaurantArray[0].getUrl()).into(imageViewRecommended1);
                        textViewRecommended2.setText(restaurantArray[1].getName());
                        Picasso.get().load(restaurantArray[1].getUrl()).into(imageViewRecommended2);
                        textViewRecommended3.setText(restaurantArray[2].getName());
                        Picasso.get().load(restaurantArray[2].getUrl()).into(imageViewRecommended3);
                        textViewRecommended4.setText(restaurantArray[3].getName());
                        Picasso.get().load(restaurantArray[3].getUrl()).into(imageViewRecommended4);
                        break;
                }
            }
        }
    }
}