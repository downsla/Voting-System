import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
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
	private JButton register;
	private double registerX, registerY;
	private JButton back;
	private double backX, backY;

	private Color invalid = new Color(255, 225, 200);
	private Color defaultBackground = new Color(250, 250, 250);

	private String[] sexes = Candidate.getSexList();

	private String[] races = Candidate.getRaceList();
	
	private String[] voterInfo;
	private static String[] voterWords = VoterLoginView.getVoterWords();

	private boolean loaded;

	public RegisterVoterInfoView(Launcher l, String[] vi)
	{
		voterInfo = vi;
		
		currentDriver = l;

		this.setLayout(null);

		firstName = new TextField();
		firstName.setPlaceholder(voterWords[1]);
		firstName.setBackground(defaultBackground);
		firstName.setSize(100, 50);
		firstNameX = 4/20.0;
		firstNameY = 2/10.0;
		if(voterInfo[1] != null)
		{
			firstName.setText(voterInfo[1]);
		}

		lastName = new TextField();
		lastName.setPlaceholder(voterWords[2]);
		lastName.setBackground(defaultBackground);
		lastName.setSize(100, 50);
		lastNameX = 10/20.0;
		lastNameY = 2/10.0;
		if(voterInfo[2] != null)
		{
			lastName.setText(voterInfo[2]);
		}

		birthday = new TextField();
		birthday.setPlaceholder(voterWords[7] + " DDMMYYYY");
		birthday.setBackground(defaultBackground);
		birthday.setSize(150,50);
		birthdayX = 16/20.0;
		birthdayY = 2/10.0;
		if(voterInfo[7] != null)
		{
			birthday.setText(voterInfo[7]);
		}

		address = new TextField();
		address.setPlaceholder(voterWords[3]);
		address.setBackground(defaultBackground);
		address.setSize(150, 50);
		addressX = 4/20.0;
		addressY = 5/10.0;
		if(voterInfo[3] != null)
		{
			address.setText(voterInfo[3]);
		}

		city = new TextField();
		city.setPlaceholder(voterWords[4]);
		city.setBackground(defaultBackground);
		city.setSize(100,50);
		cityX = 7.5/20.0;
		cityY = 5/10.0;
		if(voterInfo[4] != null)
		{
			city.setText(voterInfo[4]);
		}

		state = new JComboBox<String>(Candidate.getStatesList());
		state.insertItemAt(voterWords[5],0); //Check they don't leave "State" selected
		state.setSelectedIndex(0);
		state.setBackground(defaultBackground);
		state.setSize(50,20);
		stateX = 9.70/20.0;
		stateY = 5/10.0;
		if(voterInfo[5] != null)
		{
			state.setSelectedItem(voterInfo[5]);
		}

		zip = new TextField();
		zip.setPlaceholder(voterWords[6] + " (5 digits)");
		zip.setBackground(defaultBackground);
		zip.setSize(100,50);
		zipX = 11.75/20.0;
		zipY = 5/10.0;
		if(voterInfo[6] != null)
		{
			zip.setText(voterInfo[6]);
		}

		race = new JComboBox<String>(races);
		race.insertItemAt(voterWords[9],0); //Check they don't leave "Race" selected
		race.setSelectedIndex(0);
		race.setBackground(defaultBackground);
		race.setSize(100,20);
		raceX = 14/20.0;
		raceY = 5/10.0;

		race.addActionListener(e -> { 
			String getRace = (String) race.getSelectedItem();
			if (races[0].equals(getRace)) {
				voterInfo[9] = "N";
			}
			else if(races[1].equals(getRace)) {
				voterInfo[9] = "A";
			}
			else if(races[2].equals(getRace)) {
				voterInfo[9] = "B";
			}
			else if(races[3].equals(getRace)) {
				voterInfo[9] = "H";
			}
			else if(races[4].equals(getRace)) {
				voterInfo[9] = "P";
			}
			else if(races[5].equals(getRace)) {
				voterInfo[9] = "W";
			}
		});

		sex = new JComboBox<String>(sexes);
		sex.insertItemAt(voterWords[8],0); //Check they don't leave "Sex" selected
		sex.setSelectedIndex(0);
		sex.setBackground(defaultBackground);
		sex.setSize(50,20);
		sexX = 17/20.0;
		sexY = 5/10.0;

		sex.addActionListener(e -> {
			String getSex = (String) sex.getSelectedItem();
			if (sexes[0].equals(getSex)) {
				voterInfo[8] = "M";
			}
			else if(sexes[1].equals(getSex)) {
				voterInfo[8] = "F";
			}
			else if(sexes[2].equals(getSex)) {
				voterInfo[8] = "O";
			}
		});

		register = new JButton();
		register.setText("Register");
		register.setSize(100,50);
		registerX = 16/20.0;
		registerY = 8/10.0;
		register.addActionListener(e -> {
			if(validateInput())
			{
				// Pull text from boxes if login is skipped by pressing register and no text was entered in
				// at the login screen. Capitalize all input before insertion in database
				voterInfo[1] = firstName.getIsDefault()?null: firstName.getText().trim().toUpperCase();
				voterInfo[2] = lastName.getIsDefault()?null: lastName.getText().trim().toUpperCase();
				voterInfo[3] = address.getIsDefault()?null:address.getText().trim().toUpperCase();
				voterInfo[4] = city.getIsDefault()?null:city.getText().trim().toUpperCase();
				voterInfo[5] = (String)state.getSelectedItem();
				voterInfo[6] = zip.getIsDefault()?null:zip.getText().trim();
				voterInfo[7] = birthday.getIsDefault()?null:birthday.getText().trim();
				//voterInfo[8] = (String)sex.getSelectedItem();
				//voterInfo[9] = (String)race.getSelectedItem();

				// Check to ensure new voter isn't the database. Returns true if new voter is not in database
				// Returns false if they already are
				String searchKey = Voter.getSearchKeyNAD(voterInfo);
				long locInFile;
				if (!Voter.checkKeyNAD(searchKey))
					voterInfo = Voter.register(voterInfo);
					locInFile = Voter.getKeyValNAD(searchKey);
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
		this.add(register);
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
		register.setBounds((int)(x* registerX - register.getWidth()/2), (int)(y* registerY - register.getHeight()/2), register.getWidth(), register.getHeight());
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