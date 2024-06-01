package org.cognizant;

import java.util.InputMismatchException;
import java.util.Scanner;

import api.AdminResource;
import model.Room;
import model.RoomType;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public static void adminMenu() {
        boolean keepRunning = true;

        while (keepRunning) {
            displayAdminMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> addRoom();
                case 2 -> {
                    System.out.println("All rooms:");
                    adminResource.getAllRooms().forEach(System.out::println);
                }
                case 3 -> {
                    System.out.println("All customers:");
                    adminResource.getAllCustomers().forEach(System.out::println);
                }
                case 4 -> {
                    System.out.println("All reservations:");
                    adminResource.displayAllReservations();
                }
                case 5 -> keepRunning = false;
                default -> System.out.println("Please enter a valid option (1-5).");
            }
        }
    }

    private static void displayAdminMenu() {
        System.out.println("+----------------------------+");
        System.out.println("|         Admin Menu         |");
        System.out.println("+----------------------------+");
        System.out.printf("| %-2d. %-22s |\n", 1, "Add a room");
        System.out.printf("| %-2d. %-22s |\n", 2, "See all rooms");
        System.out.printf("| %-2d. %-22s |\n", 3, "See all customers");
        System.out.printf("| %-2d. %-22s |\n", 4, "See all reservations");
        System.out.printf("| %-2d. %-22s |\n", 5, "Back to main menu");
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

    private static void addRoom() {
        System.out.println("Enter room number:");
        String roomNumber = scanner.next();

        boolean validPrice = false;
        double price = 0.0;
        while (!validPrice) {
            try {
                System.out.println("Enter room price:");
                price = scanner.nextDouble();
                validPrice = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid price.");
                scanner.next();
            }
        }

        System.out.println("Enter room type: 1 for Single bed, 2 for Double bed");
        RoomType roomType = null;
        boolean validRoomType = false;
        while (!validRoomType) {
            int roomTypeOption = scanner.nextInt();
            switch (roomTypeOption) {
                case 1 -> {
                    roomType = RoomType.SINGLE;
                    validRoomType = true;
                }
                case 2 -> {
                    roomType = RoomType.DOUBLE;
                    validRoomType = true;
                }
                default -> System.out.println("Invalid input. Please enter 1 for Single bed, 2 for Double bed.");
            }
        }

        Room room = new Room(roomNumber, price, roomType);
        adminResource.addRoom(room);
        System.out.println("Room added successfully: " + room);
    }
}
