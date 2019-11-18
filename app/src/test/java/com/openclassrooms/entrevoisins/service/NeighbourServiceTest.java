package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void getNeighbourDetailWithSuccess() {
        Neighbour neighbour = service.getNeighbours().get(0);
        assertNotNull(neighbour.getId());
        assertNotNull(neighbour.getAvatarUrl());
        assertNotNull(neighbour.getName());
    }

    @Test
    public void addNeighbourInFavorites() {
        List<Neighbour> favoritesNeighbours = new ArrayList<Neighbour>();

        Neighbour neighbour = service.getNeighbours().get(0);
        neighbour.setFavorite(true);

        favoritesNeighbours.add(neighbour);
        assertTrue(favoritesNeighbours.contains(neighbour));
        assertTrue(neighbour.isFavorite());
    }

    @Test
    public void removeNeighbourFromFavorites() {
        List<Neighbour> favoritesNeighbours = new ArrayList<Neighbour>();

        Neighbour neighbour = service.getNeighbours().get(0);
        neighbour.setFavorite(true);

        // Add in favorite
        favoritesNeighbours.add(neighbour);
        assertTrue(favoritesNeighbours.contains(neighbour));
        assertTrue(neighbour.isFavorite());

        // Remove from favorite
        neighbour.setFavorite(false);
        favoritesNeighbours.remove(neighbour);
        assertFalse(favoritesNeighbours.contains(neighbour));
        assertFalse(neighbour.isFavorite());
    }

    @Test
    public void getFavoritesNeighboursWithSuccess() {
        List<Neighbour> favoritesNeighbours = new ArrayList<Neighbour>();

        Neighbour neighbour = service.getNeighbours().get(0);
        neighbour.setFavorite(true);
        favoritesNeighbours.add(neighbour);

        assertTrue(favoritesNeighbours.contains(neighbour));
        assertTrue(neighbour.isFavorite());
    }
}
