package devicecontrollerv2.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateOrder {
    private String gtin;
    private String name;
    private String nomenclature;
    private String sku;
}