import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DisplayCandidateStatsView extends JPanel
{

	private JLabel label1;
	private double label1X, label1Y;
	private JButton next;
	private double nextX, nextY;
	private JComboBox<String> state;
	private double stateX, stateY;

	JScrollPane scroll;

	private JTextArea textArea;
	private JFrame frame;

	private Color defaultBackground = new Color(250, 250, 250);

	private boolean loaded;

	public DisplayCandidateStatsView ()
	{

		this.setLayout(null);
		
		label1 = new JLabel("Please select the state for which you would like to view the stats:");
		label1.setFont(new Font("Consolas", Font.PLAIN, 16));
		label1.setSize(1000, 20);
		label1.setVerticalAlignment(JLabel.BOTTOM);
		label1.setHorizontalAlignment(JLabel.CENTER);
		label1X = 2 / 7.0;
		label1Y = 2 / 10.0;

		state = new JComboBox<String>(Candidate.getStatesList());
		state.insertItemAt("State",0); //Check they don't leave "State" selected
		state.setSelectedIndex(0);
		state.setBackground(defaultBackground);
		state.setSize(100,20);
		stateX = 5/10.0;
		stateY = 3/7.0;

		next = new JButton();
		next.setEnabled(true);
		next.setText("Display");
		next.setSize(100,50);
		nextX = 0.5;
		nextY = 8/10.0;
		next.addActionListener(e -> {
			if(!(state.getSelectedItem().equals(" ")))
			{
				next.setEnabled(false);
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
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	next.setEnabled(true);
	        }
	    });
	}

	private String formatDisplay(String state)
	{
		StringBuilder sb = new StringBuilder();
		if(0 < Candidate.getPosNum()) {
			if(Candidate.isPresElec()) {
				sb.append(presi(state));
				for(int i = 2; i < (Candidate.getPosNum() + 1); i++) {
					sb.append("\n"+candy(state, i));
				}
				return sb.toString();
			} else {
				for(int i = 1; i < (Candidate.getPosNum() + 1); i++) {
					sb.append(candy(state, i)+"\n");
				}
				return sb.toString();
			}
		} else {
			return "There is no Election";
		}
	}

	public static String candy(String state, int i)
    {
        Candidate.loadData(state);
        String [][] cand = Candidate.getAllCandStats(i);
        return TestDriver_Liam.formatPrint(cand);
    }

	public static String presi(String state)
    {
        Candidate.loadData(state);
        String [][] cand = Candidate.getAllPresStats();
        return TestDriver_Liam.formatPrint(cand);
    }

}