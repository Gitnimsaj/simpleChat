// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the server.
   */
  ChatIF serverUI; 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF server) 
  {
    super(port);
    serverUI = server;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  
	String message = (String) msg;
	serverUI.display("Message received: " + msg + " from " + client.getInfo("loginId"));
	if (message.startsWith("#login")) {
		if (client.getInfo("loginId")!=null) {
			try {
				client.sendToClient("You are already login, this command should be the first: server closed connection");
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			client.setInfo("loginId", message.substring(7,message.length()-1)); 
			this.sendToAllClients(client.getInfo("loginId")+" has logged on.");
		}
		
	}
	else{
		message = (client.getInfo("loginId") + "> " + (String) msg );
	    this.sendToAllClients(message);
	}
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
	  serverUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
	  serverUI.display
      ("Server has stopped listening for connections.");
  }
  
 protected void clientConnected(ConnectionToClient client) {
	 serverUI.display
     (client.getInfo("loginId")+" has logged on.");
  }
 
 synchronized protected void clientDisconnected(ConnectionToClient client) {
	 serverUI.display
     (client.getInfo("loginId")+" has deconnected");
 }
 
 synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
	 serverUI.display
     (client.getInfo("loginId")+" deconnected");
 }
 
}
 
  

//End of EchoServer class
