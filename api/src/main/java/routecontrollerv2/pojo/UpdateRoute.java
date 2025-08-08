package routecontrollerv2.pojo;

public class UpdateRoute {
    private AddInfo add_info;
    private String from_endpoint_name;
    private String name;
    private Params params;
    private String to_endpoint_name;
    private String type;

    public UpdateRoute() {}

    public UpdateRoute(AddInfo add_info, String from_endpoint_name, String name, Params params, String to_endpoint_name, String type) {
        this.add_info = add_info;
        this.from_endpoint_name = from_endpoint_name;
        this.name = name;
        this.params = params;
        this.to_endpoint_name = to_endpoint_name;
        this.type = type;
    }

    public AddInfo getAdd_info() {
        return add_info;
    }

    public void setAdd_info(AddInfo add_info) {
        this.add_info = add_info;
    }

    public String getFrom_endpoint_name() {
        return from_endpoint_name;
    }

    public void setFrom_endpoint_name(String from_endpoint_name) {
        this.from_endpoint_name = from_endpoint_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public String getTo_endpoint_name() {
        return to_endpoint_name;
    }

    public void setTo_endpoint_name(String to_endpoint_name) {
        this.to_endpoint_name = to_endpoint_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}