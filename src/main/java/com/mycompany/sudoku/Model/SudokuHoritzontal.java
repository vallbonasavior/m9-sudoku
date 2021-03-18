/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sudoku.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author jordi
 */
public class SudokuHoritzontal {
    private Integer sudoku[][] = new Integer[9][9];
    private Integer sudokuAux[][] = new Integer[9][9];
    private Integer valorsInicials[][] = new Integer[9][9];
    public static boolean ocupats[][] = new boolean[9][9];
    public static Semaphore semafor = new Semaphore(1);
    private List<CalculaFila> cf = new ArrayList<CalculaFila>();
    private List<Thread> fils = new ArrayList<Thread>();
    private Integer pesosFileres[] = new Integer[9];
    private Barreja barreja;

    private void inicialitzaSudoku(){
        for (int i=0;i<9;i++){
            for (int c=0;c<9;c++){
                sudoku[i][c]= 0;
                valorsInicials[i][c]=0;
                ocupats[i][c]=false;
            }
        }
    }
    public SudokuHoritzontal() {
        inicialitzaSudoku();
    }
    public Integer[][] getSudoku(){
        // Torna el sudoku finalitzat
        return sudoku;
    }
    public void assignaOcupat(int fila, int columna, int valor){
        // Assignació incial de valors introduits per l'usuari
        ocupats[fila][columna] = true;
        sudoku[fila][columna]=valor;
        valorsInicials[fila][columna]=valor;
    }
    private int pesFilera(int fila){
        int numRepetits =0;
        for (int fil=0; fil<9;fil++){
            for (int col=0; col<9; col++){
                if (fila!=fil && sudokuAux[fil][col]==sudokuAux[fila][col]){
                    numRepetits++;
                }
            }
        }
        return numRepetits;
    }
    private void calculaPesosFileres(){
        for (int fila=0; fila<9;fila++){
            pesosFileres[fila]=pesFilera(fila);
        }
    }
    private int pesTotal(){
        int pes=0;
        for (int fila=0; fila<9; fila++){
            pes+=pesosFileres[fila];
        }
        return pes;
    }
    private int filaPesMaxim(){
        int pes=0;
        int filapesmaxim=-1;
        for (int fila=0;fila<9; fila++){
            if (pesosFileres[fila]>pes){
                filapesmaxim=fila;
                pes = pesosFileres[fila];
            }
        }
        return filapesmaxim;
    }
    private boolean comprovaFinal(){
        for (int fila=0; fila<9; fila++){
            for (int fil=0; fil<9;fil++){
                for (int col=0; col<9; col++){
                    if (fila!=fil && sudokuAux[fil][col]==sudokuAux[fila][col]){
//                        System.out.println(fil+"x"+col+", "+fila+"x"+col+"= "+sudokuAux[fil][col]);
                        return false;
                    }
                }
            }        
        }
        for (int columna=0; columna<9; columna++){
            for (int col=0; col<9;col++){
                for (int fil=0; fil<9; fil++){
                    if (columna!=col && sudokuAux[fil][col]==sudokuAux[fil][columna]){
//                        System.out.println(fil+"x"+col+", "+fil+"x"+columna+"= "+sudokuAux[fil][col]);
                        return false;
                    }
                }
            }        
        }        
        return true;
    }
    private void generaFirst() throws InterruptedException {
        // Generació inicial de les nous fileres del sudoku.
        int fila;
        for (fila = 0; fila<9; fila++){
            cf.add(new CalculaFila(valorsInicials[fila], ocupats[fila]));
            fils.add(new Thread(cf.get(fila)));
            fils.get(fila).start();
        } 
        for (fila = 0; fila<9; fila++){
            fils.get(fila).join();
            sudoku[fila]=cf.get(fila).getFilera();
        }
        fils.clear();
        cf.clear();
    }
    private void optimitzacio() throws InterruptedException{
        int fila, pesActual, pesTotalActual, penjat=0, penjat2=0;
        calculaPesosFileres();
        while(pesTotal()>8){
            if (penjat<30){
                fila = filaPesMaxim();
            }else {
                penjat=0;
                penjat2++;
                do {
                    fila = (int) Math.floor(Math.random()*9);
                }while(pesosFileres[fila]==0);
                if (penjat2==1000){
                    //Gran penjada: reinicia
                    generaFirst();
                    sudokuAux = sudoku.clone();
                    penjat=0;
                    penjat2=0;
                    calculaPesosFileres();
                }
            }
            pesActual = pesosFileres[fila];
            pesTotalActual = pesTotal();
            cf.add(new CalculaFila(valorsInicials[fila], ocupats[fila]));
            fils.add(new Thread(cf.get(0)));
            fils.get(0).start();
            fils.get(0).join();
            sudokuAux[fila]=cf.get(0).getFilera();
            calculaPesosFileres();
            if (((pesTotalActual>pesTotal()) && pesActual>pesosFileres[fila])){
                // Confirmem el canvi
                sudoku[fila]=sudokuAux[fila].clone();
                penjat=0;
                System.out.println("Nou pes total: "+pesTotal());
            }else {
                // Desfer el canvi
                sudokuAux[fila]=sudoku[fila].clone();
                calculaPesosFileres();
                penjat++;
            }
            fils.clear();
            cf.clear();
        }        
    }
    private boolean finalitza(){
        // Després de la optimització, barrejo valors entre les posicions
        // conflictives fins trobar una solució
        List<Integer> llistaValorsConflictius = new ArrayList();
        List<Integer> llistaFilesConflictives = new ArrayList();
        List<Integer> llistaColumnesConflictives = new ArrayList();

        //Recull valors conflictius 
        for (int fila=0; fila<9; fila++){
            for (int fil=0; fil<9;fil++){
                for (int col=0; col<9; col++){
                    if (fila!=fil && sudokuAux[fil][col]==sudokuAux[fila][col]){
                        llistaFilesConflictives.add(fila);
                        llistaColumnesConflictives.add(col);
                        llistaValorsConflictius.add(sudokuAux[fila][col]);
                    }
                }
            }        
        }
        
        // Barrejo els valors entre les posicions conflictives
        int posi, fil, col, valor, comptador=0;
        long maximrepeticions = (long) Math.pow(llistaValorsConflictius.size(),4);
        do {
            barreja = new Barreja(llistaValorsConflictius);
            llistaValorsConflictius = barreja.tornaValors();
            // Recolocar els valors barrejats en sudoku aux i calcular pesos
            for (posi=0;posi<llistaValorsConflictius.size(); posi++){
                fil = llistaFilesConflictives.get(posi);
                col = llistaColumnesConflictives.get(posi);
                valor = llistaValorsConflictius.get(posi);
                sudokuAux[fil][col]=valor;
                System.out.println(fil+"x"+col+"= "+valor+". comptador: "+comptador);
            }
            calculaPesosFileres();
            comptador++;
        }while ((!comprovaFinal())&&(comptador<maximrepeticions));
        if (comptador>=maximrepeticions)
            return false;
        else
            return true;
    }
    public void genera() throws InterruptedException{
        do {
            generaFirst();   // Càlcul inicial
            sudokuAux = sudoku.clone();   //A partir d'aquí les operacions es fan amb sudokuAux
            optimitzacio();
        }while (!finalitza());
        sudoku = sudokuAux.clone();
        mostra();
    }
    public boolean comprovaValorsInicials(){
        int valor, fil, col, fil2, col2;
        for (fil=0;fil<9;fil++){
            for (col=0;col<9;col++){
                valor = valorsInicials[fil][col];
                if (valor>0){
                    for (fil2=0;fil2<9;fil2++){
                        if (fil!=fil2 && valorsInicials[fil2][col]==valor){
                            return false;
                        }
                    }
                    for (col2=0;col2<9;col2++){
                        if (col!=col2 && valorsInicials[fil][col2]==valor){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void mostra(){
        // Mostra el Sudoku pel terminal
        for (int i=0;i<9;i++){
            for (int c=0;c<9;c++){
                System.out.print(sudoku[i][c]+" ");
                
            }
            System.out.print(". Pes: "+pesosFileres[i]+"\n");
        }
        System.out.println("Pes total: "+pesTotal());
    }
    
    public void doStop(){
        
    }
}
