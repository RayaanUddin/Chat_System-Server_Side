import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Packet implements Serializable {
    final private ClientInfo sender;

    final private String message;

    final private LocalDate date;

    final private LocalTime time;

    public Packet(String messageInp, ClientInfo senderInp) {
        sender = senderInp;
        message = messageInp;
        date = LocalDate.now();
        time = LocalTime.now();
    }

    public String getMessage() {
        return message;
    }

    public String getDateTime() {
        return date.toString() + " " + time.toString();
    }
}
