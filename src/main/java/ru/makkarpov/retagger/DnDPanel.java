package ru.makkarpov.retagger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

public class DnDPanel extends ImagePanel {
	private static final long serialVersionUID = 636701124757448781L;

	private DropTarget dropTarget;
	private DropTargetHandler dropTargetHandler;
	private boolean dragOver;
	
	private JTextField dirField;
	
	public DnDPanel(JTextField dirField) {
		super("/images/drophere.png");
		updateBorder();
		this.dirField = dirField;
	}
	
	private void updateBorder() {
		setBorder(new MatteBorder(1, 1, 1, 1, dragOver ? new Color(0, 128, 0) : Color.LIGHT_GRAY));
	}

	protected DropTarget getMyDropTarget() {
		if (dropTarget == null)
			dropTarget = new DropTarget(this, DnDConstants.ACTION_LINK, null);
		return dropTarget;
	}
	
    protected DropTargetHandler getDropTargetHandler() {
        if (dropTargetHandler == null) 
            dropTargetHandler = new DropTargetHandler();
        return dropTargetHandler;
    }
	
    @Override
    public void addNotify() {
        super.addNotify();
        try {
            getMyDropTarget().addDropTargetListener(getDropTargetHandler());
        } catch (TooManyListenersException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        getMyDropTarget().removeDropTargetListener(getDropTargetHandler());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dragOver) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(0, 255, 0, 64));
            g2d.fill(new Rectangle(getWidth(), getHeight()));
            g2d.dispose();
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void importFiles(List files) {
    	String dirStr = "";
    	for (File f: (List<File>) files) {
    		if (!dirStr.equals(""))
    			dirStr += ";";
    		try {
				dirStr += f.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
				dirStr += "<IO EXCEPTION!>";
			}
    	}
    	dirField.setText(dirStr);
    }
    
    protected class DropTargetHandler implements DropTargetListener {
        protected void processDrag(DropTargetDragEvent dtde) {
            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrag(DnDConstants.ACTION_LINK);
            } else {
                dtde.rejectDrag();
            }
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            processDrag(dtde);
            SwingUtilities.invokeLater(new DragUpdate(true, dtde.getLocation()));
            repaint();
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
            processDrag(dtde);
            SwingUtilities.invokeLater(new DragUpdate(true, dtde.getLocation()));
            repaint();
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
            SwingUtilities.invokeLater(new DragUpdate(false, null));
            repaint();
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            SwingUtilities.invokeLater(new DragUpdate(false, null));

            Transferable transferable = dtde.getTransferable();
            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(dtde.getDropAction());
                try {
                    List transferData = (List) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if (transferData != null && transferData.size() > 0) {
                        importFiles(transferData);
                        dtde.dropComplete(true);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                dtde.rejectDrop();
            }
        }
    }

    public class DragUpdate implements Runnable {

        private boolean dragOver;
        private Point dragPoint;

        public DragUpdate(boolean dragOver, Point dragPoint) {
            this.dragOver = dragOver;
            this.dragPoint = dragPoint;
        }

        @Override
        public void run() {
            DnDPanel.this.dragOver = dragOver;
//            DropPane.this.dragPoint = dragPoint;
            DnDPanel.this.updateBorder();
            DnDPanel.this.repaint();
        }
    }
}
