package contentsharing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

    int port;
    String ipaddress;
    SemBoundedBuffer buffer1;
    SemBoundedBuffer buffer2;
    
    public Client(String ipaddress, int port, SemBoundedBuffer buffer1, SemBoundedBuffer buffer2) {
        this.ipaddress = ipaddress;
        this.port = port;
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
    }

    @Override
    public void run()
    {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(ipaddress, port);
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        while(true)
        {
        /*Code to write to buffer, so that the UI can read the server's message*/
        
        System.out.println("Test line....");
            
        try
        {
            String sentence;
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            System.out.println("Message from Server:" + inFromServer);
            
            sentence = inFromServer.readLine();
            
            //System.out.println("Blocking Code?");
            
            System.out.println("READ FROM SERVER " + sentence);
                
            	if (sentence != null)
                {
                    buffer1.put(sentence);
                }
                else
                {
                    Thread.sleep(200);
                }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        /*Code to read UI's message from buffer, and send it back to the server*/
        
        try
        {
        	DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

		String toSend = (String) buffer2.get();
		System.out.println(toSend+" is being sent");
		outToServer.writeBytes(toSend+"\n");
		//outToServer.writeBytes("333222");
		outToServer.flush();
	}
	catch(Exception e)
	{
            e.printStackTrace();
        }
        
       }    //end of while(true)
    }
}
