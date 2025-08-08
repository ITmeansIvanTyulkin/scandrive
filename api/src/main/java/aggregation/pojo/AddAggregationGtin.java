package aggregation.pojo;

// Класс для создания JSON для класса 'AggregationGtinController'.

public class AddAggregationGtin {
    private String elementName;

    public AddAggregationGtin() {}

    public AddAggregationGtin(String elementName) {
        this.elementName = elementName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}