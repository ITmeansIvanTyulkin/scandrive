package ordersimportincms.pojo;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderData {
    private String orderId;
    private String gtin;
    private String itemCode;
    private String itemName;
    private String batch;
    private String productionDate;
    private String expirationDate;
    private Boolean isMarked;
    private Integer quantity;
    private List<SerialNumbers> serialNumbers;
}