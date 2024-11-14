/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import chart.ModelPolarAreaChart;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Vector;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.MySQL;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author ADMIN
 */
public class SalesAndMarketing extends javax.swing.JFrame {

    private String meetingName;

    private double jan;
    private double feb;
    private double mar;
    private double apr;
    private double may;
    private double jun;
    private double jul;
    private double aug;
    private double sept;
    private double oct;
    private double nov;
    private double dec;

    /**
     * Creates new form SalesAndMarketing
     */
    public SalesAndMarketing() {
        initComponents();
        init();
        monthAnalysis();
        showLineChart();
        countCustomers();
        countActiveStock();
        countDeactiveStock();
        topCustomer();
        topSellingProduct();
        revenueCal();
        revYear();
//        jLabel3.setText(Employee);

        jComboBox1.setSelectedIndex(2);

        polarAreaChart1.addItem(new ModelPolarAreaChart(new Color(255, 51, 0), "Below 18", 40));
        polarAreaChart1.addItem(new ModelPolarAreaChart(new Color(255, 165, 0), "19-25", 30));
        polarAreaChart1.addItem(new ModelPolarAreaChart(new Color(255, 102, 51), "26-35", 30));
        polarAreaChart1.addItem(new ModelPolarAreaChart(new Color(0, 204, 204), "36-45", 30));
        polarAreaChart1.addItem(new ModelPolarAreaChart(new Color(202, 102, 202), "46-55", 30));
        polarAreaChart1.addItem(new ModelPolarAreaChart(new Color(0, 255, 0), "above 55", 30));

        polarAreaChart1.start();

    }

    private void countCustomers() {
        try {
            ResultSet customerCount = MySQL.executeSearch("SELECT COUNT(mobile) AS custmerCount FROM `customer`");
            customerCount.next();
            int count = customerCount.getInt("custmerCount");
            customerCount.close();
            jLabel9.setText(String.valueOf(count));
//            System.out.println("MyTable has " + count + " row(s).");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void countActiveStock() {
        try {
            ResultSet stock = MySQL.executeSearch("SELECT COUNT(`stock`.`id`) AS `stockCount` FROM `stock` WHERE `stock`.`qty`> '0' AND `stock`.`status_id` = '1'");
            stock.next();
            int count = stock.getInt("stockCount");
            stock.close();
            jLabel13.setText(String.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void countDeactiveStock() {
        try {
            ResultSet deactiveStock = MySQL.executeSearch("SELECT COUNT(`stock`.`id`) AS `DeactiveStockCount` FROM `stock` WHERE `stock`.`status_id` != '1'");
            deactiveStock.next();
            int count = deactiveStock.getInt("DeactiveStockCount");
            deactiveStock.close();
            jLabel17.setText(String.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void topCustomer() {

        try {
            ResultSet rs = MySQL.executeSearch("SELECT SUM(stock.selling_price * invoice_item.qty) AS total,"
                    + "customer.mobile AS mobile,COUNT(invoice.id) AS invoicecount, "
                    + "customer.f_name AS fname FROM invoice_item "
                    + "INNER JOIN invoice ON invoice_item.invoice_id = invoice.id "
                    + "INNER JOIN customer ON invoice.customer_mobile = customer.mobile "
                    + "INNER JOIN stock ON stock.id = invoice_item.stock_id "
                    + "GROUP BY customer.mobile order BY total DESC LIMIT 20");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("fname"));
                vector.add(rs.getString("mobile"));
                vector.add(rs.getString("invoicecount"));
                vector.add(rs.getString("total"));

                model.addRow(vector);

            }

            jTable1.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void topSellingProduct() {

        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT `product`.`id` AS productId, `product`.`name` AS productName, `stock`.`id` AS stockId, SUM(`invoice_item`.`qty`) AS qty FROM `product` "
                    + "INNER JOIN `stock` ON `product`.`id` = `stock`.`product_id` INNER JOIN `invoice_item` ON `invoice_item`.`stock_id` = `stock`.`id` \n"
                    + "GROUP BY `stock`.`id` order BY qty DESC LIMIT 20;");

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector vector = new Vector();
                vector.add(resultSet.getString("productId"));
                vector.add(resultSet.getString("productName"));
                vector.add(resultSet.getString("stockId"));
                vector.add(resultSet.getString("qty"));

                model.addRow(vector);
            }
            jTable2.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void revenueCal() {

        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT SUM(`grn_item`.`buying_price`*`invoice_item`.`qty`) "
                    + "AS `totalBoughtPrice`, SUM(`invoice_item`.`qty`*`stock`.`selling_price`) AS `totalSoldRevenue` "
                    + "FROM `invoice_item` INNER JOIN `stock` ON `invoice_item`.`stock_id` = `stock`.`id` "
                    + "INNER JOIN `grn_item` ON `grn_item`.`stock_id` = `stock`.`id` "
                    + "GROUP BY `grn_item`.`stock_id`");

            double revenue;

            resultSet.next();
            double totalSale = Double.parseDouble(resultSet.getString("totalSoldRevenue"));
            double totalBought = Double.parseDouble(resultSet.getString("totalBoughtPrice"));
            revenue = totalSale - totalBought;
            jLabel11.setText("LKR " + String.valueOf(revenue));

            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppointment() {
        try {
            String query = "SELECT * FROM `appointment` ";
            LocalDate date = LocalDate.now();

            if (query.contains("WHERE")) {
                query += "AND ";
            } else {
                query += "WHERE ";
            }

            if (jComboBox1.getSelectedItem() == "Today") {
                query += "`date` = '" + date + "'";

//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                String date1 = format.format(date);
            } else if (jComboBox1.getSelectedItem() == "Week") {
                //add 2 week to the current date
                LocalDate next1Week = date.plus(1, ChronoUnit.WEEKS);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                String date1 = format.format(next1Week);
                query += "`date` BETWEEN '" + date + "' AND '" + next1Week + "'";
//                System.out.println(query);
            } else if (jComboBox1.getSelectedItem() == "All") {
                query += " `status` != '2'";
            }

            String meetingName;
            if (!jTextField1.getText().isEmpty()) {
                meetingName = jTextField1.getText();
                if (query.contains("WHERE")) {
                    query += " AND ";
                } else {
                    query += "WHERE ";
                }
                query += "`title` ='" + meetingName + "'";
            }

            ResultSet resultSet = MySQL.executeSearch(query);
            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector vector = new Vector();
                vector.add(resultSet.getString("id"));
                vector.add(resultSet.getString("title"));
                vector.add(resultSet.getString("guest"));
                vector.add(resultSet.getString("date"));
                vector.add(resultSet.getString("time"));
                vector.add(resultSet.getString("description"));
                if (Integer.parseInt(resultSet.getString("status")) == 0) {
                    vector.add("Cancelled");
                } else if (Integer.parseInt(resultSet.getString("status")) == 1) {
                    vector.add("Pending");
                } else if (Integer.parseInt(resultSet.getString("status")) == 2) {
                    vector.add("Done");
                } else if (Integer.parseInt(resultSet.getString("status")) == 3) {
                    vector.add("Postponed");
                }
                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void revYear() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-");
//        System.out.println(format.format(date));
        try {
            ResultSet janRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"01%' GROUP BY `invoice`.`id`");
            
            while (janRev.next()) {                
                double soldRev = Double.parseDouble(janRev.getString("soldRev"));
                double buy = Double.parseDouble(janRev.getString("buy"));
                jan += soldRev-buy;           
                
            }
            
            ResultSet febRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"02%' GROUP BY `invoice`.`id`");
            
            while (febRev.next()) {                
                double soldRev = Double.parseDouble(febRev.getString("soldRev"));
                double buy = Double.parseDouble(febRev.getString("buy"));
                feb += soldRev-buy;           
                
            }
            
            ResultSet marRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"03%' GROUP BY `invoice`.`id`");
            
            while (marRev.next()) {                
                double soldRev = Double.parseDouble(marRev.getString("soldRev"));
                double buy = Double.parseDouble(marRev.getString("buy"));
                mar += soldRev-buy;           
                
            }
            
            ResultSet aprRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"04%' GROUP BY `invoice`.`id`");
            
            while (aprRev.next()) {                
                double soldRev = Double.parseDouble(aprRev.getString("soldRev"));
                double buy = Double.parseDouble(aprRev.getString("buy"));
                apr += soldRev-buy;           
                
            }
            
            ResultSet mayRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"05%' GROUP BY `invoice`.`id`");
            
            while (mayRev.next()) {                
                double soldRev = Double.parseDouble(mayRev.getString("soldRev"));
                double buy = Double.parseDouble(mayRev.getString("buy"));
                may += soldRev-buy;           
                
            }
            
            ResultSet junRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"06%' GROUP BY `invoice`.`id`");
            
            while (junRev.next()) {                
                double soldRev = Double.parseDouble(junRev.getString("soldRev"));
                double buy = Double.parseDouble(junRev.getString("buy"));
                jun += soldRev-buy;           
                
            }
            
            ResultSet julRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"07%' GROUP BY `invoice`.`id`");
            
            while (julRev.next()) {                
                double soldRev = Double.parseDouble(julRev.getString("soldRev"));
                double buy = Double.parseDouble(julRev.getString("buy"));
                jul += soldRev-buy;           
                
            }
            
            ResultSet augRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"08%' GROUP BY `invoice`.`id`");
            
            while (augRev.next()) {                
                double soldRev = Double.parseDouble(augRev.getString("soldRev"));
                double buy = Double.parseDouble(augRev.getString("buy"));
                aug += soldRev-buy;           
                
            }
            
            ResultSet septRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"09%' GROUP BY `invoice`.`id`");
            
            while (septRev.next()) {                
                double soldRev = Double.parseDouble(septRev.getString("soldRev"));
                double buy = Double.parseDouble(septRev.getString("buy"));
                sept += soldRev-buy;           
                
            }
            
            ResultSet octRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"10%' GROUP BY `invoice`.`id`");
            
            while (octRev.next()) {                
                double soldRev = Double.parseDouble(octRev.getString("soldRev"));
                double buy = Double.parseDouble(octRev.getString("buy"));
                oct += soldRev-buy;           
                
            }
            
            ResultSet novRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"11%' GROUP BY `invoice`.`id`");
            
            while (novRev.next()) {                
                double soldRev = Double.parseDouble(novRev.getString("soldRev"));
                double buy = Double.parseDouble(novRev.getString("buy"));
                nov += soldRev-buy;           
                
            }
            
            ResultSet decRev = MySQL.executeSearch("SELECT SUM(`stock`.`selling_price`*`invoice_item`.`qty`) AS soldRev, SUM(`grn_item`.`buying_price`* `invoice_item`.`qty`) AS buy "
                    + "FROM `stock` INNER JOIN `invoice_item` ON "
                    + "`stock`.`id` = `invoice_item`.`stock_id` INNER JOIN `grn_item` ON "
                    + "`stock`.`id` = `grn_item`.`stock_id` INNER JOIN `invoice` "
                    + "ON `invoice`.`id` = `invoice_item`.`invoice_id` WHERE invoice.date_time LIKE '"+format.format(date)+"12%' GROUP BY `invoice`.`id`");
            
            while (decRev.next()) {                
                double soldRev = Double.parseDouble(decRev.getString("soldRev"));
                double buy = Double.parseDouble(decRev.getString("buy"));
                dec += soldRev-buy;           
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showLineChart() {
        //create dataset for the graph
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(jan, "Amount", "january");
        dataset.setValue(feb, "Amount", "february");
        dataset.setValue(mar, "Amount", "march");
        dataset.setValue(apr, "Amount", "april");
        dataset.setValue(may, "Amount", "may");
        dataset.setValue(jun, "Amount", "june");
        dataset.setValue(jul, "Amount", "July");
        dataset.setValue(aug, "Amount", "August");
        dataset.setValue(sept, "Amount", "September");
        dataset.setValue(oct, "Amount", "October");
        dataset.setValue(nov, "Amount", "November");
        dataset.setValue(dec, "Amount", "December");

        //create chart
        JFreeChart linechart = ChartFactory.createLineChart("Monthly Earning", "monthly", "amount",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        //create plot object
        CategoryPlot lineCategoryPlot = linechart.getCategoryPlot();
//         lineCategoryPlot.setRangeGridlinePaint(Color.BLUE);
        lineCategoryPlot.setBackgroundPaint(Color.white);

        //create render object to change the moficy the line properties like color
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(204, 0, 51);
        lineRenderer.setSeriesPaint(0, lineChartColor);

        //create chartPanel to display chart(graph)
        ChartPanel lineChartPanel = new ChartPanel(linechart);
        jPanel17.removeAll();
        jPanel17.add(lineChartPanel, BorderLayout.CENTER);
        jPanel17.validate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();
        jPanel9 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel17 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        polarAreaChart1 = new chart.PolarAreaChart();
        jPanel5 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sales and Marketing");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Homeowners");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setText("Welcome back,");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("Employee Name");

        jLabel4.setText("Here's What happening with your store today");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourcess/bellIcon.png"))); // NOI18N
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 153));

        jLabel8.setText("Total Customers");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel9.setText("Total Customers");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(204, 255, 204));

        jLabel10.setText("Total Sales Revenue");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel11.setText("Total Revenue");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(255, 153, 255));

        jLabel12.setText("Active Stock");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setText("Total Stock");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Id", "Product Name", "Stock", "Total Sales"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setFocusable(false);
        jScrollPane4.setViewportView(jTable2);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel14.setText("Top Selling Products");

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(jScrollPane1);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel19.setBackground(new java.awt.Color(255, 102, 102));

        jLabel16.setText("Deactive Stock");

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel17.setText("Total Stock");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setText("Top Customers");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Mobile", "Total Purches", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setFocusable(false);
        jTable1.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(257, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel6)
                .addGap(16, 16, 16)
                .addComponent(jScrollPane3))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 938, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(6, 6, 6))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Dashboad", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setPreferredSize(new java.awt.Dimension(200, 549));

        jButton1.setText("Sales Analysis");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Category Analysis");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(447, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel16, java.awt.BorderLayout.LINE_START);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setLayout(new java.awt.BorderLayout());
        jPanel12.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel12, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Sales perfomance", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(285, Short.MAX_VALUE)
                .addComponent(polarAreaChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 912, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(polarAreaChart1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Customer insight", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jButton4.setBackground(new java.awt.Color(255, 102, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resourcess/Add_Ico_white.png"))); // NOI18N
        jButton4.setText("New Appointment");
        jButton4.setPreferredSize(new java.awt.Dimension(128, 30));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Today", "Week", "All" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jTextField1.setToolTipText("Meeting Name");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setText("Meeting Appointments");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Meeting Name", "Meeting With", "Date", "Time", "Description", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable3);

        jLabel15.setFont(new java.awt.Font("Segoe UI Emoji", 0, 12)); // NOI18N
        jLabel15.setText("There's Your schedule appointments. Check it ,Now");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1317, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(0, 0, 0)
                .addComponent(jLabel15)
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Marketing", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        monthAnalysis();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        categoryAnalysis();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        NewAppointment na = new NewAppointment(this, rootPaneCheckingEnabled);
        na.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
        Notification notification = new Notification(this, rootPaneCheckingEnabled);
        notification.setVisible(true);
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        loadAppointment();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        // TODO add your handling code here:
        loadAppointment();
    }//GEN-LAST:event_jTextField1KeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SalesAndMarketing().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JPanel jPanel16;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private chart.PolarAreaChart polarAreaChart1;
    // End of variables declaration//GEN-END:variables

    private void monthAnalysis() {
        jPanel18.removeAll();

        MonthAnalysis ma = new MonthAnalysis();
        jPanel18.add(ma, BorderLayout.CENTER);

        SwingUtilities.updateComponentTreeUI(jPanel18);
    }

    private void categoryAnalysis() {
        jPanel18.removeAll();

        CategoryAnalysis categoryAnalysis = new CategoryAnalysis();
        jPanel18.add(categoryAnalysis, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(jPanel18);

    }

    private void init() {
        jTextField1.putClientProperty("JComponent.roundRect", true);
        jTextField1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "search by meeting name");
        jTextField1.putClientProperty(FlatClientProperties.STYLE, "margin:0,10,0,10");
        jTextField1.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        jTable1.setDefaultRenderer(Object.class, renderer);
        jTable2.setDefaultRenderer(Object.class, renderer);
        jTable3.setDefaultRenderer(Object.class, renderer);

//        LocalDate today = LocalDate.now();
//        System.out.println("Current date: " + today);
//
//        //add 2 week to the current date
//        LocalDate next2Week = today.plus(1, ChronoUnit.WEEKS);
//        System.out.println("Next week: " + next2Week);
    }
}
