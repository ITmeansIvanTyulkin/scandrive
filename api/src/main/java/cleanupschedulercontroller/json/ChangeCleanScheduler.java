package cleanupschedulercontroller.json;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangeCleanScheduler {
    private String cron;
    private Integer days_to_save;
    private String next_time_run;
    private String type;
}