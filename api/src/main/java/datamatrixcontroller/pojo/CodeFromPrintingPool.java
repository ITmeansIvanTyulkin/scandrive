package datamatrixcontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CodeFromPrintingPool {
    private Attributes attributes;
    private String batch_dm;
    private Integer counter;
    private String name;
    private Parameters params;
    private String production_date;
    private Boolean send_printed_codes_to_queue;
    private String state;
    private String type;
    private String url;
}