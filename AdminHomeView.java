import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdminHomeView extends JPanel
{
    private Launcher currentDriver;

    private JLabel welcomeTag;
    private double welcomeTagX, welcomeTagY;
    private JButton voterInf;
    private double voterInf_X, voterInf_Y, voterInf_XOffset, voterInf_YOffset;
    private JButton cand;
    private double candX, candY, candXOffset, candYOffset;
    private JButton logout;
    private double logoutX, logoutY;
    private JButton finalize;
    private double finalizeX, finalizeY, finalizeXOffset, finalizeYOffset;
    private JPanel content;
    private double contentX, contentY, contentXOffset, contentYOffset;
    private JLabel bar;
    private double barX, barY, barXOffset, barYOffset;
    
    private HashMap<String, ArrayList<String[]>> candidates;

	//private Color contentBackground = new Color(245, 245, 245);

    private static boolean isAnElection;
    private boolean loaded;
    
    public AdminHomeView(Launcher l, boolean b) throws InterruptedException
    {
        currentDriver = l;

        //isAnElection = Candidate.getIsAnElection();
        isAnElection = b;
        
        this.setLayout(null);
        //this.setOpaque(true);

        welcomeTag = new JLabel("Welcome Admin");
        welcomeTag.setFont(new Font("Consolas", Font.PLAIN, 16));
        welcomeTag.setSize(2000, 20);
        welcomeTag.setVerticalAlignment(JLabel.BOTTOM);
        welcomeTag.setHorizontalAlignment(JLabel.CENTER);
        welcomeTagX = 1/2.0;
        welcomeTagY = 1/10.0;

        voterInf = new JButton("Request Voter Information");
        voterInf.setFocusable(false);
        voterInf.setSize(200, 50);
        voterInf_X = 8/20.0;
        voterInf_Y = 1/5.0;
        voterInf_XOffset = -(voterInf.getWidth()+3);
        voterInf_YOffset = 0;
        voterInf.addActionListener(e -> {
            //currentDriver.switchScene(new VoterLookupView(currentDriver));
        	this.remove(content);
        	//this.repaint();
        	content = new VoterLookupView(currentDriver);
        	this.add(content);
        	//this.setVisible(true);
        	//this.repaint();
        	currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()+1);
        	currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()-1);
        });

        cand = new JButton("Add Candidate");
        if(isAnElection)
        {
        	cand.setText("New Election");
        }
        cand.setFocusable(false);
        cand.setSize(200, 50);
        candX = 8/20.0;
        candY = 1/5.0;
        candXOffset = (3);
        candYOffset = 0;
        cand.addActionListener(e -> {
        	if(isAnElection)
        	{
        		Candidate.clearAllCandidates();
        		candidates.clear();
        		cand.setText("Add Candidate");
        		finalize.setEnabled(true);
        	}
        	this.remove(content);
        	//this.repaint();
        	content = new AddCandidateView(currentDriver, this);
        	this.add(content);
        	System.out.println(candidates+"**********************");
        	//this.setVisible(true);
        	//this.repaint();
        	currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()+1);
        	currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()-1);
        });
        //System.out.println(Candidate.getPosNum()+"-------------------------------------------------------------------------");
        //System.out.println(Candidate.getIsAnElection());

        logout = new JButton("Logout");
        logout.setFocusable(false);
        logout.setSize(200, 50);
        logoutX = .05;
        logoutY = .05;
        logout.addActionListener(e -> {
            try {
                currentDriver.switchScene(new HomeView(currentDriver));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
        
        content = new JPanel();
        content.setSize(this.getWidth(), this.getHeight());
        contentX = 0;
        contentY = 1/5.0;
        contentXOffset = 0;
        contentYOffset = 55;
        
        bar = new JLabel();
        bar.setOpaque(true);
        bar.setBackground(new Color(0,0,150));
        bar.setSize(100,5);
        barX = 0;
        barY = .2;
        barXOffset = 0;
        barYOffset = 50;
        
        finalize = new JButton("Finalize Candidate List");
        finalize.setSize(200,50);
        finalizeX = candX;
        finalizeY = candY;
        finalizeXOffset = 6+candXOffset+cand.getWidth();
        finalizeYOffset = 0;
        finalize.addActionListener(e -> {
        	this.clearContent();
        	isAnElection = true;
        	System.out.println(candidates);
        	cand.setText("New Election");
        	//Add candidates to data file
        	//candidates.get("President/Vice") will be the presidential candidates
        	//Loop through the keyset for the rest
        	//Candidate.addPres(putInFormat(candidates.get("President/Vice")));
        	if(candidates.containsKey("President/Vice"))
        	{
        		addPres(candidates.get("President/Vice"));
        	}
        	for(String s : candidates.keySet())
        	{
        		if(!s.equals("President/Vice"))
        		{
        			//Candidate.addPos(putInFormat(candidates.get(s)));
        			addPos(candidates.get(s), s);
        		}
        	}
        	Candidate.setIsAnElection(true);
        	finalize.setEnabled(false);
        });
        if(isAnElection)
        {
        	finalize.setEnabled(false);
        }

        this.add(welcomeTag);
        this.add(voterInf);
        this.add(cand);
        this.add(logout);
        this.add(content);
        this.add(bar);
        this.add(finalize);
        
        candidates = new HashMap<String, ArrayList<String[]>>();
        
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
        voterInf.setBounds(f(x, voterInf_X, voterInf_XOffset), f(y, voterInf_Y, voterInf_YOffset), voterInf.getWidth(), voterInf.getHeight());
        cand.setBounds(f(x, candX, candXOffset), f(y, candY, candYOffset), cand.getWidth(), cand.getHeight());
        logout.setBounds(f(x, logoutX, 0), f(y, logoutY, 0), logout.getWidth(), logout.getHeight());
        welcomeTag.setBounds(f(x, welcomeTagX, -welcomeTag.getWidth()/2), f(y, welcomeTagY, -welcomeTag.getHeight()/2), welcomeTag.getWidth(), welcomeTag.getHeight());
        content.setBounds(f(x, contentX, contentXOffset), f(y, contentY, contentYOffset), this.getWidth(), (int)(this.getHeight()*(1-contentY)));
        //content.setBackground(contentBackground);
        bar.setBounds(f(x, barX, barXOffset), f(y, barY, barYOffset), this.getWidth(), bar.getHeight());
        finalize.setBounds(f(x, finalizeX, finalizeXOffset), f(y, finalizeY, finalizeYOffset), finalize.getWidth(), finalize.getHeight());
    }

    private static int f(int n, double m, double z)
    {
        return (int)(n * m + z);
    }
    
    public void addCandidate(String key, String[] s)
    {
    	System.out.println("Adding");
    	if(!candidates.containsKey(key))
    	{
    		candidates.put(key, new ArrayList<String[]>());
    	}
    	
    	candidates.get(key).add(s);
    	System.out.println(candidates);
    }
    
    public void clearContent()
    {
    	this.remove(content);
    	currentDriver.repaint();
    }
    
    private void addPos(ArrayList<String[]> a, String pos)
    {
    	HashMap<String, ArrayList<String[]>> subset = new HashMap<String, ArrayList<String[]>>();
    	for(String[] x : a)
    	{
    		if(!subset.containsKey(x[3]))
    		{
    			subset.put(x[3], new ArrayList<String[]>());
    		}
    		subset.get(x[3]).add(x);
    	}
    	for(String state : subset.keySet())
    	{
    		ArrayList<String[]> c = subset.get(state);
    		String[] s = new String[c.size()*2+1];
    		s[0] = pos;
        	for(int i = 0; i < c.size(); i++)
        	{
        		//System.out.println(Arrays.toString(a.get(i)));
        		s[(i)*2+1] = c.get(i)[1];
        		s[(i+1)*2] = c.get(i)[2];
        	}
        	//System.out.println(Arrays.toString(s));
        	Candidate.addPos(s, state);
    	}
    }
    
    private void addPres(ArrayList<String[]> a)
    {
    	String[] c = new String[a.size()*2+1];
    	c[0] = "President/Vice";
    	int index = 1;
    	for(String[] s : a)
    	{
    		c[index] = s[1];
    		c[index+1] = s[2];
    		index += 2;
    		System.out.println(Arrays.toString(s));
    	}
    	Candidate.addPres(c);
    }
}