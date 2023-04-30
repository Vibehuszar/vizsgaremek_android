package hu.petrik.gorillago_android.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;

import hu.petrik.gorillago_android.GorillaGoActivity;
import hu.petrik.gorillago_android.R;
import hu.petrik.gorillago_android.RegistrationActivity;
import hu.petrik.gorillago_android.RequestHandler;
import hu.petrik.gorillago_android.Response;
import hu.petrik.gorillago_android.classes.Order;
import hu.petrik.gorillago_android.classes.Restaurant;
import hu.petrik.gorillago_android.classes.User;

public class OrderAddressFragment extends Fragment {
    private TextView textViewAddress;
    private TextInputEditText editTextCity, editTextStreet, editTextHouseNumber, editTextZip;
    private Button buttonNext;
    private String url = "http://10.0.2.2:3000/order";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_order_address, container, false);
        init(view);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order();
            }
        });
        return view;
    }
    private void order(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shopping_cart", MODE_PRIVATE);
        int totalprice = sharedPreferences.getInt("total_price", 0);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", 0);
        String city= editTextCity.getText().toString().trim();
        String street = editTextStreet.getText().toString().trim();
        int houseNumber = 0;
        int postalCode = 0;
        try {
            houseNumber = Integer.parseInt(editTextHouseNumber.getText().toString());
            postalCode = Integer.parseInt(editTextZip.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "House number and postal code must be numbers", Toast.LENGTH_SHORT).show();
            return;
        }
        if (city.isEmpty() || street.isEmpty() || houseNumber == 0 || postalCode == 0){
            Toast.makeText(getActivity(), "Minden mezőt ki kell tölteni", Toast.LENGTH_SHORT).show();
        }else{
            Order order = new Order(0,userId,totalprice,city,street,houseNumber,postalCode);
            Gson jsonConverter = new Gson();
            RequestTask task = new RequestTask(url, "POST", jsonConverter.toJson(order));
            task.execute();
        }
    }
    public void init(View view){
        textViewAddress = view.findViewById(R.id.textViewOrderAddress);
        editTextCity = view.findViewById(R.id.editTextCity);
        editTextStreet = view.findViewById(R.id.editTextStreet);
        editTextHouseNumber = view.findViewById(R.id.editTextHouseNumber);
        editTextZip = view.findViewById(R.id.editTextZip);
        buttonNext = view.findViewById(R.id.buttonProceedToCheckout);
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
                    case "POST":
                        response = RequestHandler.post(requestUrl, requestParams);
                        break;
                }
            } catch (IOException e) {

            }
            return response;
        }
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response == null || response.getResponseCode() >= 400) {
                Toast.makeText(getActivity(), "Hiba a rendelés leadása során", Toast.LENGTH_SHORT).show();
            } else {
                switch (requestType) {
                    case "POST":
                        Toast.makeText(getActivity(), "Sikeres rendelés", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shopping_cart", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(getActivity(), GorillaGoActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        }
    }
}