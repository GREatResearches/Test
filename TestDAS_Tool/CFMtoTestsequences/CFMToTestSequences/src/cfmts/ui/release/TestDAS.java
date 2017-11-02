package cfmts.ui.release;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import br.ufc.great.controlreport.view.MainWindow;
import cfmts.example.DFTSGraphs_Mobiline_Exp;
import cfmts.example.DFTSGraphs_SmartHome_Exp;
import fmp.CFMtoCKS.ExtractorCks;
import fmp.contextEvolModel.CKS;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.contextModel.CFM;
import fmp.contextModel.ContextFeature;
import fmp.contextModel.ContextGroup;
import fmp.contextModel.ContextGroupType;
import fmp.contextModel.ContextType;
import fmp.example.GenerateXLS_For_Exp;
import fmp.featureModel.DSPL;
import fmp.featureModel.ExcludeRule;
import fmp.featureModel.Feature;
import fmp.featureModel.FeatureGroup;
import fmp.featureModel.FeatureGroupType;
import fmp.featureModel.FeatureType;
import fmp.featureModel.RequireRule;
import fmp.mapping.Parser;
import fmp.runSpin.ui.RunSpinChecker;
import fmp.utilities.GenerateFiles;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTextField;

// TestDAS v 1.0. Release
// 19.09.2017 - a Classe TestJSON_CAFM já tem os métodos de leitura e escrita. Falta atualizar o código para:
// 1 - ler JSON, 2 - gerar XLS a partir do JSON, 3 - ler XLS, 4 - Gerar grafo, 5 - Gerar Promela, 6 - Gerar Test Sequences 
public class TestDAS {
	
	int systemId = 1;

	private JFrame frmToactivate;
	
	private HashMap <String,String> propertiesList;
	
	private CKS cks;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestDAS window = new TestDAS();
					window.frmToactivate.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestDAS() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmToactivate = new JFrame();
		frmToactivate.setTitle("TestDAS");
		frmToactivate.setBounds(100, 100, 376, 302);
		frmToactivate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmToactivate.getContentPane().setLayout(null);
		
		JButton btnModelChecking = new JButton("Model Checking");
		btnModelChecking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(systemId == 1){
					//Model Checking for Smart Phone
				}
				else if(systemId == 2){
					//Model Checking for Mobiline
					 File file = null;
					 JFileChooser chooser = new JFileChooser();
					 chooser.setDialogTitle("Open the Promela Code");
					 chooser.setCurrentDirectory(new File("C:/Spin"));
					 chooser.setFileFilter(new javax.swing.filechooser.FileFilter(){
						   public boolean accept(File f){
							      return (f.getName().endsWith(".pml")) || f.isDirectory();
							   }
							   public String getDescription(){
							       return "*.pml";
							       
							   }
							});
					 
					 int returnValue = chooser.showOpenDialog( null ) ;
					 
					 
					 
					 
					 if(returnValue == JFileChooser.APPROVE_OPTION) {
					        file = chooser.getSelectedFile() ;
					 }
					 if(file != null)
					 {
				      String filePath = file.getPath();
				      String fileName = file.getName();
				     
					  //runModelCheckingUI_Mobiline(filePath);
				      runModelCheckUI_Mobiline(fileName);
					 }
				}else if(systemId == 3){
					//Model Checking for Smart Home
				}
				
			}
		});
		
		textField = new JTextField();
		textField.setBounds(23, 37, 183, 20);
		frmToactivate.getContentPane().add(textField);
		textField.setColumns(10);
		btnModelChecking.setBounds(55, 136, 132, 23);
		frmToactivate.getContentPane().add(btnModelChecking);
		
		JButton btnCks = new JButton("CKS");
		btnCks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 File file = null;
				 JFileChooser chooser = new JFileChooser();

				 int returnValue = chooser.showOpenDialog( null ) ;
				 if(returnValue == JFileChooser.APPROVE_OPTION) {
				        file = chooser.getSelectedFile() ;
				 }
				 if(file != null)
				 {
			      String filePath = file.getPath();
			      GenerateXLS_For_Exp genXLS = new GenerateXLS_For_Exp();
				  if(systemId == 1 )
						cks = genXLS.genXMLPhone_Training(filePath+".xls");
				  if(systemId == 2)
					    cks =  genXLS.genXLSMobiline(filePath+".xls");
			      if(systemId == 3)
			    	    cks = genXLS.genXLSSmartHome(filePath+".xls");
				  JOptionPane.showMessageDialog(null, "File Generated", "InfoBox: " + "Status", JOptionPane.INFORMATION_MESSAGE);
				  System.out.println(systemId);
				 } 
			}
		});
		btnCks.setBounds(55, 68, 132, 23);
		frmToactivate.getContentPane().add(btnCks);
		
		JButton btnTestSequence = new JButton(" Test Sequence");
		btnTestSequence.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

					 File filexls = null;
					 JFileChooser chooserxls = new JFileChooser();
					
					chooserxls.setFileFilter(new javax.swing.filechooser.FileFilter(){
					   public boolean accept(File f){
						      return (f.getName().endsWith(".xls")) || f.isDirectory();
						   }
						   public String getDescription(){
						       return "*.xls";
						   }
						});
				
					chooserxls.setDialogTitle("Open the XLS with the CKS file");
					int return1 = chooserxls.showOpenDialog(null);
					 if(return1 == JFileChooser.APPROVE_OPTION) {
						 filexls = chooserxls.getSelectedFile() ;
					 }
					 
					 File file = null;
					 JFileChooser chooser = new JFileChooser();
					 chooser.setDialogTitle("Save the Test Sequence");
					 int returnValue = chooser.showOpenDialog( null ) ;
					 if(returnValue == JFileChooser.APPROVE_OPTION) {
					        file = chooser.getSelectedFile() ;
					 }
					 if(file != null)
					 {
						 String filePath = file.getPath();
						if(systemId==2){
							DFTSGraphs_Mobiline_Exp genSeqM = new DFTSGraphs_Mobiline_Exp();
							genSeqM.genTestSeq(filePath+".json");
						}else if(systemId==3){
							DFTSGraphs_SmartHome_Exp genSeqSH = new DFTSGraphs_SmartHome_Exp();
							genSeqSH.genTestSeq(filePath+".json");
						}
						JOptionPane.showMessageDialog(null, "File Generated", "InfoBox: " + "Status", JOptionPane.INFORMATION_MESSAGE);
					 }
			}
		});
		btnTestSequence.setBounds(55, 170, 132, 23);
		frmToactivate.getContentPane().add(btnTestSequence);
		
		JButton btnNewButton = new JButton("Control Report");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainWindow control = new MainWindow();
			}
		});
		btnNewButton.setBounds(57, 204, 130, 23);
		frmToactivate.getContentPane().add(btnNewButton);
		
		JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(comboBox.getSelectedItem().equals("SmartPhone - Training"))
					systemId = 1;
				else if (comboBox.getSelectedItem().equals("Mobiline"))
					systemId = 2;
				else if (comboBox.getSelectedItem().equals("SmartHome"))
					systemId = 3;
			}
		});		
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"SmartPhone - Training", "Mobiline", "SmartHome"}));
		comboBox.setBounds(29, 21, 177, 20);
		frmToactivate.getContentPane().add(comboBox);
		
		JButton btnGetPmlFile = new JButton("PML File");
		btnGetPmlFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 File filexls = null;
				 JFileChooser chooserxls = new JFileChooser();
				
				chooserxls.setFileFilter(new javax.swing.filechooser.FileFilter(){
				   public boolean accept(File f){
					      return (f.getName().endsWith(".xls")) || f.isDirectory();
					   }
					   public String getDescription(){
					       return "*.xls";
					   }
					});
			
				chooserxls.setDialogTitle("Open the XLS with the CKS file");
				int return1 = chooserxls.showOpenDialog(null);
				if(return1 == JFileChooser.APPROVE_OPTION) {
					 filexls = chooserxls.getSelectedFile() ;					 
				}
				
				File filePromela = null;
				JFileChooser chooserPromela = new JFileChooser();
				
				chooserPromela.setDialogTitle("Save the Promela File");
				chooserPromela.setCurrentDirectory(new File("C:/Spin"));
				int return2 = chooserPromela.showSaveDialog(null);
				if(return2 == JFileChooser.APPROVE_OPTION) {
					filePromela = chooserPromela.getSelectedFile() ;					 
				}
				
				if(systemId == 1){
					// SAVE Promela File for SmarthPhone 
					
				}else if(systemId == 2){
					// SAVE Promela File for Mobiline
					if(filexls != null && filePromela != null)
						genPMLFile_Mobiline(filexls.getPath(), filePromela.getPath());
					 JOptionPane.showMessageDialog(null, "File Generated. PUT it in the SPIN Folder", "InfoBox: " + "Status", JOptionPane.WARNING_MESSAGE);
					
				}else if(systemId == 3){
					// SAVE Promela File for Smart Home
					
				}
				
			}
		});
		btnGetPmlFile.setBounds(55, 102, 132, 23);
		frmToactivate.getContentPane().add(btnGetPmlFile);
		
		JButton btnUpload = new JButton("Upload");
		btnUpload.setBounds(229, 20, 89, 23);
		frmToactivate.getContentPane().add(btnUpload);
		
		
	}
	
	public void genPMLFile_Mobiline(String cksPath, String promelaCodePath){
		// Features
		Feature mobileGuide = new Feature();
		mobileGuide.setName("MobileGuide");
		
		Feature documents = new Feature();
		documents.setName("Files");
		documents.setFeatureType(FeatureType.Mandatory);
		documents.setFatherFeature(mobileGuide);
		
		Feature video = new Feature();
		video.setName("Video");
		video.setFeatureType(FeatureType.Optional);
		video.setFatherFeature(documents);
		
		Feature image = new Feature();
		image.setName("Image");
		image.setFeatureType(FeatureType.Optional);
		image.setFatherFeature(documents);
		
		Feature text = new Feature();
		text.setName("Text");
		text.setFeatureType(FeatureType.Mandatory);
		text.setFatherFeature(documents);
	
		Feature showEvents = new Feature();
		showEvents.setName("ShowEvents");
		showEvents.setFeatureType(FeatureType.Mandatory);
		showEvents.setFatherFeature(mobileGuide);
		
		Feature allEvents = new Feature();
		allEvents.setName("allEvents");
		allEvents.setFeatureType(FeatureType.Group);
		allEvents.setFatherFeature(showEvents);
		
		Feature current = new Feature();
		current.setName("current");
		current.setFeatureType(FeatureType.Group);
		current.setFatherFeature(showEvents);
		
		FeatureGroup eventsGroup = new FeatureGroup();
		eventsGroup.append(allEvents);
		eventsGroup.append(current);
		eventsGroup.setGroupType(FeatureGroupType.OR_Group);
		
		//Context Root
			ContextFeature root = new ContextFeature("Root Context");
			
			// Context Propositions
			ContextFeature isBtFull = new ContextFeature("BtFull");
			isBtFull.setContextType(ContextType.Group);
			ContextFeature isBtNormal = new ContextFeature("BtNormal");
			isBtNormal.setContextType(ContextType.Group);
			ContextFeature isBtLow = new ContextFeature("BtLow");
			isBtLow.setContextType(ContextType.Group);
			ContextFeature slowNetwork = new ContextFeature("slowNetwork");
			slowNetwork.setContextType(ContextType.Optional);		
			
			//Context Groups
			ContextGroup battery = new ContextGroup("Baterry");
			battery.setType(ContextGroupType.XOR);
			battery.append(isBtFull);
			battery.append(isBtNormal);
			battery.append(isBtLow);		
			
			ContextGroup network = new ContextGroup("Network"); // To the interleaving testing
			network.setType(ContextGroupType.NONE);
			network.append(slowNetwork);		
			
			// Adaptation Rules
			
			//Adaptation Rule isBtLow => Video off, Image off
			AdaptationRuleWithCtxFeature rule1 = new AdaptationRuleWithCtxFeature();
			LinkedList<ContextFeature> contextTrigger1 = new LinkedList<ContextFeature>();
			contextTrigger1.add(isBtLow);
			rule1.setContextRequired(contextTrigger1);
			
			LinkedList<Feature> toDeactiveFeatures1 = new LinkedList<Feature>();
			toDeactiveFeatures1.add(image);
			toDeactiveFeatures1.add(video);
			rule1.setToDeactiveFeatureList(toDeactiveFeatures1);
				
			//Adaptation Rule isBtNormal => Video off, Image on
			AdaptationRuleWithCtxFeature rule3 = new AdaptationRuleWithCtxFeature();
			LinkedList<ContextFeature> contextTrigger3 = new LinkedList<ContextFeature>();
			contextTrigger3.add(isBtNormal);		
			rule3.setContextRequired(contextTrigger3);
			
			LinkedList<Feature> toActiveFeature3 = new LinkedList<Feature>();
			toActiveFeature3.add(image);
			rule3.setToActiveFeatureList(toActiveFeature3);
			
			LinkedList<Feature> toDeactiveFeatures3 = new LinkedList<Feature>();
			toDeactiveFeatures3.add(video);
			rule3.setToDeactiveFeatureList(toDeactiveFeatures3);
			
			//Adaptation Rule isBtFull => Video on, Image on
			AdaptationRuleWithCtxFeature rule5 = new AdaptationRuleWithCtxFeature();
			LinkedList<ContextFeature> contextTrigger5 = new LinkedList<ContextFeature>();
			contextTrigger5.add(isBtFull);		
			rule5.setContextRequired(contextTrigger5);
			
			LinkedList<Feature> toActiveFeature5 = new LinkedList<Feature>();
			toActiveFeature5.add(image);
			toActiveFeature5.add(video);
			rule5.setToActiveFeatureList(toActiveFeature5);
			
			//Adaptation Rule SlowNetwork => allEvents off
			AdaptationRuleWithCtxFeature rule6 = new AdaptationRuleWithCtxFeature();
			LinkedList<ContextFeature> contextTrigger6 = new LinkedList<ContextFeature>();
			contextTrigger6.add(slowNetwork);		
			rule6.setContextRequired(contextTrigger6);
			
			LinkedList<Feature> toDeactiveFeature6 = new LinkedList<Feature>();
			toDeactiveFeature6.add(allEvents);		
			rule6.setToDeactiveFeatureList(toDeactiveFeature6);
			
			// COMO é um OR, se ele desativou o outro, automaticamente ele ativa esse
			LinkedList<Feature> toActiveFeature6 = new LinkedList<Feature>();
			toActiveFeature6.add(current);		
			rule6.setToActiveFeatureList(toActiveFeature6);
			
			//Context Model
			CFM ctxModel = new CFM();
			ctxModel.setContextRoot(root);
			ctxModel.getContextGroups().add(battery);
			ctxModel.getContextGroups().add(network);
			ctxModel.getAdaptationRules().add(rule1);
			ctxModel.getAdaptationRules().add(rule3);
			ctxModel.getAdaptationRules().add(rule5);
			ctxModel.getAdaptationRules().add(rule6);
			
			//DSPL
			DSPL mobilineDspl = new DSPL();
				//DSPL features
			mobilineDspl.getFeatures().add(text);
			mobilineDspl.getFeatures().add(video);
			mobilineDspl.getFeatures().add(image);
			mobilineDspl.getFeatures().add(documents);
			mobilineDspl.getFeatures().add(allEvents);
			mobilineDspl.getFeatures().add(current);
			mobilineDspl.getFeatures().add(showEvents);
			mobilineDspl.getFeatures().add(mobileGuide);	
				//DSPL Initial Configuration - mandatory features and from group features
			mobilineDspl.getInitialConfiguration().add(documents);
			mobilineDspl.getInitialConfiguration().add(text);
			mobilineDspl.getInitialConfiguration().add(image);
			mobilineDspl.getInitialConfiguration().add(showEvents);
			mobilineDspl.getInitialConfiguration().add(current);
			mobilineDspl.getInitialConfiguration().add(mobileGuide);
				//DSPL Feature Groups
			mobilineDspl.getFeatureGroups().add(eventsGroup);
			
			GenerateFiles gen = new GenerateFiles();
			
			ExtractorCks extractor = new ExtractorCks();
			// cksPath is the same file generate before, but it is filled out
			extractor.getContextChangesFromXLS(cksPath, cks.getContextStates());
			
			
			Parser parser = new Parser(mobilineDspl, cks);
			propertiesList = new HashMap<String,String>();
			String promelaCodeGenerated = parser.generatePromelaCodefromDSPL(propertiesList);
			//System.out.println(promelaCodeGenerated);
			GenerateFiles genFiles = new GenerateFiles();
			genFiles.generatePMLFile(promelaCodeGenerated, promelaCodePath);
	}
	
	public void runModelCheckUI_Mobiline(String fileName){
		// The file should be in the Spin folder
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//String path = "C:/promelaCodeGenerated.pml";
					if(propertiesList!= null){
						RunSpinChecker window = new RunSpinChecker(propertiesList, fileName);
					}
					else{
						JOptionPane.showMessageDialog(null, "Empty Properties List! Generate the PML File", "InfoBox: " + "Status", JOptionPane.ERROR_MESSAGE);
					}
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void runModelCheckingUI_Mobiline(String path){
		// Step 1 - User modeling in the Fixture both Feature Model and Context Model
				//--------------------------> FEATURE MODEL <-------------------/
				// Features
				Feature documents = new Feature();
				documents.setName("Documents");
				documents.setFeatureType(FeatureType.Mandatory);
				
				Feature video = new Feature();
				video.setName("Video");
				video.setFatherFeature(documents);
				video.setFeatureType(FeatureType.Group);
				
				Feature image = new Feature();
				image.setName("Image");
				image.setFatherFeature(documents);
				image.setFeatureType(FeatureType.Group);
				
				FeatureGroup documentsGroup = new FeatureGroup();
				documentsGroup.append(video);
				documentsGroup.append(image);
				documentsGroup.setGroupType(FeatureGroupType.OR_Group);
				
				Feature text = new Feature();
				text.setName("Text");
				text.setFeatureType(FeatureType.Mandatory);
				text.setFatherFeature(documents);
				
				//Dependency Rule
				//Example A require B
				Feature A = new Feature();
				A.setFeatureType(FeatureType.Optional);
				A.setName("A");
				A.setFatherFeature(documents);
				
				Feature B = new Feature();
				B.setFeatureType(FeatureType.Optional);
				B.setName("B");
				B.setFatherFeature(documents);
				
				RequireRule reqRule = new RequireRule();
				reqRule.setFeatureClaimant(A);
				LinkedList<Feature> reqFeatureList = new LinkedList<Feature>();
				reqFeatureList.add(B);
				reqRule.setRequiredFeatureList(reqFeatureList);
				
				//Exclude Rule
				//Example A excludes C 
				Feature C = new Feature();
				C.setFeatureType(FeatureType.Optional);
				C.setName("C");
				C.setFatherFeature(documents);
				
				ExcludeRule excRule = new ExcludeRule();
				excRule.setFeatureClaimant(A);
				LinkedList<Feature> excFeatureList = new LinkedList<Feature>();
				excFeatureList.add(C);
				excRule.setRequiredFeatureList(excFeatureList);		
				
				//DSPL
				DSPL mobilineDspl = new DSPL();
					//DSPL features
				mobilineDspl.getFeatures().add(text);
				mobilineDspl.getFeatures().add(video);
				mobilineDspl.getFeatures().add(image);
				mobilineDspl.getFeatures().add(documents);
				mobilineDspl.getFeatures().add(A);
				mobilineDspl.getFeatures().add(B);
				mobilineDspl.getFeatures().add(C);	
					//DSPL Initial Configuration - mandatory features and from group features
				mobilineDspl.getInitialConfiguration().add(documents);
				mobilineDspl.getInitialConfiguration().add(text);
				mobilineDspl.getInitialConfiguration().add(image);		
					//DSPL Cross-tree Rules
				mobilineDspl.getExcludeRules().add(excRule);
				mobilineDspl.getRequireRules().add(reqRule);
					//DSPL Feature Groups
				mobilineDspl.getFeatureGroups().add(documentsGroup);
				
				
				//--------------------------> CONTEXT MODEL <-------------------/
				//Context Root
				ContextFeature root = new ContextFeature("Root Context");
				
				// Context Propositions
				ContextFeature isBtFull = new ContextFeature("isBtFull");
				isBtFull.setContextType(ContextType.Group);
				ContextFeature isBtNormal = new ContextFeature("isBtNormal");
				isBtNormal.setContextType(ContextType.Group);
				ContextFeature isBtLow = new ContextFeature("isBtLow");
				isBtLow.setContextType(ContextType.Group);
				ContextFeature hasPwSource = new ContextFeature("hasPwSource");
				hasPwSource.setContextType(ContextType.Optional);
				
				//Context Groups
				ContextGroup battery = new ContextGroup("Baterry");
				battery.setType(ContextGroupType.XOR);
				battery.append(isBtFull);
				battery.append(isBtNormal);
				battery.append(isBtLow);
				
				ContextGroup source = new ContextGroup("Source");
				source.setType(ContextGroupType.NONE);
				source.append(hasPwSource);

				// Adaptation Rules
				
				//Adaptation Rule isBtLow => Video off, Image off
				AdaptationRuleWithCtxFeature rule1 = new AdaptationRuleWithCtxFeature();
				LinkedList<ContextFeature> contextTrigger1 = new LinkedList<ContextFeature>();
				contextTrigger1.add(isBtLow);
				rule1.setContextRequired(contextTrigger1);
				
				LinkedList<Feature> toDeactiveFeatures1 = new LinkedList<Feature>();
				toDeactiveFeatures1.add(image);
				toDeactiveFeatures1.add(video);
				rule1.setToDeactiveFeatureList(toDeactiveFeatures1);
					
				//Adaptation Rule isBtLow and HasPwSrc => Video off, Image on
				AdaptationRuleWithCtxFeature rule2 = new AdaptationRuleWithCtxFeature();
				LinkedList<ContextFeature> contextTrigger2 = new LinkedList<ContextFeature>();
				contextTrigger2.add(isBtLow);
				contextTrigger2.add(hasPwSource);
				rule2.setContextRequired(contextTrigger2);
				
				LinkedList<Feature> toActiveFeature2 = new LinkedList<Feature>();
				toActiveFeature2.add(image);
				rule2.setToActiveFeatureList(toActiveFeature2);
				
				LinkedList<Feature> toDeactiveFeatures2 = new LinkedList<Feature>();
				toDeactiveFeatures2.add(video);
				rule2.setToDeactiveFeatureList(toDeactiveFeatures2);
				
				//Adaptation Rule isBtNormal => Video off, Image on
				AdaptationRuleWithCtxFeature rule3 = new AdaptationRuleWithCtxFeature();
				LinkedList<ContextFeature> contextTrigger3 = new LinkedList<ContextFeature>();
				contextTrigger3.add(isBtNormal);		
				rule3.setContextRequired(contextTrigger3);
				
				LinkedList<Feature> toActiveFeature3 = new LinkedList<Feature>();
				toActiveFeature3.add(image);
				rule3.setToActiveFeatureList(toActiveFeature3);
				
				LinkedList<Feature> toDeactiveFeatures3 = new LinkedList<Feature>();
				toDeactiveFeatures3.add(video);
				rule3.setToDeactiveFeatureList(toDeactiveFeatures3);
				
				//Adaptation Rule isBtNormal and HasPwSrc => Video on, Image on
				AdaptationRuleWithCtxFeature rule4 = new AdaptationRuleWithCtxFeature();
				LinkedList<ContextFeature> contextTrigger4 = new LinkedList<ContextFeature>();
				contextTrigger4.add(isBtNormal);
				contextTrigger4.add(hasPwSource);
				rule4.setContextRequired(contextTrigger4);
					
				LinkedList<Feature> toActiveFeature4 = new LinkedList<Feature>();
				toActiveFeature4.add(image);
				toActiveFeature4.add(video);
				rule4.setToActiveFeatureList(toActiveFeature4);		
				
				//Adaptation Rule isBtFull => Video on, Image on
				AdaptationRuleWithCtxFeature rule5 = new AdaptationRuleWithCtxFeature();
				LinkedList<ContextFeature> contextTrigger5 = new LinkedList<ContextFeature>();
				contextTrigger5.add(isBtFull);		
				rule5.setContextRequired(contextTrigger5);
				
				LinkedList<Feature> toActiveFeature5 = new LinkedList<Feature>();
				toActiveFeature5.add(image);
				toActiveFeature5.add(video);
				rule5.setToActiveFeatureList(toActiveFeature5);
				
				//Context Model
				CFM ctxModel = new CFM();
				ctxModel.setContextRoot(root);
				ctxModel.getContextGroups().add(battery);
				ctxModel.getContextGroups().add(source);
				ctxModel.getAdaptationRules().add(rule1);
				ctxModel.getAdaptationRules().add(rule2);
				ctxModel.getAdaptationRules().add(rule3);
				ctxModel.getAdaptationRules().add(rule4);
				ctxModel.getAdaptationRules().add(rule5);

				//mobilineDspl.print();
				//ctxModel.print();
				
				// Step 2 - Identifying the ContextStates
				// Step 3 - Generating the XLS File with the contextStates
				ExtractorCks extractor = new ExtractorCks();
				CKS cks = extractor.getContextKripkeStructureFromContextModel(ctxModel,"D:/GeneratedFile.xls");		
				
				// Step 4 - Read the XLS File with the context evolution completing the CKS
				// GeneratedFile2 is the same file generate before, but it is filled out
				extractor.getContextChangesFromXLS("GeneratedFile2.xls", cks.getContextStates());
				
				cks.print();
				
				// Step 5 - Parser the CKS to PML Code, and generate the PML file
				Parser parser = new Parser(mobilineDspl, cks);
				HashMap <String,String> propertiesList = new HashMap<String,String>();
				String promelaCodeGenerated = parser.generatePromelaCodefromDSPL(propertiesList);
				//System.out.println(promelaCodeGenerated);
				GenerateFiles gen = new GenerateFiles();
				gen.generatePMLFile(promelaCodeGenerated, "promelaCodeGenerated");
				
				// Step 6 - Run the PML Code
				// Step 7 - Show the results
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							//String path = "C:/promelaCodeGenerated.pml";
							RunSpinChecker window = new RunSpinChecker(propertiesList, path); 
							//window.frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}
}
