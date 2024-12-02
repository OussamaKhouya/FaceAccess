module team.project.faceaccess {
    requires javafx.controls;
    requires javafx.fxml;


    opens team.project.faceaccess to javafx.fxml;
    exports team.project.faceaccess;
}