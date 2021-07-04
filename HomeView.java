import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HomeView extends JPanel
{
	private Launcher currentDriver;
	
	private JLabel welcomeTag;
	private double welcomeTagX, welcomeTagY;
	private JButton voter;
	private double voterX, voterY;
	private JButton admin;
	private double adminX, adminY;
	
	private boolean loaded;
	
	public HomeView(Launcher l) throws InterruptedException
	{
		currentDriver = l;
		
		this.setLayout(null);
		this.setOpaque(true);
		
		welcomeTag = new JLabel("Welcome are you a(n):");
		welcomeTag.setFont(new Font("Consolas", Font.PLAIN, 16));
		welcomeTag.setSize(200, 20);
		welcomeTag.setVerticalAlignment(JLabel.BOTTOM);
		welcomeTag.setHorizontalAlignment(JLabel.CENTER);
		welcomeTagX = 1/2.0;
		welcomeTagY = 1/3.0;
		
		voter = new JButton("Voter");
		voter.setFocusable(false);
		voter.setSize(100, 50);
		voterX = 12/20.0;
		voterY = 1/2.0;
		voter.addActionListener(e -> {
			currentDriver.switchScene(new VoterLoginView(currentDriver));
		});
		
		admin = new JButton("Admin");
		admin.setFocusable(false);
		admin.setSize(100, 50);
		adminX = 8/20.0;
		adminY = 1/2.0;
		admin.addActionListener(e -> {
			currentDriver.switchScene(new AdminLoginView(currentDriver));
		});
		
		this.add(welcomeTag);
		this.add(voter);
		this.add(admin);
		
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
		voter.setBounds(f(x, voterX, voter.getWidth()), f(y, voterY, voter.getHeight()), voter.getWidth(), voter.getHeight());	
		admin.setBounds(f(x, adminX, admin.getWidth()), f(y, adminY, admin.getHeight()), admin.getWidth(), admin.getHeight());
		welcomeTag.setBounds(f(x, welcomeTagX, welcomeTag.getWidth()), f(y, welcomeTagY, welcomeTag.getHeight()), welcomeTag.getWidth(), welcomeTag.getHeight());
	}
	
	private static int f(int n, double m, int z)
	{
		return (int)(n * m - z/2);
	}
}
