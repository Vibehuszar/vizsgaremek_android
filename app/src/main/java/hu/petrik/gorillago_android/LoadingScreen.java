package hu.petrik.gorillago_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class LoadingScreen extends AppCompatActivity {

    static int LOADING_TIME = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_loading_screen);
        loadingStart();
    }

    public void loadingStart(){
        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences=getSharedPreferences("MyData", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("name","");
            //Toast.makeText(LoadingScreen.this,"SharedPreference adat:"+token,Toast.LENGTH_SHORT).show();
            if (!token.equals("")){
                startActivity(new Intent(LoadingScreen.this, GorillaGoActivity.class));
            }
            else{
                Intent intent = new Intent(LoadingScreen.this, MainActivity.class);
                startActivity(intent);
            }

            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, LOADING_TIME);
    }
}