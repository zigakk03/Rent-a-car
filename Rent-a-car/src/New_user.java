import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class New_user extends JFrame{
    private JPanel Jpanelvpis;
    private JTextField textFieldIME;
    private JTextField textFieldPRI;
    private JTextField textFieldEPO;
    private JTextField textFieldGES;
    private JTextField textFieldVOZ;
    private JComboBox comboBox1;
    private JTextField textFieldTEL;
    private JButton dodajButton;
    private JButton nazajButton;
    private List<String[]> vsiKrai = new ArrayList<String[]>();


    public New_user(){
        setContentPane(Jpanelvpis);
        setTitle("Registracija");
        setSize(600, 450);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);


        DefaultComboBoxModel num = new DefaultComboBoxModel();
        DefaultComboBoxModel krajiModel = new DefaultComboBoxModel<>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from kraji order by ime;");
            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String ime = rs.getString("ime");
                String kratica = rs.getString("posta");

                String[] vse = {id, ime, kratica};
                vsiKrai.add(vse);

                krajiModel.addElement(kratica + ", " + ime);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        comboBox1.setModel(krajiModel);
        comboBox1.setSelectedIndex(-1);





            nazajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User_sign_in sg = new User_sign_in();

                close();
            }
        });
        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String passwordToHash = textFieldGES.getText() ;
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

                Connection c = null;
                Statement stmt = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
                    c.setAutoCommit(false);
                    //System.out.println("Opened database successfully");

                    stmt = c.createStatement();
                    String[] selId = vsiKrai.get(comboBox1.getSelectedIndex());
                    String sql = "INSERT INTO uporabniki (id, eposta, geslo, vozniska, tel, ime, priimek, kraj_id, admin) VALUES (DEFAULT, '"+textFieldEPO.getText()+"', '"+generatedPassword+"', '"+textFieldVOZ.getText()+"', '"+textFieldTEL.getText()+"', '"+textFieldIME.getText()+"', '"+textFieldPRI.getText()+"', "+selId[0]+", 0)";
                    stmt.executeUpdate(sql);
                    c.commit();

            textFieldPRI.setText("");
            textFieldIME.setText("");
            textFieldTEL.setText("");

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
User_sign_in sd = new User_sign_in();
                close();

            }
        });
    }



    private void close(){
        this.dispose();
    }
}
