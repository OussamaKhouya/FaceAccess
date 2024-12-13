package team.project.faceaccess.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.File;
import java.util.List;
@Data
@NoArgsConstructor @AllArgsConstructor
public class Admin extends User {
    private String role = "Admin";

    public Admin(int id, String name, List<File> photos, boolean accessStatus) {
        super(id, name, photos, accessStatus);
    }
}
