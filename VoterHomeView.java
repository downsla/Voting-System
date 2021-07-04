import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VoterHomeView extends JPanel
{
	private Launcher currentDriver;
	
    private JLabel welcomeTag;
    private double welcomeTagX, welcomeTagY;
	private JButton logout;
	private double logoutX, logoutY;
	private JButton vote;
	private double voteX, voteY;
	private JButton displayStats;
	private double displayStatsX, displayStatsY;
	private DisplayCandidateStatsView stats;
	private double statsX, statsY, statsXOffset, statsYOffset;
	private JLabel bar;
    private double barX, barY, barXOffset, barYOffset;
    
	private String[] voterInfo;
	private long locInFile;
	
	private boolean showStats;
	private CastBallotView popup;
	private boolean loaded;

	public VoterHomeView(Launcher l, String[] vi, long lif)
	{	
		voterInfo = Voter.lookup(lif);
		currentDriver = l;
		
		this.setLayout(null);
		
		//voterInfo = vi;
		locInFile = lif;
		System.out.println(Arrays.toString(vi));
		
		Ballot.loadData(voterInfo[5]);
		
		welcomeTag = new JLabel("Welcome " + voterInfo[1] + " " + voterInfo[2]);
        welcomeTag.setFont(new Font("Consolas", Font.PLAIN, 16));
        welcomeTag.setSize(2000, 20);
        welcomeTag.setVerticalAlignment(JLabel.BOTTOM);
        welcomeTag.setHorizontalAlignment(JLabel.CENTER);
        welcomeTagX = 1/2.0;
        welcomeTagY = 1/10.0;
		
		logout = new JButton("Logout");
		logout.setSize(100,50);
		logoutX = .05;
		logoutY = .05;
		logout.addActionListener(e -> {
			try {
				currentDriver.switchScene(new HomeView(currentDriver));
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}
		});
		
		vote = new JButton("Cast Vote");
		//Set to false if voter has voted
		vote.setEnabled(true);
		vote.setSize(100,50);
		voteX = .3;
		voteY = .2;
		vote.addActionListener(e -> {
			currentDriver.setEnabled(false);
			popup = new CastBallotView(currentDriver, this, voterInfo);
		});
		System.out.println(voterInfo[0] + " " + Ballot.checkKey(voterInfo[0]));
		if(Ballot.checkKey(voterInfo[0]) || (Candidate.getPosNum() == 0))
		{
			vote.setEnabled(false);
		}
		
		//System.out.println(Arrays.toString(Candidate.lookup("4237996769", 2)));
		stats = new DisplayCandidateStatsView(currentDriver);
		displayStats = new JButton("Show Election Stats");
		displayStats.setSize(200,50);
		displayStatsX = .7;
		displayStatsY = .2;
		displayStats.addActionListener(e -> {
			if(!showStats)
			{
				showStats = true;
				displayStats.setText("Hide Election Stats");
				this.remove(stats);
				stats = new DisplayCandidateStatsView(currentDriver);
				this.add(stats);
				//currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()+1);
				//currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()-1);
				//currentDriver.repaint();
			}
			else
			{
				showStats = false;
				displayStats.setText("Show Election Stats");
				this.remove(stats);
				//currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()+1);
				//currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()-1);
				//currentDriver.repaint();
			}
			//this.repaint();
			currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()+1);
			currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()-1);
		});
		
		statsX = 0;
		statsY = .2;
		statsXOffset = 0;
		statsYOffset = 55;
		
		bar = new JLabel();
        bar.setOpaque(true);
        bar.setBackground(new Color(0,0,150));
        bar.setSize(100,5);
        barX = 0;
        barY = .2;
        barXOffset = 0;
        barYOffset = 50;
		
		this.add(logout);
		this.add(vote);
		this.add(displayStats);
		this.add(bar);
		this.add(welcomeTag);
		
		showStats = false;
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
		
		logout.setBounds((int)(x*logoutX), (int)(y*logoutY), logout.getWidth(), logout.getHeight());
		vote.setBounds((int)(x*voteX-vote.getWidth()/2), (int)(y*voteY-vote.getHeight()/2), vote.getWidth(), vote.getHeight());
		displayStats.setBounds((int)(x*displayStatsX-displayStats.getWidth()/2), (int)(y*displayStatsY-displayStats.getHeight()/2), displayStats.getWidth(), displayStats.getHeight());
		stats.setBounds((int)(x*statsX-statsXOffset), (int)(y*statsY+statsYOffset), x, (int)(y*(1-statsY)));
	    bar.setBounds((int)(x*barX-barXOffset), (int)(y*barY+barYOffset), this.getWidth(), bar.getHeight());
	    welcomeTag.setBounds((int)(x*welcomeTagX-welcomeTag.getWidth()/2), (int)(y*welcomeTagY-welcomeTag.getHeight()/2), welcomeTag.getWidth(), welcomeTag.getHeight());

	}
	
	public void disableVote()
	{
		vote.setEnabled(false);
	}
}