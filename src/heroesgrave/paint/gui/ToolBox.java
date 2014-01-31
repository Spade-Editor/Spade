package heroesgrave.paint.gui;

import heroesgrave.paint.main.Paint;
import heroesgrave.paint.tools.Brush;
import heroesgrave.paint.tools.Ellipse;
import heroesgrave.paint.tools.Eraser;
import heroesgrave.paint.tools.Fill;
import heroesgrave.paint.tools.Line;
import heroesgrave.paint.tools.Move;
import heroesgrave.paint.tools.Picker;
import heroesgrave.paint.tools.Pixel;
import heroesgrave.paint.tools.Rectangle;
import heroesgrave.paint.tools.SelectTool;
import heroesgrave.paint.tools.Tool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class ToolBox {

	private final int BUTTON_SIZE = 32;
	
	public static Tool DEF;

	private JToolBar dialog;
	private JPanel panel;
	private ButtonGroup buttonGroup;

	public ToolBox(JFrame parentFrame) {
		dialog = new JToolBar("Tools", JToolBar.VERTICAL);
		dialog.setFloatable(false);
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(BUTTON_SIZE*2, 500));
		panel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		buttonGroup = new ButtonGroup();

		DEF = new Pixel("Pencil");
		Paint.main.currentTool = DEF;
		
		addButton(new ToolBoxButton("Pencil", DEF), true);
		addButton(new ToolBoxButton("Brush", new Brush("Brush")));
		addButton(new ToolBoxButton("Eraser", new Eraser("Eraser")));
		addButton(new ToolBoxButton("Colour Picker", new Picker("Colour Picker")));
		addButton(new ToolBoxButton("Paint Bucket", new Fill("Paint Bucket")));
		addButton(new ToolBoxButton("Line", new Line("Straight Line")));
		addButton(new ToolBoxButton("Rectangle", new Rectangle("Rectangle")));
		addButton(new ToolBoxButton("Ellipse", new Ellipse("Ellipse")));
		addButton(new ToolBoxButton("Select", new SelectTool("Select")));
		addButton(new ToolBoxButton("Move", new Move("Move")));

		dialog.add(panel);
		dialog.validate();
		
		parentFrame.add(dialog, BorderLayout.WEST);
	}

	public JToolBar getDialog() {
		return dialog;
	}

	private void addButton(ToolBoxButton button, boolean... selected) {
		if (selected != null && selected.length>0) {
			button.setSelected(selected[0]);
		}
		buttonGroup.add(button);
		panel.add(button);
	}
	
	private class ToolBoxButton extends JToggleButton {

		private static final long serialVersionUID = -7985116966168623216L;
		private Tool tool;

		public ToolBoxButton(String name, Tool tool) {
			super();

			setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
			setMargin(new Insets(0, 0, 0, 0));
			setToolTipText(name);

			this.tool = tool;
			// TRY to load the icon!
			try
			{
				URL url = this.getClass().getResource("/heroesgrave/paint/res/icons/tools/" + name + ".png");

				if (url != null)
				{
					this.setIcon(new ImageIcon(ImageIO.read(url)));
				}
				else
				{
					this.setIcon(new ImageIcon(ImageIO.read(Paint.questionMarkURL)));
				}

			} catch (IOException e1)
			{
				System.err.println("Error: Tool '" + name + "' is missing an icon!");
			}

			this.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					Paint.setTool(((ToolBoxButton) e.getSource()).getTool());
				}
			});
		}

		public Tool getTool() {
			return tool;
		}
	}
}