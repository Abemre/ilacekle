package com.example.firebasekullanimi;

public class Medicine {
    private String name;
    private String dose;
    private String imageUrl;

    public Medicine() {
        // Default constructor required for calls to DataSnapshot.getValue(Medicine.class)
    }

    public Medicine(String name, String dose, String imageUrl) {
        this.name = name;
        this.dose = dose;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDose() {
        return dose;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
