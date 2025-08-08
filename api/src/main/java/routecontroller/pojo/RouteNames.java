package routecontroller.pojo;

import java.util.List;

public class RouteNames {
    private List<String> route_names;

    public RouteNames() {}

    public RouteNames(List<String> route_names) {
        this.route_names = route_names;
    }

    public List<String> getRoute_names() {
        return route_names;
    }

    public void setRoute_names(List<String> route_names) {
        this.route_names = route_names;
    }
}