package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button goToGameButton;
    private Integer currentLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Profil anlegen?
        // save player name to shared preferences
        SharedPreferences sharedPref = getSharedPreferences("playerName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("playerName", "{currentLevel:1,userName:Werner,highScore:1337}");
        editor.commit();



        TextView testTxt = findViewById(R.id.testText);
        JSONObject obj;
        try {
            obj = new JSONObject(sharedPref.getString("playerName", "Player"));
            testTxt.setText(obj.getString("userName"));
            currentLevel = obj.getInt("currentLevel");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        goToGameButton = (Button) findViewById(R.id.goToGameScreenButton);
        goToGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameActivity();
            }
        });

    }

    // activity wechseln
    public void openGameActivity() {
        Intent intent = new Intent(this, GameScreenActivity.class);
        intent.putExtra("currentLevel", this.currentLevel);
        startActivity(intent);
    }

}
