package printtemplatecontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddPrintTemplate {
    private Boolean enabled_for_conveyor;
    private Boolean enabled_for_workstation;
    private String encoding;
    private Boolean error_enabled_for_conveyor;
    private String name;
    private String template;
}