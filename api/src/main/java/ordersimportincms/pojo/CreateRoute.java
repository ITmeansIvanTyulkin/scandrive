package ordersimportincms.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateRoute {
    private AddInfo add_info;
    private String from_endpoint_name;
    private String name;
    private RouteParameters params;
    private String to_endpoint_name;
    private String type;
}