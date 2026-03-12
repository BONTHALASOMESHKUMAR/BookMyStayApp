import java.util.*;

// RoomInventory class
class RoomInventory {

    private Map<String, Integer> roomAvailability;

    public RoomInventory() {
        roomAvailability = new HashMap<>();
        roomAvailability.put("Single", 4);   // after booking
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }
}


// CancellationService class
class CancellationService {

    // Stack that stores recently released room IDs
    private Stack<String> releasedRoomIds;

    // Map reservation ID to room type
    private Map<String, String> reservationToRoomTypeMap;

    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationToRoomTypeMap = new HashMap<>();
    }

    // Registers confirmed booking
    public void registerBooking(String reservationId, String roomType) {
        reservationToRoomTypeMap.put(reservationId, roomType);
    }

    // Cancel booking and restore inventory
    public void cancelBooking(String reservationId, RoomInventory inventory) {

        if (!reservationToRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Invalid reservation ID.");
            return;
        }

        String roomType = reservationToRoomTypeMap.get(reservationId);

        releasedRoomIds.push(reservationId);

        Map<String, Integer> availability = inventory.getRoomAvailability();

        availability.put(roomType, availability.get(roomType) + 1);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);

        System.out.println("Rollback History (Most Recent First):");
        showRollbackHistory();

        System.out.println("Updated " + roomType + " Room Availability: " + availability.get(roomType));
    }

    // Display rollback history
    public void showRollbackHistory() {
        for (String id : releasedRoomIds) {
            System.out.println("Released Reservation ID: " + id);
        }
    }
}


// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation\n");

        RoomInventory inventory = new RoomInventory();

        CancellationService service = new CancellationService();

        // Simulate confirmed booking
        service.registerBooking("Single-1", "Single");

        // Cancel booking
        service.cancelBooking("Single-1", inventory);
    }
}