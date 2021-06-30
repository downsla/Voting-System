import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class CandidateShellView extends JPanel
{
	private AddCandidateView parent;
	
	private TextField name;
    private double nameX, nameY;
    private JComboBox<String> state;
    private double stateX, stateY;
    private JComboBox<String> position;
    private double positionX, positionY;
    private JComboBox<String> party;
    private double partyX, partyY;
    
    private Color invalid = new Color(255, 225, 200);
    private Color defaultBackground = new Color(250, 250, 250);
    
    private String[] states =
        {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
                "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
                "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
                "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
                "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};

    private String[] parties =
        {"Democrat", "Republican", "Third Party"};

    private String[] positions =
        {"President/Vice", "Senator", "Congress", "Treasurer", "Etc"};
    
    private boolean isVice;
    private boolean loaded;
	
	public CandidateShellView(AddCandidateView p, boolean iv)
	{
		parent = p;
		isVice = iv;
		
		this.setLayout(null);
		
		name = new TextField();
        name.setPlaceholder("Candidate Name");
        name.setBackground(defaultBackground);
        name.setSize(250, 50);
        nameX = 0;
        nameY = 1/10.0;
        
        state = new JComboBox<String>(states);
        state.insertItemAt("State", 0);
        state.setSelectedIndex(0);
        state.setSize(150, 20);
        stateX = 0;
        stateY = 3/10.0;
        
        position = new JComboBox<String>(positions);
        position.insertItemAt("Position", 0);
        position.setSelectedIndex(0);
        position.setSize(150, 20);
        positionX = 0;
        positionY = 5/10.0;
        position.addActionListener(e -> {
        	if(position.getSelectedItem().equals("President/Vice"))
        	{
        		state.setEnabled(false);
        		parent.setVice(true);
        	}
        	else
        	{
        		parent.setVice(false);
        		state.setEnabled(true);
        	}
        	parent.setSize(parent.getWidth(), parent.getHeight()+1);
        	parent.setSize(parent.getWidth(), parent.getHeight()-1);
        });
        
        party = new JComboBox<String>(parties);
        party.insertItemAt("Party", 0);
        party.setSelectedIndex(0);
        party.setSize(150, 20);
        partyX = 0;
        partyY = 7/10.0;
        
        if(!isVice)
        {
        	this.add(state);
        	this.add(party);
        	this.add(position);
        }
        this.add(name);
		
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
		
		name.setBounds(10, (int)(y*nameY), name.getWidth(), name.getHeight());
		state.setBounds(10, (int)(y*stateY), state.getWidth(), state.getHeight());
		position.setBounds(10, (int)(y*positionY), position.getWidth(), position.getHeight());
		party.setBounds(10, (int)(y*partyY), party.getWidth(), party.getHeight());
	}
	
	public boolean validateInput()
	{
		boolean isValid = true;
		
		if(name.getIsDefault())
		{
			isValid = false;
			name.setBackground(invalid);
		}
		else
		{
			name.setBackground(defaultBackground);
		}
		
		if(!isVice)
		{
			if(state.getSelectedItem().equals("State") && !position.getSelectedItem().equals("President/Vice"))
			{
				isValid = false;
				state.setBackground(invalid);
			}
			else
			{
				state.setBackground(defaultBackground);
			}
			
			if(position.getSelectedItem().equals("Position"))
			{
				isValid = false;
				position.setBackground(invalid);
			}
			else
			{
				position.setBackground(defaultBackground);
			}
			
			if(party.getSelectedItem().equals("Party"))
			{
				isValid = false;
				party.setBackground(invalid);
			}
			else
			{
				party.setBackground(defaultBackground);
			}
		}
		
		return isValid;
	}
	
	public String getPosition()
	{
		return (String)position.getSelectedItem();
	}
	
	public String getState()
	{
		return (String)state.getSelectedItem();
	}
	
	public String getParty()
	{
		return (String)party.getSelectedItem();
	}
	
	public String getName()
	{
		return name.getText();
	}
}
