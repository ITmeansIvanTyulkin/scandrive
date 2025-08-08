package datamatrixcontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AvailableCodesForPrinting {
    private String gtin;
    private String name;
    private String nomenclature;
    private String sku;
}