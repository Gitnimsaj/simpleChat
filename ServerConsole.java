// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 
  
  /**
   * The instance of the EchoServer that created this ServerConsole.
   */
	EchoServer server;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {

    	server = new EchoServer(port, this);
    	try {
			server.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the server's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        display(message);
        
        if (message.startsWith("#")) {
        	command(message);
        }
    	else server.sendToAllClients("SERVER MSG> "+message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * method call to handle commands
   * @param message
   * @throws IOException
   */
  private void command(String message) throws IOException {

	  if(message.equals("#quit")) {
  		  server.close();
  		  System.exit(0);
  	  }
  		else if(message.equals("#stop")) server.stopListening();
  		else if(message.equals("#close")){
  			server.close();
  			}

  		else if(message.startsWith("setport")) {
  			if(server.isListening() || server.getNumberOfClients()!=0) display("This command is invalid while the server is open");
  			else server.setPort(Integer.parseInt(message.substring(9, message.length()-2)));
  		}
  		else if(message.equals("#start")) {
  			if(server.isListening()) display("Server is already listening to connection");
  			else server.listen();
  		}
  		else if(message.equals("#getport")) {
  			display(""+server.getPort());
  		}
}


/**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("SERVER MSG> " + message);
    }

  
  //Class methods ***************************************************
 
  public static void main(String[] args) 
  {

    int port = DEFAULT_PORT;

    try
    {
      port = Integer.parseInt(args[0]);
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
    }
    
    ServerConsole chat= new ServerConsole(port);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class

