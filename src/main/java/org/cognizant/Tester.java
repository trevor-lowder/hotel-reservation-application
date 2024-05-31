package org.cognizant;

import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import model.RoomType;

import java.util.*;

public class Tester {
    public static void main(String[] args) {
        System.out.println("Testing Hotel Methods:\n");
        // Get instance of HotelResource
        HotelResource hotelResource = HotelResource.getInstance();

        // Test createCustomer and getCustomer methods
        hotelResource.createCustomer("John", "Doe", "john.doe@example.com");
        hotelResource.createCustomer("Jane", "Doe", "jane.doe@example.com");

        Customer customer1 = hotelResource.getCustomer("john.doe@example.com");
        Customer customer2 = hotelResource.getCustomer("jane.doe@example.com");

        System.out.println("Customer 1: " + customer1);
        System.out.println("Customer 2: " + customer2);

        // Test addRoom and getRoom methods
        Room room1 = new Room("101", 100.0, RoomType.SINGLE);
        Room room2 = new Room("102", 150.0, RoomType.DOUBLE);

        hotelResource.getReservationService().addRoom(room1);
        hotelResource.getReservationService().addRoom(room2);

        IRoom retrievedRoom1 = hotelResource.getRoom("101");
        IRoom retrievedRoom2 = hotelResource.getRoom("102");

        System.out.println("Retrieved Room 1: " + retrievedRoom1);
        System.out.println("Retrieved Room 2: " + retrievedRoom2);

        // Test bookRoom method
        Calendar calendar = Calendar.getInstance();

        calendar.set(2024, Calendar.JUNE, 1);
        Date checkInDate1 = calendar.getTime();

        calendar.set(2024, Calendar.JUNE, 5);
        Date checkOutDate1 = calendar.getTime();

        Reservation reservation1 = hotelResource.bookRoom("john.doe@example.com", room1, checkInDate1, checkOutDate1);

        calendar.set(2024, Calendar.JUNE, 10);
        Date checkInDate2 = calendar.getTime();

        calendar.set(2024, Calendar.JUNE, 15);
        Date checkOutDate2 = calendar.getTime();

        Reservation reservation2 = hotelResource.bookRoom("jane.doe@example.com", room2, checkInDate2, checkOutDate2);

        System.out.println("Reservation 1: " + reservation1);
        System.out.println("Reservation 2: " + reservation2);

        // Test getCustomerReservations method
        Collection<Reservation> customer1Reservations = hotelResource.getCustomerReservations("john.doe@example.com");
        Collection<Reservation> customer2Reservations = hotelResource.getCustomerReservations("jane.doe@example.com");

        System.out.println("Customer 1 Reservations: " + customer1Reservations);
        System.out.println("Customer 2 Reservations: " + customer2Reservations);

        // Test findRoom method
        calendar.set(2024, Calendar.JUNE, 1);
        Date searchCheckInDate = calendar.getTime();

        calendar.set(2024, Calendar.JUNE, 5);
        Date searchCheckOutDate = calendar.getTime();

        Collection<IRoom> availableRooms = hotelResource.findRoom(searchCheckInDate, searchCheckOutDate);
        System.out.println("Available rooms from June 1, 2024 to June 5, 2024:");
        for (IRoom room : availableRooms) {
            System.out.println(room);
        }

        // Additional check for room availability with no reservations
        calendar.set(2024, Calendar.JUNE, 20);
        Date newSearchCheckInDate = calendar.getTime();

        calendar.set(2024, Calendar.JUNE, 25);
        Date newSearchCheckOutDate = calendar.getTime();

        Collection<IRoom> availableRoomsLater = hotelResource.findRoom(newSearchCheckInDate, newSearchCheckOutDate);
        System.out.println("Available rooms from June 20, 2024 to June 25, 2024:");
        for (IRoom room : availableRoomsLater) {
            System.out.println(room);
        }
        System.out.println();
        System.out.println("Testing Admin Methods:\n");
        // Get instance of AdminResource
        AdminResource adminResource = AdminResource.getInstance();

        // Test getCustomer method
        Customer customer = adminResource.getCustomer("john.doe@example.com");
        System.out.println("Retrieved Customer: " + customer);

        // Test addRoom method
        List<IRoom> roomsToAdd = new ArrayList<>();
        roomsToAdd.add(new Room("101", 100.0, RoomType.SINGLE));
        roomsToAdd.add(new Room("102", 150.0, RoomType.DOUBLE));
        adminResource.addRoom(roomsToAdd);
        System.out.println("Rooms added successfully.");

        // Test getAllRooms method
        Collection<IRoom> allRooms = adminResource.getAllRooms();
        System.out.println("All Rooms:");
        for (IRoom room : allRooms) {
            System.out.println(room);
        }

        // Test getAllCustomers method
        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        System.out.println("All Customers:");
        for (Customer cust : allCustomers) {
            System.out.println(cust);
        }

        // Test displayAllReservations method
        System.out.println("All Reservations:");
        adminResource.displayAllReservations();
    }
    }