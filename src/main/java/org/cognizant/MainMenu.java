package org.cognizant;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.Collection;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

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
        System.out.println("Enter Check-In Date (yyyy-mm-dd):");
        Date checkInDate = parseDate(scanner.next());
        System.out.println("Enter Check-Out Date (yyyy-mm-dd):");
        Date checkOutDate = parseDate(scanner.next());

        Collection<IRoom> availableRooms = hotelResource.findRoom(checkInDate, checkOutDate);

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for the selected dates.");
        } else {
            System.out.println("Available rooms:");
            for (IRoom room : availableRooms) {
                System.out.println(room);
            }

            System.out.println("Enter room number to reserve:");
            String roomNumber = scanner.next();
            IRoom room = hotelResource.getRoom(roomNumber);

            System.out.println("Enter your email:");
            String email = scanner.next();
            Customer customer = hotelResource.getCustomer(email);

            if (customer == null) {
                System.out.println("No customer found with the given email. Please create an account first.");
            } else {
                Reservation reservation = hotelResource.bookRoom(email, room, checkInDate, checkOutDate);
                System.out.println("Reservation successful: " + reservation);
            }
        }
    }

    private static void seeMyReservations() {
        System.out.println("Enter your email:");
        String email = scanner.next();
        Collection<Reservation> reservations = hotelResource.getCustomerReservations(email);

        if (reservations.isEmpty()) {
            System.out.println("No reservations found for the given email.");
        } else {
            System.out.println("Your reservations:");
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    private static void createAccount() {
        System.out.println("Enter First Name:");
        String firstName = scanner.next();
        System.out.println("Enter Last Name:");
        String lastName = scanner.next();
        System.out.println("Enter Email (name@domain.com):");
        String email = scanner.next();

        hotelResource.createCustomer(firstName, lastName, email);
        System.out.println("Account created successfully.");
    }

    private static Date parseDate(String dateStr) {
        return java.sql.Date.valueOf(dateStr);
    }
}
