module team.project.faceaccess {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens team.project.faceaccess to javafx.fxml;
    exports team.project.faceaccess;
}