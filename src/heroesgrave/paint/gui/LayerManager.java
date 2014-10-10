// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.paint.gui;

import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.main.Paint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.tree.WebTree;

public class LayerManager
{
	public WebDialog dialog;
	
	protected LayerSettings lsettings;
	
	protected Layer rootNode;
	protected DefaultTreeModel model;
	protected WebTree<Layer> tree;
	protected WebPanel controls;
	
	public LayerManager()
	{
		dialog = new WebDialog(Paint.main.gui.frame, "Layers").center();
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setTitle("Layers");
		dialog.getContentPane().setPreferredSize(new Dimension(200, 300));
		
		tree = new WebTree<Layer>((TreeModel) null);
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.getSelectionModel().addTreeSelectionListener(new SelectionListener());
		tree.setVisibleRowCount(8);
		tree.setExpandsSelectedPaths(true);
		
		JScrollPane scroll = new JScrollPane(tree);
		
		controls = new WebPanel();
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
				Layer n;
				if(path == null)
					n = rootNode;
				else
					n = (Layer) path.getLastPathComponent();
				//FIXME n.createLayer();
			}
		});
		
		deleteLayer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				Layer n;
				if(path == null)
					return;
				else
					n = (Layer) path.getLastPathComponent();
				if(n.equals(rootNode))
					return;
				//FIXME n.delete();
			}
		});
		
		moveUp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				Layer n;
				if(path == null)
					return;
				else
					n = (Layer) path.getLastPathComponent();
				if(n.equals(rootNode))
					return;
				//Paint.main.history.addChange(new LayerMoveOp(n, LayerMoveOp.MOVE_UP));
				redrawTree();
				tree.setSelectionPath(new TreePath(n.getPath()));
				//Paint.main.gui.canvas.getPanel().repaint();
			}
		});
		
		moveDown.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				Layer n;
				if(path == null)
					return;
				else
					n = (Layer) path.getLastPathComponent();
				if(n.equals(rootNode))
					return;
				//Paint.main.history.addChange(new LayerMoveOp(n, LayerMoveOp.MOVE_DOWN));
				redrawTree();
				tree.setSelectionPath(new TreePath(n.getPath()));
				//Paint.main.gui.canvas.getPanel().repaint();
			}
		});
		
		moveIn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				Layer n;
				if(path == null)
					return;
				else
					n = (Layer) path.getLastPathComponent();
				if(n.equals(rootNode))
					return;
				//Paint.main.history.addChange(new LayerMoveOp(n, LayerMoveOp.MOVE_IN));
				redrawTree();
				tree.setSelectionPath(new TreePath(n.getPath()));
				//Paint.main.gui.canvas.getPanel().repaint();
			}
		});
		
		moveOut.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				Layer n;
				if(path == null)
					return;
				else
					n = (Layer) path.getLastPathComponent();
				if(n.equals(rootNode))
					return;
				//Paint.main.history.addChange(new LayerMoveOp(n, LayerMoveOp.MOVE_OUT));
				redrawTree();
				tree.setSelectionPath(new TreePath(n.getPath()));
				//Paint.main.gui.canvas.getPanel().repaint();
			}
		});
		
		merge.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				Layer n;
				if(path == null)
					return;
				else
					n = (Layer) path.getLastPathComponent();
				if(n.isRoot())
					return;
				Layer parent = (Layer) n.getParent();
				// FIXME parent.merge(n);
				redrawTree();
				tree.setSelectionPath(new TreePath(parent.getPath()));
			}
		});
		
		settings.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				TreePath path = tree.getSelectionModel().getSelectionPath();
				Layer n;
				if(path == null)
					return;
				else
					n = (Layer) path.getLastPathComponent();
				//lsettings.showFor(n.canvas);
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
	
	public void setDocument(Document doc)
	{
		model = new DefaultTreeModel(doc.getRoot());
		tree.setModel(model);
	}
	
	public void setRoot(Layer root)
	{
		rootNode = root;
		model.setRoot(rootNode);
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.getSelectionModel().addTreeSelectionListener(new SelectionListener());
		tree.setVisibleRowCount(10);
		tree.setExpandsSelectedPaths(true);
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
	
	public void dispose()
	{
		dialog.dispose();
		//lsettings.dispose();
	}
	
	public void redrawTree()
	{
		// Capture the current state of the JTree node expansions.
		Vector<TreePath> paths = new Vector<TreePath>();
		
		Enumeration<TreePath> e = tree.getExpandedDescendants(new TreePath(rootNode));
		Layer selpath = (Layer) tree.getSelectionPath().getLastPathComponent();
		
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
		
		tree.setSelectionPath(new TreePath(selpath.getPath()));
	}
	
	public boolean isVisible()
	{
		return dialog.isVisible();
	}
	
	private class SelectionListener implements TreeSelectionListener
	{
		public void valueChanged(TreeSelectionEvent e)
		{
			if(e.getNewLeadSelectionPath() == null)
			{
				//Paint.main.gui.canvas.selectRoot();
				controls.setVisible(false);
				return;
			}
			Layer l = (Layer) e.getNewLeadSelectionPath().getLastPathComponent();
			if(l == null)
			{
				Paint.main.document.setCurrent(Paint.main.document.getRoot());
				controls.setVisible(false);
			}
			else
			{
				Paint.main.document.setCurrent(l);
				controls.setVisible(true);
				// TODO LayerSettings.update()
			}
		}
	}
	
	public JDialog getDialog()
	{
		return dialog;
	}
}
