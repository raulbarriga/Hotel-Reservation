import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    /** @noinspection BusyWait*/
    public static void adminMenu() {
        String userInput;
        boolean enterMode = true;

        printAdminMenu();

        while (enterMode) {
            userInput = scanner.nextLine();

            if (userInput.isEmpty()) {
                // empty input
                try {
                    System.out.println("Invalid Input.");
                    sleep(1000);
                    adminMenu();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (userInput.length() == 1 && userInput.matches("^[1-5]$")) {
                // valid input
                enterMode = false;
                switch (userInput.charAt(0)) {
                    case '1' -> {
                        printAllCustomers();
                        try {
                            sleep(2000);
                            adminMenu();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case '2' -> {
                        printAllRooms();
                        try {
                            sleep(2000);
                            adminMenu();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case '3' -> {
                        printAllReservations();
                        try {
                            sleep(2000);
                            adminMenu();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    case '4' -> addARoom();
                    case '5' -> MainMenu.handleMainMenu();
                    default -> {
                        System.out.println("Unknown action\n");
                        adminMenu();
                    }
                }
            } else {
                // all other invalid user input
                try {
                    System.out.println("Invalid Input.");
                    sleep(1000);
                    adminMenu();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void printAdminMenu() {
        System.out.println("\nAdmin Menu: ");

        for (int i = 0; i < 30; i++){
            System.out.print("_");
        }

        System.out.println();
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a room");
        System.out.println("5. Back to Main Menu");

        for (int i = 0; i < 30; i++){
            System.out.print("_");
        }

        System.out.println();
        System.out.println("Please select a number for the menu option: ");
    }

    private static void printAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer.toString());
            }
        }
    }

    private static void printAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if(rooms.isEmpty()) {
            System.out.println("No rooms.");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room.toString());
            }
        }
    }

    private static void printAllReservations() {
        adminResource.displayAllReservations();
    }

    /** @noinspection BusyWait*/
    private static void addARoom() {
        String userInput = "y";
        List<IRoom> rooms = new ArrayList<>();
        boolean enterMode = true; // for while loop
        final String validMoneyFormat = "^\\$?\\d+(,\\d{3})*(\\.\\d+)?$";

        while (Character.toLowerCase(userInput.charAt(0)) == 'y') {

            System.out.println("Enter room number: ");
            String roomNumber = null;
            while (enterMode) {
                roomNumber = scanner.nextLine();

                if (roomNumber.isEmpty()) {
                    try {
                        System.out.println("Invalid Input.");
                        sleep(2000);
                        System.out.println("Enter room number: ");
                    } catch(InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else if (!roomNumber.matches("^[1-9][0-9]*$")) {
                    try {
                        System.out.println("Invalid Input.");
                        sleep(2000);
                        System.out.println("Enter room number: ");
                    } catch(InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // check if that room # already exists, null means it doesn't exist
                    if (adminResource.getARoom(roomNumber) == null) {
                        enterMode = false;
                    } else {
                        try {
                            System.out.println("Room number already exists.");
                            sleep(2000);
                            System.out.println("Enter room number: ");
                        } catch(InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            Double pricePerNight = null;
            System.out.println("Enter price per night: ");
            enterMode = true;
            while (enterMode) {
                String pricePerNightStr = scanner.nextLine();

                if (pricePerNightStr.isEmpty()) {
                    try {
                        System.out.println("Invalid Input.");
                        sleep(2000);
                        System.out.println("Enter price per night: ");
                    } catch(InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else if (!pricePerNightStr.matches(validMoneyFormat)) {
                    try {
                        System.out.println("Invalid Input.");
                        sleep(2000);
                        System.out.println("Enter price per night: ");
                    } catch(InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        pricePerNight = Double.parseDouble(pricePerNightStr.replace("$", ""));
                        enterMode = false;
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid format for room price, decimals have to be separated by a decimal point (.)");
                }
                }
            }

            RoomType roomType = null;
            String input;
            System.out.println("Enter room type (1 for single bed, 2 for double beds): ");
            enterMode = true;
            while (enterMode) {
                input = scanner.nextLine();
                if (input.length() != 1) {
                    try {
                        System.out.println("Invalid Input.");
                        sleep(2000);
                        System.out.println("Type in 1 for single bed and 2 for double bed.");
                    } catch(InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if (input.charAt(0) == '1' || input.charAt(0) == '2') {
                        if (input.charAt(0) == '1') {
                            roomType = RoomType.valueOf("SINGLE");
                        } else {
                            roomType = RoomType.valueOf("DOUBLE");
                        }
                        enterMode = false;
                    } else {
                        try {
                            System.out.println("Invalid Input.");
                            sleep(2000);
                            System.out.println("Type in 1 for single bed and 2 for double bed.");
                        } catch(InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            Room room = new Room(roomNumber, roomType, pricePerNight);
            rooms.add(room);
            adminResource.addRoom(rooms);
            enterMode = true;
            System.out.println("Would you like to add another room? (y/n)");
            while (enterMode) {
                userInput = scanner.nextLine();
                if (userInput.length() != 1) {
                    System.out.println("Please enter Y (Yes) or N (No): ");
                } else {
                    if (Character.toLowerCase(userInput.charAt(0)) == 'y' || Character.toLowerCase(userInput.charAt(0)) == 'n') {
                        if (Character.toLowerCase(userInput.charAt(0)) == 'y') {
                            enterMode = false;
                            addARoom();
                        } else {
                            enterMode = false;
                        }
                    } else {
                        System.out.println("Please enter Y (Yes) or N (No): ");
                    }
                }
            }
        }
        adminMenu();
    }
}