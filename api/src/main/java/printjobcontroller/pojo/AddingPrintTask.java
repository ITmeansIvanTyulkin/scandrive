package printjobcontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddingPrintTask {
    private String batch;
    private String datamatrix;
    private String name;
    private String nomenclature;
    private OrderResponse order;
    private Integer plan;
    private String printed_device;
    private String production_date;
    private String sku;
}