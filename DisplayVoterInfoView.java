import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DisplayVoterInfoView extends JPanel
{
	private Launcher currentDriver;
	
	private JLabel voterDisplay;
	
	private String[] voterInfo;
	
	private boolean loaded;
	
	public DisplayVoterInfoView(Launcher l, String[] vi)
	{
		voterInfo = vi;
		currentDriver = l;
		
		this.setLayout(null);
		
		voterDisplay = new JLabel();
		voterDisplay.setSize(this.getWidth(), this.getHeight());
		voterDisplay.setFont(new Font("Consolas", Font.PLAIN, 16));
		voterDisplay.setHorizontalAlignment(JLabel.CENTER);
		voterDisplay.setVerticalAlignment(JLabel.CENTER);
		voterDisplay.setOpaque(true);
		//voterDisplay.setBackground(Color.cyan);
		//voterDisplay.setForeground(Color.black);
		//voterDisplay
		
		//this.setOpaque(true);
		//this.setBackground(Color.GREEN);
		this.add(voterDisplay);
		
		loaded = true;
	}
	
	public void setVoterInfo(String[] vi)
	{
		voterInfo = vi;
		voterDisplay.setText(Arrays.toString(voterInfo));
		
    	this.setVisible(true);
		this.repaint();
	}
	
	public void repaint()
	{
		if(!loaded)
		{
			return;
		}
		
		int x = this.getWidth();
		int y = this.getHeight();
		
		voterDisplay.setBounds(0, 0, x, y);
		//voterDisplay.setText(Arrays.toString(voterInfo));
	}
}
