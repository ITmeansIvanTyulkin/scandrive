package orderscontroller.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrder {
    private Boolean is_code_validation_enabled;
    private String nomenclature;
    private String order_number;
    private Boolean reassembly_aggregate_enabled;
    private String sku;
    private String creation_date;
    private String gtin;
    private Boolean is_aggregation_from_subOrder;
    private Boolean is_load_codes_from_orchestrator_enabled;
    private Integer level;
    private String sub_order_number;
    private String user;
}