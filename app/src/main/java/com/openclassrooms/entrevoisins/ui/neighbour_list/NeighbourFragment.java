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
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_detail.DetailNeighbourActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class NeighbourFragment extends Fragment {

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    private List<Neighbour> mFavoritesNeighbours;
    private RecyclerView mRecyclerView;

    public static final int DETAIL_NEIGHBOUR_ACTIVITY_REQUEST_CODE = 50;


    /**
     * Create and return a new instance
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance() {
        NeighbourFragment fragment = new NeighbourFragment();
        return fragment;
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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        initList();
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        SharedPreferences mPreferences = getContext().getSharedPreferences("ENTREVOISINS",Context.MODE_PRIVATE);
        if (mPreferences.getInt("TAB",0) == 1) {
            if(mPreferences.getString("FAVORITE_NEIGHBOURS", null) != null) {
                Type type = new TypeToken<List<Neighbour>>(){}.getType();
                mNeighbours = new Gson().fromJson(mPreferences.getString("FAVORITE_NEIGHBOURS", null), type);
            } else {
                mNeighbours =  new ArrayList<>();
            }
        } else {
            mNeighbours = mApiService.getNeighbours();
        }

        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mNeighbours));
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

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        mApiService.deleteNeighbour(event.neighbour);

        SharedPreferences mPreferences = getContext().getSharedPreferences("ENTREVOISINS",Context.MODE_PRIVATE);
        if(mPreferences.getString("FAVORITE_NEIGHBOURS", null) != null) {
            Type listType = new TypeToken<List<Neighbour>>() {}.getType();
            List<Neighbour> mFavoritesNeighbours = new Gson().fromJson(mPreferences.getString("FAVORITE_NEIGHBOURS", null), listType);

            if (mFavoritesNeighbours != null) {
                for (Neighbour neighbour : mFavoritesNeighbours) {
                    if (neighbour.equals(event.neighbour)) {
                        mFavoritesNeighbours.remove(neighbour);
                    }
                }

                if (mFavoritesNeighbours.size() == 0) {
                    mPreferences.edit().putString("FAVORITE_NEIGHBOURS",null).apply();
                } else {
                    Gson gson = new Gson();
                    String jsonFavoritesNeighbours = gson.toJson(mFavoritesNeighbours);
                    mPreferences.edit().putString("FAVORITE_NEIGHBOURS",jsonFavoritesNeighbours).apply();
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
        SharedPreferences mPreferences = getContext().getSharedPreferences("ENTREVOISINS",Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String jsonNeighbour = null;

        boolean getNeighbour = false;

        if(mPreferences.getString("FAVORITE_NEIGHBOURS", null) != null) {
            Type listType = new TypeToken<List<Neighbour>>() {}.getType();
            mFavoritesNeighbours = new Gson().fromJson(mPreferences.getString("FAVORITE_NEIGHBOURS", null), listType);

            if (mFavoritesNeighbours != null) {
                for (Neighbour neighbour : mFavoritesNeighbours) {
                    // Get good neighbour
                    if (neighbour.equals(event.neighbour)) {
                        getNeighbour = true;
                        jsonNeighbour = gson.toJson(neighbour);
                    }
                }
            }
        }

        if (!getNeighbour) {
            jsonNeighbour = gson.toJson(event.neighbour);
        }

        Intent detailNeighbourActivityIntent = new Intent(getActivity(), DetailNeighbourActivity.class);
        detailNeighbourActivityIntent.putExtra("JSON_NEIGHBOUR", jsonNeighbour);
        startActivityForResult(detailNeighbourActivityIntent, DETAIL_NEIGHBOUR_ACTIVITY_REQUEST_CODE);
    }
}
