import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DisplayVoterInfoView extends JPanel
{
	
	private JLabel voterDisplay, voterDisplay2;
	
	private JButton ballot;
	private double ballotX, ballotY;
	
	private String[] voterInfo;
	
	private boolean loaded;
	
	JScrollPane scroll;
	
	private JTextArea textArea;
	private JFrame frame;
	
	public DisplayVoterInfoView(String[] vi)
	{
		voterInfo = vi;
		
		this.setLayout(null);
    	
		voterDisplay = new JLabel();
		voterDisplay.setSize(this.getWidth(), this.getHeight());
		voterDisplay.setFont(new Font("Consolas", Font.PLAIN, 16));
		voterDisplay.setHorizontalAlignment(JLabel.CENTER);
		voterDisplay.setVerticalAlignment(JLabel.CENTER);
		//voterDisplay.setOpaque(true);
		
		voterDisplay2 = new JLabel();
		voterDisplay2.setSize(this.getWidth(), this.getHeight());
		voterDisplay2.setFont(new Font("Consolas", Font.PLAIN, 16));
		voterDisplay2.setHorizontalAlignment(JLabel.CENTER);
		voterDisplay2.setVerticalAlignment(JLabel.CENTER);
		//voterDisplay2.setOpaque(true);
		//voterDisplay.setBackground(Color.cyan);
		//voterDisplay.setForeground(Color.black);
		//voterDisplay
		
		ballot = new JButton();
		ballot.setEnabled(true);
		ballot.setText("Display Ballot");
		ballot.setSize(200,50);
		ballotX = 0.5;
		ballotY = 8/10.0;
		ballot.addActionListener(e -> {
			if(voterInfo.length == 11)
			{
				ballot.setEnabled(false);
				displayStats(voterInfo);
			}
		});
		
		//this.setOpaque(true);
		//this.setBackground(Color.GREEN);
		this.add(voterDisplay);
		this.add(voterDisplay2);
		this.add(ballot);
		
		loaded = true;
	}
	
	public void setVoterInfo(String[] vi)
	{
		voterInfo = vi;
		if(voterInfo.length == 11) {
			String[] sArr = formatHeader(new String[][] {VoterLoginView.getVoterWords(), voterInfo});
			voterDisplay.setText(sArr[0]);
			voterDisplay2.setText(sArr[1]);
		} else {
			voterDisplay.setText(voterInfo[0]);
			voterDisplay2.setText("");
			ballot.setEnabled(false);
		}
		
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
		voterDisplay2.setBounds(0, 20, x, y);
		//voterDisplay.setText(Arrays.toString(voterInfo));
		ballot.setBounds((int)(x* ballotX - ballot.getWidth()/2), (int)(y* ballotY - ballot.getHeight()/2), ballot.getWidth(), ballot.getHeight());
	}
	
	private void displayStats(String[] voterInfo)
	{
		Ballot.loadData(voterInfo[5]);
		Candidate.loadData(voterInfo[5]);
		
		frame = new JFrame(voterInfo[1]+" "+voterInfo[2]+"'s Ballot");
		textArea = new JTextArea(30, 70); //Rows and cols to be displayed
		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		textArea.setText(formatDisplay(voterInfo[0]));
		textArea.setEditable ( false ); // set textArea non-editable
		scroll = new JScrollPane(textArea);
		frame.add(scroll); //We add the scroll, since the scroll already contains the textArea
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	ballot.setEnabled(true);
	        }
	    });
	}
	
	private String formatDisplay(String vuid)
	{	
		if(Ballot.checkKey(vuid)) {
			String[] ballotInfo = Ballot.lookup(Ballot.getKeyVal(vuid));
			String[][] ballotArr = new String[ballotInfo.length - 1][3];
			for(int i = 1; i < ballotInfo.length; i++) {
			    ballotArr[i - 1] = Candidate.lookup(ballotInfo[i], i);
			}
			return TestDriver_Liam.formatPrint(ballotArr);
		} else {
			return "No Ballot Submitted";
		}
	}

	private String[] formatHeader(String[][] sArr) {
		int[] spacing = new int[] {10, 0, 0, 0, 0, 5, 5, 8, 3, 4, 15};
		for(int i = 1; i < 5; i++) {
			if(sArr[0][i].length() <= sArr[1][i].length()) {
				spacing[i] = sArr[1][i].length();
			} else {
				spacing[i] = sArr[0][i].length();
			}
		}
		String[] sr = new String[2];
		for(int i = 0; i < sArr.length; i++) {
			StringBuilder sb = new StringBuilder();
			for(int j = 0; j < sArr[0].length; j++) {
				char[] ch = new char[spacing[j] - sArr[i][j].length()];
				Arrays.fill(ch, ' ');
				sb.append(" | ").append(sArr[i][j]).append(ch);
			}
			sr[i] = sb.append(" | ").toString();
		}
		return sr;
	}
	
}
