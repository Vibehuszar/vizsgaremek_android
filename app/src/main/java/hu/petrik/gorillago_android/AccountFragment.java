package hu.petrik.gorillago_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import hu.petrik.gorillago_android.classes.UpdateUser;
import hu.petrik.gorillago_android.classes.UpdateUserPassword;
import hu.petrik.gorillago_android.classes.User;


public class AccountFragment extends Fragment {

    TextView textViewAccount;
    TextInputEditText editTextLastName, editTextFirstName, editTextEmail, editTextPhoneNumber ,editTextOldPassword, editTextNewPassword;

    TextInputLayout textInputNewPasswordLayout;

    MaterialButton buttonSavePersonalInfo, buttonSavePassword;
    private String url = "http://10.0.2.2:3000/users";
    private String url_password = "http://10.0.2.2:3000/users/password";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        refreshPage();
        init(view);

        buttonSavePersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePersonalInfo();
            }
        });
        buttonSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
        return view;
    }

    private void updatePersonalInfo(){
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId",0);
        String lastName = editTextLastName.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        UpdateUser updateUser = new UpdateUser(lastName, firstName, email, phoneNumber);
        Gson jsonConverter = new Gson();
        RequestTask task = new RequestTask(url + "/" + userId, "PUT", jsonConverter.toJson(updateUser));
        task.execute();
        refreshPage();
        System.out.println(updateUser);
        System.out.println(url + "/" + userId);
    }

    private  void updatePassword(){
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId",0);
        String oldPassword = editTextOldPassword.getText().toString().trim();
        String newPassword = editTextNewPassword.getText().toString().trim();
        if (newPassword.length() < 8){
            textInputNewPasswordLayout.setError("A jelszónak minimum 8 karakternek kell lennie.");
            editTextNewPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textInputNewPasswordLayout.setError(null); // Clear the error
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Do nothing
                }
            });
        }
        UpdateUserPassword updateUserPassword = new UpdateUserPassword(oldPassword, newPassword);
        Gson jsonConverter = new Gson();
        RequestTask task = new RequestTask(url_password + "/" + userId, "PUT", jsonConverter.toJson(updateUserPassword));
        task.execute();
        System.out.println(jsonConverter.toJson(updateUserPassword));
        System.out.println(url_password + "/" + userId);
    }

    private void refreshPage(){
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId",0);
        RequestTask task = new RequestTask(url + "/" + userId, "GET");
        task.execute();
    }

    public void init(View view){
        textViewAccount = view.findViewById(R.id.textViewAccount);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        editTextOldPassword = view.findViewById(R.id.editTextOldPassword);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);
        buttonSavePersonalInfo = view.findViewById(R.id.buttonSavePersonalInfo);
        buttonSavePassword = view.findViewById(R.id.buttonSavePassword);
        textInputNewPasswordLayout = view.findViewById(R.id.textInputNewPasswordLayout);
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
                    case "PUT":
                        response = RequestHandler.put(requestUrl, requestParams);
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
                if (response == null) {
                    Log.e("TAG", "Response is null");
                } else {
                    Log.e("TAG", "Response code: " + response.getResponseCode());
                    Log.e("TAG", "Response content: " + response.getContent());
                }
                Toast.makeText(getActivity(), "Hiba", Toast.LENGTH_SHORT).show();
            } else {
                switch (requestType) {
                    case "GET":
                        User user = converter.fromJson(response.getContent(), User.class);
                        editTextLastName.setText(user.getLastName());
                        editTextFirstName.setText(user.getFirstName());
                        editTextEmail.setText(user.getEmail());
                        editTextPhoneNumber.setText(user.getPhoneNumber());
                        break;
                    case "PUT":
                        if (response==null|| response.getResponseCode() >= 400){
                            Toast.makeText(getActivity(), "Hiba a módosítás során", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getActivity(), "Sikeres módosítás", Toast.LENGTH_SHORT).show();
                            editTextOldPassword.setText("");
                            editTextNewPassword.setText("");
                            refreshPage();
                        }
                }
            }
        }
    }
}