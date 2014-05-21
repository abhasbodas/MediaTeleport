/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contentsharing;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
/**
 *
 * @author Abhas
 */
public class draw extends JPanel
{
    int numberofvertices;
    
//    draw()
//    {
//        numberofvertices = getNumberOfVertices();
//        drawing();
//    }
//    
    draw(int numberofvertices)
    {
        this.numberofvertices = numberofvertices;
        drawing();
    }
    
    public void drawing()
    {
        System.out.println("-------------draw.drawing-------------");
        repaint();
    }
    
    public int[][] getPolygonCoordinates(int vertices)
    {
        System.out.println("-------------draw.getPolygonCoordinates-------------");
        
        int[][] coordinates = new int[2][vertices];
        
        int center[] = {80,80};
        
        int radius = 50;
        
        //loop to calculate n vertices of a symmetric polygon, based on given centre and radius of its circumcircle
        
        for(int i=1;i<=vertices;i++)
        {
            coordinates[0][i-1] = (int) (center[0] + radius * Math.cos(  (2 * i * Math.PI) / vertices));      //x coordinate of vertex number i
            coordinates[1][i-1] = (int) (center[1] + radius * Math.sin(  (2 * i * Math.PI) / vertices));      //y coordinate of vertex number i
            
            System.out.println("Coordinates of point\t" + i + ": \t" + coordinates[0][i-1] + "," + coordinates[1][i-1]);
            //System.out.println("Y Coordinate of point " + i + "is: " + coordinates[1][i-1]);
        }
        
        //coordinates[0] contains x coordinates of all vertices
        //coordinates[1] contains y coordinates of all vertices
        
        return coordinates;
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
        
        int[][] coordinates = null;
        
        coordinates = getPolygonCoordinates(numberofvertices);
        
        int count = coordinates[0].length;
        
        Polygon shape = new Polygon(coordinates[0] , coordinates[1], count);
        
        g.fillPolygon(shape);
        
        System.out.println("Displayed Polygon.\n");
    }
}
