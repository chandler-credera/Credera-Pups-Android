package com.example.crederapups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AnimalActivity extends AppCompatActivity {

    Animal [] animals;
    Animal animal;

    String mainPhoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        ImageView selectedAnimalImage = findViewById(R.id.selectedAnimalImage);

        TextView selectedAnimalName = findViewById(R.id.selectedAnimalName);
        TextView selectedAnimalAge = findViewById(R.id.selectedAnimalAge);
        TextView selectedAnimalGender = findViewById(R.id.selectedAnimalGender);

        Intent intent = getIntent();
        int position = intent.getIntExtra(getString(R.string.position_key), -1);

        animals = AnimalAdapter.animals;
        animal = animals[position];

        System.out.println(animal.name);

        if(position != -1 && animal.photos.length > 0){
            mainPhoto = animal.photos[0].full;
            Picasso.get().load(mainPhoto).resize(200, 200).centerCrop().into(selectedAnimalImage);
            selectedAnimalName.setText(animal.name);
            selectedAnimalAge.setText(animal.age);
            selectedAnimalGender.setText(animal.gender);

        }
    }

    public void setSelectedAnimalFavorite(View view){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.favorite_name_key), animal.name);
        editor.putString(getString(R.string.favorite_image_key), mainPhoto);
        editor.apply();

        Intent intent = new Intent(AnimalActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
