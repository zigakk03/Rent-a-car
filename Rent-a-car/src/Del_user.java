import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Del_user extends JFrame{
    private JButton daButton;
    private JButton neButton;
    private JPanel delUserFrame;

    public Del_user(int userId){
        setContentPane(delUserFrame);
        setTitle("Izbriši račun.");
        setSize(200, 135);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);


        neButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Na_voljo_vozila naVoljoVozila = new Na_voljo_vozila(userId);
                close();
            }
        });
        daButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection c = null;
                Statement stmt = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
                    c.setAutoCommit(false);
                    //System.out.println("Opened database successfully");

                    stmt = c.createStatement();
                    String sql = "call izbrisi_uporabnika("+ userId +");";
                    stmt.executeUpdate(sql);
                    c.commit();


                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
                User_sign_in userSignIn = new User_sign_in();
                close();
            }
        });
    }

    private void close(){
        this.dispose();
    }
}
