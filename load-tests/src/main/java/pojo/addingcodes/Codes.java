package pojo.addingcodes;

import java.util.List;

public class Codes {
    private List<String> codes;  // Поле для хранения списка кодов

    public Codes() {}

    public Codes(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}