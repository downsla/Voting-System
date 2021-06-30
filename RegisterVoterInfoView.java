import java.awt.Color;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class RegisterVoterInfoView extends JPanel
{
	private Launcher currentDriver;

	private TextField firstName;
	private double firstNameX, firstNameY;
	private TextField lastName;
	private double lastNameX, lastNameY;
	private TextField birthday;
	private double birthdayX, birthdayY;
	private TextField address;
	private double addressX, addressY;
	private TextField city;
	private double cityX, cityY;
	private JComboBox<String> state;
	private double stateX, stateY;
	private JComboBox<String> sex;
	private double sexX, sexY;
	private JComboBox<String> race;
	private double raceX, raceY;
	private TextField zip;
	private double zipX, zipY;
	private JButton login;
	private double loginX, loginY;
	private JButton back;
	private double backX, backY;

	private Color invalid = new Color(255, 225, 200);
	private Color defaultBackground = new Color(250, 250, 250);

	private String[] states =
			{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
			 "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
			 "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
			 "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
			 "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};

	private String[] sexes =
			{"Male", "Female",};

	private String[] races =
			{"White", "African American", "Asian", "American Indian", "Hispanic", "Pacific Islander"};
	
	private String[] voterInfo;

	private boolean loaded;

	public RegisterVoterInfoView(Launcher l, String[] vi)
	{
		voterInfo = vi;
		
		currentDriver = l;

		this.setLayout(null);

		firstName = new TextField();
		firstName.setPlaceholder("First Name");
		firstName.setBackground(defaultBackground);
		firstName.setSize(100, 50);
		firstNameX = 4/20.0;
		firstNameY = 2/10.0;
		if(voterInfo[2] != null)
		{
			firstName.setText(voterInfo[2]);
			voterInfo[2] = voterInfo[2].toUpperCase();
		}

		lastName = new TextField();
		lastName.setPlaceholder("Last Name");
		lastName.setBackground(defaultBackground);
		lastName.setSize(100, 50);
		lastNameX = 10/20.0;
		lastNameY = 2/10.0;
		if(voterInfo[1] != null)
		{
			lastName.setText(voterInfo[1]);
			voterInfo[1] = voterInfo[1].toUpperCase();
		}

		birthday = new TextField();
		birthday.setPlaceholder("Birthday: DDMMYYYY");
		birthday.setBackground(defaultBackground);
		birthday.setSize(150,50);
		birthdayX = 16/20.0;
		birthdayY = 2/10.0;
		if(voterInfo[7] != null)
		{
			birthday.setText(voterInfo[7]);
			voterInfo[7] = voterInfo[7].toUpperCase();
		}

		address = new TextField();
		address.setPlaceholder("Address");
		address.setBackground(defaultBackground);
		address.setSize(150, 50);
		addressX = 4/20.0;
		addressY = 5/10.0;
		if(voterInfo[3] != null)
		{
			address.setText(voterInfo[3]);
			voterInfo[3] = voterInfo[3].toUpperCase();
		}

		city = new TextField();
		city.setPlaceholder("City");
		city.setBackground(defaultBackground);
		city.setSize(100,50);
		cityX = 7.5/20.0;
		cityY = 5/10.0;
		if(voterInfo[4] != null)
		{
			city.setText(voterInfo[4]);
			voterInfo[4] = voterInfo[4].toUpperCase();
		}

		state = new JComboBox<String>(states);
		state.insertItemAt("State",0); //Check they don't leave "State" selected
		state.setSelectedIndex(0);
		state.setBackground(defaultBackground);
		state.setSize(50,20);
		stateX = 9.70/20.0;
		stateY = 5/10.0;
		if(voterInfo[5] != null)
		{
			state.setSelectedItem(voterInfo[5]);
			voterInfo[5] = voterInfo[5].toUpperCase();
		}

		zip = new TextField();
		zip.setPlaceholder("ZIP (5 digits)");
		zip.setBackground(defaultBackground);
		zip.setSize(100,50);
		zipX = 11.75/20.0;
		zipY = 5/10.0;
		if(voterInfo[6] != null)
		{
			zip.setText(voterInfo[6]);
		}

		race = new JComboBox<String>(races);
		race.insertItemAt("Race",0); //Check they don't leave "Race" selected
		race.setSelectedIndex(0);
		race.setBackground(defaultBackground);
		race.setSize(100,20);
		raceX = 14/20.0;
		raceY = 5/10.0;

		race.addActionListener(e -> {
			String getRace = (String) race.getSelectedItem();
			if ("White".equals(getRace)) {
				voterInfo[9] = "W";
			}
			else if("African American".equals(getRace)) {
				voterInfo[9] = "A";
			}
			else if("Asian".equals(getRace)) {
				voterInfo[9] = "S";
			}
			else if("American Indian".equals(getRace)) {
				voterInfo[9] = "A";
			}
			else if("Hispanic".equals(getRace)) {
				voterInfo[9] = "H";
			}
			else if("Pacific Islander".equals(getRace)) {
				voterInfo[9] = "P";
			}
		});

		sex = new JComboBox<String>(sexes);
		sex.insertItemAt("Sex",0); //Check they don't leave "Sex" selected
		sex.setSelectedIndex(0);
		sex.setBackground(defaultBackground);
		sex.setSize(50,20);
		sexX = 17/20.0;
		sexY = 5/10.0;

		sex.addActionListener(e -> {
			String getSex = (String) sex.getSelectedItem();
			if ("Male".equals(getSex)) {
				voterInfo[8] = "M";
			}
			else if("Female".equals(getSex)) {
				voterInfo[8] = "F";
			}
		});

		login = new JButton();
		login.setText("Register");
		login.setSize(100,50);
		loginX = 16/20.0;
		loginY = 8/10.0;
		login.addActionListener(e -> {
			if(validateInput())
			{
				System.out.println("voterInfo: " + Arrays.toString(voterInfo));

				//Register to the system
				Voter.register(voterInfo);
				long locInFile = 0l;
				currentDriver.switchScene(new VoterHomeView(currentDriver,voterInfo,locInFile));
			}
			else {}
		});


		back = new JButton();
		back.setText("Back");
		back.setSize(100,50);
		backX = 4/20.0;
		backY = 8/10.0;
		back.addActionListener(e -> {currentDriver.switchScene(new VoterLoginView(l));});


		this.add(firstName);
		this.add(birthday);
		this.add(address);
		this.add(city);
		this.add(state);
		this.add(race);
		this.add(sex);
		this.add(zip);
		this.add(login);
		this.add(back);
		this.add(lastName);

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

		firstName.setBounds((int)(x* firstNameX - firstName.getWidth()/2), (int)(y* firstNameY - firstName.getHeight()/2), firstName.getWidth(), firstName.getHeight());
		lastName.setBounds((int)(x*  lastNameX -  lastName.getWidth()/2), (int)(y*  lastNameY -  lastName.getHeight()/2),  lastName.getWidth(),  lastName.getHeight());
		birthday.setBounds((int)(x*birthdayX-birthday.getWidth()/2), (int)(y*birthdayY-birthday.getHeight()/2), birthday.getWidth(), birthday.getHeight());
		address.setBounds((int)(x*addressX-address.getWidth()/2), (int)(y*addressY-address.getHeight()/2), address.getWidth(), address.getHeight());
		city.setBounds((int)(x*cityX-city.getWidth()/2), (int)(y*cityY-city.getHeight()/2), city.getWidth(), city.getHeight());
		state.setBounds((int)(x*stateX-state.getWidth()/2), (int)(y*stateY-state.getHeight()/2), state.getWidth(), state.getHeight());
		sex.setBounds((int)(x*sexX-sex.getWidth()/2), (int)(y*sexY-sex.getHeight()/2), sex.getWidth(), sex.getHeight());
		race.setBounds((int)(x*raceX-race.getWidth()/2), (int)(y*raceY-race.getHeight()/2), race.getWidth(), race.getHeight());
		zip.setBounds((int)(x*zipX-zip.getWidth()/2), (int)(y*zipY-zip.getHeight()/2), zip.getWidth(), zip.getHeight());
		login.setBounds((int)(x*loginX-login.getWidth()/2), (int)(y*loginY-login.getHeight()/2), login.getWidth(), login.getHeight());
		back.setBounds((int)(x*backX- back.getWidth()/2), (int)(y* backY - back.getHeight()/2), back.getWidth(), back.getHeight());
	}

	private boolean validateInput()
	{
		boolean isValid = true;

		if(firstName.getIsDefault())
		{
			firstName.setBackground(invalid);
			isValid = false;
		}
		else
		{
			firstName.setBackground(defaultBackground);
		}

		if(lastName.getIsDefault())
		{
			lastName.setBackground(invalid);
			isValid = false;
		}
		else
		{
			lastName.setBackground(defaultBackground);
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

		if(sex.getSelectedItem().equals("Sex"))
		{
			sex.setBackground(invalid);
			isValid = false;
		}

		else
		{
			sex.setBackground(defaultBackground);
		}

		if(race.getSelectedItem().equals("Race"))
		{
			race.setBackground(invalid);
			isValid = false;
		}
		else
		{
			race.setBackground(defaultBackground);
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
}
