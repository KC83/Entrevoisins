package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
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
        assertFalse(neighbour.isFavorite());
    }

    @Test
    public void addNeighbourInFavorites() {
        service.setFavorite(0, true);
        Neighbour neighbour = service.getNeighbours().get(0);
        assertTrue(neighbour.isFavorite());
    }

    @Test
    public void removeNeighbourFromFavorites() {
        service.setFavorite(0, false);
        Neighbour neighbour = service.getNeighbours().get(0);
        assertFalse(neighbour.isFavorite());
    }

    @Test
    public void getFavoritesNeighboursWithSuccess() {
        service.setFavorite(0,true);
        service.setFavorite(3, true);
        List<Neighbour> favoritesNeighbours = service.getFavoritesNeighbours();

        assertEquals(2,favoritesNeighbours.size());

        assertEquals("1",favoritesNeighbours.get(0).getId().toString());
        assertEquals("Caroline",favoritesNeighbours.get(0).getName());
        assertEquals("http://i.pravatar.cc/150?u=a042581f4e29026704d", favoritesNeighbours.get(0).getAvatarUrl());
        assertTrue(favoritesNeighbours.get(0).isFavorite());

        assertEquals("4",favoritesNeighbours.get(1).getId().toString());
        assertEquals("Vincent",favoritesNeighbours.get(1).getName());
        assertEquals("http://i.pravatar.cc/150?u=a042581f4e29026704a", favoritesNeighbours.get(1).getAvatarUrl());
        assertTrue(favoritesNeighbours.get(1).isFavorite());
    }
}
