package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;

public class ReservationService {
    private static ReservationService INSTANCE;
    private static final Map<String, IRoom> totalHotelRooms = new HashMap<>();
    private final Set<Reservation> reservations = new HashSet<>();

    public static ReservationService getInstance() {
        if (INSTANCE == null)
            // creates a new instance if there isn't one yet
            INSTANCE = new ReservationService();

        // otherwise, returns the already created one;
        return INSTANCE;
    }

    // for admin use only
    // to add a room to hotel system
    public void addRoom(IRoom room) {
        Room newRoom = new Room(room.getRoomNumber(), room.getRoomType(), room.getRoomPrice());
        // newRoom will contain a room object will all of that room's info
        totalHotelRooms.putIfAbsent(newRoom.getRoomNumber(), newRoom);
    }

    // for admin use only
    // to fetch a room from the hotel system
    public IRoom getARoom(String roomId) {
        return totalHotelRooms.get(roomId);
    }

    public Map<String, IRoom> getAllRooms(){
        return totalHotelRooms;
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);

        // we've already covered checking the desired date range with any existing reservation date ranges
        // so here we're just creating the new reservation
        if (reservations.contains(reservation)) {
            throw new IllegalArgumentException("This room is already reserved during these dates.");
        } else {
            reservations.add(reservation);
        }

        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {

        // a copy of total of all rooms
        Map<String, IRoom> freeRooms = new HashMap<>(totalHotelRooms);

        for (Reservation reservation : reservations) {

            boolean checkDateRangeConflict = checkDateRangeConflict(checkInDate, checkOutDate);
            // this will remove any existing date range reservation conflicts with an upcoming desired reservation date range
            // thus, the user will only see rooms that don't have a conflict with the desired date range at the time of choosing a room
            if (checkDateRangeConflict) {
                freeRooms.remove(reservation.getRoom().getRoomNumber());
            } //Do nothing if reservation has no issue with dates
        }

        return new ArrayList<>(freeRooms.values());

    }

    public boolean checkDateRangeConflict(Date startDate, Date endDate) {
        for (Reservation reservation : reservations) {
            // Check if the end date of the existing reservation is on or after the start date
            if (reservation.getCheckOutDate().compareTo(startDate) >= 0) {
                // Check if the start date of the existing reservation is before the end date
                if (reservation.getCheckInDate().compareTo(endDate) < 0) {
                    // The date ranges conflict if the start and end dates are not exactly the same
                    if (!reservation.getCheckInDate().equals(startDate) || !reservation.getCheckOutDate().equals(endDate)) {
                        return true;
                    }
                    // Identical date ranges are also conflicting
                    else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> customersReservation = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)){
                customersReservation.add(reservation);
            }
        }
        return customersReservation;
    }

    public void printAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations.");
        } else {
            for (Reservation reservation : reservations){
                System.out.println(reservation.toString());
            }
        }
    }
}