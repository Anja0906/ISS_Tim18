package org.tim_18.UberApp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String image;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    public Document(Integer id, String image, Driver driver) {
        this.id     = id;
        this.image  = image;
        this.driver = driver;
    }
    public Document() {}

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

}