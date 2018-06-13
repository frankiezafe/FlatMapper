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

import javax.swing.JFileChooser;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import oscP5.*;
import netP5.*;
import controlP5.*;
import processing.video.*;

public FlatMap map = null;
ResolutionChooser resc;
ControlFrame cf;

public String default_texture_path = "grid.png";
public java.util.HashMap<String,TextureRef> texture_atlas;

private OscP5 oscP5;
private int oscP5_port = 23000;

public int editmode;
public Mappable editmappable = null;
public Line editline = null;
public Plane editplane = null;
public boolean newmappable;
public PVector offset;
public String flatmap_path;

Movie movie;

public FlatMapper() {
  
  /* Display a file chooser to select the .flatmap to load
   * anywhere in the harddrive.
   */
  //project_selector();
  
  /* Change the default resolution configuration
   * before the ResolutionChooser popup.
   */
  //ResolutionConfig.width = 800;
  //ResolutionConfig.height = 600;
  //ResolutionConfig.offsetx = 400;
  
  /* Display a configuration popup,
   * see ResolutionChooser.java for the code
   */
  resc = new ResolutionChooser();
  int sel = resc.show();
  if ( sel == 1 ) {
    System.exit(0);
  }
  
}

public void settings() {
  size( ResolutionConfig.width, ResolutionConfig.height, P3D );
  if ( ResolutionConfig.fullscreen ) {
    fullScreen( ResolutionConfig.screenid );
  }
}

public void setup() {
  
  load_default_texture();
  
  /* This part shows how to create a sub-sketch usable
   * with the mapping shapes. The sub-sketch 'DEMO_RT'
   * draws in a PGraphics ( demo_rt ), registered as an
   * image in the texture_atlas (texture_register_pgraphic).
   * For more details, go to DEMO_RT tab.
   */
  setup_demo_rt();
  texture_register_pgraphic( "demo", demo_rt );
  
  movie = new Movie(this, "mass_production_øøøø.mkv");
  movie.loop();
  texture_register_movie( "mass_prod", movie );
  
  cf = new ControlFrame( this, 310, 600, "Controls");
  surface.setLocation(420, 10);
  
  deselect_all();
  editmode = ControlFrame.EDITMODE_NORMAL;
  offset = new PVector();

  start_osc();
  
  smooth(8);
  background(0);
  textureMode(NORMAL);
  
}

// called by ControlFrame at the end of setup
public void on_controlframe_loaded() {
  
  cf.ui_refresh();
  
  load_flatmap();
  
  if ( map == null ) {
    map = new FlatMap();
  }
  cf.regenerate_ui();

  if ( !ResolutionConfig.fullscreen ) {
    surface.setLocation( ResolutionConfig.offsetx, ResolutionConfig.offsety );
  }
  
}

public void select( Line sel ) {
  editmappable = sel;
  editline = sel;
  editplane = null;
  editmappable.enable_edit( true );
}

public void select( Plane sel ) {
  editmappable = sel;
  editline = null;
  editplane = sel;
  editmappable.enable_edit( true );
}

public void deselect_all() {
  if ( editmappable != null ) {
    editmappable.enable_edit( false );
  }
  newmappable = false;    
  editmappable = null;
  editline = null;
  editplane = null;
}

public void movieEvent(Movie m) {
  m.read();
}

public synchronized void confirm() {
  if ( editmode == ControlFrame.EDITMODE_EDIT || editmode == ControlFrame.EDITMODE_DRAG ) {
    if ( editline != null && newmappable ) {
      map.add( editline );
    } else if ( editplane != null && newmappable ) {
      map.add( editplane );
    }
    editmappable.set_osc_address( ((Textfield) cf.get_edit_element(ControlFrame.ee_osc)).getText() );
    editmappable.set_texture_path( ((Textfield) cf.get_edit_element(ControlFrame.ee_tex)).getText() );
    editmappable.load_texture();
    deselect_all();
    editmode = ControlFrame.EDITMODE_NORMAL;
  }
}

public synchronized void del() {
  if ( editmode == ControlFrame.EDITMODE_EDIT || editmode == ControlFrame.EDITMODE_DRAG ) {
    if ( editline != null && !newmappable ) {
      map.rmv( editline );
    } else if ( editplane != null && !newmappable ) {
      map.rmv( editplane );
    }
    deselect_all();
    editmode = ControlFrame.EDITMODE_NORMAL;
  }
}

public synchronized void draw() {
  
  update_demo_rt();
  
  cf.redraw();
  
  background(0);
  
  for( Mappable m : map.ms ) {
    m.tri();
  }
  
  if ( editmappable != null ) {
    if ( !map.ms.contains( editmappable ) ) {
      editmappable.tri();
    }
    editmappable.edit();
  }
  
}

public void mouseMoved() {
  if ( editmode == ControlFrame.EDITMODE_EDIT ) {
    editmappable.hit( mouseX, mouseY );
  }
}

public void mouseDragged() {
  if ( editmode == ControlFrame.EDITMODE_DRAG ) {
    editmappable.drag( mouseX + offset.x, mouseY + offset.y );
  }
}

public void mouseReleased() {
  if ( editmode == ControlFrame.EDITMODE_DRAG ) {
    editmode = ControlFrame.EDITMODE_EDIT;
    cf.ui_refresh();
  }
}

public void mousePressed() {
  if ( editmode == ControlFrame.EDITMODE_EDIT ) {
    PVector v = editmappable.hit( mouseX, mouseY );
    if ( v != null ) {
      offset.set( v.x - mouseX, v.y - mouseY );
      editmode = ControlFrame.EDITMODE_DRAG;
      cf.ui_refresh();
    }
  }
}