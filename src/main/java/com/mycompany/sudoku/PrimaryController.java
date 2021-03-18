package com.mycompany.sudoku;

import com.mycompany.sudoku.Model.SudokuHoritzontal;
import static com.mycompany.sudoku.Model.SudokuHoritzontal.ocupats;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private GridPane grid;

    private final Spinner[][] espin = new Spinner[9][9];
    @FXML
    private Button trobarSolucioBtn;
    
    public void initialize() {
        //String quadrant[] = {"#EFE", "#FEE", "#EEF", "#FEA", "#AEF", "#EFA", "#AFE", "#9EE", "#E5F"};
        grid.setMinWidth(500);
        grid.setPrefHeight(500);

        for (int f=0;f<9;f++){
            for (int c=0;c<9;c++){
                espin[f][c]=new Spinner(0,9,0);
                espin[f][c].setPrefSize(75, 25);
                grid.setHalignment(espin[f][c], HPos.CENTER);
                                
                grid.setStyle("-fx-background-color: #EEF;");
                //grid.getChildren().get(f*9+c).setStyle("-fx-background-color: "+quadrant[(f+1)/3+(c+1)/3]);
                grid.add(espin[f][c], c, f);
            }
        }

    }

    @FXML
    private void trobarSolucioAct(ActionEvent event) throws InterruptedException {
        SudokuHoritzontal sh = new SudokuHoritzontal();
        for (int f=0;f<9;f++){
            for (int c=0;c<9;c++){ 
                int valor = (int) espin[f][c].getValue();
                if (valor>0){
                    espin[f][c].setStyle("-fx-font-weight:bold;-fx-text-inner-color: blue;");
                    sh.assignaOcupat(f, c, valor);
                }else {
                    ocupats[f][c] = false;
                    espin[f][c].setStyle("-fx-text-inner-color: green;");
                }
            }   
        }
        if (sh.comprovaValorsInicials()) {
            long startTime = System.currentTimeMillis();
            sh.genera();
            long endTime = System.currentTimeMillis() - startTime;
            Integer sudoku[][] = sh.getSudoku();

            for (int f=0;f<9;f++){
                for (int c=0;c<9;c++){        
                    espin[f][c].getValueFactory().setValue(sudoku[f][c]);

                }
            }
            Alert a = new Alert(AlertType.INFORMATION);
            a.setTitle("Sudoku trobat!");
            a.setHeaderText("Hi ha com a mínim una solució al teu sudoku");
            a.setContentText("A trigat: "+(endTime)+" mil·lisegons a trobar-lo");
            a.show();
        }else {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Missatge d'Error");
            a.setHeaderText("Dades inicials errònies");
            a.setContentText("Hi ha valors repetits en les columnes o fileres");
            a.show();
        }
    }

    @FXML
    private void SortirAct(ActionEvent event) {
            Platform.exit();
            System.exit(0);
    }

}
