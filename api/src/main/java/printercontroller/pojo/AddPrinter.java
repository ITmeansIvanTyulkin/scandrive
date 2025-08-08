package printercontroller.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPrinter {
    private String encoding;
    private String ip;
    private String name;
    private String port;
    private Boolean send_enter_at_end;
    private String template;
    private String zpl_gs1_128_prefix;
    private String zpl_gs1_databar_expanded_prefix;
    private String zpl_gs_separator_replacer;
    private String zpl_sscc_prefix;
}