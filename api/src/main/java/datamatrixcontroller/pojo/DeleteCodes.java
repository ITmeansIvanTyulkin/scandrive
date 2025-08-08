package datamatrixcontroller.pojo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteCodes {
    private String from_date;
    private String to_date;
}