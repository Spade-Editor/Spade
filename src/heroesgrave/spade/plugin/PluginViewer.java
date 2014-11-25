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

package heroesgrave.spade.plugin;

import heroesgrave.spade.gui.AboutDialog;
import heroesgrave.spade.gui.GUIManager;
import heroesgrave.spade.main.Spade;
import heroesgrave.utils.misc.Metadata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataListener;
import javax.swing.text.StyledDocument;

import com.alee.laf.rootpane.WebDialog;

/**
 * 
 * @author Longor1996
 *
 */
public class PluginViewer
{
	private JDialog dialog;
	private JScrollPane listScrollPane;
	private JList<PluginListItem> list;
	private PluginListModel model;
	private JTextPane pluginInfoArea;
	
	public PluginViewer(final PluginManager pluginManager)
	{
		dialog = new WebDialog(Spade.main.gui.frame, "Plugin Viewer");
		dialog.setIconImage(GUIManager.ICON);
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		dialog.setTitle("Plugin Viewer");
		dialog.setPreferredSize(new Dimension(400, 350));
		dialog.setLayout(new BorderLayout());
		
		list = new JList<PluginListItem>();
		model = new PluginListModel();
		list.setModel(model);
		model.addAll(pluginManager.getPlugins());
		list.revalidate();
		
		pluginInfoArea = new JTextPane();
		pluginInfoArea.setText("Select a Plugin for more info");
		pluginInfoArea.setEditable(false);
		pluginInfoArea.setVisible(true);
		
		final JScrollPane infoScrollPane = new JScrollPane();
		infoScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		infoScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		infoScrollPane.setViewportView(pluginInfoArea);
		infoScrollPane.setPreferredSize(new Dimension(300, 150));
		
		list.addMouseListener(new MouseAdapter()
		{
			@SuppressWarnings("unchecked")
			public void mouseClicked(MouseEvent evt)
			{
				// We absolutely know that the source is our plugin-list, so we can safely cast.
				JList<PluginListItem> list = (JList<PluginListItem>) evt.getSource();
				
				//if (evt.getClickCount() == 2)
				if(pluginManager.getPluginCount() > 0)
				{
					int index = list.locationToIndex(evt.getPoint());
					PluginListModel dlm = (PluginListModel) list.getModel();
					PluginListItem item = dlm.getElementAt(index);
					list.ensureIndexIsVisible(index);
					
					// Clear the TextArea so the plugin-info-element can write the info...
					pluginInfoArea.setText("");
					item.constructText(pluginInfoArea);
				}
			}
		});
		
		listScrollPane = new JScrollPane();
		listScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		listScrollPane.setViewportView(list);
		
		dialog.add(listScrollPane, BorderLayout.CENTER);
		dialog.add(infoScrollPane, BorderLayout.SOUTH);
		
		dialog.pack();
		dialog.setResizable(true);
		dialog.setVisible(false);
	}
	
	public class PluginListModel implements ListModel<PluginListItem>
	{
		ArrayList<PluginListItem> items;
		
		public PluginListModel()
		{
			items = new ArrayList<PluginListItem>();
		}
		
		@Override
		public int getSize()
		{
			return items.size();
		}
		
		@Override
		public PluginListItem getElementAt(int index)
		{
			return items.get(index);
		}
		
		// We will ignore these for now!
		@Override
		public void addListDataListener(ListDataListener l)
		{
		}
		
		@Override
		public void removeListDataListener(ListDataListener l)
		{
		}
		
		public void addAll(ArrayList<Plugin> pluginList)
		{
			for(Plugin plugin : pluginList)
			{
				items.add(new PluginListItem(plugin));
			}
		}
	}
	
	public class PluginListItem
	{
		private Plugin plugin;
		
		public PluginListItem(Plugin plugin)
		{
			this.plugin = plugin;
		}
		
		public String toString()
		{
			if(!plugin.isLoaded())
			{
				return "<html><strike>" + plugin.getInfo().get("name") + "</strike></html>";
			}
			return "<html><b>" + plugin.getInfo().get("name") + "</b></html>";
		}
		
		public void constructText(JTextPane pluginInfoArea)
		{
			Metadata info = plugin.getInfo();
			StyledDocument doc = pluginInfoArea.getStyledDocument();
			AboutDialog.addStylesToDocument(doc);
			AboutDialog.append(doc, "Plugin Name: ", "bold");
			AboutDialog.append(doc, info.get("name") + "\n", "regular");
			
			AboutDialog.append(doc, "Author(s): ", "bold");
			AboutDialog.append(doc, info.get("authors") + "\n", "regular");
			
			AboutDialog.append(doc, "Current Version: ", "bold");
			AboutDialog.append(doc, info.get("version") + "\n", "regular");
			
			AboutDialog.append(doc, "Last Updated: ", "bold");
			AboutDialog.append(doc, info.get("updated") + "\n", "regular");
			
			AboutDialog.append(doc, "Location: ", "bold");
			AboutDialog.append(doc, info.get("location") + "\n", "regular");
			
			AboutDialog.append(doc, "Compatible with Spade: ", "bold");
			if(info.has("min-spade-version"))
			{
				if(info.has("max-spade-version"))
				{
					AboutDialog.append(doc, info.get("min-spade-version") + " - " + info.get("max-spade-version"), "regular");
				}
				else
				{
					AboutDialog.append(doc, info.get("min-spade-version") + "+", "regular");
				}
			}
			else if(info.has("max-spade-version"))
			{
				AboutDialog.append(doc, "<" + info.get("max-spade-version"), "regular");
			}
			else
			{
				AboutDialog.append(doc, "any", "regular");
			}
			
			AboutDialog.append(doc, "\n\nDescription: ", "bold");
			AboutDialog.append(doc, info.get("description"), "regular");
		}
	}
	
	public void show()
	{
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(Spade.main.gui.frame);
	}
}
