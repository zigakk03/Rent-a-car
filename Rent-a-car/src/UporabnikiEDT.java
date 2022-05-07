import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UporabnikiEDT extends JFrame{
    private JTable uporabnikiTable;
    private JTextField imeTextField;
    private JTextField priimekTextField;
    private JButton nazajButton;
    private JButton izbrisiButton;
    private JButton urediButton;
    private JButton dodajButton;
    private JComboBox placeComboBox;
    private JComboBox krajComboBox;
    private JPanel uporabnikiEDTFrame;
    private JTextField telefonTextField;
    private JTextField epostaTextField;
    private JButton izposojeButton;
    private List<String[]> vsiKraji = new ArrayList<String[]>();
    private List<String[]> vsiUporabniki = new ArrayList<String[]>();


    public UporabnikiEDT(){
        setContentPane(uporabnikiEDTFrame);
        setTitle("Uporabniki EDT");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        uporabnikiTable.setEnabled(false);

        update();
        placeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String[] izbraniUporabnik = vsiUporabniki.get(placeComboBox.getSelectedIndex());
                imeTextField.setText(izbraniUporabnik[1]);
                priimekTextField.setText(izbraniUporabnik[2]);
                telefonTextField.setText(izbraniUporabnik[3]);
                epostaTextField.setText(izbraniUporabnik[4]);
                int index = -1;
                for (String[] temp : vsiKraji) {
                    if (temp[0].contains(izbraniUporabnik[5])){
                        index = Integer.parseInt(temp[3]);
                    }
                }
                krajComboBox.setSelectedIndex(index);
            }
        });
        nazajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Meni meni = new Meni();
                close();
            }
        });
        dodajButton.addActionListener(new ActionListener() {
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
                    String[] krajId = vsiKraji.get(krajComboBox.getSelectedIndex());
                    String sql = "insert into uporabniki(ime, priimek, tel, eposta, kraj_id) values('"+ imeTextField.getText() +"', '"+ priimekTextField.getText() +"', '"+ telefonTextField.getText() +"', '"+ epostaTextField.getText() +"', "+ krajId[0] +");";
                    stmt.executeUpdate(sql);

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
                imeTextField.setText("");
                priimekTextField.setText("");
                telefonTextField.setText("");
                epostaTextField.setText("");
                krajComboBox.setSelectedIndex(-1);
                update();
            }
        });
        urediButton.addActionListener(new ActionListener() {
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
                    String[] idSel = vsiUporabniki.get(placeComboBox.getSelectedIndex());
                    String[] krId = vsiKraji.get(krajComboBox.getSelectedIndex());
                    String sql = "update uporabniki set ime = '"+ imeTextField.getText() +"', priimek = '"+ priimekTextField.getText() +"', tel = '"+ telefonTextField.getText() +"', eposta = '"+ epostaTextField.getText() +"', kraj_id = "+ krId[0] +" where (id = "+ idSel[0] +");";
                    stmt.executeUpdate(sql);
                    c.commit();

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
                imeTextField.setText("");
                priimekTextField.setText("");
                telefonTextField.setText("");
                epostaTextField.setText("");
                krajComboBox.setSelectedIndex(-1);
                update();
            }
        });
        izbrisiButton.addActionListener(new ActionListener() {
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
                    String[] selId = vsiUporabniki.get(placeComboBox.getSelectedIndex());
                    String sql = "delete from uporabniki where (id = "+ selId[0] +");";
                    stmt.executeUpdate(sql);
                    c.commit();


                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
                imeTextField.setText("");
                priimekTextField.setText("");
                telefonTextField.setText("");
                epostaTextField.setText("");
                krajComboBox.setSelectedIndex(-1);
                update();
            }
        });
        izposojeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String[] idSel = vsiUporabniki.get(placeComboBox.getSelectedIndex());
                    IzposojeEDT izposojeEDT = new IzposojeEDT(-1, Integer.parseInt(idSel[0]));
                }catch (Exception ex){
                    IzposojeEDT izposojeEDT = new IzposojeEDT(-1, -1);
                }
                close();
            }
        });
    }

    private void update(){
        vsiKraji.clear();
        vsiUporabniki.clear();
        List<String[]> tableList = new ArrayList<String[]>();
        DefaultComboBoxModel num = new DefaultComboBoxModel();
        DefaultComboBoxModel drzaveModel = new DefaultComboBoxModel<>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            int i = 0;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "select * from kraji order by ime;" );
            while ( rs.next() ) {
                String id = String.valueOf(rs.getInt("id"));
                String ime = rs.getString("ime");
                String posta = rs.getString("posta");

                String[] vse = {id, ime, posta, String.valueOf(i)};
                vsiKraji.add(vse);

                drzaveModel.addElement(posta + ", " + ime);
                i++;
            }

            i = 1;
            stmt = c.createStatement();
            rs = stmt.executeQuery( "select u.id, u.ime, u.priimek, u.tel, u.eposta, u.kraj_id, k.ime from uporabniki u inner join kraji k on k.id = u.kraj_id order by u.ime;" );
            while ( rs.next() ) {
                String id = String.valueOf(rs.getInt(1));
                String ime = rs.getString(2);
                String priimek = rs.getString(3);
                String telefon = rs.getString(4);
                String eposta = rs.getString(5);
                String krajId = String.valueOf(rs.getInt(6));
                String krajIme = rs.getString(7);


                String[] vse = {id, ime, priimek, telefon, eposta, krajId};
                vsiUporabniki.add(vse);

                String[] tbLs = {i + ".", ime, priimek, krajIme, telefon, eposta};
                tableList.add(tbLs);
                num.addElement(i + ".");
                i++;
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        Object[][] data = tableList.toArray(Object[][]::new);
        uporabnikiTable.setModel(new DefaultTableModel(data, new String[]{"Mesto", "Ime", "Priimek", "Kraj", "Telefon", "E-pošta"}));
        placeComboBox.setModel(num);
        placeComboBox.setSelectedIndex(-1);
        krajComboBox.setModel(drzaveModel);
        krajComboBox.setSelectedIndex(-1);
    }

    private void close(){
        this.dispose();
    }
}
