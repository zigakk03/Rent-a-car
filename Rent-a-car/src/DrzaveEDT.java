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

public class DrzaveEDT extends JFrame{
    private JPanel drzaveEDTFrame;
    private JTable drzaveTable;
    private JTextField imeTextField;
    private JButton dodajButton;
    private JButton urediButton;
    private JButton izbrisiButton;
    private JButton nazajButton;
    private JTextField kraticaTextField;
    private JComboBox placeComboBox;
    private List<String[]> vseDrzave = new ArrayList<String[]>();



    public DrzaveEDT(){
        setContentPane(drzaveEDTFrame);
        setTitle("Države EDT");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        drzaveTable.setEnabled(false);

        tableUpdate();
        placeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String[] izbranaDrzava = vseDrzave.get(placeComboBox.getSelectedIndex());
                imeTextField.setText(izbranaDrzava[1]);
                kraticaTextField.setText(izbranaDrzava[2]);
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
                    String sql = "insert into drzave (ime, kratica) values ('"+ imeTextField.getText() +"', '"+ kraticaTextField.getText() +"');";
                    stmt.executeUpdate(sql);

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
                tableUpdate();
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
                    String[] idSel = vseDrzave.get(placeComboBox.getSelectedIndex());
                    String sql = "update drzave set  ime = '"+ imeTextField.getText() +"', kratica = '"+ kraticaTextField.getText() +"' where (id = "+ idSel[0] +");";
                    stmt.executeUpdate(sql);
                    c.commit();

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
                tableUpdate();
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
                    String[] selId = vseDrzave.get(placeComboBox.getSelectedIndex());
                    String sql = "delete from drzave where (id = "+ selId[0] +");";
                    stmt.executeUpdate(sql);
                    c.commit();


                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
                imeTextField.setText("");
                kraticaTextField.setText("");
                tableUpdate();
            }
        });
    }

    private void tableUpdate(){
        vseDrzave.clear();
        List<String[]> tableList = new ArrayList<String[]>();
        DefaultComboBoxModel num = new DefaultComboBoxModel();
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
            ResultSet rs = stmt.executeQuery( "select * from drzave order by ime;" );
            while ( rs.next() ) {
                String id = String.valueOf(rs.getInt("id"));
                String ime = rs.getString("ime");
                String kratica = rs.getString("kratica");

                String[] vse = {id, ime, kratica};
                vseDrzave.add(vse);

                String[] tbLs = {i + ".", ime, kratica};
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
        drzaveTable.setModel(new DefaultTableModel(data, new String[]{"Mesto", "Ime", "Kratice"}));
        placeComboBox.setModel(num);
        placeComboBox.setSelectedIndex(-1);
    }
}
