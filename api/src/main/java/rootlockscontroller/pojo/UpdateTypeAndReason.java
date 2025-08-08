package rootlockscontroller.pojo;

public class UpdateTypeAndReason {
    private String entity_lock;
    private String reason;

    public UpdateTypeAndReason() {}

    public UpdateTypeAndReason(String entity_lock, String reason) {
        this.entity_lock = entity_lock;
        this.reason = reason;
    }

    public String getEntity_lock() {
        return entity_lock;
    }

    public void setEntity_lock(String entity_lock) {
        this.entity_lock = entity_lock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}