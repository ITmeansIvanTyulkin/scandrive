package rootlockscontroller.pojo;

public class BlockingRoute {
    private String entity_lock;
    private String reason;
    private String root_name;

    public BlockingRoute() {}

    public BlockingRoute(String entity_lock, String reason, String root_name) {
        this.entity_lock = entity_lock;
        this.reason = reason;
        this.root_name = root_name;
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

    public String getRoot_name() {
        return root_name;
    }

    public void setRoot_name(String root_name) {
        this.root_name = root_name;
    }
}