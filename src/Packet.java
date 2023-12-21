import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Packet implements Serializable {
    final private ClientDetails senderDetails;

    final private String message;

    final private LocalDate date;

    final private LocalTime time;

    public Packet(String message, ClientDetails senderDetails) {
        this.senderDetails = senderDetails;
        this.message = message;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public String getMessage() {
        return message;
    }

    public String getDateTime() {
        return date.toString() + " " + time.toString();
    }
}
