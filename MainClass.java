/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contentsharing;

/**
 *
 * @author Abhas
 */
public class MainClass
{
    
    public static void main(String[] args)
    {
        String ipaddress = new String("160.39.52.22");
        int port = 1541;
        
        if(args.length>0)
        {
        	ipaddress = args[0];
        	port = Integer.parseInt(args[1]);
        }
        
        SemBoundedBuffer buffer1 = new SemBoundedBuffer(100);
        
        SemBoundedBuffer buffer2 = new SemBoundedBuffer(100);
        
        
        Runnable client = new Client(ipaddress, port, buffer1, buffer2);
        
        Thread t2 = new Thread(client);
        
        t2.start();
        
        Runnable ui = new swingsframe(buffer1, buffer2);
        
        Thread t1 = new Thread(ui);
        
        t1.start();
        
        
        //ob.displayShapeFrame();
//        Runnable r1 = new Client();
//        
//        int numberofvertices = 3;
//        String filepath = null;
//        
//        
//        Runnable r2 = new shapes(numberofvertices, filepath);
    }
    
}
