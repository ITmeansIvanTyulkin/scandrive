package pojo.addingprinter;

public class AddingPrinter {
    private Attributes attributes;
    private String batch_dm;
    private Integer counter;
    private String name;
    private Params params;
    private String production_date;
    private Boolean send_printed_codes_to_queue;
    private String state;
    private String type;
    private String url;

    public AddingPrinter() {}

    public AddingPrinter(Attributes attributes, String batch_dm, Integer counter, String name, Params params, String production_date, Boolean send_printed_codes_to_queue, String state, String type, String url) {
        this.attributes = attributes;
        this.batch_dm = batch_dm;
        this.counter = counter;
        this.name = name;
        this.params = params;
        this.production_date = production_date;
        this.send_printed_codes_to_queue = send_printed_codes_to_queue;
        this.state = state;
        this.type = type;
        this.url = url;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public String getBatch_dm() {
        return batch_dm;
    }

    public void setBatch_dm(String batch_dm) {
        this.batch_dm = batch_dm;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
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

    public String getProduction_date() {
        return production_date;
    }

    public void setProduction_date(String production_date) {
        this.production_date = production_date;
    }

    public Boolean getSend_printed_codes_to_queue() {
        return send_printed_codes_to_queue;
    }

    public void setSend_printed_codes_to_queue(Boolean send_printed_codes_to_queue) {
        this.send_printed_codes_to_queue = send_printed_codes_to_queue;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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