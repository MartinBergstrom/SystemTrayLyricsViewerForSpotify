package customEvent;

public class ServerEvent<T> {
    private ServerEventType type;
    private T payload;

    public ServerEvent(ServerEventType type) {
        this.type = type;
    }

    public ServerEvent(ServerEventType type, T payload) {
        this.type = type;
        this.payload = payload;
    }

    public ServerEventType getType() {
        return type;
    }

    public T getPayload() {
        return payload;
    }
}
