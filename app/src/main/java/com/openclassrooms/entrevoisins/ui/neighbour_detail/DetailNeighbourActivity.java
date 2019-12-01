package com.openclassrooms.entrevoisins.ui.neighbour_detail;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.Constants;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.lang.reflect.Type;

public class DetailNeighbourActivity extends AppCompatActivity {

    NeighbourApiService mApiService;
    Neighbour mNeighbour;

    int mPosition;

    ImageView mDetailAvatar;
    TextView mDetailAvatarName;
    FloatingActionButton mDetailFavBtn;
    TextView mDetailName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        // Initialization of the service
        mApiService = DI.getNeighbourApiService();

        // Get neighbour (format Json)
        Intent detailNeighbourActivityIntent = getIntent();
        String jsonNeighbour = detailNeighbourActivityIntent.getStringExtra(Constants.JSON_NEIGHBOUR);
        Type type = new TypeToken<Neighbour>(){}.getType();
        mNeighbour = new Gson().fromJson(jsonNeighbour, type);

        // Get the position of the neighbour
        mPosition = detailNeighbourActivityIntent.getIntExtra(Constants.EXTRA_NEIGHBOUR_POSITION,0);

        // Initialization of layout id
        mDetailAvatar = findViewById(R.id.detail_avatar);
        mDetailAvatarName = findViewById(R.id.detail_avatar_name);
        mDetailFavBtn = findViewById(R.id.detail_fav_btn);
        mDetailName = findViewById(R.id.detail_name);

        // Change button style if favorite
        if(mNeighbour.isFavorite()) {
            mDetailFavBtn.setImageResource(R.drawable.ic_star_white_24dp);
            mDetailFavBtn.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
            mDetailFavBtn.setTag("isFavorite");
        }

        Glide.with(this)
                .load(mNeighbour.getAvatarUrl())
                .into(mDetailAvatar);

        mDetailAvatarName.setText(mNeighbour.getName());
        mDetailName.setText(mNeighbour.getName());


        mDetailFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFavoriteClicked();
            }
        });
    }

    private void onFavoriteClicked() {
        if(mNeighbour.isFavorite()) {
            // Not favorite anymore
            mApiService.setFavorite(mPosition, false);

            // Message
            mNeighbour.setFavorite(false);
            Toast.makeText(this,"Retirer des favoris", Toast.LENGTH_SHORT).show();

            // Change button style
            mDetailFavBtn.setImageResource(R.drawable.ic_star_yellow_24dp);
            mDetailFavBtn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            mDetailFavBtn.setTag("");
        } else {
            // New favorite
           mApiService.setFavorite(mPosition,true);

            // Message
            mNeighbour.setFavorite(true);
            Toast.makeText(this,"Ajouter aux favoris", Toast.LENGTH_SHORT).show();

            // Change button style
            mDetailFavBtn.setImageResource(R.drawable.ic_star_white_24dp);
            mDetailFavBtn.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
            mDetailFavBtn.setTag("isFavorite");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
