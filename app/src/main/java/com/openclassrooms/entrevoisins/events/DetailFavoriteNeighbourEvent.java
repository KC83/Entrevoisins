package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

public class DetailFavoriteNeighbourEvent {
    /**
     * Neighbour to show detail
     */
    private Neighbour neighbour;
    private int mPosition;

    public Neighbour getNeighbour() {
        return neighbour;
    }

    /**
     * Constructor.
     * @param neighbour
     * @param adapterPosition
     */
    public DetailFavoriteNeighbourEvent(Neighbour neighbour, int adapterPosition) {
        this.neighbour = neighbour;
        mPosition = adapterPosition;
    }

    public int getAdapterPosition() {
        return mPosition;
    }
}
