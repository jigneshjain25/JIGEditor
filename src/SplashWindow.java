import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;


class SplashWindow
{
    public SplashWindow()
    {
        
    	JWindow jw = new JWindow();
    	JPanel content = (JPanel) jw.getContentPane();
        content.setBackground(Color.white);
        
        int width = 400;
        int height = 300;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        jw.setBounds(x, y, width, height);
        
        
        JLabel label = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/SplashTest.png"))));
        content.add(label, BorderLayout.CENTER);
        Color oraRed = new Color(156, 20, 20, 255);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 0));
        
        jw.setAlwaysOnTop(true);
        jw.toFront();
        jw.setVisible(true);
      
        
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("Interupted fom sleep :Splash Screen");
			e.printStackTrace();
		}
        jw.dispose();
    }
    public static void main(String[] args){
    	new SplashWindow();
    }
}