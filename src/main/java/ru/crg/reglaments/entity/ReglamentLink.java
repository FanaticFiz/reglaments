package ru.crg.reglaments.entity;

import lombok.Data;
import lombok.ToString;

import java.net.URL;

@Data
@ToString
public class ReglamentLink {

    private URL url;
    private String text;
    private boolean disabled;

}
