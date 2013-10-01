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
		
	}
	
	public void show(PluginManager pluginManager) {
		final JDialog dialog = new CentredJDialog();
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setTitle("Plugin Manager");
		dialog.setSize(400, 300);
		dialog.setPreferredSize(new Dimension(400,300));
		dialog.setLayout(new BorderLayout());
		
		JScrollPane listScrollPane = new JScrollPane();
		JList<PluginListItem> list = new JList<PluginListItem>();
		PluginListModel model = new PluginListModel();
		list.setModel(model);
		model.addAll(pluginManager.getPluginList());
		list.revalidate();
		list.addMouseListener(new MouseAdapter() {
		    @SuppressWarnings("unchecked")
			public void mouseClicked(MouseEvent evt) {
		    	// We absolutely know that the source is our plugin-list, so we can safely cast.
		        JList<PluginListItem> list = (JList<PluginListItem>)evt.getSource();
		        if (evt.getClickCount() == 2) {
		            int index = list.locationToIndex(evt.getPoint());
		            
		            System.out.println("Clicked Element: " + index);
		        }
		    }
		});
		
		listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScrollPane.setViewportView(list);
		
		dialog.add(listScrollPane, BorderLayout.CENTER);
		
		dialog.pack();
		dialog.setResizable(true);
		dialog.setVisible(true);
	}
	
	
	
}
