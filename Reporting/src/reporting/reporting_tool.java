/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reporting;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import model.MySQL;
import net.sf.jasperreports.engine.JasperExportManager;

/**
 *
 * @author Kamaj
 */
public class reporting_tool extends javax.swing.JFrame {

    /**
     * Creates new form reporting_tool
     */
    public reporting_tool() {
        initComponents();

        loadSearchombobox();
        loadCombobox();
        loadTable();
    }

    private void loadSearchombobox() {
        String Main = String.valueOf(jComboBox1.getSelectedItem());

        if (Main == "Financial Management") {
            jComboBox2.removeAllItems();
            jComboBox2.addItem("Select");
            jComboBox2.addItem("Income Statement");
            jComboBox2.addItem("Blance Sheet");
            jComboBox2.addItem("Cash Sheet");
            jComboBox2.addItem("Expense");
        } else if (Main == "Human Resources") {
            jComboBox2.removeAllItems();
            jComboBox2.addItem("Select");
            jComboBox2.addItem("Employee List");
            jComboBox2.addItem("Monthly Payroll");
            jComboBox2.addItem("Monthly Leave");
            jComboBox2.addItem("Role Management");
        } else if (Main == "Inventory Mangement") {
            jComboBox2.removeAllItems();
            jComboBox2.addItem("Select");
            jComboBox2.addItem("GRN");
            jComboBox2.addItem("Supplier Details");
            jComboBox2.addItem("Stock Details");
            jComboBox2.addItem("Product Details");
        } else if (Main == "Salse and Marketing") {
            jComboBox2.removeAllItems();
            jComboBox2.addItem("Select");
            jComboBox2.addItem("Top Customer");
            jComboBox2.addItem("Top product");
            jComboBox2.addItem("Top Brand");
        }

    }

    private void loadCombobox() {
        String Main = String.valueOf(jComboBox1.getSelectedItem());

        if (Main == "Select") {
            jComboBox7.removeAllItems();
            jComboBox7.addItem("Select");
        } else if (Main == "Financial Management") {
            jComboBox7.removeAllItems();
            jComboBox7.addItem("Income Statement");
            jComboBox7.addItem("Blance Sheet");
            jComboBox7.addItem("Cash Sheet");
            jComboBox7.addItem("Expense");
        } else if (Main == "Human Resources") {
            jComboBox7.removeAllItems();
            jComboBox7.addItem("Employee List");
            jComboBox7.addItem("Monthly Payroll");
            jComboBox7.addItem("Monthly Leave");
            jComboBox7.addItem("Role Management");
        } else if (Main == "Inventory Mangement") {
            jComboBox7.removeAllItems();
            jComboBox7.addItem("GRN");
            jComboBox7.addItem("Supplier Details");
            jComboBox7.addItem("Stock Details");
            jComboBox7.addItem("Product Details");
        } else if (Main == "Salse and Marketing") {
            jComboBox7.removeAllItems();
            jComboBox7.addItem("Top Customer");
            jComboBox7.addItem("Top product");
            jComboBox7.addItem("Top Brand");
        }
    }

    private void loadTable() {
        Date minDate = jDateChooser2.getDate();
        Date maxDate = jDateChooser3.getDate();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate1 = (minDate != null) ? format.format(minDate) : "";
        String stringDate2 = (maxDate != null) ? format.format(maxDate) : "";

        String type = String.valueOf(jComboBox2.getSelectedItem());
        String whereClause = "";
        boolean hasCondition = false;

        if (!"Select".equals(type)) {
            whereClause += " WHERE r_type = '" + type + "'";
            hasCondition = true;
        }

        if (!stringDate1.isEmpty() && !stringDate2.isEmpty()) {
            if (hasCondition) {
                whereClause += " AND";
            } else {
                whereClause += " WHERE";
            }
            whereClause += " date >= '" + stringDate1 + "' AND date <= '" + stringDate2 + "'";
        } else if (!stringDate1.isEmpty() || !stringDate2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select both dates", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ResultSet rs = MySQL.executeSearch("SELECT * FROM report_tool" + whereClause);

            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getString("id"));
                v.add(rs.getString("r_name"));
                v.add(rs.getString("r_type"));
                v.add(rs.getString("date"));
                dtm.addRow(v);
            }

            jTable1.setModel(dtm);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox7 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jButton5 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 20)); // NOI18N
        jLabel1.setText("Reporting Tool");

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setText("Report Name");

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setText("Report Type");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Financial Management", "Human Resources", "Inventory Mangement", "Salse and Marketing" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox1MouseClicked(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComboBox1KeyReleased(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton1.setText("Generate Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton2.setText("Delete Report");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 70, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, 0, 1, Short.MAX_VALUE)
                            .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(55, 55, 55)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(321, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel4.setText("Report Filter");

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setText("Report Type");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel6.setText("Start Date");

        jDateChooser2.setDateFormatString("yyyy-MM-dd");

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel7.setText("End Date");

        jDateChooser3.setDateFormatString("yyyy-MM-dd");

        jButton5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton5.setText("Filter");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(36, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox2)
                            .addComponent(jLabel6))
                        .addGap(32, 32, 32)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Report Name", "Report Type", "Generated Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 9, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyReleased

    }//GEN-LAST:event_jComboBox1KeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        loadCombobox();
        loadSearchombobox();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String Rname = String.valueOf(jComboBox1.getSelectedItem());
        String Rtype = String.valueOf(jComboBox7.getSelectedItem());
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String StringDate = format.format(d);

        String type = String.valueOf(jComboBox7.getSelectedItem());

        String Gpath = "";
        String path = "";
        String Rpath = "";

        if (type == "Income Statement") {
            Gpath = "report/incomeStatement.jasper";
            path = "D:\\\\HomeOwners\\\\incomestatement";
            Rpath = "D:/HomeOwners/incomestatement/incomestatement" + StringDate + ".pdf";
        } else if (type == "Blance Sheet") {
            Gpath = "report/balanceSheet.jasper";
            path = "D:\\\\HomeOwners\\\\balancesheet";
            Rpath = "D:/HomeOwners/balancesheet/balancesheet" + StringDate + ".pdf";
        } else if (type == "Cash Sheet") {
            Gpath = "report/cashSheet.jasper";
            path = "D:\\\\HomeOwners\\\\cashsheet";
            Rpath = "D:/HomeOwners/cashsheet/cashsheet" + StringDate + ".pdf";
        } else if (type == "Expense") {
            Gpath = "report/expense.jasper";
            path = "D:\\\\HomeOwners\\\\expense";
            Rpath = "D:/HomeOwners/expense/expense" + StringDate + ".jasper";
        } else if (type == "Employee List") {
            Gpath = "report/employeeDetails.jasper";
            path = "D:\\\\HomeOwners\\\\employeelist";
            Rpath = "D:/HomeOwners/employeelist/employeelist" + StringDate + ".jasper";
        } else if (type == "Monthly Payroll") {
            Gpath = "report/monthlyPayroll.jasper";
            path = "D:\\\\HomeOwners\\\\monthlypayroll";
            Rpath = "D:/HomeOwners/monthlypayroll/monthlypayroll" + StringDate + ".jasper";
        } else if (type == "Monthly Leave") {
            Gpath = "report/monthlyLeave.jasper";
            path = "D:\\\\HomeOwners\\\\monthlyleave";
            Rpath = "D:/HomeOwners/monthlyleave/monthlyleave" + StringDate + ".jasper";
        } else if (type == "Role Management") {
            Gpath = "report/role.jasper";
            path = "D:\\\\HomeOwners\\\\rolemanagement";
            Rpath = "D:/HomeOwners/rolemanagement/rolemanagement" + StringDate + ".jasper";
        } else if (type == "GRN") {
            Gpath = "report/grn.jasper";
            path = "D:\\\\HomeOwners\\\\grn";
            Rpath = "D:/HomeOwners/grn/grn" + StringDate + ".jasper";
        } else if (type == "Supplier Details") {
            Gpath = "report/SupplierDetails2.jasper";
            path = "D:\\\\HomeOwners\\\\supplierdetails";
            Rpath = "D:/HomeOwners/supplierdetails/supplierdetails" + StringDate + ".jasper";
        } else if (type == "Stock Details") {
            Gpath = "report/stock.jasper";
            path = "D:\\\\HomeOwners\\\\stockdetails";
            Rpath = "D:/HomeOwners/stockdetails/stockdetails" + StringDate + ".pdf";
        } else if (type == "Product Details") {
            Gpath = "report/product.jasper";
            path = "D:\\\\HomeOwners\\\\productdetails";
            Rpath = "D:/HomeOwners/productdetails/productdetails" + StringDate + ".pdf";
        } else if (type == "Top Customer") {
            Gpath = "report/customerProfile.jasper";
            path = "D:\\\\HomeOwners\\\\topcustomer";
            Rpath = "D:/HomeOwners/topcustomer/topcustomer" + StringDate + ".pdf";
        } else if (type == "Top product") {
//            Gpath = "report/customerProfile.jasper";
            path = "D:\\\\HomeOwners\\\\topproduct";
            Rpath = "D:/HomeOwners/topproduct/topproduct" + StringDate + ".pdf";
        } else if (type == "Top Brand") {
//            Gpath = "report/customerProfile.jasper";
            path = "D:\\\\HomeOwners\\\\topbrand";
            Rpath = "D:/HomeOwners/topbrand/topbrand" + StringDate + ".pdf";
        }

        if (Rname == "Select") {
            JOptionPane.showMessageDialog(this, "Please Select Report Name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (Rtype == "") {
            JOptionPane.showMessageDialog(this, "Please Select Report Type", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                MySQL.executeIUD("INSERT INTO `report_tool` (`r_name`,`r_type`,`date`,`path`) VALUES ('" + Rname + "','" + Rtype + "','" + StringDate + "','" + path + "')");

//                ----------------------------------------------------------------------------------------------------
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/homeowners", "root", "Kamaji")) {

                    InputStream reportStream = getClass().getClassLoader().getResourceAsStream(Gpath);

                    if (reportStream == null) {
                        System.out.println("Report file not found!");
                        return;
                    }

                    HashMap<String, Object> parameters = new HashMap<>();
                    JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, connection);
                    JasperViewer.viewReport(jasperPrint, false);
                    JasperExportManager.exportReportToPdfFile(jasperPrint, Rpath);

                } catch (Exception e) {
                    e.printStackTrace();
                }

//                ----------------------------------------------------------------------------------------------------
                loadTable();
                JOptionPane.showMessageDialog(this, "Creat the Report Successfuly", "Done", JOptionPane.INFORMATION_MESSAGE);;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        int row = jTable1.getSelectedRow();
        

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please Select a Row", "Done", JOptionPane.INFORMATION_MESSAGE);
        } else {
            int row2 = jTable1.getSelectedRow();
            String id = String.valueOf(jTable1.getValueAt(row2, 0));
            
            try {

                MySQL.executeIUD("DELETE FROM `report_tool` WHERE `id` = '" + id + "'");

                loadTable();

                JOptionPane.showMessageDialog(this, "Delete Report Successfuly", "Done", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        loadTable();
        jComboBox2.removeAllItems();
        jComboBox7.removeAllItems();
        jComboBox1.setSelectedItem("Select");
        jComboBox2.addItem("Select");
        jComboBox7.addItem("Select");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseClicked

    }//GEN-LAST:event_jComboBox1MouseClicked

    private void jComboBox1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyPressed

    }//GEN-LAST:event_jComboBox1KeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {

            int row = jTable1.getSelectedRow();

            String id = String.valueOf(jTable1.getValueAt(row, 0));
            try {
                ResultSet rs = MySQL.executeSearch("SELECT * FROM `report_tool` WHERE `id` = '" + id + "'");

                if (rs.next()) {
                    String path = rs.getString("path");
                    File folder = new File(path);

                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.open(folder);
                        } catch (IOException e) {
                            System.out.println("Failed to open folder: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Desktop is not supported on this system.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No record found for selected ID");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jTable1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(reporting_tool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(reporting_tool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(reporting_tool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(reporting_tool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new reporting_tool().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox7;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
