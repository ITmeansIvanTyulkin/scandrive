package pojo.addingcodes;

import java.util.List;

public class AddingCodes {
    private String bestBeforeDate;
    private List<String> codes;
    private String searchingAttribute;

    public AddingCodes() {}

    public AddingCodes(String bestBeforeDate, List<String> codes, String searchingAttribute) {
        this.bestBeforeDate = bestBeforeDate;
        this.codes = codes;
        this.searchingAttribute = searchingAttribute;
    }

    public String getBestBeforeDate() {
        return bestBeforeDate;
    }

    public void setBestBeforeDate(String bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public String getSearchingAttribute() {
        return searchingAttribute;
    }

    public void setSearchingAttribute(String searchingAttribute) {
        this.searchingAttribute = searchingAttribute;
    }
}