package com.example.martin.projectskola.databaze;

public class Place {

    private int id;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private String city;
    private String cesta;

    public Place(int id, String title, String description, double latitude, double longitude, String city, String cesta) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.city = city;
        this.cesta = cesta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCesta(String cesta) {
        this.cesta = cesta;
    }

    public String getCesta() {
        return cesta;
    }
}
