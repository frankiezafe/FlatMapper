import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import oscP5.*;

public class Mappable implements java.io.Serializable {
  
  public static final int MESH_SUBDIVIDE = 5;
  public static final int DELTA_DEFAULT = 0;
  public static final int DELTA_LEFT = 1;
  public static final int DELTA_RIGHT = 2;
  
  protected transient FlatMapper parent;
  protected transient PImage texture;
  /* array of 4 2D PVector (x:u,y:v) storing the location of 
   * the corners of the texture uv -  generated in clockwise order, 
   * starting from top left, normalised between 0 and 1
   */
  protected PVector[] uv_quad; 
  protected transient float[][] geom_uv;
  
  protected PVector selected;
  protected String osc_address;

  protected String texture_path;
  protected float[] rgba;
  protected int msubdivide;
  protected int msubdivide_request;
  
  protected boolean display;
  protected boolean edited;
  protected boolean debugged;

  public Mappable() {
    
    parent = null;
    texture = null;
    geom_uv = null;
    selected = null;
    osc_address = null;
    texture_path = null;
    
    uv_quad = new PVector[4];
    uv_quad[0] = new PVector( 0,0 ); // top left
    uv_quad[1] = new PVector( 1,0 ); // top right
    uv_quad[2] = new PVector( 1,1 ); // bottom right
    uv_quad[3] = new PVector( 0,1 ); // bottom left
    
    rgba = new float[] {1,1,1,1};
    
    display = true;
    edited = false;
    debugged = false;

    msubdivide = MESH_SUBDIVIDE;
    msubdivide_request = MESH_SUBDIVIDE;
    
  }

  public void reset() {
    texture = null;
    selected = null;
    geom_uv = null;
  }
  
  public PVector get_uv( int i ) {
    return uv_quad[i];
  }
  
  public void set_uv( int i, float x, float y ) {
    uv_quad[i].set( x,y,0 );
    generate_geometry();
  }
  
  public PVector get_vertex( int i ) {
    System.out.println( "Mappable.get_vertex(i), overload to disable this comment... " + i );
    return null;
  }
  
  public void set_vertex( int i, float x, float y ) {
    set_vertex( i, new PVector( x,y,0 ) );
  }
  
  public void set_vertex( int i, float x, float y, float z ) {
    set_vertex( i, new PVector( x,y,z ) );
  }
  
  public void set_vertex( int i, PVector v ) {
    System.out.println( "Mappable.set_vertex(int, PVector), overload to disable this comment... " + i + " : " + v );
  }
  
  public int get_subdivision() {
    return msubdivide;
  }
  
  public void set_subdivision( int i ) {
    if ( i > 0 ) {
      msubdivide_request = i;
    }
  }

  public void load_texture() {
    if ( parent == null ) return;
    if ( texture_path.equals("") ) { 
      texture = null;
      return;
    }
    texture = parent.get_texture( texture_path );
  }

  public PImage get_texture() {
    return texture;
  }

  public String get_texture_path() {
    if ( texture_path == null ) texture_path = "";
    return texture_path;
  }
  
  public void set_texture_path( String s ) {
    texture_path = s;
  }

  public float get_rgba( int i ) {
    return rgba[i];
  }
  
  public void set_rgba( int i, float f ) {
    rgba[i] = f;
  }
  
  public float get_opacity() {
    return rgba[3];
  }

  public void set_opacity( float o ) {
    rgba[3] = o;
  }
  
  public void set_parent( FlatMapper p ) {
    if ( parent == null ) {
      parent = p;
      update_vectors();
      generate_geometry();
      load_texture();
    }
  }

  public String get_osc_address() {
    if ( osc_address == null ) osc_address = "/M";
    return osc_address;
  }

  public void set_osc_address( String s ) {
    osc_address = s;
  }

  public void set_display( boolean enable ) {  
    display = enable;
  }
  
  public boolean is_debug() {
    return debugged;
  }
  
  public void enable_debug( boolean enable ) {
    debugged = enable;
  }
  
  public boolean is_edit() {
    return edited;
  }
  
  public void enable_edit( boolean enable ) {
    edited = enable;
  }
  
  protected void update_vectors() {
    System.out.println( "Mappable.update_vectors(), overload to disable this comment..." );
  }
  
  protected void generate_geometry() {
    System.out.println( "Mappable.generate_geometry(), overload to disable this comment..." );
  }
  
  public void tri() {
    if (
        parent == null || 
        geom_uv == null ||
        ( !display && parent.editmappable != this )
    ) { return; }
    
    if ( msubdivide != msubdivide_request ) {
      msubdivide = msubdivide_request;
      generate_geometry();
    }
    
    parent.noStroke();
    if ( texture != null ) { 
      parent.tint( 255*rgba[0], 255*rgba[1], 255*rgba[2], 255*rgba[3] );
      parent.texture(texture);
    } else {
      parent.fill( 255*rgba[0], 255*rgba[1], 255*rgba[2], 255*rgba[3] );
    }
    if ( edited && debugged ) {
      parent.stroke( parent.frameCount % 255 );
    }
    parent.beginShape( parent.TRIANGLES );
    if ( texture != null ) { 
      parent.texture(texture);
    }
    int len = geom_uv.length;
    for( int i = 0; i < len; ++i ) {
       parent.vertex( geom_uv[i][0],geom_uv[i][1],geom_uv[i][2],geom_uv[i][3],geom_uv[i][4] );
    }
    parent.endShape();
    if ( texture != null ) { 
      parent.noTint();
    }
  }
  
  public void edit() {
    System.out.println( "Mappable.edit(), overload to disable this comment..." );
  }
  
  public void drag( float x, float y ) {
    if ( selected == null ) {
      return;
    }
    selected.x = x;
    selected.y = y;
    update_vectors();
    generate_geometry();
  }
  
  public PVector hit( int mx, int my ) {
    System.out.println( "Mappable.hit(int,int), overload to disable this comment..." );
    return null;
  }
  
  public void parse(OscMessage msg) {
    
    if ( !msg.checkAddrPattern(osc_address) ) return;
    
    byte[] types = msg.getTypetagAsBytes();
    int argnum = types.length;
    try {
      boolean parsed = true;
      for( int i = 0; i < argnum; ++i ) {
        if ( types[i] == 's'  ) {
          String argname = msg.get(i).stringValue();
          if ( argname.equals("opacity") ) {
            ++i; rgba[3] = msg.get(i).floatValue();
          } else if ( argname.equals("red") ) {
            ++i; rgba[0] = msg.get(i).floatValue();
          } else if ( argname.equals("green") ) {
            ++i; rgba[1] = msg.get(i).floatValue();
          } else if ( argname.equals("blue") ) {
            ++i; rgba[2] = msg.get(i).floatValue();
          } else if ( argname.equals("rgb") ) {
            ++i; rgba[0] = msg.get(i).floatValue();
            ++i; rgba[1] = msg.get(i).floatValue();
            ++i; rgba[2] = msg.get(i).floatValue();
          } else if ( argname.equals("rgba") ) {
            ++i; rgba[0] = msg.get(i).floatValue();
            ++i; rgba[1] = msg.get(i).floatValue();
            ++i; rgba[2] = msg.get(i).floatValue();
            ++i; rgba[3] = msg.get(i).floatValue();
          } else if ( argname.equals("pos") ) {
            ++i; int posi = msg.get(i).intValue();
            ++i; float x = msg.get(i).floatValue();
            ++i; float y = msg.get(i).floatValue();
            set_vertex( posi, x, y );
          } else if ( argname.equals("div") ) {
            ++i; set_subdivision( msg.get(i).intValue() );
          } else if ( argname.equals("tex") ) {
            ++i; set_texture_path( msg.get(i).stringValue() );
            load_texture();
          } else if ( argname.equals("osc") ) {
            ++i; set_osc_address( msg.get(i).stringValue() );
          } else if ( argname.equals("hide") ) {
            set_display( false );
          } else if ( argname.equals("show") ) {
            set_display( true );
          } else {
            parsed = false;
          }
        } 
      }
      if ( !parsed ) {
        parse_custom( msg );
      }
    } catch( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
  protected void parse_custom(OscMessage msg) {
    System.out.println( "Mappable.parse_custom(msg), overload to disable this comment... " + msg.addrPattern() );
  }

}