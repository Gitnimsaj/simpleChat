// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	//looking if the message is a command 
    	if (message.startsWith("#")) {
    		//if yes strip the white space each side of the string 
    		message.strip();
    		
    		if(message=="#quit") connectionClosed();
    		else if(message=="#logoff") quit();
    		
    		//getting the first 8 char to set the comment setHost 
    		//or setPort (setPort<5555>) and using the 10e char 
    		//until before the last one 
    		else if(message.substring(0, 7 )=="#sethost") {
    			if(isConnected())setHost(message.substring(9, message.length()-2));
    			else clientUI.display("This command is invalid while being connected");
    		}
    		else if(message.substring(0, 7)=="#setport") {
    			if(isConnected()) setPort(Integer.parseInt(message.substring(9, message.length()-2)));
    			else clientUI.display("This command is invalid while being connected");
    		}
    		else if(message=="#login") {
    			if (!isConnected()) openConnection();
    		}
    		else if(message=="#gethost")clientUI.display(getHost());
    		else if(message=="#getport")clientUI.display(""+getPort());
    		}
    	else sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
	 * Method called after the connection has been closed. 
	 * It display a message to tell the client that the server is close
	 * and quit
	 */
	protected void connectionClosed() {
		clientUI.display("The server has stop");
		quit();
	}
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
