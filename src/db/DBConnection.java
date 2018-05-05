/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import gui.Dashboard;
import gui.LoginF;

import javax.swing.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hasan
 */
public class DBConnection {
    Connection conn;
    Statement stmt;
    LoginF login;
    String user;
    String pass;
    DBConnection dc;
    public DBConnection(){
        dc = this;
        loginPage();
        //setConn();
    }

    void getInfo(){
        
        user = login.usrTF.getText();
        pass = new String(login.passTF.getPassword());
    }

    public void setConn(){
        try {
            getInfo();
            Class.forName("com.mysql.jdbc.Driver");

            conn =  (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/j_brothers", user, pass);
            
            showMainPage();
            login.dispose();
            
        } catch (ClassNotFoundException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            if(e.getMessage().contains("Communications link failure")){
                JOptionPane.showMessageDialog(null, "Server is down!","Not found", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog (null, "Wrong username or password\nTry Again?",
                                                                "Error", dialogButton, JOptionPane.ERROR_MESSAGE);
            if(dialogResult == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
           // wrongUser(e);
        }
    }

    void wrongUser(SQLException e){
        if(e.getMessage().contains("Access denied for user ")){
            setConn();
        }
    }

    
    
    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loginPage() {
        try {
            
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            //java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                login = new LoginF(dc);
                login.setLocationRelativeTo(null);
                login.setResizable(false);
                login.setVisible(true);
                
            }
        });
    }

    private void showMainPage() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            
        } 
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               // Form f = new Form();
               Dashboard dash = new Dashboard(dc);
               dash.setVisible(true);
                
            }
        });
    }
    
    public ResultSet getRS(String sql){
        ResultSet rs = null;
        try {
            stmt = (Statement) conn.createStatement();
            rs = stmt.executeQuery(sql);
            
        }catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
}
