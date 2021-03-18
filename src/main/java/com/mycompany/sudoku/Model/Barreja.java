/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sudoku.Model;

import java.util.List;

/**
 *
 * @author ctic
 */
public class Barreja {
    List<Integer> valors;

    public Barreja(List<Integer> valors) {
        this.valors = valors;
    }
    public List<Integer> tornaValors(){
       int m= valors.size()-1;
       int alea;
       int temp;
       for (int i=m;i>1;i--){ 
          alea=(int) Math.floor(i*Math.random()); 
          temp=valors.get(i); 
          valors.set(i, valors.get(alea));
          valors.set(alea, temp);
        }
       return valors;        
    }
}
