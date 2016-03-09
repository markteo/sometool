
package qrcode;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import main.Data;

import org.jdesktop.xswingx.PromptSupport;
import org.json.JSONException;
import org.json.JSONObject;

import ui.UILogin;
import api.APICall;


public class JavaQR implements Runnable {

	private Thread t;
	private JFrame frame;
	
	public JavaQR(){
		start();
	}
	
	@Override
	public void run() {
		//		Initial Config
		frame = new JFrame("QR Code Generator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		frame.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		
		//		Top Panel
		topPanel.setLayout(new GridLayout(0,1));
		topPanel.setBorder(BorderFactory.createTitledBorder("Input Data"));

		JPanel rowTopPanel = new JPanel();
		rowTopPanel.setLayout(new GridLayout(0,2));

		JLabel accKey = new JLabel("Access Key");
		JTextField accField = new JTextField(5);
		PromptSupport.setPrompt("E.G Z76GU", accField);
		PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, accField);
		PromptSupport.setFontStyle(Font.BOLD, accField);
		accField.setEditable(false);
		accField.setText(Data.accessKey);

		JLabel regNo = new JLabel("Register Number");
		JTextField regField = new JTextField(5);
		
		PromptSupport.setPrompt("E.G THF11200054160106", regField);
		PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, regField);
		PromptSupport.setFontStyle(Font.BOLD, regField);
		regField.setEditable(false);
		regField.setText(Data.registrationNumber);
		
		JLabel licNo = new JLabel("License Number");
		JFormattedTextField licField = new JFormattedTextField();
		PromptSupport.setPrompt("E.G 2DAJS - 3J8SS - 9H8HS", licField);
		PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT, licField);
		PromptSupport.setFontStyle(Font.BOLD, licField);
		licField.setEditable(false);
		licField.setText(Data.licenseNumber);
		
		JLabel downloadLoc = new JLabel("Download Location");
		JButton dlBtn = new JButton("Choose Download Folder");

		rowTopPanel.add(accKey);
		rowTopPanel.add(accField);
		rowTopPanel.add(regNo);
		rowTopPanel.add(regField);
		rowTopPanel.add(licNo);
		rowTopPanel.add(licField);
		rowTopPanel.add(downloadLoc);
		rowTopPanel.add(dlBtn);
		topPanel.add(rowTopPanel);

		//		Center Panel
		centerPanel.setLayout(new GridLayout(0,1));
		centerPanel.setBorder(BorderFactory.createTitledBorder("Generate QR"));
		centerPanel.setPreferredSize(new Dimension(100,60));

		JPanel rowCenPanel = new JPanel();
		rowCenPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton genBtn = new JButton("Generate QR");
		JButton homeBtn = new JButton("Back to Start");
		JButton logoutBtn = new JButton("Logout");
		

		rowCenPanel.add(genBtn);
		rowCenPanel.add(homeBtn);
		rowCenPanel.add(logoutBtn);
		centerPanel.add(rowCenPanel);

//		Buttons Listener
		genBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String accessKey = accField.getText().toString();
				String regKey = regField.getText().toString();
				String licKey = licField.getText().toString();
				String dlDir = dlBtn.getText().toString();

				if(accessKey.trim().equalsIgnoreCase("") == true || regKey.trim().equalsIgnoreCase("") == true) {
					JOptionPane.showMessageDialog(frame, "Some fields are missing!");
				} else if(licKey.trim().length() < 21) {
					JOptionPane.showMessageDialog(frame, "License Key is incomplete!");
				} else if(dlDir.equalsIgnoreCase("Choose Download Folder") != true ) {
					JSONObject jsonObject = new JSONObject();
					try {
						jsonObject.put("accessKey", accessKey);
						jsonObject.put("registrationNumber", regKey);
						jsonObject.put("licenseNumber", licKey);
					} catch (JSONException e1) {
						e1.printStackTrace();
					}

					String fileLocation = dlBtn.getText().toString();

					QRLogic qrGen = new QRLogic();
					qrGen.generateQR(jsonObject, fileLocation);
					JOptionPane.showMessageDialog(frame, "QR Code Generated in " + fileLocation);
				} else {
					JOptionPane.showMessageDialog(frame, "Choose a directory!");
					System.out.println("No QR Generated");
				}
			}
		});

		dlBtn.addActionListener(new ActionListener() {;
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
				dlBtn.setText(selectedFile);
			}
		}
		});
		
		homeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Data.uiInventorySelect.setFrameVisible();
				frame.setVisible(false);
				
			}
		});
		
		logoutBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				APICall api = new APICall();
				try {
					JSONObject response = new JSONObject(api.logout(Data.targetURL, Data.sessionKey));
					if(response.getString("result").equals("ok")){
						Data.uiLogin = new UILogin();
						frame.setVisible(false);
						Data.uiLogin.runLogin();
						
					}
						
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		//      Frame Config
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setSize(400,225);
		frame.setVisible(true);

	}
	
	public void start() {
		t = new Thread(this);
		t.start();
	}
	public void setFrameVisible(){
		frame.setVisible(true);
	}
	
}
