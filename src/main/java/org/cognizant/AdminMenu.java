package org.cognizant;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void adminMenu() {
        boolean keepRunning = true;

        while (keepRunning) {
            displayAdminMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> seeAllCustomers();
                case 2 -> seeAllRooms();
                case 3 -> seeAllReservations();
                case 4 -> addRoom();
                case 5 -> keepRunning = false;
                default -> System.out.println("Please enter a valid option (1-5).");
            }
        }
    }

    private static void displayAdminMenu() {
        System.out.println("+----------------------------+");
        System.out.println("|          Admin Menu        |");
        System.out.println("+----------------------------+");
        System.out.printf("| %-2d. %-22s |\n", 1, "See all customers");
        System.out.printf("| %-2d. %-22s |\n", 2, "See all rooms");
        System.out.printf("| %-2d. %-22s |\n", 3, "See all reservations");
        System.out.printf("| %-2d. %-22s |\n", 4, "Add a room");
        System.out.printf("| %-2d. %-22s |\n", 5, "Main Menu");
        System.out.println("+----------------------------+");
        System.out.println("Please select a number for the menu option:");
    }

    private static int getUserChoice() {
        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Clear the invalid input
        }
        return choice;
    }

    private static void seeAllCustomers() {
        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        System.out.println("All Customers:");
        for (Customer customer : allCustomers) {
            System.out.println(customer);
        }
    }

    private static void seeAllRooms() {
        Collection<IRoom> allRooms = adminResource.getAllRooms();
        System.out.println("All Rooms:");
        for (IRoom room : allRooms) {
            System.out.println(room);
        }
    }

    private static void seeAllReservations() {
        System.out.println("All Reservations:");
        adminResource.displayAllReservations();
    }

    private static void addRoom() {
        List<IRoom> rooms = new ArrayList<>();

        System.out.println("Enter room number:");
        String roomNumber = scanner.next();
        System.out.println("Enter room price:");
        double price = scanner.nextDouble();
        System.out.println("Enter room type (1 for SINGLE, 2 for DOUBLE):");
        int roomTypeInput = scanner.nextInt();

        RoomType roomType = roomTypeInput == 1 ? RoomType.SINGLE : RoomType.DOUBLE;
        Room room = new Room(roomNumber, price, roomType);
        rooms.add(room);

        adminResource.addRoom(rooms);
        System.out.println("Room added successfully.");
    }
}
