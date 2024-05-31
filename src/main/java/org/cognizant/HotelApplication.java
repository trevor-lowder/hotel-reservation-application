package org.cognizant;

import api.HotelResource;
import model.Room;
import model.RoomType;

import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        initializeData();
        MainMenu.main(args);
    }

    private static void initializeData() {
        HotelResource hotelResource = HotelResource.getInstance();

        // Create customers
        hotelResource.createCustomer("John", "Doe", "johndoe@domain.com");
        hotelResource.createCustomer("Jane", "Doe", "janedoe@domain.com");

        // Create rooms
        Room room101 = new Room("101", 75.0, RoomType.SINGLE);
        Room room201 = new Room("201", 150.0, RoomType.DOUBLE);
        Room room777 = new Room("777", 0.0, RoomType.SINGLE);

        hotelResource.getReservationService().addRoom(room101);
        hotelResource.getReservationService().addRoom(room201);
        hotelResource.getReservationService().addRoom(room777);

        // Create a reservation
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JUNE, 1);
        java.util.Date checkInDate = calendar.getTime();

        calendar.set(2024, Calendar.JUNE, 5);
        java.util.Date checkOutDate = calendar.getTime();

        hotelResource.bookRoom("johndoe@domain.com", room101, checkInDate, checkOutDate);
    }
}
