package serialisation.pojo;

public class AdditionalInfoController {
    private String key;
    private String routeName;
    private String value;

    public AdditionalInfoController() {}

    public AdditionalInfoController(String key, String routeName, String value) {
        this.key = key;
        this.routeName = routeName;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}