/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contentsharing;

/**
 *
 * @author Abhas
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    public BufferedImage image;
    JLabel picLabel;

    public ImagePanel() {
       try
       {                
          image = ImageIO.read(new File("D:\\image.jpg"));
          picLabel = new JLabel(new ImageIcon(image));

          
       } catch (IOException ex) {
            ex.printStackTrace();
       }
       System.out.println("Repainting image panel.....");
       repaint();
    }

    public ImagePanel(String filepath)
    {
       try
       {                
            image = ImageIO.read(new File(filepath));
            
            System.out.println(image);
       }
       catch (IOException ex)
       {
            ex.printStackTrace();
       }
       System.out.println("Repainting image panel.....");
       repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 400, 0, null); // see javadoc for more info on the parameters
    }

}
