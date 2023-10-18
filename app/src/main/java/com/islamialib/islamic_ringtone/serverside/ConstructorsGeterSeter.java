package com.islamialib.islamic_ringtone.serverside;

public class ConstructorsGeterSeter {
    private int id;
    private String playlistName;
    private String playlistLink;
    private String username;
    private String imageUrl;

    // Default constructor
    public ConstructorsGeterSeter() {
        // Default constructor
    }

    // Parameterized constructor
    public ConstructorsGeterSeter(int id, String playlistName, String playlistLink, String username, String imageUrl) {
        this.id = id;
        this.playlistName = playlistName;
        this.playlistLink = playlistLink;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public String getPlaylistLink() {
        return playlistLink;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public void setPlaylistLink(String playlistLink) {
        this.playlistLink = playlistLink;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
