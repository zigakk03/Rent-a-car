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

public class IzposojeEDT extends JFrame{
    private JTable izposojaTable;
    private JButton nazajButton;
    private JButton izbrisiButton;
    private JButton urediButton;
    private JButton koncajButton;
    private JComboBox placeComboBox;
    private JPanel izposojeEDTFrame;
    private JTextArea kometarTextArea;
    private List<String[]> vseIzposoje = new ArrayList<String[]>();

    public IzposojeEDT(int aId, int uId){
        setContentPane(izposojeEDTFrame);
        setTitle("Izposoje EDT");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        izposojaTable.setEnabled(false);

        update(aId, uId);
        nazajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Meni meni = new Meni();
                close();
            }
        });
        placeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String[] izbranaIzp = vseIzposoje.get(placeComboBox.getSelectedIndex());
                kometarTextArea.setText(izbranaIzp[2]);
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
                    String[] selId = vseIzposoje.get(placeComboBox.getSelectedIndex());
                    String sql = "delete from izposoje where (id = "+ selId[0] +");";
                    stmt.executeUpdate(sql);
                    c.commit();

                    kometarTextArea.setText("");

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    //System.exit(0);

                    kometarTextArea.setText("Nekaj je narobe!");
                }
                update(aId, uId);
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
                    String[] idSel = vseIzposoje.get(placeComboBox.getSelectedIndex());
                    String sql = "update izposoje set komentar = '"+ kometarTextArea.getText() +"' where id = "+ idSel[0] +";";
                    stmt.executeUpdate(sql);
                    c.commit();

                    kometarTextArea.setText("");

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    //System.exit(0);

                    kometarTextArea.setText("Nekaj je narobe!");

                }
                update(aId, uId);
            }
        });
        koncajButton.addActionListener(new ActionListener() {
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
                    String[] idSel = vseIzposoje.get(placeComboBox.getSelectedIndex());
                    String sql = "update izposoje set \"do\" = current_timestamp where id = "+ idSel[0] +";";
                    stmt.executeUpdate(sql);
                    c.commit();

                    kometarTextArea.setText("");

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    //System.exit(0);

                    kometarTextArea.setText("Nekaj je narobe!");

                }
                update(aId, uId);
            }
        });
    }

    private void close(){
        this.dispose();
    }

    private void update(int avtoId, int uporabnikId){
        vseIzposoje.clear();
        List<String[]> tableList = new ArrayList<String[]>();
        DefaultComboBoxModel num = new DefaultComboBoxModel();
        Connection c = null;
        Statement stmt = null;

        if (avtoId > 0 && uporabnikId <= 0) {
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
                c.setAutoCommit(false);
                //System.out.println("Opened database successfully");

                int i = 1;
                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("select i.id, i.od, i.komentar, u.ime, u.priimek, a.znamka, a.model " +
                        "from izposoje i inner join avtomobili a on a.id = i.avtomobil_id " +
                        "inner join uporabniki u on u.id = i.uporabnik_id " +
                        "where (i.\"do\" is null)and(a.id = "+ avtoId +") " +
                        "order by i.od;");
                while (rs.next()) {
                    String id = String.valueOf(rs.getInt(1));
                    String doTime = String.valueOf(rs.getTimestamp(2));
                    String komentar = rs.getString(3);
                    String uIme = rs.getString(4);
                    String uPrii = rs.getString(5);
                    String aZnamka = rs.getString(6);
                    String aModel = rs.getString(7);

                    String[] vse = {id, doTime, komentar, uIme, uPrii, aZnamka, aModel};
                    vseIzposoje.add(vse);

                    String[] tbLs = {i + ".", doTime, uIme, uPrii, aZnamka, aModel};
                    tableList.add(tbLs);
                    num.addElement(i + ".");
                    i++;
                }

                rs.close();
                stmt.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        } else if (avtoId <= 0 && uporabnikId > 0) {
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
                c.setAutoCommit(false);
                //System.out.println("Opened database successfully");

                int i = 1;
                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("select i.id, i.od, i.komentar, u.ime, u.priimek, a.znamka, a.model " +
                        "from izposoje i inner join avtomobili a on a.id = i.avtomobil_id " +
                        "inner join uporabniki u on u.id = i.uporabnik_id " +
                        "where (i.\"do\" is null)and(u.id = "+ uporabnikId +") " +
                        "order by i.od;");
                while (rs.next()) {
                    String id = String.valueOf(rs.getInt(1));
                    String doTime = String.valueOf(rs.getTimestamp(2));
                    String komentar = rs.getString(3);
                    String uIme = rs.getString(4);
                    String uPrii = rs.getString(5);
                    String aZnamka = rs.getString(6);
                    String aModel = rs.getString(7);

                    String[] vse = {id, doTime, komentar, uIme, uPrii, aZnamka, aModel};
                    vseIzposoje.add(vse);

                    String[] tbLs = {i + ".", doTime, uIme, uPrii, aZnamka, aModel};
                    tableList.add(tbLs);
                    num.addElement(i + ".");
                    i++;
                }

                rs.close();
                stmt.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }else {
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection("jdbc:postgresql://ec2-52-48-159-67.eu-west-1.compute.amazonaws.com:5432/d4rdk5mc1jo194", "kydeaasqumgwgz", "7c3167c33c4de7f3a8730f7fbc3c861a12c643c56e1924a8d147d148fb2a1a23");
                c.setAutoCommit(false);
                //System.out.println("Opened database successfully");

                int i = 1;
                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("select i.id, i.od, i.komentar, u.ime, u.priimek, a.znamka, a.model " +
                        "from izposoje i inner join avtomobili a on a.id = i.avtomobil_id " +
                        "inner join uporabniki u on u.id = i.uporabnik_id " +
                        "where (i.\"do\" is null) " +
                        "order by i.od;");
                while (rs.next()) {
                    String id = String.valueOf(rs.getInt(1));
                    String doTime = String.valueOf(rs.getTimestamp(2));
                    String komentar = rs.getString(3);
                    String uIme = rs.getString(4);
                    String uPrii = rs.getString(5);
                    String aZnamka = rs.getString(6);
                    String aModel = rs.getString(7);

                    String[] vse = {id, doTime, komentar, uIme, uPrii, aZnamka, aModel};
                    vseIzposoje.add(vse);

                    String[] tbLs = {i + ".", doTime, uIme, uPrii, aZnamka, aModel};
                    tableList.add(tbLs);
                    num.addElement(i + ".");
                    i++;
                }

                rs.close();
                stmt.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        Object[][] data = tableList.toArray(Object[][]::new);
        izposojaTable.setModel(new DefaultTableModel(data, new String[]{"Mesto", "Od", "Ime", "Priimek", "Znamka", "Model"}));
        placeComboBox.setModel(num);
        placeComboBox.setSelectedIndex(-1);
    }
}
