package api;

import model.Customer;
import model.IRoom;
import model.Reservation;

import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private static HotelResource hotelResource;
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    public static HotelResource getInstance() {
        if (hotelResource == null) {
            hotelResource = new HotelResource();
        }
        return hotelResource;
    }
    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }
    public void createCustomer(String firstName, String lastName,String email){
        customerService.addCustomer(firstName, lastName, email);
    }
    public IRoom getRoom(String roomNumber){
        return reservationService.getRoom(roomNumber);
    }

    public Reservation bookRoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){
        Customer customer = customerService.getCustomer(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }
        return reservationService.reserveRoom(customer, room, checkInDate, checkOutDate);
    }
    public Collection<Reservation> getCustomerReservations(String customerEmail){
        Customer customer = customerService.getCustomer(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }
        return reservationService.getCustomerReservations(customer);
    }
    public Collection<IRoom> findRoom(Date checkInDate, Date checkOutDate){
        return reservationService.findRooms(checkInDate, checkOutDate);
    }

    public ReservationService getReservationService() {
        return reservationService;
    }
}
