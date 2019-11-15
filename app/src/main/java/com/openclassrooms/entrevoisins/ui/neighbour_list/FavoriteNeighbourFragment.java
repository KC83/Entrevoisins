package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteNeighbourFragment extends Fragment {

    private NeighbourApiService mApiService;
    private RecyclerView mRecyclerView;

    private boolean isSelected = false;
    private Constants mConstants = new Constants();

    /**
     * Create and return a new instance
     * @return @{@link FavoriteNeighbourFragment}
     */
    public static FavoriteNeighbourFragment newInstance() {
        return new FavoriteNeighbourFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_neighbour_list, container, false);
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

        SharedPreferences mPreferences = context.getSharedPreferences(mConstants.NAME_PREFERENCES,Context.MODE_PRIVATE);
        List<Neighbour> neighbours;
        if(mPreferences.getString(mConstants.FAVORITES_NEIGHBOURS, null) != null) {
            Type type = new TypeToken<List<Neighbour>>(){}.getType();
            neighbours = new Gson().fromJson(mPreferences.getString(mConstants.FAVORITES_NEIGHBOURS, null), type);
        } else {
            neighbours =  new ArrayList<>();
        }

        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(neighbours));
    }

    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        initList();
    }

    @Subscribe
    public void onDetailNeighbour(DetailNeighbourEvent event) {
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
