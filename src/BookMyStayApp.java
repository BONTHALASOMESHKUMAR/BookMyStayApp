import java.util.*;

// Reservation class
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


// BookingHistory class
class BookingHistory {

    // List that stores confirmed reservations
    private List<Reservation> confirmedReservations;

    // Initializes an empty booking history
    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    // Adds a confirmed reservation to booking history
    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    // Returns all confirmed reservations
    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}


// BookingReportService class
class BookingReportService {

    // Displays summary report of all confirmed bookings
    public void generateReport(BookingHistory history) {

        System.out.println("Booking History and Reporting\n");

        for (Reservation r : history.getConfirmedReservations()) {
            System.out.println("Guest: " + r.getGuestName() +
                    ", Room Type: " + r.getRoomType());
        }
    }
}


// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        // Add confirmed bookings
        history.addReservation(new Reservation("Abhi", "Single"));
        history.addReservation(new Reservation("Subha", "Double"));
        history.addReservation(new Reservation("Vanmathi", "Suite"));

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history);
    }
}