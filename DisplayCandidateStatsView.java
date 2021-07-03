import java.awt.*;
import java.util.Arrays;

import javax.swing.*;

public class DisplayCandidateStatsView extends JPanel
{
	private Launcher currentDriver;

	private JLabel label1;
	private double label1X, label1Y;
	private JButton next;
	private double nextX, nextY;
	private JComboBox<String> state;
	private double stateX, stateY;

	JScrollPane scroll;

	private JTextArea textArea;
	private JFrame frame;

	private String[] states =
			{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
					"HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
					"MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
					"NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
					"SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};

	private Color defaultBackground = new Color(250, 250, 250);

	private boolean loaded;

	public DisplayCandidateStatsView (Launcher l)
	{
		currentDriver = l;

		this.setLayout(null);

		label1 = new JLabel("Please select the state for which you would like to view the stats:");
		label1.setFont(new Font("Consolas", Font.PLAIN, 16));
		label1.setSize(400, 20);
		label1.setVerticalAlignment(JLabel.BOTTOM);
		label1.setHorizontalAlignment(JLabel.CENTER);
		label1X = 2 / 7.0;
		label1Y = 2 / 10.0;

		state = new JComboBox<String>(states);
		state.insertItemAt("State",0); //Check they don't leave "State" selected
		state.setSelectedIndex(0);
		state.setBackground(defaultBackground);
		state.setSize(100,20);
		stateX = 5/10.0;
		stateY = 3/7.0;

		next = new JButton();
		next.setText("Display");
		next.setSize(100,50);
		nextX = 4/7.0;
		nextY = 8/10.0;
		next.addActionListener(e -> {
			if(!(state.getSelectedItem().equals(" ")))
			{
				displayStats((String)state.getSelectedItem());
			}
			else
			{
				//display that popup box to go back to the main screen
			}
		});

		this.add(label1);
		this.add(state);
		this.add(next);

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


		label1.setBounds((int)(x*label1X - label1.getWidth()/2), (int)(y* label1Y - label1.getHeight()/2), label1.getWidth(), label1.getHeight());
		state.setBounds((int)(x* stateX - state.getWidth()/2), (int)(y* stateY - state.getHeight()/2), state.getWidth(), state.getHeight());
		next.setBounds((int)(x* nextX - next.getWidth()/2), (int)(y* nextY - next.getHeight()/2), next.getWidth(), next.getHeight());
	}

	private void displayStats(String state)
	{
		frame = new JFrame(state+" Voter Stats");
		textArea = new JTextArea(30, 70); //Rows and cols to be displayed
		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		textArea.setText(formatDisplay(state));
		textArea.setEditable ( false ); // set textArea non-editable
		scroll = new JScrollPane(textArea);
		frame.add(scroll); //We add the scroll, since the scroll already contains the textArea
		frame.pack();
		frame.setVisible(true);
	}

	private String formatDisplay(String state)
	{
		return(presi(state)+"\n"+candy(state));
	}

	private String candy(String state)
	{
		String formatted="";
		Candidate.loadData(state);

		for(int x=2; x<Candidate.getPosNum()+1; x++)
		{
			String[][] cand = Candidate.getAllCandStats(x);
			for (int xx = 0; xx < cand.length; xx++) {
				formatted += "\t" + formatPrint(cand[xx]);
				formatted += "\n";
			}
		}
		return (formatted);
	}

	public static String presi(String state)
	{
		String formatted="";

		Voter.loadData();
		Ballot.loadData(state);
		Candidate.loadData(state);

		String [][] cand = Candidate.getAllPresStats();
		for(int xx=0; xx< cand.length; xx++)
		{
			formatted+= "\t"+formatPrint(cand[xx]);
			formatted+="\n";
		}
		return (formatted);
	}


	private static String formatPrint(String[] sArr) {
		int[] chLen = new int[sArr.length - 1];
		int[] temp = new int[] {60, 22, 16, 16, 19, 7, 8, 7, 34, 7, 27, 20, 43, 7};
		for(int i = 0; i < chLen.length && i < temp.length; i++) {
			chLen[i] = temp[i];
		}
		if(14 < chLen.length) {
			System.arraycopy(temp, 0, chLen, 0, temp.length);
			for(int i = 14; i < chLen.length - 1; i++) {
				chLen[i] = 6;
			}
			chLen[chLen.length - 1] = 11;
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < sArr.length - 1; i++) {
			char[] ch = new char[chLen[i] - sArr[i].length()];
			Arrays.fill(ch, ' ');
			sb.append(sArr[i]).append(ch);
		}
		sb.append(sArr[sArr.length - 1]);
		return sb.toString();
	}


}