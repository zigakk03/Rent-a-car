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

public class AvtomobiliEDT extends JFrame{
    private JTable avtomobiliTable;
    private JTextField znamkaTextField;
    private JTextField modelTextField;
    private JButton nazajButton;
    private JButton izbrisiButton;
    private JButton urediButton;
    private JButton dodajButton;
    private JComboBox placeComboBox;
    private JPanel avtomobiliEDTFrame;
    private JRadioButton DARadioButton;
    private JRadioButton NERadioButton;
    private JTextField letnikTextField;
    private JTextField kmTextField;
    private JTextField kwTextField;
    private JTextField registracijaTextField;
    private JTextArea opisTextArea;
    private JButton izposojeButton;
    private List<String[]> vsiAvtomobili = new ArrayList<String[]>();


    public AvtomobiliEDT(){
        setContentPane(avtomobiliEDTFrame);
        setTitle("Avtomobili EDT");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        avtomobiliTable.setEnabled(false);

        update();
        placeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String[] izbraniAvto = vsiAvtomobili.get(placeComboBox.getSelectedIndex());
                znamkaTextField.setText(izbraniAvto[1]);
                modelTextField.setText(izbraniAvto[2]);
                letnikTextField.setText(izbraniAvto[3]);
                kmTextField.setText(izbraniAvto[4]);
                kwTextField.setText(izbraniAvto[5]);
                registracijaTextField.setText(izbraniAvto[6]);
                opisTextArea.setText(izbraniAvto[7]);
                if (izbraniAvto[8].equals("1")){
                    DARadioButton.setSelected(true);
                }
                else{
                    NERadioButton.setSelected(true);
                }
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
                    String naVoljoText = "0";
                    if (DARadioButton.isSelected()) {
                        naVoljoText = "1";
                    }
                    String sql = "insert into avtomobili(znamka, model, letnik, km, kw, registerska, opis, na_voljo) " +
                            "values ('"+ znamkaTextField.getText() +"', '"+ modelTextField.getText() +"', "+ letnikTextField.getText() +
                            ", "+ kmTextField.getText() +", "+ kwTextField.getText() +",'"+ registracijaTextField.getText() +"', '"+ opisTextArea.getText() +"', "+ naVoljoText +");";
                    stmt.executeUpdate(sql);

                    znamkaTextField.setText("");
                    modelTextField.setText("");
                    letnikTextField.setText("");
                    kmTextField.setText("");
                    kwTextField.setText("");
                    registracijaTextField.setText("");
                    opisTextArea.setText("");
                    NERadioButton.setSelected(true);

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    //System.exit(0);

                    znamkaTextField.setText("Nekaj je narobe!");
                    modelTextField.setText("");
                    letnikTextField.setText("");
                    kmTextField.setText("");
                    kwTextField.setText("");
                    registracijaTextField.setText("");
                    opisTextArea.setText("");
                    NERadioButton.setSelected(true);
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
                    String naVoljoText = "0";
                    if (DARadioButton.isSelected()) {
                        naVoljoText = "1";
                    }
                    String[] idSel = vsiAvtomobili.get(placeComboBox.getSelectedIndex());
                    String sql = "update avtomobili set znamka = '"+ znamkaTextField.getText() +"', model = '"+ modelTextField.getText() +"', letnik = "+ letnikTextField.getText() +
                            ", km = "+ kmTextField.getText() +", kw = "+ kwTextField.getText() +", registerska = '"+ registracijaTextField.getText() +
                            "', opis = '"+ opisTextArea.getText() +"', na_voljo = "+ naVoljoText +" where(id = "+ idSel[0] +");";
                    stmt.executeUpdate(sql);
                    c.commit();

                    znamkaTextField.setText("");
                    modelTextField.setText("");
                    letnikTextField.setText("");
                    kmTextField.setText("");
                    kwTextField.setText("");
                    registracijaTextField.setText("");
                    opisTextArea.setText("");
                    NERadioButton.setSelected(true);

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    //System.exit(0);
                    znamkaTextField.setText("Nekaj je narobe!");
                    modelTextField.setText("");
                    letnikTextField.setText("");
                    kmTextField.setText("");
                    kwTextField.setText("");
                    registracijaTextField.setText("");
                    opisTextArea.setText("");
                    NERadioButton.setSelected(true);
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
                    String[] selId = vsiAvtomobili.get(placeComboBox.getSelectedIndex());
                    String sql = "delete from avtomobili where (id = "+ selId[0] +");";
                    stmt.executeUpdate(sql);
                    c.commit();

                    znamkaTextField.setText("");
                    modelTextField.setText("");
                    letnikTextField.setText("");
                    kmTextField.setText("");
                    kwTextField.setText("");
                    registracijaTextField.setText("");
                    opisTextArea.setText("");
                    NERadioButton.setSelected(true);

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    //System.exit(0);

                    znamkaTextField.setText("Nekaj je narobe!");
                    modelTextField.setText("");
                    letnikTextField.setText("");
                    kmTextField.setText("");
                    kwTextField.setText("");
                    registracijaTextField.setText("");
                    opisTextArea.setText("");
                    NERadioButton.setSelected(true);
                }
                update();
            }
        });
    }

    private void update(){
        vsiAvtomobili.clear();
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
            ResultSet rs = stmt.executeQuery( "select * from avtomobili order by znamka, model, letnik, registerska, na_voljo;" );
            while ( rs.next() ) {
                String id = String.valueOf(rs.getInt(1));
                String registracija = rs.getString(2);
                int letnik = rs.getInt(3);
                int km = rs.getInt(4);
                String opis = rs.getString(5);
                int kw = rs.getInt(6);
                String znamka = rs.getString(7);
                String model = rs.getString(8);
                int naVoljo = rs.getInt(9);

                String[] vse = {id, znamka, model, String.valueOf(letnik), String.valueOf(km), String.valueOf(kw), registracija, opis, String.valueOf(naVoljo)};
                vsiAvtomobili.add(vse);

                String naVoljoText = "Ne";
                if (naVoljo == 1){
                    naVoljoText = "Da";
                }

                String[] tbLs = {i + ".", znamka, model, String.valueOf(letnik), String.valueOf(km), String.valueOf(kw), registracija, naVoljoText};
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
        avtomobiliTable.setModel(new DefaultTableModel(data, new String[]{"Mesto", "Znamka", "Model", "Letnik", "Km", "Kw", "Registracija", "Na voljo"}));
        placeComboBox.setModel(num);
        placeComboBox.setSelectedIndex(-1);
    }

    private void close(){
        this.dispose();
    }
}
