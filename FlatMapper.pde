import java.util.Base64;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import oscP5.*;
import netP5.*;
import controlP5.*;
import processing.video.*;

public FlatMap map = null;

// source: http://michaelagamesartba1b.blogspot.be/2015/12/ba1b-consists-of-3-project-briefs-which.html
public String default_texture_path = "grid.png";
public java.util.HashMap<String,PImage> texture_atlas;

private OscP5 oscP5;
private int oscP5_port = 23000;

public int editmode;
public Mappable editmappable = null;
public Line editline = null;
public Plane editplane = null;
public boolean newmappable;
public PVector offset;

ResolutionChooser resc;
ControlFrame cf;

Movie movie;

public FlatMapper() {
  
  ResolutionConfig.width = 800;
  ResolutionConfig.height = 600;
  ResolutionConfig.offsetx = 400;
  
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
  
  setup_demo_rt();
  register_pgraphic( "demo", demo_rt );
  
  movie = new Movie(this, "mass_production_øøøø.mkv");
  movie.loop();
  register_movie( "mass_prod", movie );
  
  cf = new ControlFrame( this, 310, 600, "Controls");
  surface.setLocation(420, 10);
  
  deselect_all();
  editmode = ControlFrame.EDITMODE_NORMAL;
  offset = new PVector();

  start_osc();
  
  smooth(8);
  background(0);
  textureMode(NORMAL);
  textureWrap(REPEAT);
  
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
    if ( m == editmappable ) {
      continue;
    }
    m.tri();
  }
  
  if ( editmappable != null ) {
    editmappable.tri();
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