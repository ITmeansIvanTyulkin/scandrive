package routecontroller.pojo;

public class RestartRoute {
    private String elementName;
    public RestartRoute() {}

    public RestartRoute(String elementName) {
        this.elementName = elementName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}