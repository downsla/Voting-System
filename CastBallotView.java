import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class CastBallotView extends JFrame implements WindowListener, ComponentListener
{
	private Launcher currentDriver;
	private VoterHomeView parent;
	private String[] voterInfo;
	
	private JLabel position;
	private double positionX, positionY;
	private JComboBox<String> candidates;
	private double candidatesX, candidatesY;
	private JButton nextPosition;
	private double nextPositionX, nextPositionY;
	private JButton submit;
	private double submitX, submitY;
	
	private int index;
	private int max;
	private int[] votes;
	private boolean loaded;

	public CastBallotView(Launcher l, VoterHomeView p, String[] v)
	{
		//Candidate.getPosNum() to get the number of lines
		//Loop from 1 to that^
		//formatPrint(pos[0])
		//Add each position to a map and connect it to each candidate w/ Candidate.getAllCandidate(i)
		currentDriver = l;
		parent = p;
		voterInfo = v;
		index = 1;
		Candidate.loadData(voterInfo[5]);
		Ballot.loadData(voterInfo[5]);
		max = Candidate.getPosNum();
		votes = new int[max];
		
		position = new JLabel();
		position.setSize(400,50);
		position.setHorizontalAlignment(JLabel.CENTER);
		position.setVerticalAlignment(JLabel.CENTER);
		positionX = .3;
		positionY = .15;
		position.setFont(new Font("Consolas",Font.PLAIN,16));
		
		candidates = new JComboBox<String>();
		candidates.setSize(600,50);
		candidatesX = .1;
		candidatesY = .4;
		candidates.setFont(new Font("Consolas", Font.PLAIN, 16));
		
		nextPosition = new JButton("Next");
		nextPosition.setSize(100,50);
		nextPositionX = .65;
		nextPositionY = .8;
		System.out.println(max);
		nextPosition.addActionListener(e -> {
			if(index < max)
			{
				System.out.println(index + " " + candidates.getSelectedIndex() + " " + candidates.getSelectedItem());
				votes[index-1] = candidates.getSelectedIndex();
				index++;
				nextPosition();
			}
			else
			{
				System.out.println(index + " " + candidates.getSelectedIndex() + " " + candidates.getSelectedItem());
				votes[index-1] = candidates.getSelectedIndex();
				index++;
				this.remove(nextPosition);
				this.remove(candidates);
				position.setText("Thank you for performing your civic duty");
				this.add(submit);
			}
			//this.repaint();
			this.setSize(this.getWidth()+1, this.getHeight());
			this.setSize(this.getWidth()-1, this.getHeight());
		});
		nextPosition();
		
		submit = new JButton("Submit");
		submit.setSize(100,50);
		submitX = .65;
		submitY = .8;
		submit.addActionListener(e -> {
			System.out.println(Arrays.toString(votes));
			System.out.println(Arrays.toString(voterInfo));
			Ballot.submit(votes, voterInfo);
			currentDriver.setEnabled(true);
			parent.disableVote();
			this.dispose();
		});
		
		this.setSize(l.getWidth(),l.getHeight());
		this.addWindowListener(this);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.add(position);
		this.add(candidates);
		this.add(nextPosition);
		this.addComponentListener(this);
		this.setVisible(true);
		
		loaded = true;
		
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
		
		position.setBounds((int)(x*positionX-position.getWidth()/2), (int)(y*positionY-position.getHeight()/2), position.getWidth(), position.getHeight());
		candidates.setBounds((int)(x*candidatesX), (int)(y*candidatesY-candidates.getHeight()/2), candidates.getWidth(), candidates.getHeight());
		nextPosition.setBounds((int)(x*nextPositionX-nextPosition.getWidth()/2), (int)(y*nextPositionY-candidates.getHeight()/2), nextPosition.getWidth(), nextPosition.getHeight());
		submit.setBounds((int)(x*submitX-submit.getWidth()/2), (int)(y*submitY-submit.getHeight()/2), submit.getWidth(), submit.getHeight());
	}
	
	private void nextPosition()
	{
		//Candidate.setCurrentState(voterInfo[5]);
				/*Candidate.loadData(voterInfo[5]);
				int posCount = Candidate.getPosNum();
				System.out.println("\nchoices:");
				for(int i = 1; i < (posCount + 1); i++) { //Each iteration is a row in the file
					String[][] candidates = Candidate.getAllCand(i); //candidates[0] is the position
					System.out.println("\t" + TestDriver_Liam.formatPrint(candidates[0]));
					for(int j = 1; j < candidates.length; j++) { //Each row in this matrix is a [candidate,party]
						System.out.println(Arrays.toString(candidates[j]));
						System.out.println("\t" + TestDriver_Liam.formatPrint(candidates[j])); //candidate info: party and name
					}
					System.out.println();
				}*/
		candidates.removeAllItems();
		String[][] m = Candidate.getAllCand(index);
		position.setText(m[0][0]);
		candidates.addItem("SKIP");
		for(int i = 1; i < m.length; i++)
		{
			candidates.addItem(String.format("%-40s| %s", m[i][0],m[i][1]));
		}
		this.repaint();
	}

	@Override
	public void windowActivated(WindowEvent arg0) 
	{}

	@Override
	public void windowClosed(WindowEvent arg0) 
	{}

	@Override
	public void windowClosing(WindowEvent arg0) 
	{

		currentDriver.setEnabled(true);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) 
	{}

	@Override
	public void windowDeiconified(WindowEvent arg0) 
	{}

	@Override
	public void windowIconified(WindowEvent arg0) 
	{}

	@Override
	public void windowOpened(WindowEvent arg0) 
	{}

	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {}

	@Override
	public void componentResized(ComponentEvent arg0) 
	{
		this.repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {}
}
