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
  
  /**
   * Login id 
   */
  String loginId;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginId, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginId = loginId;
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
  public void handleMessageFromClientUI(String message){
	  clientUI.display(message);
	  if (message.startsWith("#")) {
		  
		try {
			command(message.strip());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			}
		} else
		try {
			sendToServer(message);
		} catch(IOException e)
	    {
		      clientUI.display
		        ("Could not send message to server.  Terminating client.");
		      quit();
		    }
    
  }
  
  /**
   * method call to handle command from the clientUI
   * @param message
   * @throws IOException
   */
  private void command(String message) throws IOException {
	  if(message.startsWith("#quit")) {
		  quit();
	  }
		else if(message.equals("#logoff")) closeConnection();
		
		
		else if(message.startsWith("#sethost")) {
			if(isConnected()) clientUI.display("This command is invalid while being connected");
			else setHost(message.substring(9, message.length()-2));
		}
		else if(message.startsWith("#setport")) {
			if(isConnected()) clientUI.display("This command is invalid while being connected"); 
			else setPort(Integer.parseInt(message.substring(9, message.length()-2)));
		}
		else if(message.equals("#login")) {
			if (!isConnected()) openConnection();
		}
		else if(message.equals("#gethost"))clientUI.display(getHost());
		else if(message.equals("#getport"))clientUI.display(""+getPort());
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
	 * method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		clientUI.display("The server has stop");
		quit();
	}
	
	
	/**
	 * method called after a connection has been established.
	 */
	protected void connectionEstablished() {
		try {
			sendToServer("#login<"+loginId+">");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
