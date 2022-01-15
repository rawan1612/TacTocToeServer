/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverxogame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import org.apache.derby.jdbc.ClientDriver;
import java.sql.Statement;
//import tic.tac.toe.User;
import java.util.ArrayList;
import java.util.List;


public class DAL {

    User checker = new User();
    static Connection con;

    public static void initDatabase() {

        try {

            DriverManager.registerDriver(new ClientDriver());
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/Tic Tac Toe DB", "root", "root");

        } catch (SQLException ex) {
            Logger.getLogger(DAL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void selectPalyer(User client) //Client.getUsername()
    {
        try {

            initDatabase();

            Statement stmt = con.createStatement();
            String queryString = new String("Select * from ROOT.PALYERSDATA where USERNAME = '" + client.getUsername() + "'");
            ResultSet rs = stmt.executeQuery(queryString);
            System.out.println(rs);
            stmt.close();
            con.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void InsertPlayer(User client) {
        try {

            initDatabase();
            Statement stmt = con.createStatement();
            String queryString = new String("INSERT INTO ROOT.PALYERSDATA (USERNAME, PASSWORD, SCORE) VALUES ('" + client.getUsername() + "','" + client.getPassword() + "', 0)");
            stmt.executeUpdate(queryString);

            stmt.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(DAL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void UpdatePlayer(User client) //mainly for updating the scores after each game
    {
        try {

            initDatabase();
            PreparedStatement ps = con.prepareStatement("Update into ROOT.PALYERSDATA  Values(?,?,?)");
            ps.setString(1, client.getUsername());
            ps.setString(2, client.getPassword());
            ps.setInt(3, client.getScore());
            ps.executeUpdate();

            ps.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(DAL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*public static ResultSet selectAll() throws SQLException {

        initDatabase();

        Statement stmt;
        stmt = con.createStatement();
        String queryString = new String("Select * from ROOT.PALYERSDATA");
        ResultSet r = stmt.executeQuery(queryString);
        while (r.next()) {
            System.out.println(r);
        }

        stmt.close();
        con.close();
        return r;
    }*/
    
public static List<User> retrieveAll ()
    {
       List<User> clients = new ArrayList<>();
       
            try {     
                
                initDatabase();
                
                Statement stmt = con.createStatement() ; 
                String queryString = new String("Select * from ROOT.PALYERSDATA");
                
                ResultSet rs = stmt.executeQuery(queryString);
                
                int counter = 0;
                
                while(rs.next()){
                    System.out.println("retrieveAll " + rs.getString(1));
                    User client = new User();
                    client.setUsername(rs.getString(1));
                    client.setScore(rs.getInt(3));
                    //clients.add(counter, client);
                    clients.add(client);
                    counter++;
                }
                
                stmt.close();
                con.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
     
        return clients;
    }

   
    public static User checkUserExits(String username){
//        boolean flage = false;
        User user = new User();
        
        try {
            
            initDatabase();
            
            Statement stmt = con.createStatement() ;
            String queryString = new String("Select * From ROOT.PALYERSDATA Where username = ?"); 
            
            PreparedStatement pst = con.prepareStatement(queryString);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                
                user.setUsername(rs.getString(1));
                user.setPassword(rs.getString(2));
                user.setScore(rs.getInt(3));
//                flage=true;
            } else {
                user = null;
//                flage=false;
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DAL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return user;
    }
    
    
    public static String  selectscores(User client) //get score
    {
        //User user ;
        String score = null;
        try {

            initDatabase();

            Statement stmt = con.createStatement();
            String queryString = new String("Select SCORE from ROOT.PALYERSDATA  where username = '"+client.getUsername()  +"'");
            ResultSet rs = stmt.executeQuery(queryString);
           if(rs.next()){
               score= rs.getString(1);
           }
        
            stmt.close();
            con.close();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return score;
    }

}
