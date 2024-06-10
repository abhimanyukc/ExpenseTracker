

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.abhi.expensetracker;


/**
 *
 * @author abhid
 */

//code of ExpenseTracker.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class ExpenseTracker extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField amountField;
    private JComboBox<String> categoryComboBox;
    private JTextField descriptionField;
    private JComboBox<String> categoryBox;
    private DefaultPieDataset dataset;
    private HashMap<String, Double> budgets;

    public ExpenseTracker() {
        setTitle("Expense Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        // Initialize budgets
        budgets = new HashMap<>();
        budgets.put("Food", 0.0);
        budgets.put("Transport", 0.0);
        budgets.put("Entertainment", 0.0);
        budgets.put("Utilities", 0.0);
        budgets.put("Others", 0.0);

        // Table for displaying expenses
        tableModel = new DefaultTableModel(new String[]{"Description", "Amount", "Category"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 400, 300);
        add(scrollPane);
        
        // Input fields
        descriptionField = new JTextField();
        descriptionField.setBounds(450, 20, 150, 25);
        add(descriptionField);

        amountField = new JTextField();
        amountField.setBounds(450, 60, 150, 25);
        add(amountField);

        categoryBox = new JComboBox<>(new String[]{"Food", "Transport", "Entertainment", "Others"});
        categoryBox.setBounds(450, 100, 150, 25);
        add(categoryBox);

        // Add Expense button
        JButton addButton = new JButton("Add Expense");
        addButton.setBounds(450, 140, 150, 25);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
        add(addButton);

        // Edit Expense button
        JButton editButton = new JButton("Edit Expense");
        editButton.setBounds(450, 180, 150, 25);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editExpense();
            }
        });
        add(editButton);

        // Remove Expense button
        JButton removeButton = new JButton("Remove Expense");
        removeButton.setBounds(450, 220, 150, 25);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeExpense();
            }
        });
        add(removeButton);

        // Pie chart panel
        dataset = new DefaultPieDataset();
        JFreeChart chart = ChartFactory.createPieChart("Expense Distribution", dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(20, 350, 400, 200);
        add(chartPanel);

        setLocationRelativeTo(null);
    }

    private void addExpense() {
        String description = descriptionField.getText();
        String amountText = amountField.getText();
        String category = (String) categoryBox.getSelectedItem();
        try {
            double amount = Double.parseDouble(amountText);
            tableModel.addRow(new Object[]{description, amount, category});
            updateChart(category, amount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount");
        }
    }

    private void editExpense() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String category = (String) table.getValueAt(selectedRow, 2);
            double oldAmount = (double) table.getValueAt(selectedRow, 1);
            double newAmount = Double.parseDouble(amountField.getText());
            table.setValueAt(newAmount, selectedRow, 1);
            updateChart(category, newAmount - oldAmount);
        } else {
            JOptionPane.showMessageDialog(this, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeExpense() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String category = (String) table.getValueAt(selectedRow, 2);
            double amount = (double) table.getValueAt(selectedRow, 1);
            tableModel.removeRow(selectedRow);
            updateChart(category, -amount);
        } else {
            JOptionPane.showMessageDialog(this, "No expense selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateChart(String category, double amount) {
        double budget = budgets.get(category);
        budgets.put(category, budget + amount);
        dataset.setValue(category, budget + amount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExpenseTracker().setVisible(true);
            }
        });
    }
}
