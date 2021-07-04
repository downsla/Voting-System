import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TextField extends JTextField implements FocusListener
{

	private String placeholder;
	private boolean isDefault;
	//private boolean isPassword;
	//private String text;
	//private int pos;
	
	public TextField()
	{
		//pos = 0;
		//text = "";
		isDefault = true;
		this.addFocusListener(this);
		/*
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent key) 
			{
				if(key.getKeyCode() == 37)
				{
					pos = Math.max(pos-1, 0);
				}
				else if(key.getKeyCode() == 39)
				{
					pos = Math.min(pos+1, text.length());
				}
				else if(key.getKeyCode() == 8 && pos >= 1)
				{
					if(text.length() >= 1)
					{
						text = text.substring(0,pos-1)+text.substring(pos);
						pos--;
					}
				}
				else if((""+key.getKeyChar()).matches("[a-zA-z0-9`~!@#\\$%\\^&*\\(\\)\\-_=+\\[\\{\\]\\}'\"/?,<.> ]"))
				{
					text = text.substring(0,pos)+key.getKeyChar()+text.substring(pos);
					pos++;
				}
			}

			@Override
			public void keyReleased(KeyEvent key) 
			{
				if((""+key.getKeyChar()).matches("[a-zA-z0-9`~!@#\\$%\\^&*\\(\\)\\-_=+\\[\\{\\]\\}'\"/?,<.> ]") || key.getKeyCode()==8)
				{
					st();
					setText(text);
					pos = text.length();
					
				}
			}

			@Override
			public void keyTyped(KeyEvent key) {}
		});*/
	}
	
	public boolean getIsDefault()
	{
		return isDefault;
	}
	
	public void setPlaceholder(String s)
	{
		placeholder = s;
		super.setText(placeholder);
	}
	
	public String getPlaceholder()
	{
		return placeholder;
	}
	
	/*public boolean isPassword()
	{
		return isPassword;
	}*/
	
	/*public void setIsPassword(boolean b)
	{
		isPassword = b;
	}*/
	
	/*public String getText()
	{
		return text;
	}*/
	
	/*public void setText(String s)
	{
		if(isPassword && !isDefault)
		{
			text = s;
			s = "";
			for(int i = 0; i < text.length(); i++)
			{
				s += "*";
			}
		}
		super.setText(s);
	}*/
	
	public void setText(String s)
	{
		super.setText(s);
		isDefault = false;
	}
	
	public void clear()
	{
		setText("");
		isDefault = true;
	}
	
	@Override
	public void focusGained(FocusEvent arg0) 
	{
		if(isDefault)
		{
			this.setText("");
			isDefault = false;
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) 
	{
		if(getText().equals(""))
		{
			this.setText(placeholder);
			isDefault = true;
		}
		else
		{
			isDefault = false;
		}
	}
}
