package serverxogame;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TicTacToeServer extends Application {

    ServerSocket serverSocket;
    Socket s;
             int CountOnline = 0;
int Allusers=0;
List<User> ALLclients = new ArrayList<>();

   
        // piechart data
       
    User user = new User();
    
        @Override
    public void start(Stage primaryStage) throws Exception {
    
        Parent root = new FXMLBase();
        
        Scene scene = new Scene(root,500,300);
        
        primaryStage.setScene(scene);
        primaryStage.show();

          ALLclients = DAL.retrieveAll();
             for (int i = 0; i < ALLclients.size(); i++) {
              Allusers++;
        }
   
   
        // create a pie chart
 

        
                serverSocket = new ServerSocket(5005);

        new Thread(){
                @Override
                        public void run() {
        try {
            while(true){
            
                 s = serverSocket.accept();
                
                if(s.isConnected()){
                    new UserHandler(s);
                }
            }
           
            
        } catch (IOException ex) {
            Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
      

            
                        }
        }.start();
    }
                   
    public static void main(String[] args) {
                launch(args);
        
    }

   
}
