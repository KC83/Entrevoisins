package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.List;


/**
 * Neighbour API client
 */
public interface NeighbourApiService {

    /**
     * Get all my Neighbours
     * @return {@link List}
     */
    List<Neighbour> getNeighbours();

    /**
     * Deletes a neighbour
     * @param neighbour
     */
    void deleteNeighbour(Neighbour neighbour);

    /**
     * Set to a neighbour the value of isFavorite
     * @param position
     * @param isFavorite
     */
    void setFavorite(int position, boolean isFavorite);

    /**
     * Get all my FavoritesNeighbours
     * @return {@link List}
     */
    List<Neighbour> getFavoritesNeighbours();

    void setTabSelected(int tabSelected);
    int getTabSelected();
}
