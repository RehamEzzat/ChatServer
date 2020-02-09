/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

public class ClientHandler extends Thread{
    DataInputStream dataInputStream;
    PrintStream printStream;
    Socket socket;
    
    static Vector<ClientHandler> clientHandlers = new Vector<ClientHandler>();
    
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            dataInputStream = new DataInputStream(socket.getInputStream());
            printStream = new PrintStream(socket.getOutputStream());
            
            clientHandlers.add(this);
            
            start();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public void run(){
        while(true){
            try{
                String msg = dataInputStream.readLine();
                sendMessageToAll(msg);
            }catch(IOException ex){
                this.stop();
                //ex.printStackTrace();
            }
        }
    }
    
    public void sendMessageToAll(String msg){
        /*while(clientHandlers.iterator().hasNext())
            clientHandlers.iterator().next().printStream.println();*/
        for(ClientHandler clientHandler : clientHandlers){
            if(clientHandler.socket.isConnected())
                clientHandler.printStream.println(msg);
        }
    }
}
