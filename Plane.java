import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import oscP5.*;

public class Plane extends Mappable {
  
  private PVector[] points;
  private float[][] uvs;
  
  Plane() {
    
    super();
    parent = null;
    texture = null;
    texture_path = null;
    geom_uv = null;
    init();
    
    points = null;
    
  }
  
  Plane( FlatMapper parent, PVector a, PVector b, PVector c, PVector d ) {
    
    super();
    
    this.parent = parent;
    texture = null;
    texture_path = null;
    geom_uv = null;
    init();
    
    points = new PVector[4];
    points[0] = a;
    points[1] = b;
    points[2] = c;
    points[3] = d;
    generate_geometry();
    
  }
  
  protected void init() {
    
    rgba = new float[] { 1,1,1,1 };
    uvs = new float[4][2];
    uvs[0] = new float[]{ 0, 0 };
    uvs[1] = new float[]{ 1, 0 };
    uvs[2] = new float[]{ 1, 1 };
    uvs[3] = new float[]{ 0, 1 };
    
  }
  
  public void setup( float centerx, float centery, float width, float height ) {
    
    points = new PVector[4];
    points[0] = new PVector( centerx - width * 0.5f, centery - height * 0.5f, 0 );
    points[1] = new PVector( centerx + width * 0.5f, centery - height * 0.5f, 0 );
    points[2] = new PVector( centerx + width * 0.5f, centery + height * 0.5f, 0 );
    points[3] = new PVector( centerx - width * 0.5f, centery + height * 0.5f, 0 );
    generate_geometry();
    
  }
  
  public void setup( PVector a, PVector b, PVector c, PVector d ) {
    
    points = new PVector[4];
    points[0] = a;
    points[1] = b;
    points[2] = c;
    points[3] = d;
    generate_geometry();
    
  }
  
  protected void generate_geometry() {
    
    if ( points == null ) return;
    
    ArrayList<float[]> tmp_faces = new ArrayList<float[]>();
    
    // better rendering, done by subdividing the quad in a 
    // 9 x 9 grid of quads
    PVector left_delta = new PVector();
    left_delta.add( points[0] );
    left_delta.mult( -1 );
    left_delta.add( points[3] );
    left_delta.mult( 1.f / msubdivide );

    PVector right_delta = new PVector();
    right_delta.add( points[1] );
    right_delta.mult( -1 );
    right_delta.add( points[2] );
    right_delta.mult( 1.f / msubdivide );

    PVector left_top = new PVector();
    left_top.add( points[0] );
    PVector left_bottom = new PVector();
    left_bottom.add( left_top );
    left_bottom.add( left_delta );

    PVector right_top = new PVector();
    right_top.add( points[1] );
    PVector right_bottom = new PVector();
    right_bottom.add( right_top );
    right_bottom.add( right_delta );

    for ( float y = 0; y < msubdivide; ++y ) {

      PVector top_delta = new PVector();
      top_delta.add( left_top );
      top_delta.mult( -1 );
      top_delta.add( right_top );
      top_delta.mult( 1.f / msubdivide );

      PVector bottom_delta = new PVector();
      bottom_delta.add( left_bottom );
      bottom_delta.mult( -1 );
      bottom_delta.add( right_bottom );
      bottom_delta.mult( 1.f / msubdivide );

      PVector top = new PVector();
      top.add( left_top );

      PVector bottom = new PVector();
      bottom.add( left_bottom );

      float[] vdata = null;

      for ( float x = 0; x < msubdivide; ++x ) {
        
        vdata = new float[] {
          top.x, 
          top.y, 
          top.z, 
          (x + 0) / msubdivide, (y + 0) / msubdivide
        };
        tmp_faces.add(vdata);
        
        vdata = new float[] {
          top.x + top_delta.x, 
          top.y + top_delta.y, 
          top.z + top_delta.z, 
          (x + 1) / msubdivide, (y + 0) / msubdivide
        };
        tmp_faces.add(vdata);
        
        vdata = new float[] {
          bottom.x + bottom_delta.x, 
          bottom.y + bottom_delta.y, 
          bottom.z + bottom_delta.z, 
          (x + 1) / msubdivide, (y + 1) / msubdivide
        };
        tmp_faces.add(vdata);
        
        vdata = new float[] {
          bottom.x, 
          bottom.y, 
          bottom.z, 
          (x + 0) / msubdivide, (y + 1) / msubdivide
        };
        tmp_faces.add(vdata);

        top.add( top_delta );
        bottom.add( bottom_delta );
        
      }

      left_top.add( left_delta );
      left_bottom.add( left_delta );
      right_top.add( right_delta );
      right_bottom.add( right_delta );
      
    }
    
    int tmps = tmp_faces.size();
    geom_uv = new float[ ( tmps / 2 ) * 3 ][5];
    int gid = 0;
    for( int i = 0; i < tmps; i += 4 ) {
      geom_uv[gid++] = tmp_faces.get(i + 0);
      geom_uv[gid++] = tmp_faces.get(i + 1);
      geom_uv[gid++] = tmp_faces.get(i + 2);
      geom_uv[gid++] = tmp_faces.get(i + 2);
      geom_uv[gid++] = tmp_faces.get(i + 3);
      geom_uv[gid++] = tmp_faces.get(i + 0);
    }
      
  }
  
  public PVector get_vertex( int i ) {
    if ( points == null || i < 0 || i > 3 ) return null;
    return points[i];
  }
  
  public void set_vertex( int i, PVector v ) {
    if ( i < 0 || i > 3 ) return;
    points[i].set( v.x, v.y, v.z );
    update_vectors();
    generate_geometry();
  }
  
  protected void update_vectors() {}
  
  public void edit() {
    if ( parent == null ) return;
    parent.noFill();
    for( int i = 0; i < 4; ++i ) {
      if ( selected == points[i] ) {
        parent.stroke( 255, 0, 0 );
      } else {
        parent.stroke( 255, 255, 0 );
      }
      parent.ellipse( points[i].x, points[i].y, 20, 20 );
    }
  }
  
  public PVector hit( int mx, int my ) {
    if ( parent == null ) return null;
    selected = null;
    float closest = 0;
    for( int i = 0; i < 4; ++i ) {
      float d = parent.dist( mx, my, points[i].x, points[i].y );
      if ( d < 10 && ( selected == null || closest > d ) ) {
        selected = points[i];
        closest = d;
      }
    }
    return selected;
  }
  
  protected void parse_custom(OscMessage msg) {
    // nothing to do here
  }
  
}