package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AdminResource {
    private static AdminResource INSTANCE = null;
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private AdminResource() {
        // it's perfectly fine to have this Singleton constructor empty
        // Private constructor to prevent instantiation outside the class
    }

    public static AdminResource getInstance() {
        if (INSTANCE == null)
            // creates a new instance if there isn't one yet
            INSTANCE = new AdminResource();

        // otherwise, returns the already created one;
        return INSTANCE;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms){
        for (IRoom room : rooms) {
            reservationService.addRoom(room);
        }
    }

    public IRoom getARoom(String roomId){
        return reservationService.getARoom(roomId);
    }

    public Collection<IRoom> getAllRooms(){
        Map<String, IRoom> rooms = reservationService.getAllRooms();
        return new ArrayList<IRoom>(rooms.values());
    }

    public Collection<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    public void displayAllReservations(){
        reservationService.printAllReservations();
    }
}
