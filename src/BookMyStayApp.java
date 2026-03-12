import java.util.*;

public class BookMyStayApp {

    // ---------- Room ----------
    static abstract class Room {
        protected int numberOfBeds;
        protected int squareFeet;
        protected double pricePerNight;

        public Room(int beds, int size, double price) {
            numberOfBeds = beds;
            squareFeet = size;
            pricePerNight = price;
        }

        public void displayRoomDetails() {
            System.out.println("Beds: " + numberOfBeds);
            System.out.println("Size: " + squareFeet + " sqft");
            System.out.println("Price per night: " + pricePerNight);
        }
    }

    static class SingleRoom extends Room {
        public SingleRoom() { super(1,250,1500.0); }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() { super(2,400,2500.0); }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() { super(3,750,5000.0); }
    }

    // ---------- Inventory ----------
    static class RoomInventory {

        private Map<String,Integer> roomAvailability;

        public RoomInventory(){
            roomAvailability = new HashMap<>();
            roomAvailability.put("Single",5);
            roomAvailability.put("Double",3);
            roomAvailability.put("Suite",2);
        }

        public Map<String,Integer> getRoomAvailability(){
            return roomAvailability;
        }
    }

    // ---------- Reservation ----------
    static class Reservation {

        private String guestName;
        private String roomType;

        public Reservation(String guestName,String roomType){
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName(){ return guestName; }
        public String getRoomType(){ return roomType; }
    }

    // ---------- Booking Queue ----------
    static class BookingRequestQueue {

        private Queue<Reservation> requestQueue;

        public BookingRequestQueue(){
            requestQueue = new LinkedList<>();
        }

        public void addRequest(Reservation r){
            requestQueue.offer(r);
        }

        public Reservation getNextRequest(){
            return requestQueue.poll();
        }

        public boolean hasPendingRequests(){
            return !requestQueue.isEmpty();
        }
    }

    // ---------- Room Allocation ----------
    static class RoomAllocationService {

        private Set<String> allocatedRoomIds;
        private Map<String,Set<String>> assignedRoomsByType;

        public RoomAllocationService(){
            allocatedRoomIds = new HashSet<>();
            assignedRoomsByType = new HashMap<>();
        }

        public String allocateRoom(Reservation reservation,RoomInventory inventory){

            String type = reservation.getRoomType();
            Map<String,Integer> availability = inventory.getRoomAvailability();

            if(availability.get(type) <= 0){
                System.out.println("No rooms available");
                return null;
            }

            String roomId = generateRoomId(type);

            allocatedRoomIds.add(roomId);

            assignedRoomsByType
                    .computeIfAbsent(type,k->new HashSet<>())
                    .add(roomId);

            availability.put(type,availability.get(type)-1);

            System.out.println("Booking confirmed for Guest: "
                    + reservation.getGuestName() + ", Room ID: " + roomId);

            return roomId;
        }

        private String generateRoomId(String type){
            int id = assignedRoomsByType
                    .getOrDefault(type,new HashSet<>())
                    .size()+1;
            return type + "-" + id;
        }
    }

    // ---------- AddOn Service ----------
    static class AddOnService {

        private String serviceName;
        private double cost;

        public AddOnService(String name,double cost){
            serviceName = name;
            this.cost = cost;
        }

        public String getServiceName(){
            return serviceName;
        }

        public double getCost(){
            return cost;
        }
    }

    // ---------- AddOn Service Manager ----------
    static class AddOnServiceManager {

        private Map<String,List<AddOnService>> servicesByReservation;

        public AddOnServiceManager(){
            servicesByReservation = new HashMap<>();
        }

        public void addService(String reservationId,AddOnService service){

            servicesByReservation
                    .computeIfAbsent(reservationId,k->new ArrayList<>())
                    .add(service);
        }

        public double calculateTotalServiceCost(String reservationId){

            List<AddOnService> services =
                    servicesByReservation.getOrDefault(reservationId,new ArrayList<>());

            double total = 0;

            for(AddOnService s: services){
                total += s.getCost();
            }

            return total;
        }
    }

    // ---------- MAIN ----------
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Abhi","Single"));
        queue.addRequest(new Reservation("Subha","Double"));
        queue.addRequest(new Reservation("Vanmathi","Suite"));

        RoomAllocationService allocationService = new RoomAllocationService();

        String reservationId = null;

        while(queue.hasPendingRequests()){
            Reservation r = queue.getNextRequest();
            reservationId = allocationService.allocateRoom(r,inventory);
        }

        // Use Case 7
        AddOnServiceManager manager = new AddOnServiceManager();

        AddOnService breakfast = new AddOnService("Breakfast",1000);
        AddOnService spa = new AddOnService("Spa",500);

        manager.addService(reservationId,breakfast);
        manager.addService(reservationId,spa);

        System.out.println();
        System.out.println("Add-On Service Selection");
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Total Add-On Cost: " +
                manager.calculateTotalServiceCost(reservationId));
    }
}