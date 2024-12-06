module team.project.faceaccess {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;
    requires java.desktop;
    requires opencv ;
    requires javafx.swing;

    opens team.project.faceaccess to javafx.fxml;
    opens team.project.faceaccess.draft.cv to javafx.fxml;
    opens team.project.faceaccess.draft.faceRecognition to javafx.fxml;
    exports team.project.faceaccess.draft.cv to javafx.graphics;
    exports team.project.faceaccess.draft.faceRecognition to javafx.graphics;
    exports team.project.faceaccess;
    exports team.project.faceaccess.draft;
    opens team.project.faceaccess.draft to javafx.fxml;
}