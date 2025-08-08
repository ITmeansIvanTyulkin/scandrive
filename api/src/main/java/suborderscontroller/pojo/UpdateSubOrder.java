package suborderscontroller.pojo;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateSubOrder {
    private String bbd;
    private Integer code_count;
    private String code_type;
    private List<Code> codes;
    private String creation_date;
    private String gtin;
    private Boolean is_aggregation_from_subOrder;
    private Boolean is_load_codes_from_orchestrator_enabled;
    private Integer level;
    private String order_number;
    private String sub_order_number;
}