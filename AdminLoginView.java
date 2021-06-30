import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class AdminLoginView extends JPanel
{
	private Launcher currentDriver;

	private JPasswordField password;
	private double passwordX, passwordY;
	private JLabel passwordLabel;
	private double passwordLabelXOffset, passwordLabelYOffset, passwordLabelX, passwordLabelY;
	private JButton login;
	private double loginX, loginY;
	private JButton back;
	private double backX, backY;

	private Color invalid = new Color(255, 225, 200);
	private Color defaultBackground = new Color(250, 250, 250);

	private boolean loaded;
	
	private final static String adminPassword = "Password";

	public AdminLoginView(Launcher l)
	{
		currentDriver = l;

		this.setLayout(null);

		password = new JPasswordField();
		password.setBackground(defaultBackground);
		password.setSize(200, 50);
		passwordX = 9/18.0;
		passwordY = 1/5.0;
		password.addActionListener(e -> {
			login();
		});
		
		passwordLabel = new JLabel("Password:");
		passwordLabel.setSize(70, 12);
		passwordLabelXOffset = -(password.getWidth()/2+passwordLabel.getWidth());
		passwordLabelYOffset = -passwordLabel.getHeight()/2;
		passwordLabelX = passwordX;
		passwordLabelY = passwordY;

		login = new JButton();
		login.setText("Login");
		login.setSize(100,50);
		loginX = 2/3.0;
		loginY = 8/10.0;
		login.addActionListener(e -> {
			login();
		});

		back = new JButton();
		back.setText("Back");
		back.setSize(100,50);
		backX = 1/3.0;
		backY = 8/10.0;
		back.addActionListener(e -> {
			try {
				currentDriver.switchScene(new HomeView(l));
			} catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}
		});


		this.add(password);
		this.add(passwordLabel);
		this.add(login);
		this.add(back);

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
		password.setBounds((int)(x*passwordX-password.getWidth()/2), (int)(y*passwordY-password.getHeight()/2), password.getWidth(), password.getHeight());
		passwordLabel.setBounds((int)(x*passwordLabelX+passwordLabelXOffset), (int)(y*passwordLabelY+passwordLabelYOffset), passwordLabel.getWidth(), passwordLabel.getHeight());
		login.setBounds((int)(x*loginX-login.getWidth()/2), (int)(y*loginY-login.getHeight()/2), login.getWidth(), login.getHeight());
		back.setBounds((int)(x*backX-back.getWidth()/2), (int)(y*backY-back.getHeight()/2), back.getWidth(), back.getHeight());
	}
	
	private void login()
	{
		if(validateInput())
		{
			try { currentDriver.switchScene(new AdminHomeView(currentDriver)); }
			catch (InterruptedException interruptedException) { System.out.println("Exception Dummy"); interruptedException.printStackTrace(); }
		}
		else
		{}
	}

	private boolean validateInput()
	{
		boolean isValid = true;

		if(!(new String(password.getPassword()).equals(adminPassword)))
		{
			password.setBackground(invalid);
			isValid = false;
		}
		else
		{
			password.setBackground(defaultBackground);
		}

		return isValid;
	}
}
