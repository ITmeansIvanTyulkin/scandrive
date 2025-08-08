package aggregation.pojo;

public class BufferRules {
    private String gtin;
    private Integer size;
    private Integer timer;

    public BufferRules() {}

    public BufferRules(String gtin, Integer size, Integer timer) {
        this.gtin = gtin;
        this.size = size;
        this.timer = timer;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTimer() {
        return timer;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }
}