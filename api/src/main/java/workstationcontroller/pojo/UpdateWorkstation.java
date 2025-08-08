package workstationcontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateWorkstation {
    private Boolean autoprint_l2_aggregate;
    private Boolean is_dataset_aggregation_enabled;
    private String name;
    private PrintTemplate print_template;
    private Printer printer;
    private Scanner scanner;
}