package hu.petrik.gorillago_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import hu.petrik.gorillago_android.classes.User;

public class RegistrationActivity extends AppCompatActivity {


    private MaterialToolbar buttonBack;
    private Button buttonRegistration;
    private TextInputEditText inputEmail, inputPassword, inputRePassword;
    private TextInputLayout inputEmailLayout, inputPasswordLayout, inputRePasswordLayout;
    private String url = "http://10.0.2.2:3000/register";
    private String url_get = "http://10.0.2.2:3000/userbyemail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registration();
            }
        });
    }

    private void registration(){
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String repassword = inputRePassword.getText().toString().trim();
        inputEmailLayout.setError(null);
        inputPasswordLayout.setError(null);
        inputRePasswordLayout.setError(null);
        RequestTask task_get = new RequestTask(url_get + "/" + email, "GET");
        task_get.execute();


        if (email.isEmpty() || password.isEmpty() || repassword.isEmpty()){
            Toast.makeText(this, "Minden mezőt ki kell tölteni", Toast.LENGTH_SHORT).show();
        }
        if (!email.contains("@")){
           // Toast.makeText(this, "Nem megfelelő az email", Toast.LENGTH_SHORT).show();
            inputEmailLayout.setError("Az email cím nem tartalmaz @ karaktert.");
        }
        if(password.toString().length() < 8){
            // Toast.makeText(this, "A jelszónak legalább 8 karakternek kell lennie", Toast.LENGTH_SHORT).show();
            inputPasswordLayout.setError("A jelszónak legalább 8 karakternek kell lennie");
            inputPasswordLayout.setErrorIconDrawable(R.drawable.custom_error_icon);
            inputPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    inputPasswordLayout.setError(null); // Clear the error
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Do nothing
                }
            });
        }
        if (!password.equals(repassword)){
            //Toast.makeText(this, "Nem egyezik a két jelszó", Toast.LENGTH_SHORT).show();
            inputRePasswordLayout.setError("Nem egyezik a két jelszó");
            inputRePasswordLayout.setErrorIconDrawable(R.drawable.custom_error_icon);
            inputRePassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    inputRePasswordLayout.setError(null); // Clear the error
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Do nothing
                }
            });
        }
        User user = new User(0, email, password, repassword, "", "", 0);
        Gson jsonConverter = new Gson();
        RequestTask task = new RequestTask(url, "POST", jsonConverter.toJson(user));
        task.execute();

    }

    private void init()
    {
        buttonBack = findViewById(R.id.buttonBack);
        buttonRegistration = findViewById(R.id.registrationButton);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword= findViewById(R.id.inputPassword);
        inputRePassword = findViewById(R.id.inputRePassword);
        inputEmailLayout = findViewById(R.id.inputEmailLayout);
        inputPasswordLayout = findViewById(R.id.inputPasswordLayout);
        inputRePasswordLayout = findViewById(R.id.inputRePasswordLayout);
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
                Toast.makeText(RegistrationActivity.this,
                        e.toString(), Toast.LENGTH_SHORT).show();
            }
            return response;
        }

            @Override
            protected void onPostExecute(Response response) {
                super.onPostExecute(response);
                Gson converter = new Gson();
                if (response == null || response.getResponseCode() >= 400) {
                    Toast.makeText(RegistrationActivity.this, "Hiba a Regisztráció során", Toast.LENGTH_SHORT).show();
                } else {
                    switch (requestType) {
                        case "GET":
                            User user = converter.fromJson(
                                    response.getContent(), User.class);
                            if (user != null){
                                Toast.makeText(RegistrationActivity.this, "Ilyen email cím már létezik", Toast.LENGTH_SHORT).show();
                                inputEmailLayout.setError("Ilyen email cím már létezik");
                                return; // megszakítás
                            }
                            break;
                        case "POST":
                            Toast.makeText(RegistrationActivity.this, "Sikeres Regisztráció", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    }
                }
            }
        }
}