package com.example.crederapups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView favoriteNameTxt = findViewById(R.id.favoriteName);
        ImageView favoriteImg = findViewById(R.id.favoriteImage);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        String favoriteName = preferences.getString(getString(R.string.favorite_name_key), getString(R.string.favorite_name_default));
        String favoritePhotoUrl = preferences.getString(getString(R.string.favorite_image_key), "");

        System.out.println("Favorite Animal !!!!!!!!!!    " + preferences.getString("favoriteName", "No preference Found."));

        favoriteNameTxt.setText(favoriteName);

        if(favoritePhotoUrl != null && !favoritePhotoUrl.isEmpty()){
            Picasso.get().load(favoritePhotoUrl).placeholder(R.drawable.no_image).resize(300, 300).centerCrop().error(R.drawable.no_image).into(favoriteImg);

        }
    }

    public void sendMessage(View view) {
        System.out.print("\n Sending Message!");
        Intent intent = new Intent(this, AnimalsActivity.class);
        startActivity(intent);
    }
}