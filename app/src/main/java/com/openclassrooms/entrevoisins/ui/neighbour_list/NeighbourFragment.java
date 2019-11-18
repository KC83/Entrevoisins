package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.events.DetailNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.Constants;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_detail.DetailNeighbourActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Type;
import java.util.List;


public class NeighbourFragment extends Fragment {

    private NeighbourApiService mApiService;
    private RecyclerView mRecyclerView;

    /**
     * Create and return a new instance
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance() {
        return new NeighbourFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbour_list, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        initList();
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        Context context = getContext();
        if (context == null) {
            return;
        }

        List<Neighbour> neighbours;
        neighbours = mApiService.getNeighbours();

        SharedPreferences mPreferences = context.getSharedPreferences(Constants.NAME_PREFERENCES,Context.MODE_PRIVATE);
        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(neighbours, mPreferences.getInt(Constants.TAB,0)));
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        Context context = getContext();
        if (context == null) {
            return;
        }

        mApiService.deleteNeighbour(event.neighbour);

        SharedPreferences mPreferences = context.getSharedPreferences(Constants.NAME_PREFERENCES,Context.MODE_PRIVATE);
        if(mPreferences.getString(Constants.FAVORITES_NEIGHBOURS, null) != null) {
            Type listType = new TypeToken<List<Neighbour>>() {}.getType();
            List<Neighbour> mFavoritesNeighbours = new Gson().fromJson(mPreferences.getString(Constants.FAVORITES_NEIGHBOURS, null), listType);

            if (mFavoritesNeighbours != null) {
                for (Neighbour neighbour : mFavoritesNeighbours) {
                    if (neighbour.equals(event.neighbour)) {
                        mFavoritesNeighbours.remove(neighbour);
                        break;
                    }
                }

                if (mFavoritesNeighbours.size() == 0) {
                    mPreferences.edit().putString(Constants.FAVORITES_NEIGHBOURS,null).apply();
                } else {
                    Gson gson = new Gson();
                    String jsonFavoritesNeighbours = gson.toJson(mFavoritesNeighbours);
                    mPreferences.edit().putString(Constants.FAVORITES_NEIGHBOURS,jsonFavoritesNeighbours).apply();
                }
            }
        }

        initList();
    }

    /**
     * Fired if the user clicks on a neighbour
     * @param event
     */
    @Subscribe
    public void onDetailNeighbour(DetailNeighbourEvent event) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        SharedPreferences mPreferences = context.getSharedPreferences(Constants.NAME_PREFERENCES,Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonNeighbour = null;

        boolean getNeighbour = false;

        if(mPreferences.getString(Constants.FAVORITES_NEIGHBOURS, null) != null) {
            Type listType = new TypeToken<List<Neighbour>>() {}.getType();
            List<Neighbour> favoritesNeighbours = new Gson().fromJson(mPreferences.getString(Constants.FAVORITES_NEIGHBOURS, null), listType);

            if (favoritesNeighbours != null) {
                for (Neighbour neighbour : favoritesNeighbours) {
                    // Get good neighbour
                    if (neighbour.equals(event.getNeighbour())) {
                        getNeighbour = true;
                        jsonNeighbour = gson.toJson(neighbour);
                        break;
                    }
                }
            }
        }

        if (!getNeighbour) {
            jsonNeighbour = gson.toJson(event.getNeighbour());
        }

        Intent detailNeighbourActivityIntent = new Intent(getActivity(), DetailNeighbourActivity.class);
        detailNeighbourActivityIntent.putExtra(Constants.JSON_NEIGHBOUR, jsonNeighbour);
        startActivity(detailNeighbourActivityIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        initList();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
