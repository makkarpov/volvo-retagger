package ru.makkarpov.retagger;

import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class EncodeDialog extends JDialog implements EncodingListener {
	private static final long serialVersionUID = -8441753273670254834L;
	private JLabel currentFile;
	public EncodeDialog() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		
		JLabel label = new JLabel("Кодирование...");
		label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.BOLD, label.getFont().getSize() + 4f));
		panel_1.add(label);
		
		currentFile = new JLabel("");
		panel.add(currentFile, BorderLayout.CENTER);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setSize(400, 184);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setCurrentSong(final String title) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				currentFile.setText(title);
			}
		});
	}

	@Override
	public void setSong(String artist, String title) {
		setCurrentSong(artist + " - " + title);
	}
}
