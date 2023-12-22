import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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
        return date.toString() + " " + time.toString();
    }

    public ClientDetails getClientDetails() {
        return senderDetails;
    }

    public boolean isMessage() {
        return type.equalsIgnoreCase("message");
    }
}
