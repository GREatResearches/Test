package fmp.runSpin.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Desktop;
import fmp.runSpinBackground.RunOnPromptDOS;
import fmp.utilities.GenerateFiles;



import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;


public class RunSpinChecker {

	private JFrame frmModelChecking;
	private JTextField textField_0;
	private JTextField textField_1;
	private JTable table;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private HashMap<String,String> propertiesList = new HashMap<String,String>();
	private JTextField result_0;
	private JTextField result_1;
	private JTextField result_2;
	private JTextField result_3;
	private JTextField result_4;
	
	JComboBox comboBox_0 = new JComboBox();
	JComboBox comboBox_1 = new JComboBox();
	JComboBox comboBox_2 = new JComboBox();
	JComboBox comboBox_3 = new JComboBox();
	JComboBox comboBox_4 = new JComboBox();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RunSpinChecker window = new RunSpinChecker();
					window.frmModelChecking.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param path 
	 * @param propertiesList2 
	 */
	public RunSpinChecker() {
		initialize("promelaCodeGenerated.pml");
	}

	public RunSpinChecker(HashMap<String,String> propertiesList, String fileName) {
		this.propertiesList = propertiesList;
		initialize(fileName);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String fileName) {
		frmModelChecking = new JFrame();
		frmModelChecking.setTitle("Model Checking Control");
		frmModelChecking.setVisible(true);
		frmModelChecking.setBounds(100, 100, 450, 493);
		frmModelChecking.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmModelChecking.getContentPane().setLayout(null);
		
		result_0 = new JTextField();
		result_0.setEditable(false);
		result_0.setBounds(220, 37, 105, 20);
		frmModelChecking.getContentPane().add(result_0);
		result_0.setColumns(10);
		
		result_1 = new JTextField();
		result_1.setEditable(false);
		result_1.setBounds(220, 124, 105, 20);
		frmModelChecking.getContentPane().add(result_1);
		result_1.setColumns(10);
		
		result_2 = new JTextField();
		result_2.setEditable(false);
		result_2.setBounds(220, 208, 105, 20);
		frmModelChecking.getContentPane().add(result_2);
		result_2.setColumns(10);
		
		result_3 = new JTextField();
		result_3.setEditable(false);
		result_3.setBounds(220, 290, 105, 20);
		frmModelChecking.getContentPane().add(result_3);
		result_3.setColumns(10);
		
		result_4 = new JTextField();
		result_4.setEditable(false);
		result_4.setBounds(222, 375, 103, 20);
		frmModelChecking.getContentPane().add(result_4);
		result_4.setColumns(10);
		
		JButton log_0 = new JButton("Log/Trail");
		log_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getLog(fileName);
			}
		});
		log_0.setEnabled(false);
		log_0.setBounds(335, 36, 89, 23);
		frmModelChecking.getContentPane().add(log_0);
		
		JButton log_1 = new JButton("Log/Trail");
		log_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getLog(fileName);
			}
		});
		log_1.setEnabled(false);
		log_1.setBounds(335, 123, 89, 23);
		frmModelChecking.getContentPane().add(log_1);
		
		JButton log_2 = new JButton("Log/Trail");
		log_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getLog(fileName);
			}
		});
		log_2.setEnabled(false);
		log_2.setBounds(335, 207, 89, 23);
		frmModelChecking.getContentPane().add(log_2);
		
		JButton log_3 = new JButton("Log/Trail");
		log_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getLog(fileName);
			}
		});
		log_3.setEnabled(false);
		log_3.setBounds(335, 289, 89, 23);
		frmModelChecking.getContentPane().add(log_3);
		
		JButton log_4 = new JButton("Log/Trail");
		log_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getLog(fileName);
			}
		});
		log_4.setEnabled(false);
		log_4.setBounds(335, 374, 89, 23);
		frmModelChecking.getContentPane().add(log_4);
		
		JButton btnNewButton_0 = new JButton("Run SPIN");
		btnNewButton_0.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RunOnPromptDOS runCommand = new RunOnPromptDOS("C:\\Spin");
				System.out.println("--> Executing analysis of the property: "+ comboBox_0.getSelectedItem());
				String runResult;
				try {
					//runResult = runCommand.runPropertyInSpin(comboBox_0.getSelectedItem().toString(), "promelaCodeGenerated.pml");
					runResult = runCommand.runPropertyInSpin(comboBox_0.getSelectedItem().toString(), fileName);
					String numberOfErros = runResult.substring(runResult.indexOf(":")+1, runResult.length());
					result_0.setText("ERRORS: "+numberOfErros);
					if(Integer.parseInt(numberOfErros.trim()) > 0)
						log_0.setEnabled(true);
					else
						log_0.setEnabled(false);
					} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_0.setBounds(113, 36, 89, 23);
		frmModelChecking.getContentPane().add(btnNewButton_0);
		
		JButton btnNewButton_1 = new JButton("Run SPIN");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RunOnPromptDOS runCommand = new RunOnPromptDOS("C:\\Spin");
				System.out.println("--> Executing analysis of the property: "+ comboBox_1.getSelectedItem());
				String runResult;
				try {
					runResult = runCommand.runPropertyInSpin(comboBox_1.getSelectedItem().toString(), fileName);
					//runResult = runCommand.runPropertyInSpin(comboBox_1.getSelectedItem().toString(), "promelaCodeGenerated.pml");
					String numberOfErros = runResult.substring(runResult.indexOf(":")+1, runResult.length());
					result_1.setText("ERRORS: "+numberOfErros);
					if(Integer.parseInt(numberOfErros.trim()) > 0)
						log_1.setEnabled(true);
					else
						log_1.setEnabled(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(113, 123, 89, 23);
		frmModelChecking.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Run SPIN");
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RunOnPromptDOS runCommand = new RunOnPromptDOS("C:\\Spin");
				System.out.println("--> Executing analysis of the property: "+ comboBox_2.getSelectedItem());
				String runResult;
				try {
					runResult = runCommand.runPropertyInSpin(comboBox_2.getSelectedItem().toString(), fileName);
					//runResult = runCommand.runPropertyInSpin(comboBox_2.getSelectedItem().toString(), "promelaCodeGenerated.pml");
					String numberOfErros = runResult.substring(runResult.indexOf(":")+1, runResult.length());
					result_2.setText("ERRORS: "+numberOfErros);
					if(Integer.parseInt(numberOfErros.trim()) > 0)
						log_2.setEnabled(true);
					else
						log_2.setEnabled(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_2.setBounds(113, 207, 89, 23);
		frmModelChecking.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Run SPIN");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RunOnPromptDOS runCommand = new RunOnPromptDOS("C:\\Spin");
				System.out.println("--> Executing analysis of the property: "+ comboBox_3.getSelectedItem());
				String runResult;
				try {
					runResult = runCommand.runPropertyInSpin(comboBox_3.getSelectedItem().toString(), fileName);
					//runResult = runCommand.runPropertyInSpin(comboBox_3.getSelectedItem().toString(), "promelaCodeGenerated.pml");
					String numberOfErros = runResult.substring(runResult.indexOf(":")+1, runResult.length());
					result_3.setText("ERRORS: "+numberOfErros);
					if(Integer.parseInt(numberOfErros.trim()) > 0)
						log_3.setEnabled(true);
					else
						log_3.setEnabled(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_3.setBounds(113, 289, 89, 23);
		frmModelChecking.getContentPane().add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Run SPIN");
		btnNewButton_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RunOnPromptDOS runCommand = new RunOnPromptDOS("C:\\Spin");
				System.out.println("--> Executing analysis of the property: "+ comboBox_4.getSelectedItem());
				String runResult;
				try {
					runResult = runCommand.runPropertyInSpin(comboBox_4.getSelectedItem().toString(), fileName);
					//runResult = runCommand.runPropertyInSpin(comboBox_4.getSelectedItem().toString(), "promelaCodeGenerated.pml");
					String numberOfErros = runResult.substring(runResult.indexOf(":")+1, runResult.length());
					result_4.setText("ERRORS: "+numberOfErros);
					if(Integer.parseInt(numberOfErros.trim()) > 0)
						log_4.setEnabled(true);
					else
						log_4.setEnabled(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_4.setBounds(113, 374, 89, 23);
		frmModelChecking.getContentPane().add(btnNewButton_4);

		JLabel lblConfigurationCorrectness = new JLabel("P1 - Configuration Correctness");
		lblConfigurationCorrectness.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblConfigurationCorrectness.setBounds(10, 11, 198, 14);
		frmModelChecking.getContentPane().add(lblConfigurationCorrectness);
		
		JLabel lblPRule = new JLabel("P2 - Rule Liveness");
		lblPRule.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPRule.setBounds(10, 98, 123, 14);
		frmModelChecking.getContentPane().add(lblPRule);
		
		JLabel lblPInterleaving = new JLabel("P3 - Interleaving Correctness");
		lblPInterleaving.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPInterleaving.setBounds(10, 182, 177, 14);
		frmModelChecking.getContentPane().add(lblPInterleaving);
		
		JLabel lblPFeaure = new JLabel("P4 - Feaure Liveness");
		lblPFeaure.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPFeaure.setBounds(10, 264, 153, 14);
		frmModelChecking.getContentPane().add(lblPFeaure);
		
		JLabel lblPVariation = new JLabel("P5 - Variation Liveness");
		lblPVariation.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPVariation.setBounds(10, 349, 153, 14);
		frmModelChecking.getContentPane().add(lblPVariation);
		
		textField_0 = new JTextField();
		textField_0.setEditable(false);
		textField_0.setBounds(20, 67, 404, 20);
		frmModelChecking.getContentPane().add(textField_0);
		textField_0.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(20, 151, 404, 20);
		frmModelChecking.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		table = new JTable();
		table.setBounds(55, 86, 108, -27);
		frmModelChecking.getContentPane().add(table);
		
		
		comboBox_0.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println(comboBox_0.getSelectedItem());
				textField_0.setText(propertiesList.get(comboBox_0.getSelectedItem().toString()));
				result_0.setText("");
				log_0.setEnabled(false);
			}
		});
		comboBox_0.setBounds(20, 37, 65, 20);
		for (String propertyKey : propertiesList.keySet()) {
			if(propertyKey.contains("pro1"))
				comboBox_0.addItem(propertyKey);
		}
		frmModelChecking.getContentPane().add(comboBox_0);
		
		comboBox_1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println(comboBox_1.getSelectedItem());
				textField_1.setText(propertiesList.get(comboBox_1.getSelectedItem().toString()));
				result_1.setText("");	
				log_1.setEnabled(false);
			}
		});
		comboBox_1.setBounds(20, 124, 65, 20);
		for (String propertyKey : propertiesList.keySet()) {
			if(propertyKey.contains("pro2"))
				comboBox_1.addItem(propertyKey);
		}
		frmModelChecking.getContentPane().add(comboBox_1);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(20, 233, 404, 20);
		frmModelChecking.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		
		comboBox_2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println(comboBox_2.getSelectedItem());
				textField_2.setText(propertiesList.get(comboBox_2.getSelectedItem().toString()));
				result_2.setText("");
				log_2.setEnabled(false);
				
			}
		});
		comboBox_2.setBounds(20, 208, 65, 20);
		for (String propertyKey : propertiesList.keySet()) {
			if(propertyKey.contains("pro3"))
				comboBox_2.addItem(propertyKey);
		}
		frmModelChecking.getContentPane().add(comboBox_2);
		
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setBounds(20, 318, 404, 20);
		frmModelChecking.getContentPane().add(textField_3);
		textField_3.setColumns(10);		
		
		
		comboBox_3.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println(comboBox_3.getSelectedItem());
				textField_3.setText(propertiesList.get(comboBox_3.getSelectedItem().toString()));
				result_3.setText("");
				log_3.setEnabled(false);
			}
		});
		comboBox_3.setBounds(20, 290, 65, 20);
		for (String propertyKey : propertiesList.keySet()) {
			if(propertyKey.contains("pro4"))
				comboBox_3.addItem(propertyKey);
		}
		frmModelChecking.getContentPane().add(comboBox_3);
		
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setBounds(20, 405, 404, 20);
		frmModelChecking.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		
		
		comboBox_4.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println(comboBox_4.getSelectedItem());
				textField_4.setText(propertiesList.get(comboBox_4.getSelectedItem().toString()));
				result_4.setText("");
				log_4.setEnabled(false);
			}
		});
		comboBox_4.setBounds(20, 374, 65, 20);
		for (String propertyKey : propertiesList.keySet()) {
			if(propertyKey.contains("pro5"))
				comboBox_4.addItem(propertyKey);
		}
		frmModelChecking.getContentPane().add(comboBox_4);
		
		JButton btnClose = new JButton("Close");
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frmModelChecking.dispose();
			}
		});
		btnClose.setBackground(Color.LIGHT_GRAY);
		btnClose.setBounds(163, 436, 89, 23);
		frmModelChecking.getContentPane().add(btnClose);
		
		
	}
	
	private void getLog(String fileName){
		RunOnPromptDOS runCommand = new RunOnPromptDOS("C:\\Spin");
		String erroReport;
		try {
			erroReport = runCommand.getReportLastRun(fileName);
			System.out.println("ERROR FOUND");
			System.out.println("## PRINTING ERROR REPORT ## ");					
			System.out.println(erroReport);
			
			GenerateFiles gen = new GenerateFiles();
			gen.generateTXTFile(erroReport,"log");
			
			if (Desktop.isDesktopSupported()) {
			    Desktop.getDesktop().edit(new File("log.txt"));
			} else {
			    // dunno, up to you to handle this
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
