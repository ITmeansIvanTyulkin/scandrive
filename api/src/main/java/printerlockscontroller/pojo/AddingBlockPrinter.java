package printerlockscontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddingBlockPrinter {
    private String entity_lock;
    private String printer_name;
    private String reason;
}