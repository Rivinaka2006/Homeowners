/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import chart.ModelPolarAreaChart;
import com.barcodelib.barcode.Linear;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import static gui.HumanResourse.is18OrOlder;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import model.MySQL;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.chart.ModelPieChart;
import javaswingdev.chart.PieChart;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class HumanResourse extends javax.swing.JFrame {

    public HashMap<String, String> genderMap = new HashMap<>();
    public HashMap<String, String> TypeMap = new HashMap<>();
    public HashMap<String, String> EmployeeMap = new HashMap<>();

    /**
     * Creates new form HumanResourse
     */
    public HumanResourse() {
        initComponents();
        loadGender();
        loadUserType();
        loadEmployee();
        loadEmployeeRoll();
        loadUers("id", "ASC");
        loadRollManagement();
        loadleaveManagement();
        genareteSalaryId();
        chart();
        loadAttendance();
        loadDashboradCard();
    }

    private void chart() {
        getContentPane().setBackground(new Color(255, 255, 255));

        pieChart1.setChartType(PieChart.PeiChartType.DONUT_CHART);
        try {
            ResultSet rs = MySQL.executeSearch(
                    "SELECT COUNT(`type`) AS userType, `type`,`salary` "
                    + "FROM `user` "
                    + "INNER JOIN `user_type` ON `user_type`.`id` = `user`.`user_type_id` "
                    + "GROUP BY `user_type`.`type`,`salary`"
            );

            // Array of predefined colors
            Color[] colors = {
                new Color(23, 126, 238), // Blue
                new Color(34, 177, 76), // Green
                new Color(237, 28, 36), // Red
                new Color(255, 127, 39), // Orange
                new Color(63, 72, 204) // Purple
            // Add more colors as needed
            };

            int colorIndex = 0;

            while (rs.next()) {
                // Use modulo to cycle through colors
                Color color = colors[colorIndex % colors.length];
                colorIndex++;

                pieChart1.addData(new ModelPieChart(
                        rs.getString("type"),
                        rs.getInt("userType"),
                        color
                ));
                polarAreaChart.addItem(new ModelPolarAreaChart(
                        color,
                        rs.getString("type"),
                        (rs.getDouble("salary") * rs.getDouble("userType"))
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        pieChart1.addData(new ModelPieChart("Tigher", 150, new Color(23, 126, 238)));
//        pieChart1.addData(new ModelPieChart("ABC", 100, new Color(221, 65, 65)));
//        pieChart1.addData(new ModelPieChart("Coca", 1, new Color(47, 157, 64)));
//        pieChart1.addData(new ModelPieChart("Vita", 60, new Color(196, 151, 58)));
        //getContentPane().setBackground(new Color(255, 255, 255));
//        polarAreaChart.addItem(new ModelPolarAreaChart(new Color(52, 148, 203), "Coke", 60));
//        polarAreaChart.addItem(new ModelPolarAreaChart(new Color(175, 67, 237), "Fanta", 50));
//        polarAreaChart.addItem(new ModelPolarAreaChart(new Color(87, 218, 137), "Vital", 30));
        polarAreaChart.start();
    }

    ///////////////kkkkk
    private void genareteSalaryId() {
        long id = System.currentTimeMillis();
        jTextField8.setText(String.valueOf(id));
    }

    private void loadDashboradCard() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT COUNT(`user`.`id`) AS `TotalEmployee` FROM `user` "
                    + "INNER JOIN `attedance` ON `user`.`id` = `attedance`.`user_id` "
                    + "INNER JOIN `user_type` ON `user_type`.`id` = `user`.`user_type_id`");
            if (rs.next()) {
                jLabel50.setText(rs.getString("TotalEmployee"));
            }
            int rowcount = jTable7.getRowCount();
            jLabel53.setText(String.valueOf(rowcount));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAttendance() {
        String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        try {
            DefaultTableModel tableModel = (DefaultTableModel) jTable7.getModel();
            tableModel.setRowCount(0);
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `attedance` "
                    + "INNER JOIN `user` ON `user`.`id` = `attedance`.`user_id` "
                    + "INNER JOIN `user_type` ON `user_type`.`id` = `user`.`user_type_id` WHERE `attedance`.`present_day` = '" + dateTime + "'");
            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("user.id"));
                v.add(rs.getString("f_name") + " " + rs.getString("l_name"));
                v.add(rs.getString("type"));
                v.add(rs.getString("time"));
                tableModel.addRow(v);

            }

            jTable7.setModel(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmployeeRoll() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `user_type`");
            Vector<String> v = new Vector<>();
            v.add("All");
            while (rs.next()) {
                v.add(rs.getString("type"));
                EmployeeMap.put(rs.getString("type"), rs.getString("id"));
            }
            jComboBox6.setModel(new DefaultComboBoxModel(v));
            jComboBox8.setModel(new DefaultComboBoxModel(v));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGender() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `gender`");
            Vector<String> v = new Vector<>();
            v.add("Select");
            while (rs.next()) {
                v.add(rs.getString("name"));
                genderMap.put(rs.getString("name"), rs.getString("id"));
            }
            jComboBox1.setModel(new DefaultComboBoxModel(v));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUserType() {
        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `user_type`");
            Vector<String> v = new Vector<>();
            v.add("Select");
            while (rs.next()) {
                v.add(rs.getString("type"));
                TypeMap.put(rs.getString("type"), rs.getString("id"));
            }
            jComboBox2.setModel(new DefaultComboBoxModel(v));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEmployee() {
        try {
            DefaultTableModel m = (DefaultTableModel) jTable1.getModel();
            m.setRowCount(0);
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `user` INNER JOIN `gender` ON `gender`.`id` = `user`.`gender_id` "
                    + "INNER JOIN `user_type` ON `user_type`.`id` = `user`.`user_type_id` INNER JOIN `status` ON `status`.`id` = `user`.`status_id`");
            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("id"));
                v.add(rs.getString("f_name"));
                v.add(rs.getString("l_name"));
                v.add(rs.getString("mobile"));
                v.add(rs.getString("nic"));
                v.add(rs.getString("dob"));
                v.add(rs.getString("username"));
                v.add(rs.getString("password"));
                v.add(rs.getString("gender.name"));
                v.add(rs.getString("user_type.type"));
                v.add(rs.getString("status.status"));
                m.addRow(v);
            }
            jTable1.setModel(m);
            jTable6.setModel(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUers(String sort, String type) {
        try {
            DefaultTableModel m = (DefaultTableModel) jTable5.getModel();
            m.setRowCount(0);
            ResultSet rs = MySQL.executeSearch("SELECT * FROM `user_type` ORDER BY " + sort + " " + type + "");
            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("id"));
                v.add(rs.getString("type"));
                v.add(rs.getString("salary"));

                m.addRow(v);
            }
            jTable5.setModel(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean is18OrOlder(String dob) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(dob, formatter);
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        return age.getYears() < 18;
    }

    private void loadPayRoll() {

        if (jDateChooser1.getDate() != null && jDateChooser2.getDate() != null) {
            try {
                DefaultTableModel m = (DefaultTableModel) jTable2.getModel();
                m.setRowCount(0);

                // SQL query with correct condition and structure
                String query = "SELECT COUNT(DISTINCT attedance.present_day) AS dayCount,COUNT(DISTINCT leav_management.leav_date) AS leaveCount,user.id, f_name, l_name, salary, user_type.type, addtional_salary.amount "
                        + "FROM user INNER JOIN attedance ON attedance.user_id = user.id "
                        + "INNER JOIN user_type ON user_type.id = user.user_type_id "
                        + "LEFT JOIN addtional_salary ON addtional_salary.user_id = user.id "
                        + "LEFT JOIN leav_management ON leav_management.user_id = user.id ";

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date addedDate = jDateChooser2.getDate();
                Date to = jDateChooser1.getDate();

                // Add the date range filtering
                if (addedDate != null) {
                    query += "WHERE (attedance.present_day >= '" + format.format(addedDate) + "' ";

                    if (to != null) {
                        query += "AND attedance.present_day <= '" + format.format(to) + "') OR (`leav_date` >= '" + format.format(addedDate) + "' AND `leav_date` <= '" + format.format(to) + "')";
                    }
                }
//String qString = "SELECT COUNT(leav_management.leav_date) AS leaveCount FROM WHERE `lev_date` >= '"+format.format(addedDate)+"' AND `lev_date` <= '" + format.format(to) + "' ";
                // Add filtering by user type if needed
                if (jComboBox8.getSelectedIndex() > 0) {
                    query += "AND user_type.type = '" + String.valueOf(jComboBox8.getSelectedItem()) + "' ";
                }

                // Add GROUP BY for aggregation
                query += "GROUP BY user.id, f_name, l_name, salary, user_type.type, addtional_salary.amount ";

                // Add order by salary if needed
                String sort = String.valueOf(jComboBox5.getSelectedItem());
                if (sort == "Max Daily Amount") {
                    query += "ORDER BY salary DESC";
                } else if (sort == "Min Daily Amount") {
                    query += "ORDER BY salary ASC";
                } else if (sort == "Max Additional Amount") {
                    query += "ORDER BY amount DESC";
                } else if (sort == "Min Additional Amount") {
                    query += "ORDER BY amount ASC";
                }
                double total = 0;
                ResultSet rs = MySQL.executeSearch(query);
                while (rs.next()) {
                    Vector<String> v = new Vector<>();
                    v.add(rs.getString("id"));
                    v.add(rs.getString("f_name"));
                    v.add(rs.getString("l_name"));
                    v.add(rs.getString("dayCount"));
                    v.add(rs.getString("leaveCount"));

                    double Alldays = (rs.getInt("dayCount") + Double.parseDouble(rs.getString("leaveCount")) / 2);
                    v.add(String.valueOf(Alldays));
                    v.add(rs.getString("salary"));
                    v.add(rs.getString("type"));
                    v.add(rs.getString("amount"));

                    // Calculate fullAmount
                    Double fullAmount = Alldays * Double.parseDouble(rs.getString("salary"));
                    if (rs.getString("amount") != null) {
                        fullAmount += Double.parseDouble(rs.getString("amount"));
                    }
                    total += fullAmount;
                    v.add(String.valueOf(fullAmount));
                    m.addRow(v);
                }
                jLabel16.setText(String.valueOf(total));
                jTable2.setModel(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
//            System.out.println("Please select dates");
            JOptionPane.showMessageDialog(this, "Please select dates", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadRollManagement() {
        try {
            DefaultTableModel m = (DefaultTableModel) jTable3.getModel();
            m.setRowCount(0);
            String query = "SELECT * FROM `user` INNER JOIN `user_type` ON `user_type`.`id` = `user`.`user_type_id`"
                    + "INNER JOIN `gender` ON `gender`.`id` = `user`.`gender_id`"
                    + " INNER JOIN `status` ON `status`.`id` = `user`.`status_id`";
            if (jComboBox6.getSelectedIndex() > 0) {
                query += "WHERE `user_type`.`type` = '" + jComboBox6.getSelectedItem() + "'";
            }

            ResultSet rs = MySQL.executeSearch(query);
            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("id"));
                v.add(rs.getString("f_name"));
                v.add(rs.getString("l_name"));
                v.add(rs.getString("mobile"));
                v.add(rs.getString("gender.name"));
                v.add(rs.getString("type"));
                v.add(rs.getString("registered_date"));
                v.add(rs.getString("status"));
                m.addRow(v);
            }
            jTable3.setModel(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadleaveManagement() {
        try {
            DefaultTableModel m = (DefaultTableModel) jTable4.getModel();
            m.setRowCount(0);
            String query = "SELECT * FROM `user` INNER JOIN `user_type` ON `user_type`.`id` = `user`.`user_type_id` "
                    + "INNER JOIN `status` ON `status`.`id` = `user`.`status_id`"
                    + "INNER JOIN `leav_management` ON `leav_management`.`user_id` = `user`.`id`";

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date addedDate = jDateChooser5.getDate();
            Date to = jDateChooser6.getDate();

            // Add the date range filtering
            if (addedDate != null) {
                query += "WHERE leav_management.leav_date >= '" + format.format(addedDate) + "' ";

                if (to != null) {
                    query += "AND leav_management.leav_date <= '" + format.format(to) + "' ";
                }
            }
            String sort = String.valueOf(jComboBox3.getSelectedItem());
            if (sort.equals("ID ASC")) {
                System.out.println("sss");
                query += "ORDER BY `leav_management`.`id` ASC";
            } else if (sort.equals("ID DESC")) {
                query += "ORDER BY `leav_management`.`id` DESC";
            } else if (sort.equals("User ID ASC")) {
                query += "ORDER BY `user`.`id` ASC";
            } else if (sort.equals("User ID ASC")) {
                query += "ORDER BY `user`.`id` DESC";
            } else if (sort.equals("Name ASC")) {
                query += "ORDER BY `user`.`f_name` ASC";
            } else if (sort.equals("Name DESC")) {
                query += "ORDER BY `user`.`f_name` DESC";
            } else if (sort.equals("Time ASC")) {
                query += "ORDER BY `leav_management`.`leav_date` ASC";
            } else if (sort.equals("Time DESC")) {
                query += "ORDER BY `leav_management`.`leav_date` DESC";
            }

            ResultSet rs = MySQL.executeSearch(query);
            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("leav_management.id"));
                v.add(rs.getString("user.id"));
                v.add(rs.getString("f_name") + " " + rs.getString("l_name"));
                v.add(rs.getString("user_type.type"));
                v.add(rs.getString("leav_date"));
                v.add(rs.getString("leav_time"));
                v.add(rs.getString("reson"));
                v.add(rs.getString("status.status"));
                m.addRow(v);
            }
            jTable4.setModel(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        timePicker1 = new com.raven.swing.TimePicker();
        modelPieChart1 = new javaswingdev.chart.ModelPieChart();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel14 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        pieChart1 = new javaswingdev.chart.PieChart();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        calendarCustom1 = new main.CalendarCustom();
        polarAreaChart = new chart.PolarAreaChart();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jLabel56 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel31 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox<>();
        jTextField8 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel23 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel51 = new javax.swing.JLabel();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jDateChooser6 = new com.toedter.calendar.JDateChooser();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jButton15 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();

        timePicker1.set24hourMode(true);
        timePicker1.setDisplayText(jFormattedTextField1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel17.setBackground(new java.awt.Color(153, 204, 255));

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel43.setText("Total Employee");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 0, 153));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("07");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addGap(34, 34, 34))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel18.setBackground(new java.awt.Color(255, 204, 204));

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 102, 102));
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel52.setText("07");

        jLabel48.setBackground(new java.awt.Color(0, 153, 204));
        jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel48.setText("Now Joing");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addGap(34, 34, 34))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel19.setBackground(new java.awt.Color(153, 255, 204));

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 102, 102));
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel53.setText("07");

        jLabel49.setBackground(new java.awt.Color(0, 153, 204));
        jLabel49.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel49.setText("Today's Attendance");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel54.setText("Employee Type");

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel55.setText("Total Employee Type Salary");

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "User Type", "Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable7.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(jTable7);

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel56.setText("Today Attendance ");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel54)
                        .addGap(406, 406, 406)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(polarAreaChart, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel55)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pieChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(calendarCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 629, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel54)
                            .addComponent(jLabel55))
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(polarAreaChart, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(pieChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(calendarCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel56)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))))
        );

        jTabbedPane1.addTab("DashBoard", jPanel14);

        jLabel2.setText("First Name");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Last Name");

        jLabel4.setText("Mobile");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel5.setText("Username");

        jLabel6.setText("Password");

        jLabel7.setText("Gender");

        jLabel8.setText("User Type");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setBackground(new java.awt.Color(255, 153, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 153, 51));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel9.setText("Date of Birth");

        jLabel20.setText("Nic");

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Employee Registration");

        jButton9.setBackground(new java.awt.Color(255, 153, 51));
        jButton9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Update");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jDateChooser3.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel20)))
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8))
                                    .addGap(27, 27, 27)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jComboBox1, 0, 220, Short.MAX_VALUE)
                                        .addComponent(jComboBox2, 0, 220, Short.MAX_VALUE))))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(40, 40, 40)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(183, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "First Name", "Last Name", "Mobile", "NIC", "Date of Birth", "Username", "Password", "Gender", "User Type", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1364, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 785, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Employee", jPanel1);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Payroll Management");

        jLabel30.setText("Sort By");

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID ASC", "ID DESC", "MIN Salary amount", "MAX Salary amount", "User Type ASC", "User Type DESC" }));
        jComboBox7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox7ItemStateChanged(evt);
            }
        });

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Employee Type", "Daily Salary Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable5);

        jLabel31.setText("Employee Type");

        jLabel32.setText("Daily Salary Amount");

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jButton10.setText("Update");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("Add");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1577, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel32)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95)
                        .addComponent(jButton11)
                        .addGap(18, 18, 18)
                        .addComponent(jButton10)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel31)
                                    .addComponent(jLabel32)
                                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel30)
                                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "First Name", "Last Name", "Present Days", "Leave Days", "All Days", "Daily Amount", "Employee Type", "Additional amount", "Total Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel11.setText("Employee Roll");

        jLabel12.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel12.setText("From");

        jLabel13.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel13.setText("To");

        jButton4.setText("Search");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jComboBox5.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Max Daily Amount", "Min Daily Amount", "Max Additional Amount", "Min Additional Amount" }));
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel14.setText("Sort By");

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox8ItemStateChanged(evt);
            }
        });

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        jLabel15.setText("Total Monthly Amount");

        jButton5.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jButton5.setText("Save");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("0.00");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(154, 154, 154)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel12)
                        .addGap(24, 24, 24)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButton4)
                        .addGap(405, 405, 405)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1582, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel12)
                        .addComponent(jLabel13)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Payroll Management", jPanel2);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setText("Role Management");

        jLabel18.setText("Employee Roll");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox6ItemStateChanged(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "First Name", "Last Name", "Mobile", "gender", "Type", "Regisrate Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1415, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(255, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Roll Management", jPanel3);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "User ID", "Name", "User Type", "Date", "Time", "Reason", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.getTableHeader().setReorderingAllowed(false);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(6).setResizable(false);
        }

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("Sort By");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID ASC", "ID DESC", "User ID ASC", "User ID DESC", "Name ASC", "Name DESC", "Time ASC", "Time DESC" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("To");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setText("From");

        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton8.setText("Sort");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel27.setText("Leave Management");

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton6.setText("Add");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton7.setText("Update");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Time");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Reason");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("User ID");

        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT))));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Date");

        jLabel28.setText("Name");

        jLabel29.setText("User Type");

        jButton12.setText("Search");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Clear");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText("...");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jDateChooser4.setDateFormatString("yyyy-MM-dd");

        jLabel51.setText("....");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1450, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton13)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel21)
                            .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel29))
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(3, 3, 3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel51)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jButton14)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDateChooser5.setDateFormatString("yyyy-MM-dd");

        jDateChooser6.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1476, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 1482, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(193, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 910, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 90, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Leave", jPanel4);

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel33.setText("User ID Cards");

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "First Name", "Last Name", "Mobile", "NIC", "Date of Birth", "Username", "Password", "Gender", "User Type", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.getTableHeader().setReorderingAllowed(false);
        jTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable6MouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTable6);

        jButton15.setText("Print");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel40.setText("HomeOwners Hardware");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel42.setText("xxxxxxxxxxxxxxxxxxxxxxxxx");

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/profile-user (1).png"))); // NOI18N
        jLabel34.setOpaque(true);

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel36.setText("User Type");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel35.setText("Name:");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel41.setText("xxxxxxxxx");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel44.setText("xxxx-xx-xx");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel37.setText("Date of Birth:");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel38.setText("NIC");

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabel39.setText("User ID :");

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel46.setText("xxxxxxxxxxxxxxxxxx");

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel45.setText("xxxxxxxxxxxx");

        jLabel47.setBackground(new java.awt.Color(102, 255, 102));
        jLabel47.setFont(new java.awt.Font("SriLanka", 0, 36)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("444445522");
        jLabel47.setOpaque(true);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel38)
                            .addComponent(jLabel37)
                            .addComponent(jLabel36)
                            .addComponent(jLabel35))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel40)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(jLabel42))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(jLabel41))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(jLabel44))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(jLabel45))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel39)
                            .addComponent(jLabel46))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(76, 76, 76))))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel33)
                        .addGap(21, 21, 21)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 45, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("User ID", jPanel12);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1731, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String fname = jTextField1.getText();
        String lname = jTextField2.getText();
        String mobile = jTextField3.getText();
        String username = jTextField4.getText();
        String password = String.valueOf(jPasswordField1.getPassword());
        String gender = String.valueOf(jComboBox1.getSelectedItem());
        String type = String.valueOf(jComboBox2.getSelectedItem());
        String nic = jTextField5.getText();
        //  String dob = String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser3.getDate()));  
        Date dob = jDateChooser3.getDate();
        if (fname.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter employee first name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (fname.length() > 45) {
            JOptionPane.showMessageDialog(this, "Employee first name should be less than 45 charactors", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (lname.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter employee last name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (lname.length() > 45) {
            JOptionPane.showMessageDialog(this, "Employee last name should be less than 45 charactors", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (mobile.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter employee mobile number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (!mobile.matches("^07[01245678]{1}[0-9]{7}$")) {
            JOptionPane.showMessageDialog(this, "Mobile number must have 10 charactors", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (nic.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter employee nic", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (nic.length() < 9 || nic.length() > 20) {
            JOptionPane.showMessageDialog(this, "Invaild Nic Number", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (username.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter employee username", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (password.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter employee password", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (password.length() < 8 || password.length() > 20) {
            JOptionPane.showMessageDialog(this, "Password must be more than 8 characters and less than 20 characters", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (dob == null) {
            JOptionPane.showMessageDialog(this, "Please enter employee Date of Birth", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (is18OrOlder(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(dob)))) {
            JOptionPane.showMessageDialog(this, "You are under 18 years of age", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (gender.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select employee gender type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (type.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select employee type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime = format.format(date);

            try {
                ResultSet rs = MySQL.executeSearch("SELECT * FROM `user` WHERE `username` = '" + username + "' AND `mobile` = '" + mobile + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "User has already registrated", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    long id = System.currentTimeMillis();
                    MySQL.executeIUD("INSERT INTO `user`(`id`,`username`,`password`,f_name,`l_name`,`mobile`,`registered_date`,`dob`,`nic`,`gender_id`,`user_type_id`,`status_id`) VALUES"
                            + "('" + id + "','" + username + "','" + password + "','" + fname + "','" + lname + "','" + mobile + "','" + dateTime + "','" + format.format(dob) + "','" + nic + "','" + genderMap.get(gender) + "','" + TypeMap.get(type) + "','1')");
                    JOptionPane.showMessageDialog(this, "Employee Registration Sucessfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEmployee();
                    clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 1) {
            int selectRow = jTable1.getSelectedRow();
            jTextField1.setText(String.valueOf(jTable1.getValueAt(selectRow, 1)));
            jTextField2.setText(String.valueOf(jTable1.getValueAt(selectRow, 2)));
            jTextField3.setText(String.valueOf(jTable1.getValueAt(selectRow, 3)));
            jTextField5.setText(String.valueOf(jTable1.getValueAt(selectRow, 4)));
            jTextField4.setText(String.valueOf(jTable1.getValueAt(selectRow, 6)));
            jComboBox1.setSelectedItem(String.valueOf(jTable1.getValueAt(selectRow, 8)));
            jPasswordField1.setText(String.valueOf(jTable1.getValueAt(selectRow, 7)));
            jComboBox2.setEnabled(false);
            try {
                jDateChooser3.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(jTable1.getValueAt(selectRow, 5))));
            } catch (ParseException ex) {
                ex.printStackTrace();
                //Logger.getLogger(HumanResourse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (evt.getClickCount() == 2) {
            int row = jTable1.getSelectedRow();
            SubFrame sf = new SubFrame(this, true, String.valueOf(jTable1.getValueAt(row, 0)), String.valueOf(jTable1.getValueAt(row, 4)));
            sf.setVisible(true);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        String userType = jTextField7.getText();
        String amount = jFormattedTextField2.getText();
        if (userType.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please enter User type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (amount.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter salary amount", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                ResultSet rs = MySQL.executeSearch("SELECT * FROM `user_type` WHERE `type` = '" + userType + "' AND `salary` = '" + amount + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "No updated", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    MySQL.executeIUD("UPDATE `user_type` SET `salary` = '" + amount + "' WHERE `type` = '" + userType + "'");
                    userTypeClear();
                    loadUers("id", "ASC");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (jTable5.getSelectedRowCount() == 1) {
            String userType = jTextField7.getText();
            String amount = jFormattedTextField2.getText();
            if (userType.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter User type", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (userType.length() > 45) {
                JOptionPane.showMessageDialog(this, "Invaild User type", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (amount.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter salary amount", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    ResultSet rs = MySQL.executeSearch("SELECT * FROM `user_type` WHERE `type` = '" + userType + "'");
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, userType + " has already exists", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        MySQL.executeIUD("INSERT INTO `user_type` (`type`,`salary`) VALUES('" + userType + "','" + Double.parseDouble(amount) + "')");
                        userTypeClear();
                        loadUers("id", "ASC");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 1) {
            jTextField7.setText(String.valueOf(jTable5.getValueAt(jTable5.getSelectedRow(), 1)));
            jFormattedTextField2.setText(String.valueOf(jTable5.getValueAt(jTable5.getSelectedRow(), 2)));
            jTextField7.setEditable(false);
            jButton11.setEnabled(false);
        }
    }//GEN-LAST:event_jTable5MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        userTypeClear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox7ItemStateChanged
        // TODO add your handling code here:
        sort();
    }//GEN-LAST:event_jComboBox7ItemStateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        loadPayRoll();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox8ItemStateChanged
        // TODO add your handling code here:
        loadPayRoll();
    }//GEN-LAST:event_jComboBox8ItemStateChanged

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        int selectRow = jTable1.getSelectedRow();
        if (selectRow != -1) {

            String fname = jTextField1.getText();
            String lname = jTextField2.getText();
            String mobile = jTextField3.getText();
            String username = jTextField4.getText();
            String password = String.valueOf(jPasswordField1.getPassword());
            String gender = String.valueOf(jComboBox1.getSelectedItem());
            String type = String.valueOf(jComboBox2.getSelectedItem());
            String nic = jTextField5.getText();

            Date dob = jDateChooser3.getDate();
            if (fname.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter employee first name", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (fname.length() > 45) {
                JOptionPane.showMessageDialog(this, "Employee first name should be less than 45 charactors", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (lname.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter employee last name", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (lname.length() > 45) {
                JOptionPane.showMessageDialog(this, "Employee last name should be less than 45 charactors", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (mobile.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter employee mobile number", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!mobile.matches("^07[01245678]{1}[0-9]{7}$")) {
                JOptionPane.showMessageDialog(this, "Mobile number must have 10 charactors", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (nic.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter employee nic", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (nic.length() < 9 || nic.length() > 20) {
                JOptionPane.showMessageDialog(this, "Invaild Nic Number", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (username.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter employee username", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (password.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter employee password", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (password.length() < 8 || password.length() > 20) {
                JOptionPane.showMessageDialog(this, "Password must be more than 8 characters and less than 20 characters", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (dob == null) {
                JOptionPane.showMessageDialog(this, "Please enter employee Date of Birth", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (is18OrOlder(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(dob)))) {
                JOptionPane.showMessageDialog(this, "You are under 18 years of age", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (gender.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please select employee gender type", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    boolean canUpdate = false;
                    ResultSet rs = MySQL.executeSearch("SELECT * FROM `user` WHERE `mobile` = '" + mobile + "' OR `nic`= '" + nic + "'");
                    if (rs.next()) {
                        if (!rs.getString("username").equals(username)) {
                            JOptionPane.showMessageDialog(this, "This Mobile number or NIC already used", "Warrning", JOptionPane.WARNING_MESSAGE);
                        } else {
                            canUpdate = true;
                        }
                    } else {

                        canUpdate = true;
                    }
                    //Update Employee Table

                    if (canUpdate) {
                        MySQL.executeIUD("UPDATE `user` SET `f_name` = '" + fname + "',`l_name` = '" + lname + "',`mobile` = '" + mobile + "',`nic` = '" + nic + "',`password` = '" + password + "',`gender_id` = '" + genderMap.get(gender) + "',`dob` = '" + format.format(jDateChooser3.getDate()) + "'  WHERE `username` = '" + username + "'");

                        JOptionPane.showMessageDialog(this, "Update Sucessfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadEmployee();

                        jTextField4.setEditable(true);
                        jButton1.setEnabled(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select Update row", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jComboBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox6ItemStateChanged
        // TODO add your handling code here:
        loadRollManagement();
    }//GEN-LAST:event_jComboBox6ItemStateChanged

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
        int row = jTable3.getSelectedRow();
        if (evt.getClickCount() == 2) {
            String ID = String.valueOf(jTable3.getValueAt(row, 0));
            String fname = String.valueOf(jTable3.getValueAt(row, 1));
            String lname = String.valueOf(jTable3.getValueAt(row, 2));
            String mobile = String.valueOf(jTable3.getValueAt(row, 3));
            String type = String.valueOf(jTable3.getValueAt(row, 5));
            String date = String.valueOf(jTable3.getValueAt(row, 6));
            String status = String.valueOf(jTable3.getValueAt(row, 7));
            RoleUpdate ru = new RoleUpdate(this, true, ID, fname, lname, mobile, type, date, status);
            ru.setVisible(true);
            loadRollManagement();
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged
        // TODO add your handling code here:
        loadPayRoll();
    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        String userId = jTextField6.getText();
        if (userId.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter User ID", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                ResultSet rs = MySQL.executeSearch("SELECT * FROM `user` INNER JOIN `user_type` ON `user_type`.`id` = `user`.`user_type_id` WHERE `user`.`id` = '" + userId + "'");
                if (rs.next()) {
                    jLabel28.setText(rs.getString("f_name") + " " + rs.getString("l_name"));
                    jLabel29.setText(rs.getString("user_type.type"));
                    jButton12.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Invaild User ID", "Warning", JOptionPane.WARNING_MESSAGE);
                    jTextField6.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
        loadleaveManagement();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        loadleaveManagement();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        loadRollManagement();
        double totalAmount = Double.parseDouble(jLabel16.getText());
        Date fromDate = jDateChooser1.getDate();
        Date toDate = jDateChooser2.getDate();
        if (totalAmount <= 0) {
            JOptionPane.showMessageDialog(this, "Please find out the dates required to process the monthly salary !", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateTime = format.format(date);

            try {
                ResultSet rs = MySQL.executeSearch("SELECT * FROM `salary_history` WHERE `to` >= '" + format.format(fromDate) + "' AND `from` <= '" + format.format(toDate) + "' ");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "You have previously calculated employee salaries with these dates", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    MySQL.executeIUD("INSERT INTO `salary_history` (`id`,`employeePayment`,`date_time`,`to`,`from`) VALUES('" + jTextField8.getText() + "','" + totalAmount + "','" + dateTime + "','" + format.format(toDate) + "','" + format.format(fromDate) + "')");

                    for (int i = 0; i < jTable2.getRowCount(); i++) {
                        String days = String.valueOf(jTable2.getValueAt(i, 3));
                        String totalsalary = String.valueOf(jTable2.getValueAt(i, 7));
                        String type = String.valueOf(jTable2.getValueAt(i, 5));

                        MySQL.executeIUD("INSERT INTO `salary_history_item` (`present_days`,`total_salary`,`user_id`,`salary_history_id`) VALUES('" + days + "','" + totalsalary + "','" + EmployeeMap.get(type) + "','" + jTextField8.getText() + "')");
                        genareteSalaryId();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        timePicker1.showPopup(this, 100, 100);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
        int row = jTable2.getSelectedRow();
        String name = (String.valueOf(jTable2.getValueAt(row, 1)) + "" + (String.valueOf(jTable2.getValueAt(row, 2))));
        String Id = String.valueOf(jTable2.getValueAt(row, 0));
        String totalAmount = String.valueOf(jTable2.getValueAt(row, 9));
        String additionalAmount = String.valueOf(jTable2.getValueAt(row, 8));
        AdditionalAmount am = new AdditionalAmount(this, true, name, Id, totalAmount, additionalAmount);
        am.setVisible(true);
        loadPayRoll();
    }//GEN-LAST:event_jTable2MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        String userId = jTextField6.getText();
        Date leav_date = jDateChooser4.getDate();
        String leav_time = jFormattedTextField1.getText();
        String reason = jTextArea1.getText();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (userId.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter User ID", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (leav_date == null) {
            JOptionPane.showMessageDialog(this, "Please enter Leave Date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (leav_time.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter Leave Time", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (reason.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter Leave Reason", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                ResultSet rs = MySQL.executeSearch("SELECT * FROM `leav_management` WHERE `leav_date` = '" + format.format(leav_date) + "' AND `user_id` = '" + userId + "'");
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, userId + " have taken a leave today", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    MySQL.executeIUD("INSERT INTO `leav_management` (`leav_date`,`leav_time`,`reson`,`user_id`) VALUES('" + format.format(leav_date) + "','" + leav_time + "','" + reason + "','" + userId + "')");
                    JOptionPane.showMessageDialog(this, "Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadleaveManagement();
                    ClearleaveManagement();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable6MouseClicked
        // TODO add your handling code here:
        int row = jTable6.getSelectedRow();
        if (evt.getClickCount() == 2) {
            jLabel42.setText(String.valueOf(jTable6.getValueAt(row, 1) + " " + jTable6.getValueAt(row, 2)));
            jLabel41.setText(String.valueOf(jTable6.getValueAt(row, 9)));
            jLabel44.setText(String.valueOf(jTable6.getValueAt(row, 5)));
            jLabel45.setText(String.valueOf(jTable6.getValueAt(row, 4)));
            jLabel46.setText(String.valueOf(jTable6.getValueAt(row,0)));
            try {
                // Generate the barcode
                Linear barcode = new Linear();
                barcode.setType(Linear.CODE128B);
                barcode.setData(String.valueOf(jTable6.getValueAt(row, 0)));
                barcode.setI(11.0f);

                // Define file name and path
                String fname = String.valueOf(jTable6.getValueAt(row, 0));
                String filePath = "D:\\" + fname + ".png";

                // Render the barcode as a PNG file
                barcode.renderBarcode(filePath);

                // Load the image file into a JLabel
                ImageIcon barcodeIcon = new ImageIcon(filePath); // Load the image
                jLabel47.setIcon(barcodeIcon); // Set it as an icon for the JLabel
//
                String profileImagePath = "Employee_NICimages/ProfileImage-" + fname + ".png";

                ImageIcon imageIcon = new ImageIcon(profileImagePath);
                Image image = imageIcon.getImage(); // Get the image

                if (image.getWidth(null) == -1 || image.getHeight(null) == -1) {
                    //System.out.println("Image not found or failed to load.");
                    jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/profile-user (1).png")));
                } else {
                    //System.out.println("Image loaded successfully.");
                    Image resizedImage = image.getScaledInstance(jLabel34.getWidth(), jLabel34.getHeight(), Image.SCALE_SMOOTH); // Resize
                    imageIcon = new ImageIcon(resizedImage); // Update the ImageIcon
                    jLabel34.setIcon(imageIcon);
                }

            } catch (Exception e) {
                e.printStackTrace(); // Print the exception for debugging
            }
        }
    }//GEN-LAST:event_jTable6MouseClicked

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        String userId = jTextField6.getText();
        Date leav_date = jDateChooser4.getDate();
        String leav_time = jFormattedTextField1.getText();
        String reason = jTextArea1.getText();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (userId.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter User ID", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (leav_date == null) {
            JOptionPane.showMessageDialog(this, "Please enter Leave Date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (leav_time.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter Leave Time", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (reason.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter Leave Reason", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                MySQL.executeIUD("UPDATE `leav_management` SET `reson` = '" + reason + "' WHERE `id` = '" + jLabel51.getText() + "'");
                JOptionPane.showMessageDialog(this, "Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadleaveManagement();
                ClearleaveManagement();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        ClearleaveManagement();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        int selectRow = jTable4.getSelectedRow();
        if (evt.getClickCount() == 2) {
            jTextField6.setText(String.valueOf(jTable4.getValueAt(selectRow, 1)));
            jLabel28.setText(String.valueOf(jTable4.getValueAt(selectRow, 2)));
            jLabel29.setText(String.valueOf(jTable4.getValueAt(selectRow, 3)));

            try {
                jDateChooser4.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(jTable4.getValueAt(selectRow, 4))));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            jFormattedTextField1.setText(String.valueOf(jTable4.getValueAt(selectRow, 5)));
            jTextArea1.setText(String.valueOf(jTable4.getValueAt(selectRow, 6)));
            jButton6.setEnabled(false);
            jLabel51.setText((String.valueOf(jTable4.getValueAt(selectRow, 0))));
            jTextField6.setEditable(false);
            jDateChooser4.setEnabled(false);
            jFormattedTextField1.setEditable(false);
        }

    }//GEN-LAST:event_jTable4MouseClicked

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HumanResourse().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private main.CalendarCustom calendarCustom1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private com.toedter.calendar.JDateChooser jDateChooser6;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javaswingdev.chart.ModelPieChart modelPieChart1;
    private javaswingdev.chart.PieChart pieChart1;
    private chart.PolarAreaChart polarAreaChart;
    private com.raven.swing.TimePicker timePicker1;
    // End of variables declaration//GEN-END:variables
private void clear() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jPasswordField1.setText("");
        jTextField5.setText("");
        jComboBox1.setSelectedIndex(0);
        jDateChooser5.setDate(null);
        jComboBox2.setSelectedIndex(0);
        jComboBox2.setEnabled(true);
    }

    private void userTypeClear() {
        jTextField7.setText("");
        jFormattedTextField2.setText("");
        jButton11.setEnabled(true);
        jTextField7.setEditable(true);
    }

    private void sort() {
        int sort = jComboBox7.getSelectedIndex();
        if (sort == 0) {
            loadUers("id", "ASC");
        } else if (sort == 1) {
            loadUers("id", "DESC");
        } else if (sort == 2) {
            loadUers("salary", "ASC");
        } else if (sort == 3) {
            loadUers("salary", "DESC");
        } else if (sort == 4) {
            loadUers("type", "ASC");
        } else {
            loadUers("type", "DESC");
        }
    }

    private void ClearleaveManagement() {
        jTextField6.setText("");
        jButton12.setEnabled(true);
        jLabel28.setText("Name");
        jLabel29.setText("User Type");
        jDateChooser4.setDate(null);
        jTextArea1.setText("");
        jTextField6.setEditable(true);
        jDateChooser4.setEnabled(true);
        jFormattedTextField1.setEditable(true);
        jLabel51.setText("....");
        jButton6.setEnabled(true);

    }
}
