package routecontroller.pojo;

public class ResetCount {
    private String elementName;

    public ResetCount() {}

    public ResetCount(String elementName) {
        this.elementName = elementName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}