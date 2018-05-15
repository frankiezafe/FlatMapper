import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.Box;

// https://stackoverflow.com/questions/789517/java-how-to-create-a-custom-dialog-box
public class ResolutionChooser {
  
  private List<JComponent> components;

  private String title;
  private int messageType;
  private JRootPane rootPane;
  private String[] options;
  private int optionIndex;
  
  private JTextField wField;
  private JTextField hField;
  private JTextField scrField;
  private JTextField xField;
  private JTextField yField;
  private JCheckBox fullsc;
  private JCheckBox autorsz;
  
  public int screen_width;
  public int screen_height;
  public int screen_number;
  public int screen_x;
  public int screen_y;
  public boolean screen_fullscreen;
  public boolean auto_resize;
  
  public ResolutionChooser() {
    
    components = new ArrayList<>();
    setTitle("Choose resolution and position");
    setMessageType(JOptionPane.PLAIN_MESSAGE);
    setRootPane(null);
    setOptions(new String[] { "Go!", "Nope..." });
    setOptionSelection(0);
    
    wField = new JTextField(5);
    wField.setText( "" + ResolutionConfig.width );
    addComponent(new JLabel("window width:"));
    addComponent(wField);
    
    hField = new JTextField(5);
    hField.setText( "" + ResolutionConfig.height );
    addComponent(new JLabel("window height: (no fullscreen)"));
    addComponent(hField);
    
    fullsc = new JCheckBox();
    fullsc.setSelected( ResolutionConfig.fullscreen );
    addComponent(new JLabel("fullscreen?"));
    addComponent(fullsc);
    
    scrField = new JTextField(5);
    scrField.setText( "" + ResolutionConfig.screenid );
    addComponent(new JLabel("screen selector: (fullscreen)"));
    addComponent(scrField);
    
    xField = new JTextField(5);
    xField.setText( "" + ResolutionConfig.offsetx );
    addComponent(new JLabel("window offset x: (no fullscreen)"));
    addComponent(xField);
    
    yField = new JTextField(5);
    yField.setText( "" + ResolutionConfig.offsety );
    addComponent(new JLabel("window offest y:"));
    addComponent(yField);
    
    autorsz = new JCheckBox();
    autorsz.setSelected( ResolutionConfig.autoresize );
    addComponent(new JLabel("auto resize mapping? (soon)"));
    addComponent(autorsz);

  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setMessageType(int messageType) {
    this.messageType = messageType;
  }

  public void addComponent(JComponent component) {
    components.add(component);
  }

  public void addMessageText(String messageText) {
    JLabel label = new JLabel("<html>" + messageText + "</html>");
    components.add(label);
  }

  public void setRootPane(JRootPane rootPane) {
    this.rootPane = rootPane;
  }

  public void setOptions(String[] options) {
    this.options = options;
  }

  public void setOptionSelection(int optionIndex) {
    this.optionIndex = optionIndex;
  }

  public int show() {
    
    int optionType = JOptionPane.OK_CANCEL_OPTION;
    Object optionSelection = null;

    if (options.length != 0) {
      optionSelection = options[optionIndex];
    }

    int selection = JOptionPane.showOptionDialog(
      rootPane, 
      components.toArray(), 
      title, 
      optionType, 
      messageType, 
      null, 
      options, 
      optionSelection
      );
    
    if ( selection == 0 ) {
      try {
        screen_width = Integer.parseInt( wField.getText() );
        screen_height = Integer.parseInt( hField.getText() );
        screen_number = Integer.parseInt( scrField.getText() );
        screen_x = Integer.parseInt( xField.getText() );
        screen_y = Integer.parseInt( yField.getText() );
        screen_fullscreen = fullsc.isSelected();
        auto_resize = autorsz.isSelected();
      } catch (NumberFormatException nfe) {
        System.out.println("NumberFormatException: " + nfe.getMessage());
        System.out.println("It seems you fucked up the configuration...");
        selection = 1;
      }
    }
      
    System.out.println( selection );
    return selection;
  }

  public static String getLineBreak() {
    return "<br>";
  }
}