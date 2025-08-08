package datamatrixcontroller.pojo;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UploadCodes {
    private String bestBeforeDate;
    private List<String> codes;  // Для массива строк
    private String searchingAttribute;
}