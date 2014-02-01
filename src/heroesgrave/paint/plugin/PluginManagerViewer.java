/*
 *	Copyright 2013 HeroesGrave and other Paint.JAVA developers.
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

package heroesgrave.paint.plugin;

import heroesgrave.paint.gui.Menu.CentredJDialog;
import heroesgrave.paint.main.Paint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataListener;

/**
 * 
 * @author Longor1996
 *
 */
public class PluginManagerViewer
{
	private JDialog dialog = null;
	private JScrollPane listScrollPane = null;
	private JList<PluginListItem> list = null;
	private PluginListModel model = null;
	private JTextArea pluginInfoArea = null;
	
	public PluginManagerViewer(final PluginManager pluginManager)
	{
		dialog = new CentredJDialog(Paint.main.gui.frame, "Plugin Manager");
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		dialog.setTitle("Plugin Manager");
		dialog.setSize(640, 480);
		dialog.setPreferredSize(new Dimension(400, 300));
		dialog.setLayout(new BorderLayout());
		
		list = new JList<PluginListItem>();
		model = new PluginListModel();
		list.setModel(model);
		model.addAll(pluginManager.getPluginList());
		list.revalidate();
		list.addMouseListener(new MouseAdapter()
		{
			@SuppressWarnings("unchecked")
			public void mouseClicked(MouseEvent evt)
			{
				// We absolutely know that the source is our plugin-list, so we can safely cast.
				JList<PluginListItem> list = (JList<PluginListItem>) evt.getSource();
				
				//if (evt.getClickCount() == 2)
				if(pluginManager.getPluginList().size() > 0)
				{
					int index = list.locationToIndex(evt.getPoint());
					PluginListModel dlm = (PluginListModel) list.getModel();
					PluginListItem item = dlm.getElementAt(index);
					list.ensureIndexIsVisible(index);
					
					// Clear the TextArea so the plugin-info-element can write the info...
					pluginInfoArea.setText("");
					item.constructText(pluginInfoArea);
					
					// Set the info area to visible.
					pluginInfoArea.setVisible(true);
				}
			}
		});
		
		listScrollPane = new JScrollPane();
		listScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		listScrollPane.setViewportView(list);
		dialog.add(listScrollPane, BorderLayout.CENTER);
		
		pluginInfoArea = new JTextArea();
		pluginInfoArea.setLineWrap(true);
		pluginInfoArea.setWrapStyleWord(true);
		pluginInfoArea.setText("Nothing in here...");
		pluginInfoArea.setEditable(false);
		pluginInfoArea.setVisible(false);
		dialog.add(pluginInfoArea, BorderLayout.PAGE_END);
		
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
			return plugin.name;
		}
		
		public void constructText(JTextArea pluginInfoArea)
		{
			pluginInfoArea.append("Plugin Name: " + plugin.name + "\n");
			pluginInfoArea.append("Author: " + plugin.info.getProperty("author") + "\n");
			pluginInfoArea.append("Current Version: " + plugin.info.getProperty("version") + "\n");
			pluginInfoArea.append("File Size: " + plugin.info.getProperty("size") + "\n");
			pluginInfoArea.append("Last updated: " + plugin.info.getProperty("updated") + "\n");
			pluginInfoArea.append("\nDescription:\n" + plugin.info.getProperty("description") + "\n");
		}
	}
	
	public void show()
	{
		dialog.setVisible(true);
	}
}