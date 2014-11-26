// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
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

package heroesgrave.spade.gui.dialogs;

import heroesgrave.spade.editing.SelectionTool;
import heroesgrave.spade.gui.GUIManager;
import heroesgrave.spade.image.Document;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.change.doc.AnchorLayer;
import heroesgrave.spade.image.change.doc.DeleteLayer;
import heroesgrave.spade.image.change.doc.MergeLayer;
import heroesgrave.spade.image.change.doc.MoveLayer;
import heroesgrave.spade.image.change.doc.NewLayer;
import heroesgrave.spade.image.change.edit.ClearMaskChange;
import heroesgrave.spade.image.change.edit.SetMaskChange;
import heroesgrave.spade.main.Spade;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.alee.extended.checkbox.CheckState;
import com.alee.extended.tree.CheckStateChange;
import com.alee.extended.tree.CheckStateChangeListener;
import com.alee.extended.tree.WebCheckBoxTree;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.rootpane.WebDialog;

public class LayerManager implements CheckStateChangeListener<Layer>, TreeSelectionListener
{
	public WebDialog dialog;
	
	protected LayerSettings lsettings;
	
	protected Layer rootNode;
	protected DefaultTreeModel model;
	protected WebCheckBoxTree<Layer> tree;
	protected WebPanel controls;
	
	public LayerManager(JFrame frame)
	{
		dialog = new WebDialog(frame, "Layers").center();
		dialog.setIconImage(GUIManager.ICON);
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setTitle("Layers");
		dialog.getContentPane().setPreferredSize(new Dimension(200, 300));
		
		tree = new WebCheckBoxTree<Layer>((TreeModel) null);
		tree.setEditable(false);
		tree.setRecursiveChecking(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.addCheckStateChangeListener(this);
		tree.getSelectionModel().addTreeSelectionListener(this);
		tree.setExpandsSelectedPaths(true);
		
		JScrollPane scroll = new JScrollPane(tree);
		
		controls = new WebPanel();
		controls.setLayout(new GridLayout(0, 2));
		
		JButton newLayer = new JButton("New");
		JButton deleteLayer = new JButton("Delete");
		JButton moveUp = new JButton("Move Up");
		JButton moveDown = new JButton("Move Down");
		JButton moveOut = new JButton("Move Out");
		JButton moveIn = new JButton("Move In");
		JButton settings = new JButton("Settings");
		JButton merge = new JButton("Merge In");
		
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
				Document doc = n.getDocument();
				if(n.isFloating())
				{
					doc.addChange(new AnchorLayer(n));
				}
				else
				{
					doc.addChange(new NewLayer(n));
				}
				Spade.main.gui.repaint();
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
				Document doc = n.getDocument();
				doc.addChange(new DeleteLayer(n));
				Spade.main.gui.repaint();
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
				if(n.equals(rootNode) || n.getPreviousSibling() == null)
					return;
				Layer parent = n.getParentLayer();
				int index = parent.getIndex(n);
				Document doc = n.getDocument();
				doc.addChange(new MoveLayer(n, parent, index - 1));
				Spade.main.gui.repaint();
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
				if(n.equals(rootNode) || n.getNextSibling() == null)
					return;
				Layer parent = n.getParentLayer();
				int index = parent.getIndex(n);
				Document doc = n.getDocument();
				doc.addChange(new MoveLayer(n, parent, index + 1));
				Spade.main.gui.repaint();
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
				if(n.equals(rootNode) || n.getPreviousSibling() == null)
					return;
				Document doc = n.getDocument();
				doc.addChange(new MoveLayer(n, (Layer) n.getPreviousSibling(), -1));
				Spade.main.gui.repaint();
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
				if(n.equals(rootNode) || n.getParentLayer().equals(rootNode))
					return;
				Layer parent = n.getParentLayer();
				Layer grandparent = parent.getParentLayer();
				int index = grandparent.getIndex(parent);
				Document doc = n.getDocument();
				doc.addChange(new MoveLayer(n, grandparent, index + 1));
				Spade.main.gui.repaint();
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
				if(n.getChildCount() == 0)
				{
					n.getDocument().addChange(new MergeLayer(n));
					Spade.main.gui.repaint();
				}
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
				lsettings.showFor(n);
			}
		});
		
		controls.add(newLayer);
		controls.add(deleteLayer);
		controls.add(moveUp);
		controls.add(settings);
		controls.add(moveDown);
		controls.add(merge);
		controls.add(moveIn);
		controls.add(moveOut);
		
		controls.setVisible(false);
		
		WebPanel controls = new WebPanel();
		controls.setLayout(new BorderLayout());
		controls.add(this.controls, BorderLayout.CENTER);
		
		dialog.setLayout(new BorderLayout());
		dialog.add(controls, BorderLayout.SOUTH);
		dialog.add(scroll, BorderLayout.CENTER);
		
		dialog.pack();
		dialog.setResizable(true);
		
		lsettings = new LayerSettings(frame);
	}
	
	public void setVisible(Layer layer)
	{
		tree.setChecked(layer, true);
	}
	
	public boolean isVisible(Layer layer)
	{
		return tree.isChecked(layer);
	}
	
	public void updateBlendModes()
	{
		lsettings.addAllBlendModes();
	}
	
	public void setDocument(Document doc)
	{
		if(doc == null)
		{
			rootNode = null;
			model = null;
			tree.setModel(null);
			tree.revalidate();
		}
		else
		{
			setRoot(doc.getRoot());
		}
	}
	
	public void setRoot(Layer root)
	{
		rootNode = root;
		model = new DefaultTreeModel(rootNode);
		tree.setModel(model);
		tree.revalidate();
		tree.checkAll();
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
		lsettings.dispose();
	}
	
	public void select(Layer layer)
	{
		tree.setSelectedNode(layer);
	}
	
	public void redrawTree()
	{
		if(tree.getSelectionPath() != null)
		{
			// Capture the current state of the Tree node expansions.
			Vector<TreePath> paths = new Vector<TreePath>();
			
			Enumeration<TreePath> e = tree.getExpandedDescendants(new TreePath(rootNode));
			Layer selpath = (Layer) tree.getSelectionPath().getLastPathComponent();
			
			if(e != null)
				while(e.hasMoreElements())
				{
					paths.addElement(e.nextElement());
				}
			
			// Force the Tree to rebuild itself.
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			model.reload();
			
			// Recover the old expanded state of the Tree.
			for(int i = 0; i < paths.size(); i++)
			{
				TreePath path = (TreePath) paths.elementAt(i);
				tree.expandPath(path);
			}
			
			tree.setSelectionPath(new TreePath(selpath.getPath()));
		}
		else
		{
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			if(model != null)
				model.reload();
		}
		tree.revalidate();
		tree.repaint();
	}
	
	public boolean isVisible()
	{
		return dialog.isVisible();
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		if(Spade.getDocument() == null)
			return;
		if(e.getNewLeadSelectionPath() == null)
		{
			Spade.getDocument().setCurrent(Spade.getDocument().getRoot());
			controls.setVisible(false);
			return;
		}
		Layer l = (Layer) e.getNewLeadSelectionPath().getLastPathComponent();
		if(l == null)
		{
			Spade.getDocument().setCurrent(Spade.getDocument().getRoot());
			controls.setVisible(false);
		}
		else
		{
			Layer current = Spade.getDocument().getCurrent();
			if(Spade.getDocument().setCurrent(l) && current != null)
			{
				if(current.getImage().isMaskEnabled())
				{
					boolean[] mask = current.getImage().copyMask();
					current.addChange(new ClearMaskChange());
					if(Spade.main.currentTool instanceof SelectionTool)
					{
						l.addChange(new SetMaskChange(mask));
					}
				}
			}
			controls.setVisible(true);
			lsettings.updateIfVisible(l);
		}
	}
	
	@Override
	public void checkStateChanged(List<CheckStateChange<Layer>> changes)
	{
		for(CheckStateChange<Layer> c : changes)
		{
			Layer layer = c.getNode();
			layer.setVisible(c.getNewState() == CheckState.checked);
			layer.getDocument().changed(layer);
		}
	}
	
	public WebDialog getDialog()
	{
		return dialog;
	}
}
