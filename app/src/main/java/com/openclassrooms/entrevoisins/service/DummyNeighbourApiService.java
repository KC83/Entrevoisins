package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();
    private int tab;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        neighbours.remove(neighbour);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFavorite(int position, boolean isFavorite) {
        neighbours.get(position).setFavorite(isFavorite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getFavoritesNeighbours() {
        List<Neighbour> favoritesNeighbours = new ArrayList<>();
        for (Neighbour neighbour : neighbours) {
            if (neighbour.isFavorite()) {
                favoritesNeighbours.add(neighbour);
            }
        }

        return favoritesNeighbours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTabSelected(int tabSelected) {
        tab = tabSelected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTabSelected() {
        return tab;
    }
}
