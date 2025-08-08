package hookcontroller.pojo;

public class DeleteHook {
    private String actionType;
    private String deviceName;
    private String hookType;
    private String hookUrl;
    private Integer id;

    public DeleteHook() {}

    public DeleteHook(String actionType, String deviceName, String hookType, String hookUrl, Integer id) {
        this.actionType = actionType;
        this.deviceName = deviceName;
        this.hookType = hookType;
        this.hookUrl = hookUrl;
        this.id = id;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getHookType() {
        return hookType;
    }

    public void setHookType(String hookType) {
        this.hookType = hookType;
    }

    public String getHookUrl() {
        return hookUrl;
    }

    public void setHookUrl(String hookUrl) {
        this.hookUrl = hookUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}