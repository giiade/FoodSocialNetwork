package se.FSN.foodsocialnetwork;

/**
 * Created by JulioLopez on 14/1/15.
 */
public class Friend {

    private String ID;
    private String email;
    private String country;
    private String username;
    private String imgUrl;

    public Friend() {
    }

    public Friend(String mail, String country, String username, String imgUrl) {
        this.setEmail(mail);
        this.setCountry(country);
        this.setUsername(username);
        this.setImgUrl(imgUrl);
    }

    public Friend(Friend f) {
        this.setID(f.getID());
        this.setUsername(f.getUsername());
        this.setEmail(f.getEmail());
        this.setCountry(f.getCountry());
        this.setImgUrl(f.getImgUrl());
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
