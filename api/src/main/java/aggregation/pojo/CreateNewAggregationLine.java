package aggregation.pojo;

import java.util.List;

public class CreateNewAggregationLine {
    private Integer aggregation_level;
    private String aggregation_type;
    private String batch_dm;
    private List<BufferRules> buffer_rules;
    private List<FiltrationRules> filtration_rules;
    private String name;

    public CreateNewAggregationLine() {}

    public CreateNewAggregationLine(Integer aggregation_level, String aggregation_type, String batch_dm, List<BufferRules> buffer_rules, List<FiltrationRules> filtration_rules, String name) {
        this.aggregation_level = aggregation_level;
        this.aggregation_type = aggregation_type;
        this.batch_dm = batch_dm;
        this.buffer_rules = buffer_rules;
        this.filtration_rules = filtration_rules;
        this.name = name;
    }

    public Integer getAggregation_level() {
        return aggregation_level;
    }

    public void setAggregation_level(Integer aggregation_level) {
        this.aggregation_level = aggregation_level;
    }

    public String getAggregation_type() {
        return aggregation_type;
    }

    public void setAggregation_type(String aggregation_type) {
        this.aggregation_type = aggregation_type;
    }

    public String getBatch_dm() {
        return batch_dm;
    }

    public void setBatch_dm(String batch_dm) {
        this.batch_dm = batch_dm;
    }

    public List<BufferRules> getBuffer_rules() {
        return buffer_rules;
    }

    public void setBuffer_rules(List<BufferRules> buffer_rules) {
        this.buffer_rules = buffer_rules;
    }

    public List<FiltrationRules> getFiltration_rules() {
        return filtration_rules;
    }

    public void setFiltration_rules(List<FiltrationRules> filtration_rules) {
        this.filtration_rules = filtration_rules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}