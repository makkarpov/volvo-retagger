package ru.makkarpov.retagger;

import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class GUIMain extends JFrame {
	private static final long serialVersionUID = -7156941242937605445L;
	private JTextField dirField;
	private JButton startBtn;
	private JCheckBox cbNumbers;
	private JCheckBox cbHashes;

	public GUIMain() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBorder(new EmptyBorder(0, 0, 10, 0));
		panel.add(verticalBox, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Volvo-перекодировщик");
		lblNewLabel.setFont(lblNewLabel.getFont().deriveFont(lblNewLabel.getFont().getStyle() | Font.BOLD, lblNewLabel.getFont().getSize() + 5f));
		lblNewLabel.setAlignmentX(0.5f);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		verticalBox.add(lblNewLabel);
		
		JLabel lblByMakkarpov = new JLabel("by makkarpov");
		lblByMakkarpov.setAlignmentX(0.5f);
		verticalBox.add(lblByMakkarpov);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel label = new JLabel("Директория");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel_1.add(label, gbc_label);
		
		dirField = new JTextField();
		GridBagConstraints gbc_dirField = new GridBagConstraints();
		gbc_dirField.insets = new Insets(0, 0, 5, 5);
		gbc_dirField.fill = GridBagConstraints.HORIZONTAL;
		gbc_dirField.gridx = 1;
		gbc_dirField.gridy = 0;
		panel_1.add(dirField, gbc_dirField);
		dirField.setColumns(10);
		
		JButton button = Utils.fileOpenButton(dirField);
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 0);
		gbc_button.gridx = 2;
		gbc_button.gridy = 0;
		panel_1.add(button, gbc_button);
		
		cbHashes = new JCheckBox("Использовать хеши в качестве имен");
		cbHashes.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cbNumbers.setSelected(false);
			}
		});
		GridBagConstraints gbc_cbHashes = new GridBagConstraints();
		gbc_cbHashes.anchor = GridBagConstraints.WEST;
		gbc_cbHashes.gridwidth = 3;
		gbc_cbHashes.insets = new Insets(0, 0, 5, 0);
		gbc_cbHashes.gridx = 0;
		gbc_cbHashes.gridy = 3;
		panel_1.add(cbHashes, gbc_cbHashes);
		
		JPanel dropPanel = new DnDPanel(dirField);
		GridBagConstraints gbc_dropPanel = new GridBagConstraints();
		gbc_dropPanel.insets = new Insets(10, 0, 10, 0);
		gbc_dropPanel.gridwidth = 3;
		gbc_dropPanel.fill = GridBagConstraints.BOTH;
		gbc_dropPanel.gridx = 0;
		gbc_dropPanel.gridy = 1;
		panel_1.add(dropPanel, gbc_dropPanel);
		dropPanel.setLayout(new BorderLayout(0, 0));
		
		cbNumbers = new JCheckBox("Использовать цифры в качестве имен");
		cbNumbers.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				cbHashes.setSelected(false);
			}
		});
		GridBagConstraints gbc_cbNumbers = new GridBagConstraints();
		gbc_cbNumbers.anchor = GridBagConstraints.WEST;
		gbc_cbNumbers.gridwidth = 3;
		gbc_cbNumbers.gridx = 0;
		gbc_cbNumbers.gridy = 2;
		panel_1.add(cbNumbers, gbc_cbNumbers);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
		
		startBtn = new JButton("Старт!");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startRetagging();
			}
		});
		panel_2.add(startBtn);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Volvo-перекодировщик");
		setSize(416, 356);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void startRetagging() {
		String[] inS = dirField.getText().split(";");
		File[] in = new File[inS.length];
		for (int i = 0; i < inS.length; i++) in[i] = new File(inS[i]);
		
		startBtn.setEnabled(false);
		
		EncodeDialog dlg = new EncodeDialog();
		Thread t = new Thread(new Encoder(in, dlg));
		t.start();
	}

	private void showException(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);

        pw.println("Необработанное исключение:");
        pw.println();
        t.printStackTrace(pw);

        final String text = sw.toString();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, text, "Необработанное исключение", JOptionPane.ERROR_MESSAGE);
            }
        });

	}
	
	private class Encoder implements Runnable {
		private File[] fList;
		private EncodeDialog dlg;
		
		public Encoder(File[] f, EncodeDialog dlg) {
			this.fList = f;
			this.dlg = dlg;
		}
		
		@Override
		public void run() {
		    boolean cleanExit = true;

			try {
				Recoder rcd = new Recoder();
				
				rcd.setUseNumbers(cbNumbers.isSelected());
				rcd.setUseHashes(cbHashes.isSelected());
				
				for (File f: fList) {
					if (!f.isDirectory()) continue;

                    File out = new File(f.getParentFile(), f.getName() + "_retagged");
                    if (!out.exists() && !out.mkdirs())
                        throw new IOException("Failed to create directory: " + out);
                    rcd.parseDirectory(f, out, dlg);
				}
			} catch (Exception e) {
                showException(e);
                e.printStackTrace();
                cleanExit = false;
			} finally {
			    final boolean finalExit = cleanExit;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						startBtn.setEnabled(true);
						dlg.dispose();

						if (finalExit)
    						JOptionPane.showMessageDialog(null, "Файлы перекодированы", "Информация", JOptionPane.INFORMATION_MESSAGE);
					}
				});
			}
		}
	}
}
