package checkingproductiondate.pojo;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AggregationMessage {
    private String aggregateType;
    private int aggregationLevel;
    private String aggregationLineId;
    private String batchDm;
    private String creationDate;
    private String customText;
    private List<String> datamatrix;
    private String expirationDate;
    private String organisationGuid;
    private String productionDate;
}