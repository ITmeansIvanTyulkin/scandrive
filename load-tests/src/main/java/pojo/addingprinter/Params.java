package pojo.addingprinter;

public class Params {
    private String TEMPLATE;
    private String BUFFER_SIZE;

    public Params() {}

    public Params(String TEMPLATE, String BUFFER_SIZE) {
        this.TEMPLATE = TEMPLATE;
        this.BUFFER_SIZE = BUFFER_SIZE;
    }

    public String getTEMPLATE() {
        return TEMPLATE;
    }

    public void setTEMPLATE(String TEMPLATE) {
        this.TEMPLATE = TEMPLATE;
    }

    public String getBUFFER_SIZE() {
        return BUFFER_SIZE;
    }

    public void setBUFFER_SIZE(String BUFFER_SIZE) {
        this.BUFFER_SIZE = BUFFER_SIZE;
    }
}