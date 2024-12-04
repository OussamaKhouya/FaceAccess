module team.project.faceaccess {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;


    opens team.project.faceaccess to javafx.fxml;
    exports team.project.faceaccess;
}