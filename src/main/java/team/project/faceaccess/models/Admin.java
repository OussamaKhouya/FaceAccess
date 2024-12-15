package team.project.faceaccess.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.File;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private int id;
    private String username;
    private String email;
    private String password;
}
