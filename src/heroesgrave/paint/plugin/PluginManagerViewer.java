package heroesgrave.paint.plugin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import heroesgrave.paint.gui.Menu.CentredJDialog;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataListener;

public class PluginManagerViewer {

	public class PluginListModel implements ListModel<PluginListItem> {
		ArrayList<PluginListItem> items;

		public PluginListModel(){
			items = new ArrayList<PluginListItem>();
		}

		@Override
		public int getSize() {
			return items.size();
		}

		@Override
		public PluginListItem getElementAt(int index) {
			return items.get(index);
		}

		// We will ignore these for now!
		@Override public void addListDataListener(ListDataListener l) {}
		@Override public void removeListDataListener(ListDataListener l) {}

		public void addAll(ArrayList<Plugin> pluginList) {
			for(Plugin plugin : pluginList){
				items.add(new PluginListItem(plugin));
			}
		}
	}

	public class PluginListItem {
		private Plugin plugin;
		
		public PluginListItem(Plugin plugin) {
			this.plugin = plugin;
		}
		
		public String toString(){
			return plugin.name;
		}

		public void constructText(JTextArea pluginInfoArea) {
			pluginInfoArea.append("Plugin Name: " + plugin.name + "\n");
			pluginInfoArea.append("Plugin File-Size: " + plugin.info.getProperty("size") + "\n");
			pluginInfoArea.append("Plugin Last-Updated: " + plugin.info.getProperty("last-updated") + "\n");
			pluginInfoArea.append("Plugin Description: (as provided by the author)\n" + plugin.info.getProperty("description"));
			
		}
		
	}
	
	
	JDialog dialog = null;
	JScrollPane listScrollPane = null;
	JList<PluginListItem> list = null;
	PluginListModel model = null;
	JTextArea pluginInfoArea = null;
	
	public void show(PluginManager pluginManager) {
		dialog = new CentredJDialog();
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setTitle("Plugin Manager");
		dialog.setSize(640, 480);
		dialog.setPreferredSize(new Dimension(640, 480));
		dialog.setLayout(new BorderLayout());
		
		list = new JList<PluginListItem>();
		model = new PluginListModel();
		list.setModel(model);
		model.addAll(pluginManager.getPluginList());
		list.revalidate();
		list.addMouseListener(new MouseAdapter() {
		    @SuppressWarnings("unchecked")
			public void mouseClicked(MouseEvent evt) {
		    	// We absolutely know that the source is our plugin-list, so we can safely cast.
		        JList<PluginListItem> list = (JList<PluginListItem>)evt.getSource();
		        
		        //if (evt.getClickCount() == 2)
		        {
		            int index = list.locationToIndex(evt.getPoint());
		            PluginListModel dlm = (PluginListModel) list.getModel();
		            PluginListItem item = dlm.getElementAt(index);;
		            list.ensureIndexIsVisible(index);
		            
		            // Clear the TextArea so the plugin-info-element can write the info...
		            pluginInfoArea.setText("");
		            item.constructText(pluginInfoArea);
		            
		            // Set the info visible
		    		pluginInfoArea.setVisible(true);
		            
		        }
		    }
		});
		
		listScrollPane = new JScrollPane();
		listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScrollPane.setViewportView(list);
		dialog.add(listScrollPane, BorderLayout.CENTER);
		
		pluginInfoArea = new JTextArea();
		pluginInfoArea.setText("Nothing in here...");
		pluginInfoArea.setEditable(false);
		pluginInfoArea.setVisible(false);
		dialog.add(pluginInfoArea, BorderLayout.PAGE_END);
		
		dialog.pack();
		dialog.setResizable(true);
		dialog.setVisible(true);
	}
	
	
	
}
