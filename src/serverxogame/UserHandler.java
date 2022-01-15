/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverxogame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    User user = new User();
    
//    UserHandler player;
    
    static Vector<UserHandler> clients = new Vector<UserHandler>();

    UserHandler(Socket cs){
            try {
                dis = new DataInputStream(cs.getInputStream());
                ps = new PrintStream(cs.getOutputStream());   
//                player.user = user;
                System.out.print("waslt"+"\n");
                start();
               
        }catch (IOException ex) {
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    

    public void run() {
        while (true) {
            
            try{
                String operation, sender, reciever, turn, square;
                JSONObject objj;
                String search;
                JSONObject obj = new JSONObject(dis.readLine());

                System.out.println(obj.get("operation"));
                search= obj.getString("operation"); // value 
                System.out.println(search);


                switch (search){
                    case "login":
                        objj = new JSONObject();
                        String username = obj.getString("username");
                        String password = obj.getString("password");

                        user.setUsername(username);
                        user.setPassword(password);


                        //check if user exist 
                        if (check(user) != null) {
                            JSONObject confirm = new JSONObject();
                            confirm.put("operation", "User Exist");
                            confirm.put("username", user.getUsername());
                            confirm.put("score", user.getScore());
                            ps.println(confirm.toString());  // change
                            //System.out.println("here");
//                            UserHandler.clients.add(this);
//                            player.user.setUsername(user.getUsername());
//                            player.user = user;
                            UserHandler.clients.add(this);
                        } 

                        else 
                        {
                            JSONObject confirm = new JSONObject();
                            confirm.put("operation", "User Not Exist");
                            ps.println(confirm.toString());  // change
                            System.out.println("here");
                        }

                        break;
                        

                    case "register":

                        System.out.print("now you see me ");

                        JSONObject reg = new JSONObject();
                        //System.out.println(reg);


                        String usernamereg = obj.getString("usernamereg");
                        //System.out.println(usernamereg);
                        String passwordreg = obj.getString("passwordreg");


                        user.setUsername(usernamereg);
                        user.setPassword(passwordreg);
                        user.setScore(0);

                        DAL.InsertPlayer(user);

                        JSONObject confirm = new JSONObject();

                        confirm.put("operation", "regdone");
                        confirm.put("username", user.getUsername());
                        confirm.put("score", user.getScore());

                        ps.println(confirm.toString());
//                        player.user = user;
                        UserHandler.clients.add(this);

                        System.out.print("insertion done");

                        break;
                    case "loadonline":
                        System.out.println("enter the case of loadonline");

                        System.out.println("SEND"+ALLOnline());
                       
                        ps.println(ALLOnline());
                        
                        break;
                        
                    case "invitation":
                        System.out.println("inviation is here");
                        
                        reciever = obj.getString("reciever");
                        sender = obj.getString("sender");
                        
//                        System.out.println(player);
                        for (UserHandler s : clients) {
                             if (s.user.getUsername().equals(reciever))
                             {
                                 JSONObject send = new JSONObject();
                                 send.put("operation","you have invitaion");
                                 send.put("reciever", s.user.getUsername().toString());
                                 send.put("sender", sender);
                                 s.ps.println(send);
                                 
                                 System.out.println(s.user.getUsername());
                                 System.out.println(send);
                             }
                        }
                        break;
                        
                    case "reply_invitation":
                        System.out.println("reply_invitation is here");
                        System.out.println(obj);
                        
                        reciever = obj.getString("reciever");
                        sender = obj.getString("sender");
                        turn = obj.getString("turn");
                        
//                        System.out.println(player);
                        for (UserHandler s : clients) {
                             if (s.user.getUsername().equals(sender))
                             {
                                 JSONObject send = new JSONObject();
                                 send.put("operation","you have invitaion reply");
                                 send.put("reciever", reciever);
                                 send.put("sender", sender);
                                 send.put("turn", turn);
                                 s.ps.println(send);
                                 
                                 System.out.println(s.user.getUsername());
                                 System.out.println(send);
                             }
                        }
                        break;
                        
                    case "send_move":
                        System.out.println("move sent in server");
                        
                        reciever = obj.getString("reciever");
                        sender = obj.getString("sender");
                        turn = obj.getString("turn");
                        square = obj.getString("square");
//                        boolean isOver = obj.getBoolean("isOver");
                        
//                        System.out.println(player);
                        for (UserHandler s : clients) {
                             if (s.user.getUsername().equals(reciever))
                             {
                                 JSONObject send = new JSONObject();
                                 send.put("operation","move_sent");
                                 send.put("reciever", reciever);
                                 send.put("sender", sender); 
                                 send.put("turn", turn);
                                 send.put("square", square);
//                                 send.put("isOver", isOver);
                                 s.ps.println(send);
                                 
                                 System.out.println(s.user.getUsername());
                                 System.out.println(send);
                             }
                        }
                        break;
                        
                    case "score":
                        //prepare json 
                        
                        JSONObject score = new JSONObject();
                        
                        user.setUsername(obj.getString("playername")); //username 
                        String SCORE;
                        SCORE = DAL.selectscores(user);
                        
                        System.out.println(SCORE);
                        score.put("operation", "getscore");
                        score.put("score",SCORE);
                        ps.println(score);
                        break;
               }

            } 
            catch (SocketException e) {
               
                try {
                    dis.close();
                    clients.remove(this);

                } catch (IOException ex) {
                    Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
                
            }
            catch (JSONException ex) {
                Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    };
        
            
            
            
            
            
            
            
            
            
            
            //send okay to go to list view
           

            //take the request and send to other player 
            //function connect the two players 
            //connect
            // sendMessageToSender("hello there ", "sda");
            //UserHandler.clients.add(this);
        


    //}
//    public User check(User user) {
////        boolean flage = false;
//        User user1 = new User();
//        if (DAL.checkUserExits(user.getUsername()) != null) {
//            user1 = user;
//        } else {
//            user1 = null;
//        }
//        return user1;
//    };
    
    public String check(User user) {
//        boolean flage = false;
        String result="user not exist";
        User user1 = new User();
        user1 = DAL.checkUserExits(user.getUsername());
        System.out.println("in check before crahs");
//        System.out.println("check says" + user1.getPassword() + user1.getUsername());
        //check both name and password
        if(user1!= null){
            if (user1.getPassword().equals(user.getPassword()) && user1.getUsername().equals(user.getUsername())) result="datacompleted";
        if(!user1.getUsername().matches(user.getUsername()))   result="user not exist";
        else{
              //check password
              if(user1.getPassword().matches(user.getPassword())) {
                 System.out.println("okay");
              }
              else {
                   result="password is wrong";
              }
        }
       
        
        System.out.println(result);
        }
        
        return result;
    }
    
  
    public JSONObject ALLOnline() throws JSONException {
        
        //obj
        JSONObject object = new JSONObject();
        //array
        JSONArray online = new JSONArray();
        
        try {
            
            for (UserHandler s : clients) {
                
                online.put(s.user.getUsername());
                
            }
            
            //obj player carry the current player data and all online players
            object.put("player data", user.getUsername());
            object.put("online", online);
            object.put("operation","onlineready");
            

        } catch (JSONException ex) {
            Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        return object;
        
    };
 
    
    void sendMessageToSender(String str, String username) {

        for (UserHandler s : clients) {

            s.ps.println(str);
        }

        /*
        int index = -1;
        String userRetrieved = "abdelrhman";
        for(int i = 0; i < clients.size(); i++){
            if(userRetrieved.equals(clients.get(i).user.getUsername())){
                index = i;
            }
        }
        
        if(index != -1){
            clients.get(index).user.getUsername();
            clients.get(index).ps.println(str);
            System.out.println("sended");
        }*/
    }
    
//    String getCurrentUser(String username){
//        String currentUser = "";
//        for (UserHandler s : clients) {
//            
//                if (s.ps.toString().equals(ps.toString()))
//                {
//                    try {
//                        JSONObject send = new JSONObject();
//                        send.put("operation", "current_user");
//                        send.put("username", s.user.getUsername());
////                        s.ps.println(send);
//                        
//                        username = s.user.getUsername();
//                        
//                        System.out.println(s.user.getUsername());
//                        System.out.println(send);
//                    } catch (JSONException ex) {
//                        Logger.getLogger(UserHandler.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//           }
//        return username;
//    }

}