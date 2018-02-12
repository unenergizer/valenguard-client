package com.valenguard.client.maps;

import com.valenguard.client.constants.ClientConstants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MapManager {

    private final InternalFileManager internalFileManager;
    private Map<String, MapData> tmxMaps = new HashMap<String, MapData>();

    public MapManager(InternalFileManager internalFileManager) {
        this.internalFileManager = internalFileManager;


        loadAllMaps();
    }

    /**
     * This will dynamically load all TMX maps for the game.
     *
     * @throws RuntimeException No maps were found.
     */
    private void loadAllMaps() {

        // Find all our maps.
        File[] files = internalFileManager.loadFiles();

        // Check to make sure we have some maps
        if (files == null) {
            throw new RuntimeException("No game maps were loaded.");
        }

        // Print out file names for debugging...
        for (File file : files) {
            System.out.println(file.getName());
        }

        // Now load all of our maps
        for (File file : files) {
            String mapName = file.getName();
            tmxMaps.put(mapName, TmxParser.loadXMLFile(ClientConstants.MAP_DIRECTORY, mapName));
        }
    }

    /**
     * Gets the map data associated with a map name. The map name is determined by
     * the file name of the TMX map file.
     *
     * @param mapName The name of the TMX map.
     * @return MapData that contains information about this map.
     * @throws RuntimeException Requested map could not be found or was not loaded.
     */
    public MapData getMapData(String mapName) throws RuntimeException {
        if (tmxMaps.containsKey(mapName)) {
            return tmxMaps.get(mapName);
        } else {
            throw new RuntimeException("Tried to get the map " + mapName + ", but it doesn't exist or was not loaded.");
        }
    }
}
