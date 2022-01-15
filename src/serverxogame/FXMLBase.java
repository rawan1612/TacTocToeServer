package serverxogame;

import java.awt.event.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public  class FXMLBase extends AnchorPane {

    protected final Button chart;
        private Stage stage;
    private Scene scene;
    private Parent root;
    

    public FXMLBase() {

        chart = new Button();

        setId("AnchorPane");
        setPrefHeight(400.0);
        setPrefWidth(600.0);

        chart.setLayoutX(200.0);
        chart.setLayoutY(100.0);
        chart.setMnemonicParsing(false);
        chart.setPrefHeight(42.0);
        chart.setPrefWidth(142.0);
        chart.setText("View online player");

        getChildren().add(chart);
        chart.setOnAction(new EventHandler <ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            root = new FXMLDocumentBase(stage); 
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
            }
        });
                }
}
