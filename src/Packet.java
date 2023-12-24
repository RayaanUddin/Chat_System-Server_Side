/*Holds data being transmitted to clients from server. Can either contain a message or a command*/

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Packet implements Serializable {
    final private ClientDetails senderDetails;

    private String type; // message or command, message on default

    final private String message; // used to hold message or command string

    final private LocalDate date;

    final private LocalTime time;

    public Packet(String message, ClientDetails senderDetails) {
        this.senderDetails = senderDetails;
        this.message = message;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        type = "message";
    }

    public String getMessage() {
        return message;
    }

    public String getDateTime() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM")) + " " + time.getHour() + ":" + time.getMinute();
    }

    public ClientDetails getClientDetails() {
        return senderDetails;
    }

    public boolean isMessage() {
        return type.equalsIgnoreCase("message");
    }
}
