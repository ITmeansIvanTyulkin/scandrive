package datamatrixcontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteCodesForSearchingIndex {
    private String codeType;
    private String searchingAttribute;
}