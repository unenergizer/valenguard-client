package com.valenguard.client.maps;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

public class MapManager {

    private static final String MAP_DIRECTORY = "maps/";

    private Map<String, MapData> tmxMaps = new HashMap<String, MapData>();

    public MapManager() {
        loadAllMaps();
    }

    /**
     * This will dynamically load all TMX maps for the game.
     *
     * @throws RuntimeException No maps were found.
     */
    private void loadAllMaps() {

        // Find all our maps.
        File dir = new File(MAP_DIRECTORY);
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".tmx");
            }
        });

        System.out.println(dir.getAbsolutePath());

        // Check to make sure we have some maps
        if (files == null) {
            throw new RuntimeException("No game maps were loaded.");
        }

        // Now load all of our maps
        for (File file : files) {
            String mapName = file.getName();
            tmxMaps.put(mapName, TmxParser.loadXMLFile(MAP_DIRECTORY, mapName));
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
