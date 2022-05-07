import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Na_voljo_vozila extends JFrame{
    private JPanel nvvFrame;
    private JTable naVoljoTable;
    private JTable rezerviranaTable;
    private JComboBox naVoljoComboBox;
    private JButton rezervirajButton;
    private JComboBox rezerviranaComboBox;
    private JButton vrniButton;
    private JButton izbrisRacunaButton;
    private JButton posodobitevPodatkovButton;
    private List<String[]> vsiNaVoljo = new ArrayList<String[]>();
    private List<String[]> vsiRezervirani = new ArrayList<String[]>();

    public Na_voljo_vozila(int uId){
        setContentPane(nvvFrame);
        setTitle("Izposoja");
        setSize(1000, 500);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        naVoljoTable.setEnabled(false);
        rezerviranaTable.setEnabled(false);

        update(uId);

        rezervirajButton.addActionListener(new ActionListener() {
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
                    String[] izbAvto = vsiNaVoljo.get(naVoljoComboBox.getSelectedIndex());
                    String sql = "insert into izposoje(avtomobil_id, uporabnik_id) values ("+ izbAvto[0] +", "+ uId +");";
                    stmt.executeUpdate(sql);

                    stmt.close();
                    c.close();
                } catch ( Exception ee ) {
                    System.err.println( ee.getClass().getName()+": "+ ee.getMessage() );
                    System.exit(0);
                }
                update(uId);
            }
        });
    }

    private void update(int uporabnikId){
        vsiNaVoljo.clear();
        vsiRezervirani.clear();

        List<String[]> naVoljoTableList = new ArrayList<String[]>();
        DefaultComboBoxModel naVoljoNum = new DefaultComboBoxModel();

        List<String[]> rezTableList = new ArrayList<String[]>();
        DefaultComboBoxModel rezNum = new DefaultComboBoxModel();

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
            ResultSet rs = stmt.executeQuery("select a.id, a.znamka, a.model, a.letnik, a.km from avtomobili a where (a.na_voljo = 1) order by a.znamka, a.model, a.letnik;");
            while (rs.next()) {
                String id = String.valueOf(rs.getInt(1));
                String znamka = rs.getString(2);
                String model = rs.getString(3);
                String letnik = String.valueOf(rs.getInt(4));
                String preKm = String.valueOf(rs.getInt(5));

                String[] vse = {id, znamka, model, letnik, preKm};
                vsiNaVoljo.add(vse);

                String[] tbLs = {i + ".", znamka, model, letnik, preKm};
                naVoljoTableList.add(tbLs);
                naVoljoNum.addElement(i + ".");
                i++;
            }

            i = 1;
            stmt = c.createStatement();
            rs = stmt.executeQuery("select a.id, i.od, a.znamka, a.model, a.letnik, a.registerska " +
                    "from izposoje i inner join uporabniki u on u.id = i.uporabnik_id " +
                    "inner join avtomobili a on a.id = i.avtomobil_id " +
                    "where (i.\"do\" is null)and(u.id = "+ uporabnikId +");");
            while (rs.next()) {
                String id = String.valueOf(rs.getInt(1));
                String doTime = String.valueOf(rs.getTimestamp(2));
                String znamka = rs.getString(3);
                String model = rs.getString(4);
                String letnik = String.valueOf(rs.getInt(5));
                String reg = rs.getString(6);

                String[] vse = {id, doTime, znamka, model, letnik, reg};
                vsiRezervirani.add(vse);

                String[] tbLs = {i + ".", doTime, znamka, model, letnik, reg};
                rezTableList.add(tbLs);
                rezNum.addElement(i + ".");
                i++;
            }

            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        Object[][] naVoljoData = naVoljoTableList.toArray(Object[][]::new);
        naVoljoTable.setModel(new DefaultTableModel(naVoljoData, new String[]{"Mesto", "Znamka", "Model", "Letnik", "Km"}));
        naVoljoComboBox.setModel(naVoljoNum);
        naVoljoComboBox.setSelectedIndex(-1);

        Object[][] izpData = rezTableList.toArray(Object[][]::new);
        rezerviranaTable.setModel(new DefaultTableModel(izpData, new String[]{"Mesto", "Od", "Znamka", "Model", "Letnik", "Registracija"}));
        rezerviranaComboBox.setModel(rezNum);
        rezerviranaComboBox.setSelectedIndex(-1);
    }
}
