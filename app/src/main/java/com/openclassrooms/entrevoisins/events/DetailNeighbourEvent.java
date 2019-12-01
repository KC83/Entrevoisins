package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

public class DetailNeighbourEvent {
    /**
     * Neighbour to show detail
     */
    private final Neighbour neighbour;
    private final int mPosition;

    public Neighbour getNeighbour() {
        return neighbour;
    }

    /**
     * Constructor.
     * @param neighbour
     */
    public DetailNeighbourEvent(Neighbour neighbour, int position) {
        this.neighbour = neighbour;
        mPosition = position;
    }


    public int getPosition() {
        return mPosition;
    }
}
