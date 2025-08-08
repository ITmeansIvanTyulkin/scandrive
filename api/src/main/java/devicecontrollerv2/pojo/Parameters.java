package devicecontrollerv2.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Parameters {
    private String TEMPLATE;
    private String BUFFER_SIZE;
}