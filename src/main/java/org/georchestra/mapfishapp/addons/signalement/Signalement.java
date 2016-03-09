package org.georchestra.mapfishapp.addons.signalement;

public class Signalement {

    private String email;
    private String comment;
    private String mapContext;
    private String login;
    private double latitude;
    private double longitude;

    public Signalement(String email, String comment, String mapContext, double latitude, double longitude) {
        this.email = email;
        this.comment = comment;
        this.mapContext = mapContext;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // optional field in DB
    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public String getComment() {
        return comment;
    }

    public String getMapContext() {
        return mapContext;
    }

    public String getLogin() {
        return login;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}
