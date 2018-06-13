/*

 
 _________ ____  .-. _________/ ____ .-. ____ 
 __|__  (_)_/(_)(   )____<    \|    (   )  (_)
                 `-'                 `-'      
 

 art & game engines
 
____________________________________  ?   ____________________________________
                                    (._.)
 
 
 This file is part of FlatMapper
 For the latest info, see http://polymorph.cool/
 
 Copyright (c) 2018 polymorph.cool
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 
___________________________________( ^3^)_____________________________________
 
 ascii font: rotated by MikeChat & myflix
 have fun and be cool :)
 
 */

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
        
        ResolutionConfig.width = Integer.parseInt( wField.getText() );
        ResolutionConfig.height = Integer.parseInt( hField.getText() );
        ResolutionConfig.fullscreen = fullsc.isSelected();
        ResolutionConfig.screenid = Integer.parseInt( scrField.getText() );
        ResolutionConfig.offsetx = Integer.parseInt( xField.getText() );
        ResolutionConfig.offsety = Integer.parseInt( yField.getText() );
        ResolutionConfig.autoresize = autorsz.isSelected();
        
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