import java.awt.*;

import javax.swing.*;

public class VoterLookupView extends JPanel
{
    private Launcher currentDriver;

    private JLabel welcomeTag;
    private double welcomeTagX, welcomeTagY;
    private JLabel middleTag;
    private double middleTagX, middleTagY;
    private TextField username;
    private double usernameX, usernameY;
    private TextField birthday;
    private double birthdayX, birthdayY;
    private TextField birthday2;
    private double birthday2X, birthday2Y;
    private TextField address;
    private double addressX, addressY;
    private TextField city;
    private double cityX, cityY;
    private JComboBox<String> state;
    private double stateX, stateY;
    private TextField zip;
    private double zipX, zipY;
    private JButton login;
    private double loginX, loginY;
    private JButton back;
    private double backX, backY;
    
    private JPanel lookupPage;
    private DisplayVoterInfoView resultsPage;

    private Color invalid = new Color(255, 225, 200);
    private Color defaultBackground = new Color(250, 250, 250);

    private String[] states =
            {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                    "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
                    "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
                    "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
                    "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};

    private boolean loaded;

    public VoterLookupView(Launcher l)
    {
        currentDriver = l;
        
        lookupPage = new JPanel();
        lookupPage.setLayout(null);
        resultsPage = new DisplayVoterInfoView(currentDriver, new String[11]);

        this.setLayout(null);

        welcomeTag = new JLabel("Enter:");
        welcomeTag.setFont(new Font("Consolas", Font.PLAIN, 16));
        welcomeTag.setSize(200, 20);
        welcomeTag.setVerticalAlignment(JLabel.BOTTOM);
        welcomeTag.setHorizontalAlignment(JLabel.CENTER);
        welcomeTagX = 1/2.0;
        welcomeTagY = 1/7.0;

        middleTag = new JLabel("OR:");
        middleTag.setFont(new Font("Consolas", Font.PLAIN, 16));
        middleTag.setSize(200, 20);
        middleTag.setVerticalAlignment(JLabel.BOTTOM);
        middleTag.setHorizontalAlignment(JLabel.CENTER);
        middleTagX = 1/2.0;
        middleTagY = 1/2.5;

        username = new TextField();
        username.setPlaceholder("VUID(9 Digits)");
        username.setBackground(defaultBackground);
        username.setSize(100, 50);
        usernameX = 6/18.0;
        usernameY = 1/5.0;

        birthday = new TextField();
        birthday.setPlaceholder("Birthday: DDMMYYYY");
        birthday.setBackground(defaultBackground);
        birthday.setSize(150,50);
        birthdayX = 27/40.0;
        birthdayY = 1/2.0;

        birthday2 = new TextField();
        birthday2.setPlaceholder("Birthday: DDMMYYYY");
        birthday2.setBackground(defaultBackground);
        birthday2.setSize(150,50);
        birthday2X = 27/40.0;
        birthday2Y = 1/5.0;

        address = new TextField();
        address.setPlaceholder("Address");
        address.setBackground(defaultBackground);
        address.setSize(150, 50);
        addressX = 2/10.0;
        addressY = 1/2.0;

        city = new TextField();
        city.setPlaceholder("City");
        city.setBackground(defaultBackground);
        city.setSize(100,50);
        cityX = 6/16.0;
        cityY = 1/2.0;

        state = new JComboBox<String>(states);
        state.insertItemAt("State",0); //Check they don't leave "State" selected
        state.setSelectedIndex(0);
        state.setBackground(defaultBackground);
        state.setSize(100,20);
        stateX = 12/24.0;
        stateY = 1/2.0;

        zip = new TextField();
        zip.setPlaceholder("ZIP (5 digits)");
        zip.setBackground(defaultBackground);
        zip.setSize(100,50);
        zipX = 19/22.0;
        zipY = 1/2.0;

        login = new JButton();
        login.setText("Lookup");
        login.setSize(100, 50);
        loginX = 2/3.0;
        loginY = 8/10.0;
        login.addActionListener(e -> {
            if(validateVUID())
            {
                //Lookup voter given VUID and DOB
            	String[] voterInfo = {"123456789","Mugleston","Nicholas","4301","Orange","TX","77632","02062001","M","W","6/27/2022"};
            	resultsPage.setVoterInfo(voterInfo);
            	switchPage(resultsPage);
            }
            else if(validateNAD())
            {
            	//Lookup voter given address, city, state, DOB, and zip
            	String[] voterInfo = new String[11];
            	resultsPage.setVoterInfo(voterInfo);
            	switchPage(resultsPage);
            }
            else
            {}
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
        lookupPage.add(middleTag);
        lookupPage.add(username);
        lookupPage.add(birthday);
        lookupPage.add(birthday2);
        lookupPage.add(address);
        lookupPage.add(city);
        lookupPage.add(state);
        lookupPage.add(zip);
        lookupPage.add(login);
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
        username.setBounds((int)(x*usernameX-username.getWidth()/2), (int)(y*usernameY-username.getHeight()/2), username.getWidth(), username.getHeight());
        birthday.setBounds((int)(x*birthdayX-birthday.getWidth()/2), (int)(y*birthdayY-birthday.getHeight()/2), birthday.getWidth(), birthday.getHeight());
        birthday2.setBounds((int)(x*birthday2X-birthday2.getWidth()/2), (int)(y*birthday2Y-birthday2.getHeight()/2), birthday2.getWidth(), birthday2.getHeight());
        address.setBounds((int)(x*addressX-address.getWidth()/2), (int)(y*addressY-address.getHeight()/2), address.getWidth(), address.getHeight());
        city.setBounds((int)(x*cityX-city.getWidth()/2), (int)(y*cityY-city.getHeight()/2), city.getWidth(), city.getHeight());
        state.setBounds((int)(x*stateX-state.getWidth()/2), (int)(y*stateY-state.getHeight()/2), state.getWidth(), state.getHeight());
        zip.setBounds((int)(x*zipX-zip.getWidth()/2), (int)(y*zipY-zip.getHeight()/2), zip.getWidth(), zip.getHeight());
        login.setBounds((int)(x*loginX-login.getWidth()/2), (int)(y*loginY-login.getHeight()/2), login.getWidth(), login.getHeight());
        back.setBounds((int)(x* backX - back.getWidth()/2), (int)(y* backY - back.getHeight()/2), back.getWidth(), back.getHeight());
        welcomeTag.setBounds(f(x, welcomeTagX, welcomeTag.getWidth()), f(y, welcomeTagY, welcomeTag.getHeight()), welcomeTag.getWidth(), welcomeTag.getHeight());
        middleTag.setBounds(f(x, middleTagX, middleTag.getWidth()), f(y, middleTagY, middleTag.getHeight()), middleTag.getWidth(), middleTag.getHeight());
    }

    private boolean validateVUID()
    {
        boolean isValid = true;

        if(!username.getText().matches("\\d{9}"))
        {
            username.setBackground(invalid);
            isValid = false;
        }
        else
        {
            username.setBackground(defaultBackground);
        }

        if(!birthday2.getText().matches("\\d{8}"))
        {
            birthday2.setBackground(invalid);
            isValid = false;
        }
        else
        {
            birthday2.setBackground(defaultBackground);
        }
        
        return isValid;
    }
    
    private boolean validateNAD()
    {
        boolean isValid = true;

        if(!birthday.getText().matches("\\d{8}"))
        {
            birthday.setBackground(invalid);
            isValid = false;
        }
        else
        {
            birthday.setBackground(defaultBackground);
        }

        if(address.getIsDefault())
        {
            address.setBackground(invalid);
            isValid = false;
        }
        else
        {
            address.setBackground(defaultBackground);
        }

        if(city.getIsDefault())
        {
            city.setBackground(invalid);
            isValid = false;
        }
        else
        {
            city.setBackground(defaultBackground);
        }

        if(state.getSelectedItem().equals("State"))
        {
            state.setBackground(invalid);
            isValid = false;
        }
        else
        {
            state.setBackground(defaultBackground);
        }

        if(!zip.getText().matches("\\d{5}"))
        {
            zip.setBackground(invalid);
            isValid = false;
        }
        else
        {
            zip.setBackground(defaultBackground);
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
