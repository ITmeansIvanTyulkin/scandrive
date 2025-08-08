package printingcontroller.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkStation {
    private Boolean autoprint_l2_aggregate;
    private Boolean is_dataset_aggregation_enabled;
    private String name;
    private PrintTemplate printTemplate;
    private Printer printer;
    private ScannerId scanner;
}