package monitorcontroller.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Properties {
    private String key;
    private String value;
}