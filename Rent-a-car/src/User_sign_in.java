import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class User_sign_in extends JFrame{
    private JButton signUpButton;
    private JButton loginButtonLogin;
    private JTextField textFieldEmail;
    private JPasswordField textFieldGeslo;
    private JPanel Jpanel1;

    public User_sign_in() {
        setContentPane(Jpanel1);
        setTitle("Prijava");
        setSize(400, 150);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        loginButtonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String passwordToHash = textFieldGeslo.getText() ;
                String generatedPassword = null;

                try
                {

                    MessageDigest md = MessageDigest.getInstance("MD5");


                    md.update(passwordToHash.getBytes());


                    byte[] bytes = md.digest();


                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < bytes.length; i++) {
                        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                    }


                    generatedPassword = sb.toString();
                } catch (NoSuchAlgorithmException v) {
                    v.printStackTrace();
                }
              //  System.out.println(generatedPassword);

                int userId = -1;
                int userAdmin = -1;

                Connection c = null;
                Statement stmt = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
                    c.setAutoCommit(false);
                    //System.out.println("Opened database successfully");

                    int i = 1;
                    stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery( "select * from uporabniki WHERE uporabniki.eposta='"+ textFieldEmail.getText() +"';" );
                    while ( rs.next() ) {
                        int id = (rs.getInt("id"));
                        String geslo = rs.getString("geslo");
                        int admin = (rs.getInt("admin"));

                        if (geslo.equals(generatedPassword)){
                            //System.out.println("login uspesen");
                            System.out.println(id);
                            userId = id;
                            userAdmin = admin;
                        }
                    }

                    rs.close();
                    stmt.close();
                    c.close();
                } catch ( Exception t ) {
                    System.err.println( e.getClass().getName()+": "+ t.getMessage() );
                    System.exit(0);
                }

                if (userId > 0){
                    if (userAdmin == 1){
                        Meni adminMeni = new Meni();
                        close();
                    }
                    else{
                        Na_voljo_vozila naVoljoVozila = new Na_voljo_vozila(userId);
                        close();
                    }

                }
                else{
                    textFieldEmail.setText("Napačen naslov ali geslo");
                    textFieldGeslo.setText("");
                }
            }


        });
    }

    private void close(){
        this.dispose();
    }
    public static void main(String[] args) {
        User_sign_in user = new User_sign_in();
    }

}


