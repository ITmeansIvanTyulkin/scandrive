package gtinexpiration.pojo;

public class AddOrUpdate {
    private Integer days;
    private String gtin;

    public AddOrUpdate() {}

    public AddOrUpdate(Integer days, String gtin) {
        this.days = days;
        this.gtin = gtin;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }
}