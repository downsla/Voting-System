import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RegistrationSuccessView extends JPanel
{
    private Launcher currentDriver;

    private JLabel welcomeTag;
    private double welcomeTagX, welcomeTagY;
    private JButton voter;
    private double voterX, voterY;

    private boolean loaded;

    public RegistrationSuccessView(Launcher l) throws InterruptedException
    {
        currentDriver = l;

        this.setLayout(null);
        this.setOpaque(true);

        welcomeTag = new JLabel("Contratulations on your Registration, enjoy your participation in the democratic system.");
        welcomeTag.setFont(new Font("Consolas", Font.PLAIN, 16));
        welcomeTag.setSize(2000, 20);
        welcomeTag.setVerticalAlignment(JLabel.BOTTOM);
        welcomeTag.setHorizontalAlignment(JLabel.CENTER);
        welcomeTagX = 1/2.0;
        welcomeTagY = 1/3.0;

        voter = new JButton("Return to Sign-In");
        voter.setFocusable(false);
        voter.setSize(200, 50);
        voterX = 10/20.0;
        voterY = 1/2.0;
        voter.addActionListener(e -> {
            try {
                currentDriver.switchScene(new HomeView(l));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });

        this.add(welcomeTag);
        this.add(voter);

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
        welcomeTag.setBounds(f(x, welcomeTagX, welcomeTag.getWidth()), f(y, welcomeTagY, welcomeTag.getHeight()), welcomeTag.getWidth(), welcomeTag.getHeight());
        //System.out.println(welcomeTag.getWidth());
    }

    private static int f(int n, double m, int z)
    {
        return (int)(n * m - z/2);
    }
}
