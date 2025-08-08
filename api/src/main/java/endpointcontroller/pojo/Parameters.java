package endpointcontroller.pojo;

public class Parameters {
    private String LOGIN;
    private String PASSWORD;
    private String QUEUE;

    public Parameters() {}

    public Parameters(String LOGIN, String PASSWORD, String QUEUE) {
        this.LOGIN = LOGIN;
        this.PASSWORD = PASSWORD;
        this.QUEUE = QUEUE;
    }

    public String getLOGIN() {
        return LOGIN;
    }

    public void setLOGIN(String LOGIN) {
        this.LOGIN = LOGIN;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getQUEUE() {
        return QUEUE;
    }

    public void setQUEUE(String QUEUE) {
        this.QUEUE = QUEUE;
    }
}