import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Meni extends JFrame{
    private JPanel meniFrame;
    private JButton drzaveEDTButton;
    private JButton krajiEDTButton;
    private JButton uporabnikiEDTButton;
    private JButton avtomobiliEDTButton;
    private JButton nazajButton;

    public Meni(){
        setContentPane(meniFrame);
        setTitle("Admin meni");
        setSize(400, 210);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);


        drzaveEDTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrzaveEDT drzave = new DrzaveEDT();
                close();
            }
        });
        krajiEDTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KrajiEDT kraji = new KrajiEDT();
                close();
            }
        });
        uporabnikiEDTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UporabnikiEDT uporabniki = new UporabnikiEDT();
                close();
            }
        });
        nazajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User_sign_in userSignIn = new User_sign_in();
                close();
            }
        });
        avtomobiliEDTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AvtomobiliEDT avtomobili = new AvtomobiliEDT();
                close();
            }
        });
    }

    private void close(){
        this.dispose();
    }
}
