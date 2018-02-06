package com.valenguard.client.maps;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapData {

    private final String mapName;
    private final int mapWidth;
    private final int mapHeight;
    private final Tile map[][];

    public MapData(String mapName, int mapWidth, int mapHeight, Tile[][] map) {
        this.mapName = mapName;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.map = map;
    }

    /**
     * Test to see if the tile/coordinate can be walked on.
     *
     * @param x The X grid coordinate a entity is attempting to move to.
     * @param y The Y grid coordinate a entity is attempting to move to.
     * @return True if the tile/coordinate is walkable. False otherwise.
     */
    public boolean isTraversable(int x, int y) {
        return map[x][y].isTraversable();
    }

    /**
     * This is a test to make sure the entity does not go outside the map.
     *
     * @param x The X grid coordinate a entity is attempting to move to.
     * @param y The Y grid coordinate a entity is attempting to move to.
     * @return True if entity is attempting to move outside the map. False otherwise.
     */
    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= mapWidth || y < 0 || y >= mapHeight;
    }

    /**
     * Retrieves a tile by the location passed in. It is assumed that the location
     * is not out of bounds before being passed.
     *
     * @param location the location on the map.
     * @return The tile associated with the location.
     */
    public Tile getTileByLocation(Location location) {
        return map[location.getX()][location.getY()];
    }
}