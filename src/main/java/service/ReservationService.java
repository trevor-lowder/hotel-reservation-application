package service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Customer;
import model.IRoom;
import model.Reservation;

public class ReservationService {
    private static ReservationService reservationService;
    private final Map<String, IRoom> rooms = new HashMap<>();
    private final Map<Customer, List<Reservation>> reservations = new HashMap<>();

    public static ReservationService getInstance() {
        if (reservationService == null) {
            reservationService = new ReservationService();
        }
        return reservationService;
    }
    public void addRoom(IRoom room){
        rooms.put(room.getRoomNumber(), room);
    }

    public IRoom getRoom(String roomId){
        return rooms.get(roomId);
    }

    public Collection<IRoom> getAllRooms(){
        return rooms.values();
    }

    public Reservation reserveRoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
            Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
            reservations.computeIfAbsent(customer, k -> new ArrayList<>()).add(reservation);
            return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Set<IRoom> unavailableRooms = new HashSet<>();
        for (List<Reservation> customerReservations : reservations.values()) {
            for (Reservation reservation : customerReservations) {
                if (checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate())) {
                    unavailableRooms.add(reservation.getRoom());
                }
            }
        }
        Collection<IRoom> availableRooms = new ArrayList<>(rooms.values());
        availableRooms.removeAll(unavailableRooms);
        return availableRooms;
    }

    public Collection<Reservation> getCustomerReservations(Customer customer){
        return reservations.getOrDefault(customer, Collections.emptyList());
    }

    public void printAllReservations(){
        for (List<Reservation> customerReservations : reservations.values()) {
            for (Reservation reservation : customerReservations) {
                System.out.println(reservation);
            }
        }
    }
}

