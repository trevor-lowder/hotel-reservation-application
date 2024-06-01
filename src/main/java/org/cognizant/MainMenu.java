package org.cognizant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean keepRunning = true;

        while (keepRunning) {
            displayMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> findAndReserveRoom();
                case 2 -> seeMyReservations();
                case 3 -> createAccount();
                case 4 -> AdminMenu.adminMenu();
                case 5 -> {
                    System.out.println("Goodbye!");
                    keepRunning = false;
                }
                default -> System.out.println("Please enter a valid option (1-5).");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("+----------------------------+");
        System.out.println("|         Main Menu          |");
        System.out.println("+----------------------------+");
        System.out.printf("| %-2d. %-22s |\n", 1, "Find & reserve a room");
        System.out.printf("| %-2d. %-22s |\n", 2, "See my reservations");
        System.out.printf("| %-2d. %-22s |\n", 3, "Create an account");
        System.out.printf("| %-2d. %-22s |\n", 4, "Admin");
        System.out.printf("| %-2d. %-22s |\n", 5, "Exit");
        System.out.println("+----------------------------+");
        System.out.println("Please enter a number for the menu option:");
    }

    private static int getUserChoice() {
        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
        return choice;
    }

    private static void findAndReserveRoom() {
        Date checkInDate = null;
        Date checkOutDate = null;

        while (true) {
            System.out.println("Enter Check-In Date (yyyy-mm-dd):");
            checkInDate = parseDate(scanner.next());
            System.out.println("Enter Check-Out Date (yyyy-mm-dd):");
            checkOutDate = parseDate(scanner.next());

            if (checkInDate != null && checkOutDate != null) {
                if (!checkOutDate.after(checkInDate)) {
                    System.out.println("Check-Out Date must be after Check-In Date.");
                } else {
                    break;
                }
            } else {
                System.out.println("Invalid date format. Please enter dates in 'YYYY-MM-DD' format.");
            }
        }

        Collection<IRoom> availableRooms = hotelResource.findRoom(checkInDate, checkOutDate);

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for the selected dates.");
            System.out.println("How many days would you like to add to your search?");
            int offsetDays = scanner.nextInt();
            checkInDate = addDays(checkInDate, offsetDays);
            checkOutDate = addDays(checkOutDate, offsetDays);
            System.out.println("New Check-In Date: " + formatDate(checkInDate));
            System.out.println("New Check-Out Date: " + formatDate(checkOutDate));

            availableRooms = hotelResource.findRoom(checkInDate, checkOutDate);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for the new dates.");
                return;
            }
        }

        System.out.println("Available rooms:");
        for (IRoom room : availableRooms) {
            System.out.println(room);
        }

        System.out.println("Enter room number to reserve:");
        String roomNumber = scanner.next();
        IRoom room = hotelResource.getRoom(roomNumber);

        if (!availableRooms.contains(room)) {
            System.out.println("The selected room is not available. Please choose a different room.");
        } else {
            System.out.println("Enter your email:");
            String email = scanner.next();
            Customer customer = hotelResource.getCustomer(email);

            if (customer == null) {
                System.out.println("No customer found with the given email. Please create an account first.");
            } else {
                if (userHasReservationDuringDates(email, checkInDate, checkOutDate)) {
                    System.out.println("You already have a reservation during the selected dates. Please choose different dates.");
                } else {
                    Reservation reservation = hotelResource.bookRoom(email, room, checkInDate, checkOutDate);
                    System.out.println("Reservation successful: " + reservation);
                }
            }
        }
    }

    private static boolean userHasReservationDuringDates(String email, Date checkInDate, Date checkOutDate) {
        Collection<Reservation> reservations = hotelResource.getCustomerReservations(email);
        for (Reservation reservation : reservations) {
            if (datesOverlap(reservation.getCheckInDate(), reservation.getCheckOutDate(), checkInDate, checkOutDate)) {
                return true;
            }
        }
        return false;
    }

    private static boolean datesOverlap(Date start1, Date end1, Date start2, Date end2) {
        return (start1.before(end2) && start2.before(end1));
    }

    private static void seeMyReservations() {
        boolean customerFound = false;
        while (!customerFound) {
            System.out.println("Enter your email:");
            String email = scanner.next();
            try {
                Collection<Reservation> reservations = hotelResource.getCustomerReservations(email);

                if (reservations.isEmpty()) {
                    System.out.println("No reservations found for the given email.");
                } else {
                    System.out.println("Your reservations:");
                    for (Reservation reservation : reservations) {
                        System.out.println(reservation);
                    }
                }
                customerFound = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter a valid email or create an account first.");
            }
        }
    }

    private static void createAccount() {
        boolean accountCreated = false;
        while (!accountCreated) {
            System.out.println("Enter First Name:");
            String firstName = scanner.next();
            System.out.println("Enter Last Name:");
            String lastName = scanner.next();
            System.out.println("Enter Email (name@domain.com):");
            String email = scanner.next();

            try {
                hotelResource.createCustomer(firstName, lastName, email);
                System.out.println("Account created successfully.");
                accountCreated = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter a valid email address.");
            }
        }
    }

    private static Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    private static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}