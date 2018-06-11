import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import java.util.ArrayList;
import oscP5.*;

public class Line extends Mappable {

  private PVector a;
  private PVector mid;
  private PVector b;
  private PVector dir;
  private PVector normal;
  private float thickness_a;
  private float thickness_mid;
  private float thickness_b;
  
  public JSONObject json() {
    JSONObject data = super.json();
    data.setString("type",  "Line" );
    data.setJSONObject("a",  FlatMapper.obj2json( a ) );
    data.setJSONObject("b",  FlatMapper.obj2json( b ) );
    data.setFloat("thickness_a", thickness_a );
    data.setFloat("thickness_mid", thickness_mid );
    data.setFloat("thickness_b", thickness_b );
    return data;
  }
  
  public boolean json( JSONObject data ) {
    if ( !data.getString("type").equals( "Line" ) ) {
      System.err.println( "Line::json: invalid data type for " + data );
      return false;
    }
    super.json( data );
    a = FlatMapper.json2pvector( data.getJSONObject("a") );
    b = FlatMapper.json2pvector( data.getJSONObject("b") );
    thickness_a = data.getFloat("thickness_a");
    thickness_mid = data.getFloat("thickness_mid");
    thickness_b = data.getFloat("thickness_b");
    return true;
  }
  
  Line() {
    super();
    parent = null;
    osc_address = "/L";
    texture_path = "";
    float[] c = {1,1,1,1};
    rgba = c;
    reset();
  }
  
  Line( FlatMapper p ) {
    super();
    parent = p;
    osc_address = "/L";
    texture_path = "";
    float[] c = {1,1,1,1};
    rgba = c;
    reset();
  }

  public void reset() {
    
    super.reset();
    a = new PVector();
    mid = new PVector();
    b = new PVector();
    dir = new PVector();
    normal = new PVector();
    thickness_a = 10;
    thickness_mid = 10;
    thickness_b = 10;
    generate_geometry();
    
  }

  public void setup( float ax, float ay, float bx, float by ) {
    a.x = ax;
    a.y = ay;
    b.x = bx;
    b.y = by;
    update_vectors();
    generate_geometry();
  }
  
  public PVector get_vertex( int i ) {
    if ( i < 0 || i > 1 ) return null;
    if ( i == 0 ) return a;
    return b;
  }
  
  public void set_vertex( int i, PVector v ) {
    if ( i < 0 || i > 1 ) return;
    if ( i == 0 ) a.set( v.x, v.y, v.z );
    else b.set( v.x, v.y, v.z );
    update_vectors();
    generate_geometry();
  }

  public float get_thickness( int i ) {
    switch( i ) {
    case 0:
      return thickness_a;
    case 1:
      return thickness_mid;
    case 2:
      return thickness_b;
    };
    return 0;
  }

  public void set_thickness( int i, float v ) {
    switch( i ) {
    case 0:
      thickness_a = v;
      break;
    case 1:
      thickness_mid = v;
      break;
    case 2:
      thickness_b = v;
      break;
    }
    generate_geometry();
  }
  
  private void generate_tris( 
    ArrayList<float[]> faces, 
    PVector top, PVector bottom,
    PVector top_uv, PVector bottom_uv,
    float top_thick, float bottom_thick
    ) {
    
      float[] f;
      f = new float[5];
      f[0] = top.x - normal.x * top_thick;
      f[1] = top.y - normal.y * top_thick;
      f[2] = top.z - normal.z * top_thick;
      f[3] = top_uv.x;
      f[4] = top_uv.y;
      faces.add( f );
      f = new float[5];
      f[0] = top.x + normal.x * top_thick;
      f[1] = top.y + normal.y * top_thick;
      f[2] = top.z + normal.z * top_thick;
      f[3] = bottom_uv.x;
      f[4] = top_uv.y;
      faces.add( f );
      f = new float[5];
      f[0] = bottom.x;
      f[1] = bottom.y;
      f[2] = bottom.z;
      f[3] = top_uv.x + ( bottom_uv.x - top_uv.x ) * 0.5f;
      f[4] = bottom_uv.y;
      faces.add( f );
      
      f = new float[5];
      f[0] = bottom.x;
      f[1] = bottom.y;
      f[2] = bottom.z;
      f[3] = top_uv.x + ( bottom_uv.x - top_uv.x ) * 0.5f;
      f[4] = bottom_uv.y;
      faces.add( f );
      f = new float[5];
      f[0] = top.x + normal.x * top_thick;
      f[1] = top.y + normal.y * top_thick;
      f[2] = top.z + normal.z * top_thick;
      f[3] = bottom_uv.x;
      f[4] = top_uv.y;
      faces.add( f );
      f = new float[5];
      f[0] = bottom.x + normal.x * bottom_thick;
      f[1] = bottom.y + normal.y * bottom_thick;
      f[2] = bottom.z + normal.z * bottom_thick;
      f[3] = bottom_uv.x;
      f[4] = bottom_uv.y;
      faces.add( f );
      
      f = new float[5];
      f[0] = bottom.x;
      f[1] = bottom.y;
      f[2] = bottom.z;
      f[3] = top_uv.x + ( bottom_uv.x - top_uv.x ) * 0.5f;
      f[4] = bottom_uv.y;
      faces.add( f );
      f = new float[5];
      f[0] = bottom.x - normal.x * bottom_thick;
      f[1] = bottom.y - normal.y * bottom_thick;
      f[2] = bottom.z - normal.z * bottom_thick;
      f[3] = top_uv.x;
      f[4] = bottom_uv.y;
      faces.add( f );
      f = new float[5];
      f[0] = top.x - normal.x * top_thick;
      f[1] = top.y - normal.y * top_thick;
      f[2] = top.z - normal.z * top_thick;
      f[3] = top_uv.x;
      f[4] = top_uv.y;
      faces.add( f );
      
  }
  
  private void generate_geometry( 
    ArrayList<float[]> faces, 
    PVector start, PVector stop,
    PVector start_uv, PVector stop_uv,
    float start_thick, float stop_thick
    ) {
        
    // 3d
    PVector step = new PVector( 
      ( stop.x - start.x ) / msubdivide, 
      ( stop.y - start.y ) / msubdivide,
      ( stop.z - start.z ) / msubdivide );
    
    // uv
    PVector uv_step = new PVector( 
      (stop_uv.x - start_uv.x) / msubdivide, 
      (stop_uv.y - start_uv.y) / msubdivide );
    PVector uv_top = new PVector( start_uv.x, start_uv.y );
    PVector uv_bottom = new PVector( start_uv.x + uv_step.x, start_uv.y + uv_step.y );
    
    // thickness
    float thick_step = ( stop_thick - start_thick ) / msubdivide;
    float thick_top = start_thick;
    float thick_bottom = start_thick + thick_step;
    
    PVector top = new PVector( start.x, start.y, start.z );
    PVector bottom = new PVector( start.x, start.y, start.z );
    bottom.add( step );
     
    float div_tmp = 0.5f - ( 1.f / msubdivide ) * 0.5f;
     
    for ( int y = 0; y < msubdivide; ++y ) {
      
      PVector top_tmp = new PVector( 
        top.x - normal.x * thick_top * div_tmp,
        top.y - normal.y * thick_top * div_tmp,
        top.z - normal.z * thick_top * div_tmp );
      PVector top_step = new PVector( 
        normal.x * thick_top / msubdivide, 
        normal.y * thick_top / msubdivide, 
        normal.z * thick_top / msubdivide );
      
      PVector bottom_tmp = new PVector( 
        bottom.x - normal.x * thick_bottom * div_tmp,
        bottom.y - normal.y * thick_bottom * div_tmp,
        bottom.z - normal.z * thick_bottom * div_tmp );
      PVector bottom_step = new PVector( 
        normal.x * thick_bottom / msubdivide, 
        normal.y * thick_bottom / msubdivide, 
        normal.z * thick_bottom / msubdivide );
      
      for ( int x = 0; x < msubdivide; ++x ) {
        
        generate_tris( 
          faces,
          top_tmp, bottom_tmp,
          uv_top, uv_bottom,
          thick_top / msubdivide * 0.5f, 
          thick_bottom / msubdivide * 0.5f
          );
        
        top_tmp.add(top_step);
        bottom_tmp.add(bottom_step);
        
        uv_top.x += uv_step.x;
        uv_bottom.x += uv_step.x;
        
      }
      
      top.add( step );
      bottom.add( step );
      
      uv_top.x = start_uv.x;
      uv_bottom.x = start_uv.x + uv_step.x;
      uv_top.y += uv_step.y;
      uv_bottom.y += uv_step.y;
      
      thick_top += thick_step;
      thick_bottom += thick_step;
      
    }
    
  }
  
  protected void generate_geometry() {
    ArrayList<float[]> tmp_faces = new ArrayList<float[]>();
    generate_geometry(
      tmp_faces,
      a, mid,
      new PVector( 0,0 ),
      new PVector( 1,0.5f ),
      thickness_a,
      thickness_mid
      );
    generate_geometry(
      tmp_faces,
      b, mid,
      new PVector( 0,1 ),
      new PVector( 1,0.5f ),
      thickness_b,
      thickness_mid
      );
    geom_uv = new float[ tmp_faces.size() ][5];
    for( int i = 0; i < geom_uv.length; ++i ) {
      geom_uv[i] = tmp_faces.get(i);
    }
  }

  protected void update_vectors() {
    dir.set( b.x-a.x, b.y-a.y, b.z-a.z );
    dir.normalize();
    normal.set( b.x-a.x, b.y-a.y, 0 );
    mid.set( normal.x * 0.5f, normal.y * 0.5f, b.z-a.z );
    normal.normalize();
    normal = normal.cross( new PVector(0, 0, 1) );
    mid.add( a );
  }

  public void edit() {
    if ( parent == null ) return;
    parent.noFill();
    if ( selected == a ) {
      parent.stroke( 255, 0, 0 );
    } else {
      parent.stroke( 255, 255, 0 );
    }
    parent.ellipse( a.x, a.y, 20, 20 );
    if ( selected == b ) {
      parent.stroke( 255, 0, 0 );
    } else {
      parent.stroke( 255, 255, 0 );
    }
    parent.ellipse( b.x, b.y, 20, 20 );
    parent.stroke( 255 );
    if ( debugged ) {
      parent.pushMatrix();
      parent.translate( a.x - 5 - dir.x * 15, a.y - dir.y * 15, a.z - dir.z * 15 );
      parent.text( "a", 0, 0 );
      parent.popMatrix();
      parent.pushMatrix();
      parent.translate( b.x - 5 + dir.x * 15, b.y + dir.y * 15, b.z + dir.z * 15 );
      parent.text( "b", 0, 0 );
      parent.popMatrix();
    }
  }

  public PVector hit( int mx, int my ) {
    if ( parent == null ) return null;
    if ( parent.dist( mx, my, a.x, a.y ) < 10 ) {
      selected = a;
    } else if ( parent.dist( mx, my, b.x, b.y ) < 10 ) {
      selected = b;
    } else {
      selected = null;
    }
    return selected;
  }
  
  protected void parse_custom(OscMessage msg) {
    // nothing to do here
  }
  
}