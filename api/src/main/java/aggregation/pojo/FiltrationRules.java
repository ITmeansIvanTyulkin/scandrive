package aggregation.pojo;

public class FiltrationRules {
    private Integer aggregation_size;
    private String gtin;
    private String nomenclature;
    private Boolean without_aggregation;

    public FiltrationRules() {}

    public FiltrationRules(Integer aggregation_size, String gtin, String nomenclature, Boolean without_aggregation) {
        this.aggregation_size = aggregation_size;
        this.gtin = gtin;
        this.nomenclature = nomenclature;
        this.without_aggregation = without_aggregation;
    }

    public Integer getAggregation_size() {
        return aggregation_size;
    }

    public void setAggregation_size(Integer aggregation_size) {
        this.aggregation_size = aggregation_size;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(String nomenclature) {
        this.nomenclature = nomenclature;
    }

    public Boolean getWithout_aggregation() {
        return without_aggregation;
    }

    public void setWithout_aggregation(Boolean without_aggregation) {
        this.without_aggregation = without_aggregation;
    }
}