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

public class KrajiEDT extends JFrame{
    private JTable krajiTable;
    private JTextField imeTextField;
    private JTextField postaTextField;
    private JButton nazajButton;
    private JButton izbrisiButton;
    private JButton urediButton;
    private JButton dodajButton;
    private JComboBox placeComboBox;
    private JComboBox drzavaComboBox;
    private JPanel krajiEDTFrame;
    private List<String[]> vseDrzave = new ArrayList<String[]>();
    private List<String[]> vsiKraji = new ArrayList<String[]>();

    public KrajiEDT(){
        setContentPane(krajiEDTFrame);
        setTitle("Kraji EDT");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        update();
        placeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String[] izbraniKraj = vsiKraji.get(placeComboBox.getSelectedIndex());
                imeTextField.setText(izbraniKraj[1]);
                postaTextField.setText(izbraniKraj[2]);
                int index = -1;
                for (String[] temp : vseDrzave) {
                    if (temp[0].contains(izbraniKraj[3])){
                        index = Integer.parseInt(temp[3]);
                    }
                }
                drzavaComboBox.setSelectedIndex(index);
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
                    String[] drzavaId = vseDrzave.get(drzavaComboBox.getSelectedIndex());
                    String sql = "insert into kraji (ime, posta, drzava_id) values ('"+ imeTextField.getText() +"', '"+ postaTextField.getText() +"', "+ drzavaId[0] +");";
                    stmt.executeUpdate(sql);

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
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
                    String[] idSel = vsiKraji.get(placeComboBox.getSelectedIndex());
                    String[] drId = vseDrzave.get(drzavaComboBox.getSelectedIndex());
                    String sql = "update kraji set  ime = '"+ imeTextField.getText() +"', posta = '"+ postaTextField.getText() +"', drzava_id = "+ drId[0] +" where (id = "+ idSel[0] +");";
                    stmt.executeUpdate(sql);
                    c.commit();

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
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
                    String[] selId = vsiKraji.get(placeComboBox.getSelectedIndex());
                    String sql = "delete from kraji where (id = "+ selId[0] +");";
                    stmt.executeUpdate(sql);
                    c.commit();


                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
                imeTextField.setText("");
                postaTextField.setText("");
                drzavaComboBox.setSelectedIndex(-1);
                update();
            }
        });
    }

    private void close(){
        this.dispose();
    }

    private void update(){
        vseDrzave.clear();
        vsiKraji.clear();
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
            ResultSet rs = stmt.executeQuery( "select * from drzave order by ime;" );
            while ( rs.next() ) {
                String id = String.valueOf(rs.getInt("id"));
                String ime = rs.getString("ime");
                String kratica = rs.getString("kratica");

                String[] vse = {id, ime, kratica, String.valueOf(i)};
                vseDrzave.add(vse);

                drzaveModel.addElement(ime + ", " + kratica);
                i++;
            }

            i = 1;
            stmt = c.createStatement();
            rs = stmt.executeQuery( "select k.id, k.ime, k.posta, k.drzava_id, d.ime from kraji k inner join drzave d on d.id = k.drzava_id order by k.ime;" );
            while ( rs.next() ) {
                String id = String.valueOf(rs.getInt(1));
                String ime = rs.getString(2);
                String posta = rs.getString(3);
                String drzavaId = String.valueOf(rs.getInt(4));
                String drzavaIme = rs.getString(5);

                String[] vse = {id, ime, posta, drzavaId};
                vsiKraji.add(vse);

                String[] tbLs = {i + ".", ime, posta, drzavaIme};
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
        krajiTable.setModel(new DefaultTableModel(data, new String[]{"Mesto", "Ime", "Posta", "Država"}));
        placeComboBox.setModel(num);
        placeComboBox.setSelectedIndex(-1);
        drzavaComboBox.setModel(drzaveModel);
        drzavaComboBox.setSelectedIndex(-1);
    }
}
