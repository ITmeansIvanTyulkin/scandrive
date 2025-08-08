package ordersimportincms.pojo;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SerialNumbers {
    private String validityDate;
    private List<String> sntins;
}