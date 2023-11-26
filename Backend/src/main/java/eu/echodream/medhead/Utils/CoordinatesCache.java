package eu.echodream.medhead.Utils;

import eu.echodream.medhead.Models.Coordinate;

import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

public class CoordinatesCache {
    private static final ConcurrentHashMap<String, Coordinate> cache = new ConcurrentHashMap<>();
    private static final String CACHE_FILE_PATH = "coordinates.cache";

    public static Coordinate getCoordinates(String address) {
        return cache.get(address);
    }

    public static void putCoordinates(String address, Coordinate coordinates) {
        cache.put(address, coordinates);
        saveCacheToFile();
    }

    public static void initCache() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CACHE_FILE_PATH))) {
            ConcurrentHashMap<String, Coordinate> loadedCache = (ConcurrentHashMap<String, Coordinate>) ois.readObject();
            cache.putAll(loadedCache);
        } catch (FileNotFoundException e) {
            System.out.println("Fichier cache non trouvé. Création du cache.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveCacheToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CACHE_FILE_PATH))) {
            oos.writeObject(cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
