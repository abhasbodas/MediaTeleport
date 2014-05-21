/*
 * shapes.java
 */

package contentsharing;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * The main class of the application.
 */
public class swingsframe implements Runnable{

    JFrame frame=null;
    JFrame shapeframe=null;
    SemBoundedBuffer buffer1;
    SemBoundedBuffer buffer2;
    String filepath = null;
    ArrayList<String[]> devicecontent = new ArrayList<String[]>();
    
    ArrayList<String[]> globalcontent = new ArrayList<String[]>();
    
    EmbeddedMediaPlayerComponent mediaPlayerComponent;
    
    swingsframe(SemBoundedBuffer buffer1, SemBoundedBuffer buffer2)
    {
        this.buffer1 = buffer1;
        this.buffer2 = buffer2;
        
        devicecontent = initializeDeviceContent();
        
        globalcontent = initializeGlobalContent();
        
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        LibVlc libVlc = LibVlcFactory.factory().create();
        
        //Media Player Component
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    }
    
    public void displayFrame()
    {
        
        //launch(shapes.class, args);
        
        System.out.println("-------------Initializing frame.-------------");
        
        frame = new JFrame("Test");
        frame.setVisible(true);
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
//        Box content = new Box(BoxLayout.X_AXIS);

        frame.getContentPane().setLayout(new BorderLayout());
        
    }
    
    public void displayShape(int numberofvertices)
    {
        System.out.println("Adding shape panel to frame....");
        if(shapeframe!=null)
        {
            shapeframe.dispose();
        }
        
        shapeframe = new JFrame("Recognition Shape");
        
        shapeframe.setSize(200, 200);
        shapeframe.setVisible(true);
        
//        draw shapeobject = new draw(numberofvertices);
        
        dollarshape shapeobject = new dollarshape(numberofvertices);
        
        shapeframe.add(shapeobject);
    //  frame.pack();
    }

//    public void displayFileContent(Box contentbox, String filepath)
//    {
//        JLabel a = new javax.swing.JLabel();
//        a.setIcon(new javax.swing.ImageIcon("D:\\image.jpg"));
//        
//        contentbox.add(a, BorderLayout.EAST);
//        frame.pack();
//        
//        frame.setContentPane(contentbox);
//        frame.setVisible(true);
//        frame.setSize(1000,800);
//    }

    @Override
    public void run()
    {
        
    try
    {
        do
        {
            String sentence = null;

            /*Code to read Client's message from buffer, and act accordingly*/
        
            sentence = (String) buffer1.get();
            System.out.println("Message recieved by UI:" + sentence);
        
            //All swings code in this method's call
            sentence = swingsAction(sentence);
               
            /*Code to write to buffer, so that the client can respond to the server*/
        
            try
            {
                if (sentence != null)
                {    
                    System.out.println("Message sent by UI:" + sentence);
                    buffer2.put(sentence);
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
        }while(true);
    
   }
   catch(Exception e)
   {
      e.printStackTrace();
   }
   }
    
    public ArrayList<String> getTokens(String sentence)
    {
        ArrayList<String> tokens = new ArrayList<String>();
        
        StringTokenizer stk = new StringTokenizer(sentence,";");
        
        while(stk.hasMoreTokens())		//Inner loop to identify each VM in a given line of the data file
		{
			String temp = stk.nextToken();
			tokens.add(temp);
		}
        return tokens;
    }
    
    public String swingsAction(String sentence) throws Exception
    {
//        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
//        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
//        LibVlc libVlc = LibVlcFactory.factory().create();
//        
//        
//        //Media Player Component
//        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        Long time = new Long(0);
        
//      Box contentbox = displayFrame();
        
        ArrayList<String> tokens = getTokens(sentence);
        
        Integer command = Integer.parseInt( tokens.get(0) );
        
        
        
        switch ( command )
        {  
        
//1)Trigger Recognition on device
//accept number of vertices of a shape
//String Recieved: 	1;numberofvertices
//String Sent: 		1;path of file playing    
        case 1:
            int numberofvertices = Integer.parseInt( tokens.get(1) );
            displayShape(numberofvertices);
            sentence = new String("1;"+filepath);
        break;

//2)Get content of device
//send content of the device, like comma sepertated file names
//String Recieved:	2
//String Sent: 		2;{'content1':'value1','content2':'value2'}		(basically a comma seperated list of all files:types available)
        case 2:
            String files = new String("{");
            for(int i=0;i<devicecontent.size();i++)
            {
            	files = files.concat( "'" );
            	files = files.concat( (devicecontent.get(i))[0] );  //content[i]
                files = files.concat( "':'" );
                files = files.concat( (devicecontent.get(i))[1] );  //value[i]
                files = files.concat( "'" );
                
                if(i!=devicecontent.size()-1)
                {
                    files = files.concat(",");
                }
                else
                {
                    files = files.concat("}");
                }
            }
            sentence = new String("2;");
            sentence = sentence.concat(files);
        break;
        
//3)play content on device
//accept file name to be played
//String Recieved:	3;filepath;time					(this time should be 0 if media is not time based o has to be played from start)
//String Sent:		3;1 if file was played
//					3;0 if file could not be played (exception generated etc.)
        case 3:
            filepath = tokens.get(1);
            
            if(! (tokens.size()<3) )
            {
                time = Long.parseLong( tokens.get(2) );
            }
            else
            {
                time = (long) 0;
            }
            
            Iterator itr = devicecontent.iterator();
            boolean fileexists = false;
            
            System.out.println("File Requested:" + filepath);
            
            while(itr.hasNext())
            {
            	String[] temp = (String[]) itr.next();
            	
            	System.out.println("File being checked:" + temp[0]);
            	
            	if(temp[0].equals(filepath))
            	{
            		System.out.println("File Found...");
            		
            		fileexists = true;
            	}
            }
            
            if(fileexists == true)
            {
            	if(frame!=null)
                {
                    System.out.println("Removing old frame....");
                    VideoPlayer.stopPlayer(mediaPlayerComponent);
                    frame.dispose();
                }
                
                displayFrame();
                
            	VideoPlayer.startPlayer(frame, mediaPlayerComponent, filepath, time);
            	
            	sentence = new String("3;1");
            }
            else
            {
            	sentence = new String("3;0");
            }
        break;
         
//4)get status of played content
//send the time and details
//String Recieved:	4
//String Sent:		4;time 	(if media is time based)
//					4;0		(if media is not time based)
        case 4:
            time = (long)mediaPlayerComponent.getMediaPlayer().getTime();
            System.out.println("Time of play:" + time);
            if(filepath!=null)
            {
            	sentence = new String("4;"+filepath+";"+time.toString());
            }
            else
            {
            	sentence = new String("4;"+";"+time.toString());
            }
        break;
       
//5)Terminate Content playing on device
//terminate the content being played (it has been captured to phone)
//String Recieved:	5
//String Sent:		5
        case 5:
            VideoPlayer.stopPlayer(mediaPlayerComponent);
            if(frame!=null)
            	{
            		frame.dispose();
            	}
            if(shapeframe!=null)
            	{
            		shapeframe.dispose();
            	}
            sentence = new String("5");
        break;
//6)Terminate Recognition Mode
//String Recieved:	6
//String Sent:		6
        case 6:
            if(shapeframe!=null)
            	{
            		shapeframe.dispose();
            	}
            //frame2.dispose();
            sentence = new String("6");
        break;
//7) Fake Transfer
//String Recieved:	7;filepath
//String Sent:		7
        case 7:
            String fetchfile = new String(tokens.get(1));
            
            System.out.println("Old List:\n");
            for(int i=0;i<devicecontent.size();i++)
            {
            	System.out.print((devicecontent.get(i))[0] + "\t" + (devicecontent.get(i))[1] + "\t");
            }
            
            devicecontent = fakeTransfer(fetchfile, devicecontent, globalcontent);
            
            System.out.println("New List:\n");
            for(int i=0;i<devicecontent.size();i++)
            {
            	System.out.print((devicecontent.get(i))[0] + "\t" + (devicecontent.get(i))[1] + "\t");
            }
            
            sentence = new String("7");
        break;
//Unrecognized Command
        default:
            sentence = new String("0");
    }
    return sentence;
    }

	private ArrayList<String[]> fakeTransfer(String fetchfile, ArrayList<String[]> devicecontent, ArrayList<String[]> globalcontent)
	{
		Iterator<String[]> itr = globalcontent.iterator();
		
		while(itr.hasNext())
		{
			String[] arr = itr.next();
			
			if(arr[0].equals(fetchfile))
			{
				devicecontent.add(arr);
			}
		}
		
		return devicecontent;
	}

	private ArrayList<String[]> initializeDeviceContent()
	{
		ArrayList<String[]> devicecontent = new ArrayList<String[]>();
		
		try
		{
			File devicecontentfile = new File("devicecontent.txt");
		
			BufferedReader br = new BufferedReader( new FileReader(devicecontentfile) );
			
			String row;
			
			while( (row = br.readLine()) != null)
			{
				StringTokenizer stk = new StringTokenizer(row, ";");
				
				String[] filearr = new String[2];
				
				filearr[0] = stk.nextToken();
				
				filearr[1] = stk.nextToken();
				
				System.out.println("Adding " + filearr[0] + ";" + filearr[1] + "to device content list.");
				
				devicecontent.add(filearr);
			}
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return devicecontent;
	}
	
	private ArrayList<String[]> initializeGlobalContent()
	{
		ArrayList<String[]> globalcontent = new ArrayList<String[]>();
		
		try
		{
		File devicecontentfile = new File("globalcontent.txt");
		
			BufferedReader br = new BufferedReader( new FileReader(devicecontentfile) );
			
			String row;
			
			while( (row = br.readLine()) != null)
			{
				StringTokenizer stk = new StringTokenizer(row, ";");
				
				String[] filearr = new String[2];
				
				filearr[0] = stk.nextToken();
				
				filearr[1] = stk.nextToken();
				
				System.out.println("Adding " + filearr[0] + ";" + filearr[1] + "to global content list.");
				
				globalcontent.add(filearr);
			}
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return globalcontent;
	}
}
