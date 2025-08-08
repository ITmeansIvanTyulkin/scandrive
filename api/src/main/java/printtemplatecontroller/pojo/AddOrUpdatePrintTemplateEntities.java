package printtemplatecontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddOrUpdatePrintTemplateEntities {
    private String aggregation_line_external_id;
    private String batch;
    private String custom_text;
    private String expiration_date;
    private Boolean is_change_error_for_conveyor;
    private Boolean is_change_for_conveyor;
    private Boolean is_change_for_workstation;
    private String name;
    private String production_date;
}