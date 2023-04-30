package hu.petrik.gorillago_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import hu.petrik.gorillago_android.classes.Login;
import hu.petrik.gorillago_android.classes.Token;
import hu.petrik.gorillago_android.classes.User;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton buttonLogin;
    private MaterialToolbar buttonBack;
    private TextInputEditText inputEmail, inputPassword;
    private TextInputLayout inputLoginEmailLayout, inputLoginPasswordLayout;
    private Toast exitToast;
    private long backPressedTime;
    private String url = "http://10.0.2.2:3000/login";
    private String url_get = "http://10.0.2.2:3000/userbyemail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login(){
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        inputLoginEmailLayout.setError(null);
        inputLoginPasswordLayout.setError(null);
        RequestTask task_get = new RequestTask(url_get + "/" + email, "GET");
        task_get.execute();

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Minden mezőt ki kell tölteni", Toast.LENGTH_SHORT).show();
        }

        Login user = new Login( email, password);
        Gson jsonConverter = new Gson();
        RequestTask task = new RequestTask(url, "POST", jsonConverter.toJson(user));
        task.execute();
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


    private void init()
    {
        buttonBack = findViewById(R.id.buttonBack);
        buttonLogin = findViewById(R.id.buttonLogin);
        inputEmail = findViewById(R.id.inputLogin);
        inputPassword= findViewById(R.id.inputLoginPassword);
        inputLoginEmailLayout = findViewById(R.id.inputLoginEmailLayout);
        inputLoginPasswordLayout = findViewById(R.id.inputLoginPasswordLayout);
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

        public RequestTask(String requestUrl, String requestType){
            this.requestUrl = requestUrl;
            this.requestType = requestType;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response =null;
            try {
                switch (requestType){
                    case"GET":
                        response = RequestHandler.get(requestUrl);
                        break;
                    case "POST":
                        response = RequestHandler.post(requestUrl, requestParams);
                        break;
                }
            }catch (IOException e){
                Toast.makeText(LoginActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson converter = new Gson();
            if (response==null|| response.getResponseCode() >= 400){
                Toast.makeText(LoginActivity.this, "Hiba a Bejelentkezés során", Toast.LENGTH_SHORT).show();
                inputLoginPasswordLayout.setError("Hibás felhasználó vagy jelszó.");
                inputLoginPasswordLayout.setErrorIconDrawable(R.drawable.custom_error_icon);
                inputPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Do nothing
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        inputLoginPasswordLayout.setError(null); // Clear the error
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // Do nothing
                    }
                });
            }else {
                switch (requestType) {
                    case "GET":
                        User user = converter.fromJson(
                                response.getContent(), User.class);
                        if (user == null){
                            inputLoginEmailLayout.setError("Ilyen email címmel nem létezik felhasználó.");
                            return; // megszakítás
                        }
                        else {
                            String firstName = user.getFirstName();
                            SharedPreferences sharedPreferences=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("firstName", firstName);
                            editor.commit();
                            System.out.println(firstName);
                        }
                        break;
                    case "POST":
                        Toast.makeText(LoginActivity.this, "Sikeres Bejelentkezés", Toast.LENGTH_SHORT).show();
                        Token token = converter.fromJson(
                                response.getContent(), Token.class);
                        String tokenString = token.getToken();
                        int userId = token.getId();
                        SharedPreferences sharedPreferences=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("token", tokenString);
                        editor.putInt("userId", userId);
                        editor.putBoolean("canOrder", false);
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, GorillaGoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                }
            }
        }
    }
}