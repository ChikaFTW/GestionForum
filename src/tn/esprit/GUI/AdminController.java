/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.GUI;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static java.time.zone.ZoneRulesProvider.refresh;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import tn.esprit.entities.Chefs;
import tn.esprit.services.Chef_Services;
import tn.esprit.utils.DataSource;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class AdminController implements Initializable {
   private  Connection conn;
    private PreparedStatement pst;
    private Statement ste;
        
    @FXML
    private TextField lNom_Chef;
    @FXML
    private TextField lCour_associé;
    @FXML
    private TextField lAdresse;
    
    @FXML
    private TableColumn<Chefs, Integer> cId;
    @FXML
    private TableColumn<Chefs, String> cNom;
    @FXML
    private TableColumn<Chefs, String> ccour;
    @FXML
    private TableColumn<Chefs, String> cadresse;
    @FXML
    private TableView<Chefs> Table_Chefs;
    @FXML
    private TextField mots;
    
    ObservableList ChefsList = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    
    public void initialize(URL url, ResourceBundle rb) {
         
        conn = DataSource.getInstance().getCnx();
 
       
       
       String sql="SELECT * FROM Chefs";
        try {
            ste=conn.prepareStatement(sql);
           ResultSet rs=ste.executeQuery(sql);
                while(rs.next()){
                Chefs ch = new Chefs();
                ch.setID_Chef(rs.getInt("ID_Chef"));
                ch.setNom_Chef(rs.getString("Nom_Chef"));
                ch.setCours_Associe(rs.getString("Cours_Associe"));
                ch.setAdresse_Chef(rs.getString("Adresse_Chef")) ;
                ChefsList.add(ch);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        
        cId.setCellValueFactory(new PropertyValueFactory<Chefs, Integer>("ID_Chef"));
        cNom.setCellValueFactory(new PropertyValueFactory<Chefs, String>("Nom_Chef"));
        ccour.setCellValueFactory(new PropertyValueFactory<Chefs, String>("Cours_Associe"));
        cadresse.setCellValueFactory(new PropertyValueFactory<Chefs, String>("Adresse_Chef"));
        
        
        Table_Chefs.setItems(ChefsList);        
        
        
       
        FilteredList<Chefs> filtredData= new FilteredList<> (ChefsList, b->true);
        
        mots.textProperty().addListener((observable,oldValue,newValue) -> {
        filtredData.setPredicate(Chefs -> {
        
        
        if (newValue.isEmpty() || newValue==null){
        return true;
        }
        
        String searchKeyword=newValue.toLowerCase();
        
        if(Chefs.getNom_Chef().toLowerCase().indexOf(searchKeyword )> -1){
        return true;
        }else 
            return false;
                
                });
    });
        
       SortedList<Chefs> sortedData=new SortedList<>(filtredData);
       sortedData.comparatorProperty().bind(Table_Chefs.comparatorProperty());
        Table_Chefs.setItems(sortedData);
        
        
    }    

    @FXML
    private void Ajouter_Chef(ActionEvent event) {
        
        Chef_Services ch = new Chef_Services();
        Chefs c = new Chefs (lNom_Chef.getText(),lCour_associé.getText(),lAdresse.getText());
        ch.ajouter_Chef(c);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("Chef is added successfully!");
        alert.show();
        lNom_Chef.setText("");
        lCour_associé.setText("");
        lAdresse.setText("");
    }

    
//     @FXML
//    private void Modifier_Chef(ActionEvent event) throws SQLException {
//        
//        
//        Chef_Services  cs= new Chef_Services();
//           
//
//        
//            Chefs ch = new Chefs();
//          int im=ch.getID_Chef();
//          String nm=lNom_Chef.getText() ; 
//          String am=lAdresse.getText();
//          String cm=lCour_associé.getText();
//                
//           ch.setID_Chef(im);
//           ch.setNom_Chef(nm);
//           ch.setCours_Associe(cm);
//           ch.setAdresse_Chef(am);
//           
//                        
//
//           cs.modifier_Chef(im,nm,cm,am);
//           Alert alert = new Alert(Alert.AlertType.INFORMATION);
//           alert.setTitle("Success");
//           alert.setContentText("Chef is updated successfully!");
//           alert.show();
//           lNom_Chef.setText("") ;
//           lAdresse.setText("");
//           lCour_associé.setText("");
//        
//        
//        
//        
//    }
//    
//    
//    
//
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    //         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
////            alert.setHeaderText("Warning");
////            alert.setContentText("voulez-vous supprimer ce Chef ?");
////            
////            Optional<ButtonType>result =  alert.showAndWait();
////        if(result.get() == ButtonType.OK){
////         String sql = "delete from Chefs where Nom_Chef = ?";
////            pst=conn.prepareStatement(sql);
////            pst.setString(1, cNom.getText());
////            pst.execute();
////            JOptionPane.showMessageDialog(null, "Chef supprimé" );
////        
////            refresh();
////        }
////        else {
////            
////          cId.setText(null);
////          cNom.setText(null);
////          ccour.setText(null);
////          cadresse.setText(null);
////        
////        
////        }
//    
    
    
    @FXML
    private void Supprimer_Chef(ActionEvent event) throws SQLException {
        

        Chef_Services cs = new Chef_Services();
        System.out.println(lNom_Chef.getText());
        Chefs ch = new Chefs();
        String nom=lNom_Chef.getText();
               
            ch.setNom_Chef(nom);
            cs.supprimer_Chef(nom);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("chef supprimé avec succes!");
            alert.show();
            lNom_Chef.setText("");
            
    }
    
    
private void getSelected(MouseEvent event) 

{           
    
Chefs clickedChefs = Table_Chefs.getSelectionModel().getSelectedItem();         
cNom.setText(String.valueOf(clickedChefs.getNom_Chef()));         
ccour.setText(String.valueOf(clickedChefs.getCours_Associe()));         
cadresse.setText(String.valueOf(clickedChefs.getAdresse_Chef()));   


 }
    

//    public void showChef() {
//         Chef_Services  cs= new Chef_Services();
//    	ObservableList<Chefs> list = (ObservableList<Chefs>) cs.afficher();
//    	
//    	cNom.setCellValueFactory(new PropertyValueFactory<Chefs,String>("Nom_Chef"));
//    	ccour.setCellValueFactory(new PropertyValueFactory<Chefs,String>("cour_associe"));
//        cadresse.setCellValueFactory(new PropertyValueFactory<Chefs,String>("Adresse_Chef"));
//    
//    	
//    	Table_Chefs.setItems(list);
//    } 

    @FXML
    private void Refresh_Table(ActionEvent event) {
        
        try {
           
            ChefsList.clear();
            
            String sql = "SELECT * FROM `Chefs`";
             ste=conn.prepareStatement(sql);
               ResultSet rs=ste.executeQuery(sql);
            
            while (rs.next()){
                ChefsList.add(new  Chefs(
                        rs.getInt("ID_Chef"),
                        rs.getString("Nom_Chef"),
                        rs.getString("Cours_Associe"),
                        rs.getString("Adresse_Chef")));                        
                Table_Chefs.setItems(ChefsList);
                
            }
            
            
       } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
    }














   
} 
    
    
