package com.smilehacker.busbird.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;

/**
 * Created by kleist on 15/3/11.
 */
@Table(name = "Destination")
public class Destination extends Model {

    @Column(name = "title")
    public String title;

    @Column(name = "lat")
    public double lat;

    @Column(name ="lon")
    public double lon;

    public static Destination add(String title, double lat, double lon) {
        Destination destination = new Destination();
        destination.title = title;
        destination.lat = lat;
        destination.lon = lon;
        destination.save();
        return destination;
    }

    public static ArrayList<Destination> getAll() {
        return new Select().from(Destination.class)
                .execute();
    }

    public static void del(int id) {
        Destination.delete(Destination.class, id);
    }

    public static Destination update(int id, String title, double lat, double lon) {
        Destination destination = Destination.load(Destination.class, id);
        destination.title = title;
        destination.lat = lat;
        destination.lon = lon;
        destination.save();
        return destination;
    }
}
