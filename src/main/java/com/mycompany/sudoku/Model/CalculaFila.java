/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sudoku.Model;

/**
 *
 * @author jordi
 */
public class CalculaFila implements Runnable{
    private Integer filera[]; 
    private boolean mascara[];

    public CalculaFila(final Integer filera[], boolean mascara[]) {
        this.filera = filera.clone();
        this.mascara = mascara;
      
    }
    
    public Integer[] getFilera(){
        return filera;
    }
    
    public void genera(){
        Integer columna=0;

            while (columna<9){
                    Integer valor = (int) Math.floor(Math.random()*9+1);
                  
                    if (comprovaValor(valor)){
                        filera[columna]=valor;
                        columna++;
                    }
                    while(columna<9&&mascara[columna]){
                        columna++;
                    }
            }
    }
    
    @Override
    public void run() {
            genera();
    }

    private boolean comprovaValor(Integer valor) {
        boolean trobat = false;
        for (int c1=0; c1<9;c1++){
            if (filera[c1]==valor){
               trobat=true;
           } 
        }

        return !trobat;
    }
    
}
