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

public class Spremeni_user extends JFrame {
    private JTextField textFieldIME;
    private JTextField textFieldPRI;
    private JTextField textFieldEPO;
    private JTextField textFieldGES;
    private JTextField textFieldVOZ;
    private JTextField textFieldTEL;
    private JComboBox comboBox1;
    private JButton spremeniButton;
    private JButton nazajButton;
    private JPanel JpanelSpremeni;
    private List<String[]> vsiKrai = new ArrayList<String[]>();
    private List<String[]> vseIzposoje = new ArrayList<String[]>();



    public Spremeni_user(int uid){
        setContentPane(JpanelSpremeni);
        setTitle("Posodobitev podatkov");
        setSize(600, 450);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        String krajId = "-1";
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
            //c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from uporabniki where id="+uid+" ;");
            while (rs.next()) {

                String eposta = rs.getString(2);
               // String geslo = rs.getString(3);
                String vozniska =rs.getString(4);
                String tel = rs.getString(5);
                String ime = rs.getString(6);
                String priimek = rs.getString(7);
                String kraj_id = String.valueOf(rs.getString(8));


                textFieldEPO.setText(eposta);
                textFieldIME.setText(ime);
                textFieldPRI.setText(priimek);
                textFieldTEL.setText(tel);
               // textFieldGES.setText(geslo);
                textFieldVOZ.setText(vozniska);
                krajId = kraj_id;
            }


            stmt.close();
            c.close();
        } catch ( Exception ee ) {
            System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
            System.exit(0);
        }

        int j = -1;
        DefaultComboBoxModel num = new DefaultComboBoxModel();
        DefaultComboBoxModel krajiModel = new DefaultComboBoxModel<>();
        c = null;
        stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            int i = 0;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from kraji order by ime;");
            while (rs.next()) {
                String id = String.valueOf(rs.getInt("id"));
                String ime = rs.getString("ime");
                String kratica = rs.getString("posta");

                String[] vse = {id, ime, kratica};
                vsiKrai.add(vse);

                if(krajId.equals(id)){
                    j = i;
                }

                krajiModel.addElement(kratica + ", " + ime);
                i++;
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        comboBox1.setModel(krajiModel);
        comboBox1.setSelectedIndex(j);















        nazajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Na_voljo_vozila sd = new Na_voljo_vozila(uid);
                close();

            }
        });


        spremeniButton.addActionListener(new ActionListener() {
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
                    String sql = "UPDATE uporabniki  SET  eposta ='"+textFieldEPO.getText()+"', geslo='"+generatedPassword+"' , vozniska=  '"+textFieldVOZ.getText()+"', tel='"+textFieldTEL.getText()+"', ime='"+textFieldIME.getText()+"', priimek='"+textFieldPRI.getText()+"', kraj_id="+selId[0]+"WHERE id="+uid+";";

                    stmt.executeUpdate(sql);
                    c.commit();



                    stmt.close();
                    c.close();

                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    //System.exit(0);



                }
Na_voljo_vozila nvv = new Na_voljo_vozila(uid);
                close();
            }
        });
    }
    private void close(){
        this.dispose();
    }

}
