package ru.makkarpov.retagger;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 4971423365579795026L;

	private BufferedImage img;
	
	public ImagePanel(String path) {
		try {
			img = ImageIO.read(ImagePanel.class.getResourceAsStream(path));
			setOpaque(false);
			setMinimumSize(new Dimension(img.getWidth(), img.getHeight()));
			setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		} catch (IOException e) {
			e.printStackTrace();
			img = null;
		}
	}
	
	protected void paintComponent(Graphics g) {
		if (img == null) {
			super.paintComponent(g);
			return;
		}
		
		int x = (getSize().width - img.getWidth()) / 2;
		int y = (getSize().height - img.getHeight()) / 2;
		g.drawImage(img, x, y, null);
	}
}
