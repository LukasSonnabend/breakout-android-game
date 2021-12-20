package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button goToGameButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String newHighScore = "1337";
        editor.putString(getString(R.string.saved_high_score_key), newHighScore);
        editor.apply();
*/

        //sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //String defaultValue = sharedPref.getString(R.string.saved_high_score_key);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //int defaultValue = getResources().getInteger(R.integer.saved_high_score_default_key);
        int highScore = sharedPref.getInt(getString(R.string.saved_high_score_key), 0);


        TextView testTxt = findViewById(R.id.testText);
        testTxt.setText(highScore.toString);

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
        startActivity(intent);
    }

}
