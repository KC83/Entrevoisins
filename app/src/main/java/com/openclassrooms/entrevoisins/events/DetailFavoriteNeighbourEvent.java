package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

public class DetailFavoriteNeighbourEvent {
    /**
     * Neighbour to show detail
     */
    private Neighbour neighbour;

    public Neighbour getNeighbour() {
        return neighbour;
    }

    /**
     * Constructor.
     * @param neighbour
     */
    public DetailFavoriteNeighbourEvent(Neighbour neighbour) {
        this.neighbour = neighbour;
    }
}
