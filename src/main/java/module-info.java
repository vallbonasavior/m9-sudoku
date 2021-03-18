module com.mycompany.sudoku {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.sudoku to javafx.fxml;
    exports com.mycompany.sudoku;
}
