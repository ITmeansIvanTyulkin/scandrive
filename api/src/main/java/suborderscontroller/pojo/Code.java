package suborderscontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Code {
    private String applied_code_date;
    private String bbd;
    private String code;
    private String code_type;
    private String creation_date;
    private String gtin;
    private Integer level;
    private String read_code_date;
}