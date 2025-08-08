package endpointcontroller.pojo;

import java.util.List;

public class AddingNewPoint {
    private String name;
    private Parameters params;
    private String type;
    private String url;

    public AddingNewPoint() {}

    public AddingNewPoint(String name, Parameters params, String type, String url) {
        this.name = name;
        this.params = params;
        this.type = type;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parameters getParams() {
        return params;
    }

    public void setParams(Parameters params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}