package hu.petrik.gorillago_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton buttonBack, buttonLogin;
    private TextInputEditText inputEmail, inputPassword;
    private String url = "http://10.0.2.2:3000/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Minden mezőt ki kell tölteni", Toast.LENGTH_SHORT).show();
        }
        else if (!email.contains("@")){
            Toast.makeText(this, "Nem megfelelő az email", Toast.LENGTH_SHORT).show();
        }
        else if(password.toString().length() < 8){
            Toast.makeText(this, "A jelszónak legalább 8 karakternek kell lennie", Toast.LENGTH_SHORT).show();
        }

        Login user = new Login( email, password);
        Gson jsonConverter = new Gson();
        RequestTask task = new RequestTask(url, "POST", jsonConverter.toJson(user));
        task.execute();
    }


    private void init()
    {
        buttonBack = findViewById(R.id.buttonBack);
        buttonLogin = findViewById(R.id.buttonLogin);
        inputEmail = findViewById(R.id.inputLogin);
        inputPassword= findViewById(R.id.inputLoginPassword);
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
            }else {
                Toast.makeText(LoginActivity.this, "Sikeres Bejelentkezés", Toast.LENGTH_SHORT).show();
                Token token = converter.fromJson(
                        response.getContent(), Token.class);
                String tokenString = token.getToken();
                Toast.makeText(LoginActivity.this,
                        tokenString, Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences=getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("name", tokenString);
                editor.commit();
                startActivity(new Intent(LoginActivity.this, GorillaGoActivity.class));
            }
        }
    }
}