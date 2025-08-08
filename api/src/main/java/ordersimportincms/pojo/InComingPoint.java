package ordersimportincms.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InComingPoint {
    private String name;
    private Parameters parameters;
    private String type;
    private String url;
}