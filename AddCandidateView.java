import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AddCandidateView extends JPanel
{

    private JButton submit;
    private double submitX, submitY;
    private JButton back;
    private double backX, backY;

    private CandidateShellView prim;
    private double primX, primY;
    private CandidateShellView vice;
    private double viceX, viceY;
    
    private AdminHomeView parent;

    private boolean isVice;
    private boolean loaded;

    public AddCandidateView(AdminHomeView p)
    {

        this.setLayout(null);
        
        isVice = false;
        parent = p;
        
        prim = new CandidateShellView(this,false);
        prim.setSize(this.getWidth(), this.getHeight());
        primX = 0;
        primY = 0;
        //prim.addComponentListener(l);
        
        vice = new CandidateShellView(this,true);
        vice.setSize(this.getWidth(), this.getHeight());
        viceX = .4;
        viceY = 0;
        
        submit = new JButton();
        submit.setText("Register");
        submit.setSize(100,50);
        submitX = 16/20.0;
        submitY = 8/10.0;
        submit.addActionListener(e -> {
            if(validateInput())
            {
            	String[] candidate = {prim.getPosition(), prim.getParty(), "", prim.getState()};
            	if(isVice)
            	{
            		candidate[2] = prim.getName() + " and " + vice.getName();
            		candidate[3] = "";
            	}
            	else
            	{
            		candidate[2] = prim.getName();
            	}
            	System.out.println(Arrays.toString(candidate));
            	parent.addCandidate(prim.getPosition(), candidate);
                parent.clearContent();
            }
            else {}
        });

        back = new JButton();
        back.setText("Back");
        back.setSize(100,50);
        backX = 4/20.0;
        backY = 8/10.0;
        back.addActionListener(e -> {
            parent.clearContent();
        });

        this.add(submit);
        this.add(back);
        this.add(prim);

        loaded = true;
        
        this.setVisible(true);
        this.repaint();
    }

    public void repaint()
    {
        if(!loaded)
        {
            return;
        }

        int x = this.getWidth();
        int y = this.getHeight();

        submit.setBounds((int)(x*submitX-submit.getWidth()/2), (int)(y*submitY-submit.getHeight()/2), submit.getWidth(), submit.getHeight());
        back.setBounds((int)(x*backX- back.getWidth()/2), (int)(y* backY - back.getHeight()/2), back.getWidth(), back.getHeight());
        prim.setBounds((int)(x*primX), (int)(y*primY), (int)(x*.4), (int)(y*.8));
        vice.setBounds((int)(x*viceX), (int)(y*viceY), (int)(x*.4), (int)(y*.8));
    }

    private boolean validateInput()
    {
        boolean isValid = true;

        if(!prim.validateInput())
        {
        	isValid = false;
        }
        
        if(isVice && !vice.validateInput())
        {
        	isValid = false;
        }
        
        return isValid;
    }
    
    public void setVice(boolean b)
    {
    	isVice = b;
    	
    	if(b)
    	{
    		this.add(vice);
    	}
    	else
    	{
    		this.remove(vice);
    	}
    }
}
