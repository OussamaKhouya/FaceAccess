package team.project.faceaccess.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor @AllArgsConstructor
public class AccessLog {
    private int id;
    private User user;
    private LocalDateTime timestamp;
    private boolean accessGranted;
}
