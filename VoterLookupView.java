import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class VoterLookupView extends JPanel
{
    private Launcher currentDriver;

    private JLabel welcomeTag;
    private double welcomeTagX, welcomeTagY;

    private TextField vuid;
    private double vuidX, vuidY;
    private TextField birthday;
    private double birthdayX, birthdayY;

    private JButton lookup;
    private double lookupX, lookupY;
    private JButton back;
    private double backX, backY;
    
    private JPanel lookupPage;
    private DisplayVoterInfoView resultsPage;

    private Color invalid = new Color(255, 225, 200);
    private Color defaultBackground = new Color(250, 250, 250);

    private boolean loaded;
    private String[] voterInfo = new String[11];

    public VoterLookupView(Launcher l)
    {
        currentDriver = l;
        
        lookupPage = new JPanel();
        lookupPage.setLayout(null);
        resultsPage = new DisplayVoterInfoView(new String[11]);

        this.setLayout(null);

        welcomeTag = new JLabel("Enter:");
        welcomeTag.setFont(new Font("Consolas", Font.PLAIN, 16));
        welcomeTag.setSize(200, 20);
        welcomeTag.setVerticalAlignment(JLabel.BOTTOM);
        welcomeTag.setHorizontalAlignment(JLabel.CENTER);
        welcomeTagX = 1/2.0;
        welcomeTagY = 1/7.0;


        vuid = new TextField();
        vuid.setPlaceholder("VUID(10 Digits)");
        vuid.setBackground(defaultBackground);
        vuid.setSize(100, 50);
        vuidX = 6/18.0;
        vuidY = 1/5.0;

        birthday = new TextField();
        birthday.setPlaceholder("Birthday: DDMMYYYY");
        birthday.setBackground(defaultBackground);
        birthday.setSize(150,50);
        birthdayX = 27/40.0;
        birthdayY = 1/5.0;

        lookup = new JButton();
        lookup.setText("Lookup");
        lookup.setSize(100, 50);
        lookupX = 2/3.0;
        lookupY = 8/10.0;
        lookup.addActionListener(e -> {
            // Check if VUID entered data is valid
            if(validateVUID())
            {
                // Pull vuid and birthday2 text from fields
                voterInfo[0] = vuid.getText();
                voterInfo[7] = birthday.getText();
                // Build search string
                String search = voterInfo[0]+voterInfo[7];
                // If the search string is in the HashMap, then pull the full voter info from the CSV
                if (Voter.checkKeyVD(search)){
                    long keyValVD = Voter.getKeyValVD(search);
                    voterInfo = Voter.lookup(keyValVD);
                    resultsPage.setVoterInfo(voterInfo);
                }
                // Else, voter info not in Hashmap display null string array
                else {
                    resultsPage.setVoterInfo(new String[] {"Invalid"});
                }
                switchPage(resultsPage);
            }
          });

        back = new JButton();
        back.setText("Back");
        back.setSize(100,50);
        backX = 1/3.0;
        backY = 8/10.0;
        back.addActionListener(e -> {
            try {
                currentDriver.switchScene(new AdminHomeView(l));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });

        lookupPage.add(welcomeTag);
        lookupPage.add(vuid);
        lookupPage.add(birthday);
        lookupPage.add(lookup);
        lookupPage.add(back);
        
        this.add(lookupPage);

        loaded = true;

    	currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()+1);
    	currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()-1);
    }

    public void repaint()
    {
        if(!loaded)
        {
            return;
        }

        int x = this.getWidth();
        int y = this.getHeight();
        
        lookupPage.setBounds(0,0,x,y);
        resultsPage.setBounds(0,0,x,y);
        vuid.setBounds((int)(x* vuidX - vuid.getWidth()/2), (int)(y* vuidY - vuid.getHeight()/2), vuid.getWidth(), vuid.getHeight());
        birthday.setBounds((int)(x* birthdayX - birthday.getWidth()/2), (int)(y* birthdayY - birthday.getHeight()/2), birthday.getWidth(), birthday.getHeight());
        lookup.setBounds((int)(x* lookupX - lookup.getWidth()/2), (int)(y* lookupY - lookup.getHeight()/2), lookup.getWidth(), lookup.getHeight());
        back.setBounds((int)(x* backX - back.getWidth()/2), (int)(y* backY - back.getHeight()/2), back.getWidth(), back.getHeight());
        welcomeTag.setBounds(f(x, welcomeTagX, welcomeTag.getWidth()), f(y, welcomeTagY, welcomeTag.getHeight()), welcomeTag.getWidth(), welcomeTag.getHeight());
    }

    private boolean validateVUID()
    {
        boolean isValid = true;

        if(!vuid.getText().matches("\\d{10}"))
        {
            vuid.setBackground(invalid);
            isValid = false;
        }
        else
        {
            vuid.setBackground(defaultBackground);
        }

        if(!birthday.getText().matches("\\d{8}"))
        {
            birthday.setBackground(invalid);
            isValid = false;
        }
        else
        {
            birthday.setBackground(defaultBackground);
        }
        
        return isValid;
    }
    

    private static int f(int n, double m, int z)
    {
        return (int)(n * m - z/2);
    }
    
    private void switchPage(JPanel j)
    {
    	this.removeAll();
    	this.add(j);
    	currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()+1);
    	currentDriver.setSize(currentDriver.getWidth(), currentDriver.getHeight()-1);
    }
}