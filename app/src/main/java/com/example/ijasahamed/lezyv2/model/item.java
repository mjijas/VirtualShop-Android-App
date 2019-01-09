package com.example.ijasahamed.lezyv2.model;

public class item {

    private String name ;
    private String description;
    private String rating ;
    private int nb_episode;
    private String image_url;
    private String categorie;
    private String studio;

    public item() {
    }

    public item(String name, String description, String rating, int nb_episode, String image_url, String categorie, String studio) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.nb_episode = nb_episode;
        this.image_url = image_url;
        this.categorie = categorie;
        this.studio = studio;
    }

    
}
