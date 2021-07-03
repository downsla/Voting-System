import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

public class DisplayCandidateStatsView extends JPanel
{
	private JButton b;
	private double bx, by;
	
	private boolean loaded;
	
	public DisplayCandidateStatsView()
	{
		//this.setOpaque(true);
		//this.setBackground(Color.black);
		//Candidate.getPosNum() to get the number of lines
		//Loop from 1 to that^
		//formatPrint(pos[0])
		//Add each position to a map and connect it to each candidate w/ Candidate.getAllCandidate(i)
		b = new JButton("XXX");
		b.setSize(100,50);
		bx = .5;
		by = .5;
		
		this.add(b);
		
		loaded = true;
	}
	
	public void repaint()
	{
		if(!loaded)
		{
			return;
		}
		int x = this.getWidth();
		int y = this.getHeight();
		
		b.setBounds((int)(x*bx), (int)(y*by), b.getWidth(), b.getHeight());
	}
}
