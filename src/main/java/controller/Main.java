package controller;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author maycon-linux
 */
public class Main {
     public static void main(String[] args) {
       javax.swing.SwingUtilities.invokeLater(() -> {
           new view.JFWelcome().setVisible(true);
       });
       
    }
}
