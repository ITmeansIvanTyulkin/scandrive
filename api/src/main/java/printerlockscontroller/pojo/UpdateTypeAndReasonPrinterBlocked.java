package printerlockscontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTypeAndReasonPrinterBlocked {
    private String entity_lock;
    private String reason;
}