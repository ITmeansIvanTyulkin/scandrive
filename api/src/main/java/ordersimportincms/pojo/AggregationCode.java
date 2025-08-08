package ordersimportincms.pojo;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AggregationCode {
    private String packType;
    private List<String> codes;
}