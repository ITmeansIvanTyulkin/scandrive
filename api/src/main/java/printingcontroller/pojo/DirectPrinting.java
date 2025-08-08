package printingcontroller.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectPrinting {
    private String encoding;
    private String printerIp;
    private String printerPort;
    private Boolean sendEnterAtTheEnd;
    private String template;
    private String testCode;
    private String testLine;
    private String zplGsSeparatorReplacer;
}
