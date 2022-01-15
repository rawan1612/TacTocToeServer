package serverxogame;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ListView;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rawan
 */
public class data {

   static ListView<String> list;
   static  int CountOnline = 0;
   static  int Allusers = 0;
   static  List<User> ALLclients = new ArrayList<>();


    data() {

    }


        public static int onlineUsersNum() {
            CountOnline=0;

        for (UserHandler uh : UserHandler.clients) {
            CountOnline++;
        }
        return CountOnline;
        
    }
    
    
    public static int Allusers(){
                      ALLclients = DAL.retrieveAll();
             for (int i = 0; i < ALLclients.size(); i++) {
              Allusers++;
        }
             return Allusers;
    }
}
