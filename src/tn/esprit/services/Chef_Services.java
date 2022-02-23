/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.services;

import tn.esprit.entities.Chefs;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.utils.DataSource;

/**
 *
 * @author MSI
 */

public class Chef_Services {
    private Connection conn;
    private PreparedStatement pst;
    private Statement ste;

    public Chef_Services() {
        conn = DataSource.getInstance().getCnx();
    }

    public void ajouter_Chef(Chefs c) {
        
        String req = "INSERT INTO `Chefs` (`ID_Chef`,`Nom_Chef`,`Cours_Associe`,`Adresse_Chef`) VALUES (?,?,?,?)";
        
        try {
            pst = conn.prepareStatement(req);
            pst.setInt(1, c.getID_Chef());
            pst.setString(2, c.getNom_Chef());
            pst.setString(3, c.getCours_Associe());
            pst.setString(4, c.getAdresse_Chef());
            pst.executeUpdate();
            System.out.println("Chef ajoutée");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        
    }

    public List<Chefs> supprimer_Chef() {
        
        List<Chefs> Chefs = new ArrayList<>();
        String sql="DELETE FROM Chefs WHERE Id_Chef=1";
        try {
            pst=conn.prepareStatement(sql);
            pst.executeUpdate();
            System.out.println("Chef Supprimée");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Chefs;
        
    }

    public List<Chefs> update_Chef(Chefs c) {
          List<Chefs> Chefs = new ArrayList<>();
        String req;
        req = "UPDATE Chefs SET Nom_Chef = ?, Cours_Associe = ? , Adresse_Chef = ? WHERE ID_Chef = 1";
         try {
            
            pst = conn.prepareStatement(req);
            pst.setString(1, c.getNom_Chef());
            
            pst.setString(2, c.getCours_Associe());
            pst.setString(3, c.getAdresse_Chef());
            pst.executeUpdate();
            System.out.println("Chef Modifiée");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return Chefs;
    }

    public List<Chefs> afficher() {
        List<Chefs> Chefs = new ArrayList<>();
        
        String req = "SELECT * from `Chefs`";
        
        try {
            pst = conn.prepareStatement(req);
            ResultSet rs= pst.executeQuery();
            
            while(rs.next()){
                Chefs c = new Chefs();
                c.setID_Chef(rs.getInt("ID_Chef") );
                c.setNom_Chef(rs.getString(2));
                c.setCours_Associe(rs.getString(3));
                c.setAdresse_Chef(rs.getString(4));
                Chefs.add(c);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        
        return Chefs;
    }
    
    
}
    
    