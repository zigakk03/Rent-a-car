import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Vrni extends JFrame{
    private JPanel vrniFrame;
    private JTextPane komentarTextPane;
    private JButton vrniButton;
    private JButton nazajButton;

    public Vrni(int upoId, String izpId){
        setContentPane(vrniFrame);
        setTitle("Izposoja");
        setSize(400, 300);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);


        nazajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Na_voljo_vozila naVoljoVozila = new Na_voljo_vozila(upoId);
                close();
            }
        });
        vrniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;
                Statement stmt = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
                    //c.setAutoCommit(false);
                    //System.out.println("Opened database successfully");

                    stmt = c.createStatement();
                    String sql = "update izposoje set \"do\" = current_timestamp, komentar = '"+ komentarTextPane.getText() +"' where id = "+ izpId +";";
                    stmt.executeUpdate(sql);

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
                Na_voljo_vozila naVoljoVozila = new Na_voljo_vozila(upoId);
                close();
            }
        });
    }

    private void close(){
        this.dispose();
    }
}
