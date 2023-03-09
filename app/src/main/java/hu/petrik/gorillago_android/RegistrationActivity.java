package hu.petrik.gorillago_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity {


    private MaterialButton buttonBack;
    private Button buttonRegistration;
    private TextInputEditText inputEmail, inputPassword, inputRePassword;
    private String url = "http://10.0.2.2:3000/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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

        if (email.isEmpty() || password.isEmpty() || repassword.isEmpty()){
            Toast.makeText(this, "Minden mezőt ki kell tölteni", Toast.LENGTH_SHORT).show();
        }
        else if (!email.contains("@")){
            Toast.makeText(this, "Nem megfelelő az email", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(repassword)){
            Toast.makeText(this, "Nem egyezik a két jelszó", Toast.LENGTH_SHORT).show();
        }
        else if(password.toString().length() < 8){
            Toast.makeText(this, "A jelszónak legalább 8 karakternek kell lennie", Toast.LENGTH_SHORT).show();
        }

        User user = new User(0, email, password, repassword);
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
                if (response==null|| response.getResponseCode() >= 400){
                    Toast.makeText(RegistrationActivity.this, "Hiba a Regisztráció során", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RegistrationActivity.this, "Sikeres Regisztráció", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }
            }
        }
}