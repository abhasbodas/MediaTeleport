/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contentsharing;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
/**
 *
 * @author Abhas
 */
public class dollarshape extends JPanel
{
    Integer numberofvertices;
    
    public BufferedImage image;
    JLabel picLabel;
       
    dollarshape(Integer numberofvertices)
    {
        this.numberofvertices = numberofvertices;
        drawing();
    }
    
    public void drawing()
    {
        System.out.println("-------------draw.drawing-------------");
        repaint();
    }
    

//    public int getNumberOfVertices()
//    {
//        
//        System.out.println("-------------draw.getNumberOfVertices-------------");
//        
//        int vertices=0;
//        
//        while(vertices<3)       //ensure minimum 3 vertices of polygon
//        {
//            Random randomGenerator = new Random();
//            vertices = randomGenerator.nextInt(10);
//            System.out.println("Number of vertices:" + vertices);
//        
//        }
//        
//        return vertices;
//    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        System.out.println("-------------draw.paintComponent-------------");
        
        g.setColor(Color.blue);
        
        try
        {
        	String imagepath = new String();
        	if( (numberofvertices>0) && (numberofvertices<6) )
        	{
        		imagepath = new String(numberofvertices.toString() + ".jpg");
        		
        		System.out.println("Path of shape image:" + imagepath);
        	}
        	else
        	{
        		throw new IOException("Shape not defined...");
        	}
                        
           image = ImageIO.read(new File(imagepath));
           picLabel = new JLabel(new ImageIcon(image));

           
        }
        catch (IOException ex)
        {
             ex.printStackTrace();
        }
        
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
        
        System.out.println("Displayed Dollar Shape.\n");
    }
}
