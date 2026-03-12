import java.io.*;
import java.util.*;

// RoomInventory class
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    public void setRoomAvailability(Map<String, Integer> data) {
        roomAvailability = data;
    }
}

// FilePersistenceService
class FilePersistenceService {

    // Save inventory state
    public void saveInventory(RoomInventory inventory, String filePath) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Map.Entry<String, Integer> entry :
                    inventory.getRoomAvailability().entrySet()) {

                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }

            System.out.println("Inventory saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    // Load inventory state
    public void loadInventory(RoomInventory inventory, String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No valid inventory data found. Starting fresh.");
            return;
        }

        Map<String, Integer> loadedData = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("=");

                if (parts.length == 2) {
                    loadedData.put(parts[0], Integer.parseInt(parts[1]));
                }
            }

            inventory.setRoomAvailability(loadedData);

        } catch (Exception e) {
            System.out.println("Error loading inventory. Starting fresh.");
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("System Recovery\n");

        String filePath = "inventory.txt";

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistenceService = new FilePersistenceService();

        // Load saved data
        persistenceService.loadInventory(inventory, filePath);

        System.out.println("\nCurrent Inventory:");

        for (Map.Entry<String, Integer> entry :
                inventory.getRoomAvailability().entrySet()) {

            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Save inventory before shutdown
        persistenceService.saveInventory(inventory, filePath);
    }
}