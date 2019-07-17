package oceanscan.co.alc4phase1;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.View;
 import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import oceanscan.co.alc4phase1.utils.ConnectionDetector;

public class MainActivity extends AppCompatActivity {
    ConnectionDetector connection;
    CoordinatorLayout layout;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
          Button aboutAndela= findViewById(R.id.button_about_alc);
          Button myProfile=findViewById(R.id.button_my_profile);
         layout=findViewById(R.id.layout_root);
         connection=new ConnectionDetector(MainActivity.this);
         aboutAndela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkConnected=connection.isConnectingToInternet();
                if(checkConnected) {
                    Intent aboutAndelaIntent = new Intent(MainActivity.this, AboutAndela.class);
                    startActivity(aboutAndelaIntent);
                }else{
                    Snackbar snackbar = Snackbar.make(layout,"Poor internet connection. Try again!",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myProfileIntent=new Intent(MainActivity.this,MyProfile.class);
                startActivity(myProfileIntent);
            }
        });


     }

//
}
