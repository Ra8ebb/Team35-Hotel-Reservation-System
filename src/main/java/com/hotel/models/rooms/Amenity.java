package com.hotel.models.rooms;
import com.hotel.exceptions.InvalidAmenityDataException;
public class Amenity {

    private String name;

    /**
     * Constructor ensures amenity name is valid and not empty.
     */
    public Amenity(String name) {
        // Calling the setter ensures the validation logic is applied here too!
        setName(name);
    }

    public String getName() {
        return name;
    }

    /**
     * Updates amenity name.
     * Enforces encapsulation by preventing null, empty, or blank-space names.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidAmenityDataException("Amenity name cannot be null or empty.");
        }
        // .trim() removes any accidental leading/trailing spaces the user might type
        this.name = name.trim();
    }

    @Override
    public String toString() {
        return name;
    }
}