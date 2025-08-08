package templatescontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTemplate {
    private String name;
    private String printer_type;
    private String template;
}