package model;

import java.util.Date;

public class Reservation {
    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        this.customer=customer;
        this.room=room;
        this.checkInDate=checkInDate;
        this.checkOutDate=checkOutDate;
    }

    public Date getCheckInDate(){
        return checkInDate;
    }

    public Date getCheckOutDate(){
        return checkOutDate;
    }

    public IRoom getRoom(){
        return room;
    }

    @Override
    public String toString(){
        return "Reservation for "+customer.getFirstName()+" "+customer.getLastName()+": Room number "+room.getRoomNumber()+" reserved from "+checkInDate+" until "+checkOutDate;
    }
}
