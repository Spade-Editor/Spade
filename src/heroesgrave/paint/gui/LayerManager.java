/*
 *	Copyright 2013 HeroesGrave
 *
 *	This file is part of Paint.JAVA
 *
 *	Paint.JAVA is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package heroesgrave.paint.gui;

import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.image.Canvas;
import heroesgrave.paint.main.Paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class LayerManager
{
	public class LayerManagerTreeCellRenderer implements TreeCellRenderer
	{
		JLabel label = new JLabel("Node");
		Icon nodeIcon;
		
		LayerManagerTreeCellRenderer()
		{
			BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();
			g.setColor(Color.BLACK);
			g.fillOval(0, 0, 16, 16);
			nodeIcon = new ImageIcon(img);
			label.setMinimumSize(new Dimension(128, 16));
		}
		
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			
			if(value instanceof LayerNode)
			{
				LayerNode node = (LayerNode) value;
				label.setText(node.canvas.name);
				label.setIcon(nodeIcon);
				label.revalidate();
			}
			
			return label;
		}
	}
	
	public JDialog dialog;
	
	protected LayerSettings lsettings;
	
	protected LayerNode rootNode;
	protected DefaultTreeModel model;
	protected JTree tree;
	protected JPanel controls;
	
	public LayerManager(Canvas root)
	{
		dialog = new CentredJDialog(Paint.main.gui.frame, "Layers");
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setTitle("Layers");
		dialog.getContentPane().setPreferredSize(new Dimension(200, 300));
		
		rootNode = new LayerNode(root);
		model = new DefaultTreeModel(rootNode);
		tree = new JTree(model);
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.getSelectionModel().addTreeSelectionListener(new SelectionListener());
		tree.setVisibleRowCount(10);
		tree.setExpandsSelectedPaths(true);
		// XXX HeroesGrave: I like the default renderer better.
		// tree.setCellRenderer(new LayerManagerTreeCellRenderer());
		
		JScrollPane scroll = new JScrollPane(tree);
		
		controls = new JPanel();
		controls.setLayout(new GridLayout(0, 2));
		
		JButton newLayer = new JButton("New");
		JButton deleteLayer = new JButton("Delete");
		JButton moveUp = new JButton("Move Up");
		JButton moveDown = new JButton("Move Down");
		JButton moveIn = new JButton("Move In");
		JButton moveOut = new JButton("Move Out");
		JButton settings = new JButton("Settings");
		JButton merge = new JButton("Merge Out");
		
		newLayer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				LayerNode n;
				if(path == null)
					n = rootNode;
				else
					n = (LayerNode) path.getLastPathComponent();
				n.createLayer();
			}
		});
		
		deleteLayer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				LayerNode n;
				if(path == null)
					return;
				else
					n = (LayerNode) path.getLastPathComponent();
				if(n.equals(rootNode))
					return;
				n.delete();
			}
		});
		
		moveUp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				LayerNode n;
				if(path == null)
					return;
				else
					n = (LayerNode) path.getLastPathComponent();
				if(n.equals(rootNode))
					return;
				LayerNode swap = (LayerNode) n.getPreviousSibling();
				if(swap != null)
				{
					LayerNode parent = (LayerNode) n.getParent();
					parent.canvas.swap(n.canvas, swap.canvas);
					int i = parent.getIndex(n);
					int j = parent.getIndex(swap);
					parent.remove(j);
					parent.insert(n, j);
					parent.insert(swap, i);
					redrawTree();
				}
			}
		});
		
		moveDown.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				LayerNode n;
				if(path == null)
					return;
				else
					n = (LayerNode) path.getLastPathComponent();
				if(n.equals(rootNode))
					return;
				LayerNode swap = (LayerNode) n.getNextSibling();
				if(swap != null)
				{
					LayerNode parent = (LayerNode) n.getParent();
					parent.canvas.swap(n.canvas, swap.canvas);
					int i = parent.getIndex(n);
					int j = parent.getIndex(swap);
					parent.remove(i);
					parent.insert(swap, i);
					parent.insert(n, j);
					redrawTree();
				}
			}
		});
		
		moveIn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				LayerNode n;
				if(path == null)
					return;
				else
					n = (LayerNode) path.getLastPathComponent();
				if(n.equals(rootNode))
					return;
				LayerNode newParent = (LayerNode) n.getPreviousSibling();
				if(newParent == null)
					return;
				LayerNode parent = (LayerNode) n.getParent();
				newParent.add(n);
				parent.canvas.removeLayer(n.canvas);
				newParent.canvas.addLayer(n.canvas);
				redrawTree();
				tree.setSelectionPath(new TreePath(n.getPath()));
				Paint.main.gui.canvas.getPanel().repaint();
			}
		});
		
		moveOut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				LayerNode n;
				if(path == null)
					return;
				else
					n = (LayerNode) path.getLastPathComponent();
				if(n.equals(rootNode))
					return;
				LayerNode parent = (LayerNode) n.getParent();
				if(parent == null || parent.isRoot())
					return;
				LayerNode newParent = (LayerNode) parent.getParent();
				newParent.insert(n, newParent.getIndex(parent) + 1);
				parent.canvas.removeLayer(n.canvas);
				newParent.canvas.addLayer(n.canvas);
				redrawTree();
				tree.setSelectionPath(new TreePath(n.getPath()));
				Paint.main.gui.canvas.getPanel().repaint();
			}
		});
		
		merge.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				LayerNode n;
				if(path == null)
					return;
				else
					n = (LayerNode) path.getLastPathComponent();
				if(n.isRoot())
					return;
				LayerNode parent = (LayerNode) n.getParent();
				parent.merge(n);
				n.delete();
				redrawTree();
				tree.setSelectionPath(new TreePath(parent.getPath()));
			}
		});
		
		settings.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				LayerNode n;
				if(path == null)
					return;
				else
					n = (LayerNode) path.getLastPathComponent();
				lsettings.showFor(n.canvas);
			}
		});
		
		controls.add(newLayer);
		controls.add(deleteLayer);
		controls.add(merge);
		controls.add(settings);
		controls.add(moveUp);
		controls.add(moveDown);
		controls.add(moveIn);
		controls.add(moveOut);
		
		controls.setVisible(false);
		
		dialog.setLayout(new BorderLayout());
		dialog.add(controls, BorderLayout.CENTER);
		dialog.add(scroll, BorderLayout.NORTH);
		
		dialog.pack();
		dialog.setResizable(true);
		
		lsettings = new LayerSettings();
	}
	
	public void show()
	{
		dialog.setVisible(true);
	}
	
	public void hide()
	{
		dialog.setVisible(false);
	}
	
	public void toggle()
	{
		dialog.setVisible(!dialog.isVisible());
	}
	
	public void redrawTree()
	{
		// Capture the current state of the JTree node expansions.
		Vector<TreePath> paths = new Vector<TreePath>();
		
		Enumeration<TreePath> e = tree.getExpandedDescendants(new TreePath(rootNode));
		TreePath selpath = tree.getSelectionPath();
		
		if(e != null)
			while(e.hasMoreElements())
			{
				paths.addElement(e.nextElement());
			}
		
		// Force the JTree to rebuild itself.
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.reload();
		
		// Recover the old expanded state of the JTree.
		for(int i = 0; i < paths.size(); i++)
		{
			TreePath path = (TreePath) paths.elementAt(i);
			tree.expandPath(path);
		}
		
		tree.setSelectionPath(selpath);
	}
	
	public boolean isVisible()
	{
		return dialog.isVisible();
	}
	
	public class LayerNode extends DefaultMutableTreeNode
	{
		private static final long serialVersionUID = -9016111201661580573L;
		
		public Canvas canvas;
		
		public LayerNode(Canvas canvas)
		{
			super(canvas);
			this.canvas = canvas;
			if(canvas.hasChildren())
			{
				for(Canvas c : canvas.getChildren())
				{
					this.add(new LayerNode(c));
				}
			}
		}
		
		public void createLayer()
		{
			Canvas canvas = new Canvas("New Layer", this.canvas.getWidth(), this.canvas.getHeight());
			LayerNode node = new LayerNode(canvas);
			this.add(node);
			this.canvas.addLayer(canvas);
			model.reload();
			tree.setSelectionPath(new TreePath(node.getPath()));
			Paint.main.gui.canvas.getPanel().repaint();
		}
		
		public void merge(LayerNode node)
		{
			this.canvas.mergeLayer(node.canvas);
			while(node.getChildCount() > 0)
			{
				this.add((LayerNode) node.getFirstChild());
			}
		}
		
		public void delete()
		{
			LayerNode n = (LayerNode) this.getParent();
			if(n != null)
			{
				n.remove(this);
				n.canvas.removeLayer(canvas);
				model.reload();
				tree.setSelectionPath(new TreePath(n.getPath()));
				Paint.main.gui.canvas.getPanel().repaint();
			}
		}
	}
	
	private class SelectionListener implements TreeSelectionListener
	{
		public void valueChanged(TreeSelectionEvent e)
		{
			if(e.getNewLeadSelectionPath() == null)
			{
				Paint.main.gui.canvas.selectRoot();
				controls.setVisible(false);
				return;
			}
			LayerNode n = (LayerNode) e.getNewLeadSelectionPath().getLastPathComponent();
			if(n == null)
			{
				Paint.main.gui.canvas.selectRoot();
				controls.setVisible(false);
			}
			else
			{
				Paint.main.gui.canvas.select(n.canvas);
				controls.setVisible(true);
				lsettings.updateIfVisible(((LayerNode) tree.getSelectionPath().getLastPathComponent()).canvas);
			}
		}
	}
	
	public JDialog getDialog()
	{
		return dialog;
	}
}