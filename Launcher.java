import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Launcher extends JFrame implements ComponentListener
{
	private boolean startFullScreen;
	private boolean defaultLookAndFeel;
	private float widthRatio;
	private float heightRatio;
	private Dimension size;
	private JPanel currentScene;
	
	public static void main(String[] args) throws InterruptedException 
	{
		Voter.loadData();
		Candidate.loadInitData();
		new Launcher();
	}
	
	public Launcher() throws InterruptedException
	{
		size = Toolkit.getDefaultToolkit().getScreenSize();
		widthRatio = 2/3f;
		heightRatio = 2/3f;
		startFullScreen = false;
		defaultLookAndFeel = true;
		
		if(!defaultLookAndFeel)
		{
			try { 
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.setSize((int)(size.getWidth()*widthRatio),(int)(size.getHeight()*heightRatio));
		if(startFullScreen)
		{
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.addComponentListener(this);
		
		currentScene = new HomeView(this);
		currentScene.setPreferredSize(new Dimension(500,500));
		
		this.add(currentScene);
		this.setVisible(true);
	}
	
	//Each class will be passed an object of this driver so it can call this method and pass it the next window
	public void switchScene(JPanel p)
	{
		this.remove(currentScene);
		currentScene = p;
		this.add(currentScene);
		this.setVisible(true);
		this.repaint();
	}
	
	@Override
	public void componentResized(ComponentEvent arg0) 
	{
		currentScene.repaint();
	}
	
	@Override
	public void componentHidden(ComponentEvent arg0) {}
	@Override
	public void componentMoved(ComponentEvent arg0) {}
	@Override
	public void componentShown(ComponentEvent arg0) {}
}
