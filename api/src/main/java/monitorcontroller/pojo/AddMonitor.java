package monitorcontroller.pojo;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AddMonitor {
    private Integer error_count;
    private String inform_level;
    private String name;
    private List<Properties> props;
    private String protocol;
    private String status;
    private String url;
}