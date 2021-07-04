import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AddedCandidateView extends JPanel
{

    private JLabel welcomeTag;
    private double welcomeTagX, welcomeTagY;
    private JButton voter;
    private double voterX, voterY;
    private JButton admin;
    private double adminX, adminY;

    private boolean loaded;

    public AddedCandidateView() throws InterruptedException
    {

        this.setLayout(null);
        this.setOpaque(true);

        welcomeTag = new JLabel("You have added a candidate, will they win or lose?");
        welcomeTag.setFont(new Font("Consolas", Font.PLAIN, 16));
        welcomeTag.setSize(2000, 20);
        welcomeTag.setVerticalAlignment(JLabel.BOTTOM);
        welcomeTag.setHorizontalAlignment(JLabel.CENTER);
        welcomeTagX = 1/2.0;
        welcomeTagY = 1/3.0;

        voter = new JButton("Add another Candidate");
        voter.setFocusable(false);
        voter.setSize(200, 50);
        voterX = 16/20.0;
        voterY = 1/2.0;
        //voter.addActionListener(e -> { l.switchScene(new AddCandidateView(l)); } );

        admin = new JButton("Return to Admin Home");
        admin.setFocusable(false);
        admin.setSize(200, 50);
        adminX = 4/20.0;
        adminY = 1/2.0;
        admin.addActionListener(e -> {
            /*try {
                //l.switchScene(new AdminHomeView(l));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }*/
        } );

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
        //System.out.println(welcomeTag.getWidth());
    }

    private static int f(int n, double m, int z)
    {
        return (int)(n * m - z/2);
    }
}
