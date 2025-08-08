package syspropscontrollerv2.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateSysProps {
    private String inn;
    private String key;
    private Integer level;
    private Integer sequence;
    private String value;
}