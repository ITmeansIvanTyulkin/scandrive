package printingcontroller.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Template {
    private Boolean enabled_for_conveyor;
    private Boolean enabled_for_workstation;
    private String encoding;
    private Boolean error_enabled_for_conveyor;
    private String name;
    private String template;
}