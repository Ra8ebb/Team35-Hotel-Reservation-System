package com.hotel.models.rooms;
public class RoomType {

    // Enum representing allowed room categories
    public enum TypeName { SINGLE, DOUBLE, SUITE }

    private TypeName name;
    private double pricePerNight;
    private int capacity;

    /**
     * Constructor ensures valid room type creation.
     */
    public RoomType(TypeName name, double pricePerNight, int capacity) {
        setName(name);
        setPricePerNight(pricePerNight);
        setCapacity(capacity);
    }

    public TypeName getName() { return name; }

    public void setName(TypeName name) {
        if (name == null)
            throw new IllegalArgumentException("Type name cannot be null");
        this.name = name;
    }

    public double getPricePerNight() { return pricePerNight; }

    public void setPricePerNight(double price) {
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative");
        this.pricePerNight = price;
    }

    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity must be positive");
        this.capacity = capacity;
    }
}
