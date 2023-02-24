import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class MainMenu {
    private static final String DATE_FORMAT = "MM/dd/yy";
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);
    static final String emailRegex = "^(.+)@(.+).(.+)$";
    static final String namesRegex = "[A-Za-z ]*"; // * includes spaces

    public static void handleMainMenu(){
        String userInput;
        boolean enterMode = true;

        printMainMenu();

        while (enterMode) {
            userInput = scanner.nextLine();

            if (userInput.isEmpty()) {
                // empty input
                try {
                    System.out.println("Invalid Input.");
                    sleep(1000);
                    handleMainMenu();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (userInput.length() == 1 && userInput.matches("^[1-5]$")) {
                enterMode = false;
                // valid input (tests that userInput is only 1 character long & a # from 1-5
                switch (userInput.charAt(0)) {
                    case '1' -> findAndReserveRoom();
                    case '2' -> seeMyReservation();
                    case '3' -> createAccount();
                    case '4' -> AdminMenu.adminMenu();
                    case '5' -> {
                        System.out.println("Exiting.");
                        System.out.println("Thank you for using the Hotel Reservation Application.");
                        // exit the application with status code 0
                        System.exit(0);
                    }
                    default -> {
                        System.out.println("Unknown action\n");
                        handleMainMenu();
                    }
                }
            } else {
                // all other invalid user input
                try {
                    System.out.println("Invalid Input.");
                    sleep(1000);
                    handleMainMenu();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void printMainMenu(){
        System.out.println("\nMain Menu:");

        for(int i = 0; i < 30; i++){
            System.out.print("_");
        }

        System.out.println();
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account ");
        System.out.println("4. Admin ");
        System.out.println("5. Exit");

        for (int i = 0; i < 30; i++){
            System.out.print("_");
        }
        System.out.println();
        System.out.println("Please select a number for the menu option: ");
    }

    private static void findAndReserveRoom() {
        Date checkInDate = null;
        Date checkOutDate = null;
        //boolean enterMode = true; // for while loop
        boolean isValid;
        String dateSelection1;
        String dateSelection2;

        System.out.println("Enter check in date (format: mm/dd/yyyy): ");

        do {
            dateSelection1 = scanner.nextLine();
            isValid = isValidDateFormat(dateSelection1);
            if(!isValid) {
                try {
                    System.out.println("Invalid date");
                    sleep(1000);
                    System.out.println("Enter check in date (format: mm/dd/yyyy): ");
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    checkInDate = dateFormat.parse(dateSelection1);
                } catch (ParseException ex) {
                    System.out.println("Invalid date" + ex);
                }
            }
        } while (!isValid);

        System.out.println("Enter check out date (format: mm/dd/yyyy): ");

        do {
            dateSelection2 = scanner.nextLine();
            isValid = isValidDateFormat(dateSelection2);
            if(!isValid) {
                try {
                    System.out.println("Invalid date");
                    sleep(1000);
                    System.out.println("Enter check out date (format: mm/dd/yyyy): ");
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    checkOutDate = dateFormat.parse(dateSelection2);
                } catch (ParseException ex) {
                    System.out.println("Invalid date" + ex);
                }
            }
        } while (!isValid);

        if (checkInDate != null && checkOutDate != null) {
            // returns only the rooms that don't have a reservation date range
            // that doesn't conflict with the desired date range
            Collection<IRoom> freeRooms = hotelResource.findARoom(checkInDate, checkOutDate);

            // perform recommended dates/rooms feature here
            if (freeRooms.isEmpty()) {
                // adding 7 days here to the original date range given
                Date newCheckInDate = getRecommendedDate(checkInDate);
                Date newCheckOutDate = getRecommendedDate(checkOutDate);
                // updated list of free rooms based on a new date range 7 days forward
                freeRooms = hotelResource.findARoom(newCheckInDate, newCheckOutDate);
                if (!freeRooms.isEmpty()) {
                    System.out.println("There are no available rooms for given dates.");
                    System.out.println("For these alternative dates: \n" +
                            "Check-In Date: " + dateFormat.format(newCheckInDate) + ", Check-Out Date: " + dateFormat.format(newCheckOutDate));
                    System.out.println("\nWe have these available rooms: ");
                    printRooms(freeRooms);
                    askToBook(freeRooms, newCheckInDate, newCheckOutDate);
                } else {
                    System.out.println("There are no available rooms for those dates.");
                }
            } else {
                System.out.println("Available Rooms: ");
                for(int i = 0; i < 30; i++){
                    System.out.print("_");
                }
                System.out.println();
                printRooms(freeRooms);
                askToBook(freeRooms, checkInDate, checkOutDate);
            }
        } else {
            try {
                System.out.println("Invalid dates. Returning to Main Menu.");
                sleep(1000);
                handleMainMenu();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void askToBook(Collection<IRoom> availableRooms, Date checkIn, Date checkOut) {
        boolean enterMode = true; // for while loop

        System.out.println("Would you like to book a room? y/n");

        while (enterMode) {
            String bookARoom = scanner.nextLine();

            if (bookARoom.length() == 1 && Character.toLowerCase(bookARoom.charAt(0)) == 'y') {
                String existingAccount;

                System.out.println("Do you have an account? y/n");

                while (enterMode) {
                    existingAccount = scanner.nextLine();

                    if (existingAccount.length() == 1 && Character.toLowerCase(existingAccount.charAt(0)) == 'y') {

                        System.out.println("Enter your email (format: name@domain.com)");

                        while (enterMode) {
                            String customerEmail = scanner.nextLine();

                            Pattern pattern = Pattern.compile(emailRegex);

                            if (pattern.matcher(customerEmail).matches()) {
                                if (hotelResource.getCustomer(customerEmail) == null) {
                                    try {
                                        System.out.println("Customer not found, please create a new account.");
                                        enterMode = false;
                                        sleep(1000);
                                        handleMainMenu();
                                    } catch(InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    System.out.println("What room number would you like to reserve?");

                                    while (enterMode) {
                                        String myRoomNumber = scanner.nextLine();

                                        for (IRoom room : availableRooms) {
                                            // valid input
                                            if (room.getRoomNumber().equals(myRoomNumber)) {
                                                // will have all the room's info as an IRoom object
                                                IRoom desiredRoom = hotelResource.getRoom(myRoomNumber);

                                                Reservation reservation = hotelResource.bookARoom(customerEmail, desiredRoom, checkIn, checkOut);

                                                System.out.println("Reservation successfully: ");
                                                System.out.println(reservation);
                                                enterMode = false; // breaks out of the loop
                                                break; // breaks out of enhanced for each loop
                                            } else if (myRoomNumber.isEmpty()) {
                                                // if input is empty
                                                try {
                                                    System.out.println("Invalid Input.");
                                                    sleep(1000);
                                                    System.out.println("What room number would you like to reserve?");
                                                } catch(InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                // all other invalid input
                                                try {
                                                    System.out.println("Invalid Input.");
                                                    sleep(1000);
                                                    System.out.println("What room number would you like to reserve?");
                                                } catch(InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
                                // return to main menu after successfully creating a reservation
                                handleMainMenu();
                            } else {
                                try {
                                    System.out.println("Not a valid email format.");
                                    sleep(1000);
                                    System.out.println("Enter your email (format: name@domain.com)");
                                } catch(InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else if (existingAccount.length() == 1 && Character.toLowerCase(existingAccount.charAt(0)) == 'n') {
                        try {
                            System.out.println("Please Create an Account First.");
                            enterMode = false;
                            sleep(1000);
                            handleMainMenu();
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            System.out.println("Invalid Input.");
                            sleep(1000);
                            System.out.println("Do you have an account? y/n");
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (bookARoom.length() == 1 && Character.toLowerCase(bookARoom.charAt(0)) == 'n') {
                try {
                    System.out.println("Returning to Main Menu.");
                    sleep(1000);
                    enterMode = false; // still have to exit the loop since the next method will return you here,
                    // to this loop's invalid input exception at the end & continue to ask you the same question
                    handleMainMenu();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Please enter Y (Yes) or N (No): ");
            }
        }
    }

    private static boolean isValidDateFormat(String dateStr) {
        dateFormat.setLenient(false); // strict parsing

        try {
            // parse the date string
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private static Date getRecommendedDate(Date date) {
        Date finalDateFormat = null;
        try {
            // add 7 days to original check-in & check-out dates to get recommended date range
            // to see available rooms for those dates
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 7);
            //dateFormat.parse(dateFormat.format())
            finalDateFormat = c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  finalDateFormat;
    }

    private static void seeMyReservation() {
        boolean enterMode = true; // for infinite while loop
        Pattern pattern = Pattern.compile(emailRegex);
        System.out.println("To see your reservation, please enter your email (format: name@domain.com): ");
        while (enterMode) {
            String customerEmail = scanner.nextLine();
            if (pattern.matcher(customerEmail).matches()) {
                // when the customer's email is not found
                if (hotelResource.getCustomer(customerEmail) == null) {
                    // user has to navigate to the create a new account option on their own
                    System.out.println("Customer not found, please create a new account.");
                    enterMode = false;
                    handleMainMenu();
                } else {// when the customer's email is found
                    Collection<Reservation> reservations = hotelResource.getCustomersReservations(customerEmail);
                    if (reservations.isEmpty()){
                        // if he has no reservations yet
                        System.out.println("No reservations for this customer.");
                        handleMainMenu();
                    } else {
                        // if he does have a reservation already
                        for (Reservation reservation : reservations) {
                            System.out.println(reservation.toString());
                        }
                        handleMainMenu();
                    }
                    // exiting the while loop after this
                    enterMode = false;
                }
            } else if (customerEmail.isEmpty()) {
                try {
                    System.out.println("Invalid Input.");
                    sleep(1000);
                    System.out.println("Enter your email (format: name@domain.com): ");
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    System.out.println("Invalid email format.");
                    sleep(1000);
                    System.out.println("Enter your email (format: name@domain.com): ");
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void createAccount() {
        boolean enterMode = true;
        Pattern pattern = Pattern.compile(emailRegex);
        String customerEmail = "";

        System.out.println("To create an account, please enter your email address (name@domain.com): ");

        while (enterMode) {
            customerEmail = scanner.nextLine();
            if (pattern.matcher(customerEmail).matches()) {
                // valid input
                enterMode = false; // to exit the loop & go to the next user input
            } else if (customerEmail.isEmpty()) {
                // empty input
                try {
                    System.out.println("Invalid Input.");
                    sleep(1000);
                    System.out.println("Enter your email (format: name@domain.com): ");
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // invalid email input
                try {
                    System.out.println("Invalid email format.");
                    sleep(1000);
                    System.out.println("Enter your email (format: name@domain.com): ");
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        pattern = Pattern.compile(namesRegex);
        String firstName = "";

        System.out.println("Enter first name: ");
        enterMode = true;

        while (enterMode) {
            firstName = scanner.nextLine();
            if (firstName.isEmpty()) {
                // empty input
                try {
                    System.out.println("Invalid Input.");
                    sleep(1000);
                    System.out.println("Enter first name: ");
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (pattern.matcher(firstName).matches()) {
                // valid input
                enterMode = false; // break out of loop
            } else {
                try {
                    // all other invalid input
                    System.out.println("Invalid name format.");
                    sleep(1000);
                    System.out.println("Enter only letters and spaces: ");
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        System.out.println("Enter last name: ");
        String lastName = "";
        enterMode = true;

        while (enterMode) {
            lastName = scanner.nextLine();

            if (lastName.isEmpty()) {
                try {
                    System.out.println("Invalid Input.");
                    sleep(1000);
                    System.out.println("Enter last name: ");
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (pattern.matcher(lastName).matches()) {
                enterMode = false; // break out of loop
            } else {
                try {
                    System.out.println("Invalid name format.");
                    sleep(1000);
                    System.out.println("Enter only letters and spaces: ");
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

        }

        try {
            hotelResource.createACustomer(customerEmail, firstName, lastName);
            System.out.println("Account successfully created.");
            System.out.println("Welcome to the Hotel Reservation Application.");
            handleMainMenu();
        } catch (IllegalArgumentException ex) {
            try {
                System.out.println(ex.getLocalizedMessage());
                sleep(1000);
                System.out.println("Creating account failed, please retry again.");
                sleep(1000);
                createAccount();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printRooms(Collection<IRoom> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room.toString());
            }
        }
    }
}
