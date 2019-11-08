package com.openclassrooms.entrevoisins.ui.neighbour_detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailNeighbourActivity extends AppCompatActivity {

    Neighbour mNeighbour;
    List<Neighbour> mFavoritesNeighbours = new ArrayList<>();
    SharedPreferences mPreferences;

    ImageView mDetailAvatar;
    TextView mDetailAvatarName;
    FloatingActionButton mDetailHomeBtn;
    FloatingActionButton mDetailFavBtn;
    TextView mDetailName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);

        // Initialization of preferences
        mPreferences = getSharedPreferences("ENTREVOISINS",MODE_PRIVATE);

        // Get neighbour (format Json)
        Intent detailNeighbourActivityIntent = getIntent();
        String jsonNeighbour = detailNeighbourActivityIntent.getStringExtra("JSON_NEIGHBOUR");

        Type type = new TypeToken<Neighbour>(){}.getType();
        mNeighbour = new Gson().fromJson(jsonNeighbour, type);

        // Initialization of layout id
        mDetailAvatar = findViewById(R.id.detail_avatar);
        mDetailAvatarName = findViewById(R.id.detail_avatar_name);
        mDetailHomeBtn = findViewById(R.id.detail_home_btn);
        mDetailFavBtn = findViewById(R.id.detail_fav_btn);
        mDetailName = findViewById(R.id.detail_name);

        // Change button style if favorite
        if(mNeighbour.isFavorite()) {
            mDetailFavBtn.setImageResource(R.drawable.ic_star_white_24dp);
            mDetailFavBtn.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
        }

        Context holder = getApplicationContext();
        Glide.with(holder)
                .load(mNeighbour.getAvatarUrl())
                .into(mDetailAvatar);

        mDetailAvatarName.setText(mNeighbour.getName());
        mDetailName.setText(mNeighbour.getName());

        mDetailHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDetailFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mPreferences.getString("FAVORITE_NEIGHBOURS", null) != null) {
                    Type listType = new TypeToken<List<Neighbour>>() {}.getType();
                    mFavoritesNeighbours = new Gson().fromJson(mPreferences.getString("FAVORITE_NEIGHBOURS", null), listType);
                }

                if(mNeighbour.isFavorite()) {
                    // Not favorite anymore
                    mFavoritesNeighbours.remove(mNeighbour);

                    // Message
                    mNeighbour.setFavorite(false);
                    Toast.makeText(holder,"Retirer des favoris", Toast.LENGTH_SHORT).show();

                    // Change button style
                    mDetailFavBtn.setImageResource(R.drawable.ic_star_yellow_24dp);
                    mDetailFavBtn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                } else {
                    // New favorite
                    mFavoritesNeighbours.add(mNeighbour);

                    // Message
                    mNeighbour.setFavorite(true);
                    Toast.makeText(holder,"Ajouter aux favoris", Toast.LENGTH_SHORT).show();

                    // Change button style
                    mDetailFavBtn.setImageResource(R.drawable.ic_star_white_24dp);
                    mDetailFavBtn.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                }

                if (mFavoritesNeighbours.size() == 0) {
                    mPreferences.edit().putString("FAVORITE_NEIGHBOURS",null).apply();
                } else {
                    Gson gson = new Gson();
                    String jsonFavoritesNeighbours = gson.toJson(mFavoritesNeighbours);
                    mPreferences.edit().putString("FAVORITE_NEIGHBOURS",jsonFavoritesNeighbours).apply();
                }
            }
        });
    }
}