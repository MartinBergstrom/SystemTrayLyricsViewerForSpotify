package customEvent;

public class ServerEvent {
    private ServerEventType type;
    private String message;

    public ServerEvent(ServerEventType type) {
        this.type = type;
    }

    public ServerEvent(ServerEventType type, String message) {
        this.type = type;
        this.message = message;
    }

    public ServerEventType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
