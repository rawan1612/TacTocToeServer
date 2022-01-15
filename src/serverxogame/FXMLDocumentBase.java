package serverxogame;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static serverxogame.data.list;

public class FXMLDocumentBase extends AnchorPane {

    ObservableList<PieChart.Data> pieChartData;
    int CountOnline = 0;
    int Allusers = 0;
//List<User> ALLclients = new ArrayList<>();
    ListView<String> list;
    UserHandler uh;

    public FXMLDocumentBase(Stage stage) {
        final PieChart chart = new PieChart(pieChartData);
        list = new ListView<String>();

        setId("AnchorPane");
        setPrefHeight(700.0);
        setPrefWidth(600.0);
        list.setLayoutX(100.0);
        list.setLayoutY(450.0);
        list.setPrefHeight(200.0);
        list.setPrefWidth(300.0);

        chart.setTitle("Game statistics");

        new Thread() {

            public void run() {
     
               

                while(true){
               
     
                
  
                Platform.runLater(() -> {
                         list.getItems().clear();
               CountOnline=0;
               
           for (UserHandler uh : UserHandler.clients) {
            list.getItems().add(uh.user.getUsername());
            System.out.println(uh.user.getUsername());
          //  CountOnline++;
        }
                          CountOnline = data.onlineUsersNum();
                Allusers = data.Allusers();
                    pieChartData
                            = FXCollections.observableArrayList(
                                    new PieChart.Data("Online Users", CountOnline),
                                    new PieChart.Data("offline Users", Allusers - CountOnline)
                            );

                    chart.setData(pieChartData);
                  
                
                });
                
                try {
                    Thread.sleep(5000);

                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                
            }
         

        }.start();
  getChildren().add(chart);
                    getChildren().add(list);   
    }
}
