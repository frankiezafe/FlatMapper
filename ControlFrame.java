import java.util.Base64;
import java.util.ArrayList;
import processing.core.PVector;
import processing.core.PImage;
import processing.core.PGraphics;
import processing.core.PApplet;
import controlP5.*;

class ControlFrame extends PApplet {

  public static final int EDITMODE_NORMAL = 1;
  public static final int EDITMODE_EDIT = 2;
  public static final int EDITMODE_DRAG = 3;
  
  public static final float[] uv_range = new float[] { -2, 3 };
  
  public static final int ee_ticka = 0;
  public static final int ee_tickmid = ee_ticka + 1;
  public static final int ee_tickb = ee_tickmid + 1;
  public static final int ee_tickall = ee_tickb + 1;
  public static final int ee_red = ee_tickall + 1;
  public static final int ee_green = ee_red + 1;
  public static final int ee_blue = ee_green + 1;
  public static final int ee_alpha = ee_blue + 1;
  public static final int ee_xa = ee_alpha + 1;
  public static final int ee_ya = ee_xa + 1;
  public static final int ee_xb = ee_ya + 1;
  public static final int ee_yb = ee_xb + 1;
  public static final int ee_xc = ee_yb + 1;
  public static final int ee_yc = ee_xc + 1;
  public static final int ee_xd = ee_yc + 1;
  public static final int ee_yd = ee_xd + 1;
  
  public static final int ee_uvxa = ee_yd + 1;
  public static final int ee_uvya = ee_uvxa + 1;
  public static final int ee_uvxb = ee_uvya + 1;
  public static final int ee_uvyb = ee_uvxb + 1;
  public static final int ee_uvxc = ee_uvyb + 1;
  public static final int ee_uvyc = ee_uvxc + 1;
  public static final int ee_uvxd = ee_uvyc + 1;
  public static final int ee_uvyd = ee_uvxd + 1;
  
  public static final int ee_div = ee_uvyd + 1;
  public static final int ee_osc = ee_div + 1;
  public static final int ee_tex = ee_osc + 1;
  public static final int ee_deb = ee_tex + 1;
  public static final int ee_conf = ee_deb + 1;
  public static final int ee_del = ee_conf + 1;

  private ControlP5 ui = null;

  private int ui_width_large = 270;
  private int ui_width_normal = 200;
  private int ui_width_small = 120;
  
  private Button ui_save = null;
  // lines
  private Button ui_add_line = null;
  private ListBox ui_list_line = null;
  // planes
  private Button ui_add_plane = null;
  private ListBox ui_list_plane = null;
  // textures
  private ListBox ui_list_textures = null;
  // edit  
  private ArrayList<Controller> ui_main_elements = null;
  private ArrayList<Controller> ui_edit_elements = null;
  private ArrayList<Integer> ui_edit_gaps = null;
  private int[] ui_edit_slider_line;
  private int[] ui_edit_slider_plane;
  
  private int w, h;
  private int uiy_left = 20;
  private int uiy_top = 20;
  private FlatMapper parent;
  private ControlP5 cp5;
   
  private PImage display_texture_im = null;
  
  private PImage logo;

  public ControlFrame(FlatMapper _parent, int _w, int _h, String _name) {
    
    super();   
    parent = _parent;
    w=_w;
    h=_h;
    PApplet.runSketch(new String[]{this.getClass().getName()}, this);
    
  }

  public Controller get_main_element( int i ) {
    return ui_main_elements.get(i);
  }

  public Controller get_edit_element( int i ) {
    return ui_edit_elements.get(i);
  }

  public void settings() {
    size(w, h, P3D);
  }

  public void setup() {
    
    surface.setLocation(10, 10);
    
    int uiy = uiy_top;
    
    ui = new ControlP5(this);
    
    ui_main_elements = new ArrayList<Controller>();
    
    ui_add_line = ui.addButton("add_line").setPosition(uiy_left, uiy).setSize(50, 15);
    ui_main_elements.add(ui_add_line);
    ui_save = ui.addButton("save_all").setPosition(240, uiy).setSize(50, 15); uiy += 20;
    ui_main_elements.add(ui_save);
    ui_list_line = ui.addListBox("lines").setPosition(uiy_left, uiy).setSize(ui_width_large, 120); uiy += 120;
    ui_main_elements.add(ui_list_line);
    // space for a full list
    ui_add_plane = ui.addButton("add_plane").setPosition(uiy_left, uiy).setSize(50, 15); uiy += 20;
    ui_main_elements.add(ui_add_plane);
    ui_list_plane = ui.addListBox("planes").setPosition(uiy_left, uiy).setSize(ui_width_large, 120); uiy += 120;
    ui_main_elements.add(ui_list_plane);
    // space for a full list
    ui_list_textures = ui.addListBox("textures").setPosition(uiy_left, uiy).setSize(ui_width_large, 120);
    ui_main_elements.add(ui_list_textures);
    
    uiy = uiy_top;
    
    ui_edit_elements = new ArrayList<Controller>();
    ui_edit_gaps = new ArrayList<Integer>();
    int gid = 0;
    
    // thickness
    ui_edit_elements.add( ui.addSlider("thickness_a").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,300) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("thickness_mid").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,300) ); 
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("thickness_b").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,300) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("thickness_all").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,300) );
    ui_edit_gaps.add( 20 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    // rgba
    ui_edit_elements.add( ui.addSlider("red_tint").setPosition(uiy_left,uiy).setSize(ui_width_small, 12).setRange(0,1) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("green_tint").setPosition(uiy_left,uiy).setSize(ui_width_small, 12).setRange(0,1) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("blue_tint").setPosition(uiy_left,uiy).setSize(ui_width_small, 12).setRange(0,1) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("opacity").setPosition(uiy_left,uiy).setSize(ui_width_small, 12).setRange(0,1) );
    ui_edit_gaps.add( 20 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    
    // positions *********
    // positions A
    ui_edit_elements.add( ui.addSlider("pos_x_a").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,parent.width) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("pos_y_a").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,parent.height) );
    ui_edit_gaps.add( 20 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    // positions B
    ui_edit_elements.add( ui.addSlider("pos_x_b").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,parent.width) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("pos_y_b").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,parent.height) );
    ui_edit_gaps.add( 20 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    // positions C
    ui_edit_elements.add( ui.addSlider("pos_x_c").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,parent.width) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("pos_y_c").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,parent.height) );
    ui_edit_gaps.add( 20 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    // positions D
    ui_edit_elements.add( ui.addSlider("pos_x_d").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,parent.width) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("pos_y_d").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(0,parent.height) );
    ui_edit_gaps.add( 20 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    
    // UVS *********
    // uv a
    ui_edit_elements.add( ui.addSlider("uv_x_a").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(uv_range[0],uv_range[1]) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("uv_y_a").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(uv_range[0],uv_range[1]) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    // uv b
    ui_edit_elements.add( ui.addSlider("uv_x_b").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(uv_range[0],uv_range[1]) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("uv_y_b").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(uv_range[0],uv_range[1]) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    // uv c
    ui_edit_elements.add( ui.addSlider("uv_x_c").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(uv_range[0],uv_range[1]) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("uv_y_c").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(uv_range[0],uv_range[1]) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    // uv d
    ui_edit_elements.add( ui.addSlider("uv_x_d").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(uv_range[0],uv_range[1]) );
    ui_edit_gaps.add( 15 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addSlider("uv_y_d").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(uv_range[0],uv_range[1]) );
    ui_edit_gaps.add( 20 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    
    ui_edit_elements.add( ui.addSlider("subdivisions").setPosition(uiy_left,uiy).setSize(ui_width_normal, 12).setRange(1,30) );
    ui_edit_gaps.add( 20 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    
    ui_edit_elements.add( ui.addTextfield("osc_address").setPosition(uiy_left,uiy).setSize(ui_width_normal,15) );
    ui_edit_gaps.add( 30 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addTextfield("texture_path").setPosition(uiy_left,uiy).setSize(ui_width_normal,15) );
    ui_edit_gaps.add( 40 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    
    //ui_debug = ;
    ui_edit_elements.add( ui.addToggle("debug").setPosition(uiy_left,uiy).setSize(15,15) );
    ui_edit_gaps.add( 40 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    
    ui_edit_elements.add( ui.addButton("confirm").setPosition(uiy_left,uiy).setSize(50,15) );
    ui_edit_gaps.add( 0 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    ui_edit_elements.add( ui.addButton("del").setPosition(170,uiy).setSize(50,15) );
    ui_edit_gaps.add( 20 ); uiy += ui_edit_gaps.get( gid ); ++gid;
    
    ui_edit_slider_line = new int[] { 
      ee_ticka, ee_tickmid, ee_tickb, ee_tickall,
      ee_red, ee_green, ee_blue, ee_alpha,
      ee_xa, ee_ya, ee_xb, ee_yb,
      ee_div, ee_osc, ee_tex, ee_deb, ee_conf, ee_del
    };
    ui_edit_slider_plane = new int[] { 
      ee_red, ee_green, ee_blue, ee_alpha,
      ee_xa, ee_ya, ee_xb, ee_yb, ee_xc, ee_yc, ee_xd, ee_yd, 
      ee_uvxa, ee_uvya, ee_uvxb, ee_uvyb, ee_uvxc, ee_uvyc, ee_uvxd, ee_uvyd, 
      ee_div, ee_osc, ee_tex, ee_deb, ee_conf, ee_del
    };
    
    logo = loadImage( parent.dataPath("") + "/logo.png" );
    
    parent.on_controlframe_loaded();
    
    noLoop();
    
  }

  public void draw() {
    
    background(0);
    
    if ( display_texture_im != null ) {
      float w = display_texture_im.width;
      float h = display_texture_im.height;
      float r = w / h;
      float dw = ui_width_large;
      float dh = ui_width_large / r;
      if ( dh > 200 ) {
        dh = 200;
        dw = dh * r;
      }
      image( display_texture_im, ui_list_textures.getPosition()[0], ui_list_textures.getPosition()[1] + 15, dw, dh );
      
    }
    
    image( logo, 20, height - ( logo.height + 20 ) );
    
  }
  
  public void add_line(int i) {
    parent.newmappable = true;
    Line newl = new Line( parent );
    newl.setup( 
      parent.width * 0.5f, 
      parent.height * 0.15f, 
      parent.width * 0.5f, 
      parent.height * 0.25f );
    newl.set_texture_path( parent.default_texture_path );
    newl.load_texture();
    parent.select( newl );
    parent.editmode = EDITMODE_EDIT;
    ui_refresh();
  }
  
  public void add_plane(int i) {
    parent.newmappable = true;
    Plane newp = new Plane();
    newp.setup( 
      parent.width * 0.5f, 
      parent.height * 0.5f, 
      parent.width * 0.2f, 
      parent.height * 0.2f );
    newp.set_texture_path( parent.default_texture_path );
    newp.set_parent( parent );
    parent.select( newp );
    parent.editmode = EDITMODE_EDIT;
    ui_refresh();
  }
  
  public void save_all(int i) {
    parent.save_flatmap();
  }
  
  public void confirm(int i) {
    parent.confirm();
    regenerate_ui();
    ui_refresh();
  }
  
  public void del(int i) {
    parent.del();
    regenerate_ui();
    ui_refresh();
  }
  
  public void thickness_a( float v ) {
    if ( parent.editline != null ) {
      parent.editline.set_thickness( 0, v );
    }
  }
  
  public void thickness_mid( float v ) {
    if ( parent.editline != null ) {
      parent.editline.set_thickness( 1, v );
    }
  }
  
  public void thickness_b( float v ) {
    if ( parent.editline != null ) {
      parent.editline.set_thickness( 2, v );
    }
  }
  
  public void thickness_all( float v ) {
    if ( parent.editline != null ) {
      ((Slider) ui_edit_elements.get( ee_ticka )).setValue( v );
      ((Slider) ui_edit_elements.get( ee_tickmid )).setValue( v );
      ((Slider) ui_edit_elements.get( ee_tickb )).setValue( v );
    }
  }
  
  public void red_tint( float v ) {
    if ( parent.editmappable != null ) {
      parent.editmappable.set_rgba( 0, v );
    }
  }
  
  public void green_tint( float v ) {
    if ( parent.editmappable != null ) {
      parent.editmappable.set_rgba( 1, v );
    }
  }
  
  public void blue_tint( float v ) {
    if ( parent.editmappable != null ) {
      parent.editmappable.set_rgba( 2, v );
    }
  }
  
  public void opacity( float v ) {
    if ( parent.editmappable != null ) {
      parent.editmappable.set_rgba( 3, v );
    }
  }
  
  private void pos_generic_x( int i, float x ) {
    if ( parent.editmappable != null ) {
      parent.editmappable.set_vertex( i, x, 
        parent.editmappable.get_vertex(i).y
      );
    }
  }
  
  private void pos_generic_y( int i, float y ) {
    if ( parent.editmappable != null ) {
      parent.editmappable.set_vertex( i, 
        parent.editmappable.get_vertex(i).x, 
        y
      );
    }
  }
  
  private void uv_generic_x( int i, float v ) {
    if ( parent.editmappable != null ) {
      parent.editmappable.set_uv( i, 
        v,
        parent.editmappable.get_uv(i).y
      );
    }
  }
  
  private void uv_generic_y( int i, float v ) {
    if ( parent.editmappable != null ) {
      parent.editmappable.set_uv( i, 
        parent.editmappable.get_uv(i).x, 
        v
      );
    }
  }
  
  public void pos_x_a( float v ) {
    pos_generic_x( 0, v );
  }
  
  public void pos_y_a( float v ) {
    pos_generic_y( 0, v );
  }
  
  public void pos_x_b( float v ) {
    pos_generic_x( 1, v );
  }
  
  public void pos_y_b( float v ) {
    pos_generic_y( 1, v );
  }
  
  public void pos_x_c( float v ) {
    pos_generic_x( 2, v );
  }
  
  public void pos_y_c( float v ) {
    pos_generic_y( 2, v );
  }
  
  public void pos_x_d( float v ) {
    pos_generic_x( 3, v );
  }
  
  public void pos_y_d( float v ) {
    pos_generic_y( 3, v );
  }
  
  public void uv_x_a( float v ) {
    uv_generic_x( 0, v );
  }
  public void uv_y_a( float v ) {
    uv_generic_y( 0, v );
  }
  public void uv_x_b( float v ) {
    uv_generic_x( 1, v );
  }
  public void uv_y_b( float v ) {
    uv_generic_y( 1, v );
  }
  public void uv_x_c( float v ) {
    uv_generic_x( 2, v );
  }
  public void uv_y_c( float v ) {
    uv_generic_y( 2, v );
  }
  public void uv_x_d( float v ) {
    uv_generic_x( 3, v );
  }
  public void uv_y_d( float v ) {
    uv_generic_y( 3, v );
  }
  
  public void subdivisions( int v ) {
    if ( 
      parent.editmappable != null &&
      parent.editmappable.get_subdivision() != v
      ) {
        parent.editmappable.set_subdivision( v );
      }
  }
  
  public void controlEvent(ControlEvent e) { //<>//
    
    if( e.getName().equals("lines") ) {
    
      int lineid = (int)e.getValue();
      parent.deselect_all();
      Line l = parent.map.get_line( lineid );
      if ( l != null ) {
        parent.select( l );
        parent.editmode = EDITMODE_EDIT;
        ui_refresh();
      }
    
    } else if( e.getName().equals("planes") ) {
    
      int planeid = (int)e.getValue();
      parent.deselect_all();
      Plane p = parent.map.get_plane( planeid );
      if ( p != null ) {
        parent.select( p );
        parent.editmode = EDITMODE_EDIT;
        ui_refresh();
      }
    
    } else if( e.getName().equals("debug") ) {
    
      if ( parent.editmappable == null ) {
        return;
      }
      if ( e.getValue() == 0 ) {
        parent.editmappable.enable_debug( false );
      } else {
        parent.editmappable.enable_debug( true );
      }
    
    } else if( e.getName().equals("textures") ) {
    
      int tid = (int)e.getValue();
      PImage im = parent.get_texture( tid );
      if ( im == null ) {
        return;
      }
      if ( im == display_texture_im ) {
        display_texture_im = null;
      } else {
        display_texture_im = im;
      }
      
    }
    
  }
  
  public void show_main( boolean visible ) {
    if ( visible ) {
      for( Controller c : ui_main_elements ) {
        c.show();
      }
    } else {
      for( Controller c : ui_main_elements ) {
        c.hide();
      }
    }
  }
  
  public void show_edit( boolean visible ) {
    
    if ( visible ) {
      
      int[] id_list = null;
      if ( parent.editline != null ) {
        id_list = ui_edit_slider_line;
      } else if ( parent.editplane != null ) {
        id_list = ui_edit_slider_plane;
      }
      int uiy = uiy_left;
      for( int u = 0; u < id_list.length; ++u ) {
        int uiid = id_list[u];
        Controller c = ui_edit_elements.get( uiid );
        c.setPosition( c.getPosition()[0], uiy );
        uiy += ui_edit_gaps.get( uiid );
        c.show();
      }
    
    } else {
    
      for( Controller c : ui_edit_elements ) {
        c.hide();
      }
    
    }
    
  }
  
  public void ui_refresh() {
    if ( ui == null ) {
      return;
    }
    
    int[] id_list = null;
    if ( parent.editline != null ) {
      id_list = ui_edit_slider_line;
    } else if ( parent.editplane != null ) {
      id_list = ui_edit_slider_plane;
    }
    
    switch( parent.editmode ) {
      
      case EDITMODE_NORMAL:
        show_main(true);
        show_edit(false);
        break;
        
      case EDITMODE_EDIT:
        for ( int u = 0; u < id_list.length; ++u ) {
          int uiid = id_list[u];
          Controller c = ui_edit_elements.get( uiid );
          switch( uiid ) {
            case ee_ticka:
            case ee_tickmid:
            case ee_tickb:
              ((Slider) c).setValue( parent.editline.get_thickness( uiid - ee_ticka ) );
              break;
            case ee_red:
            case ee_green:
            case ee_blue:
            case ee_alpha:
              ((Slider) c).setValue( parent.editmappable.get_rgba( uiid - ee_red ) );
              break;
            case ee_xa:
            case ee_ya:
            case ee_xb:
            case ee_yb:
            case ee_xc:
            case ee_yc:
            case ee_xd:
            case ee_yd:
              {
                int off = uiid - ee_xa;
                PVector v = parent.editmappable.get_vertex( off / 2 );
                if ( v != null ) {
                  if ( off % 2 == 0 ) {
                    ((Slider) c).setValue( v.x );
                  } else {
                    ((Slider) c).setValue( v.y );
                  }
                }
              }
              break;
            case ee_uvxa:
            case ee_uvya:
            case ee_uvxb:
            case ee_uvyb:
            case ee_uvxc:
            case ee_uvyc:
            case ee_uvxd:
            case ee_uvyd:
              {
                int off = uiid - ee_uvxa;
                PVector v = parent.editmappable.get_uv( off / 2 );
                if ( v != null ) {
                  if ( off % 2 == 0 ) {
                    ((Slider) c).setValue( v.x );
                  } else {
                    ((Slider) c).setValue( v.y );
                  }
                }
              }
              break;
            case ee_div:
              ((Slider) c).setValue( parent.editmappable.get_subdivision() );
              break;
            case ee_osc:
              ((Textfield) c).setValue( parent.editmappable.get_osc_address() );
              break;
            case ee_tex:
              ((Textfield) c).setValue( parent.editmappable.get_texture_path() );
              break;
            case ee_deb:
              ((Toggle) c).setValue( parent.editmappable.is_debug() );
              break;
          }
        }
        show_main(false);
        show_edit(true);
        break;
        
      case EDITMODE_DRAG:
        show_main(false);
        show_edit(true);
        for ( int u = 0; u < id_list.length; ++u ) {
          int uiid = id_list[u];
          Controller c = ui_edit_elements.get( uiid );
          switch( uiid ) {
            case ee_xa:
            case ee_ya:
            case ee_xb:
            case ee_yb:
            case ee_xc:
            case ee_yc:
            case ee_xd:
            case ee_yd:
              {
                int off = uiid - ee_xa;
                int vid = off / 2;
                if ( off % 2 == 0 ) {
                  ((Slider) c).setValue( parent.editmappable.get_vertex(vid).x );
                } else {
                  ((Slider) c).setValue( parent.editmappable.get_vertex(vid).y );
                }
              }
              break;
          }
        }
        break;
    }
  }
  
  public void line_list(int i) {
    print( "line selected: " + i );
  }
  
  public void regenerate_ui() {
    int i = 0;
    String[] names = null;
    
    i = 0;
    names = new String[parent.map.ls.size()];
    for( Line l : parent.map.ls ) {
      names[i] = "" + i + " * " + l.get_osc_address() + " * " + l.get_texture_path();
      if ( l.get_texture() == null ) {
        names[i] += " /!\\";
      }
      ++i;
    }
    ui_list_line.setItems( names );
    
    i = 0;
    names = new String[parent.map.ps.size()];
    for( Plane p : parent.map.ps ) {
      names[i] = "" + i + " * " + p.get_osc_address() + " * " + p.get_texture_path();
      if ( p.get_texture() == null ) {
        names[i] += " /!\\";
      }
      ++i;
    }
    ui_list_plane.setItems( names );
    
    i = 0;
    names = new String[parent.texture_atlas.keySet().size()];
    names = parent.texture_atlas.keySet().toArray(names);
    ui_list_textures.setItems( names );
    
  }

}