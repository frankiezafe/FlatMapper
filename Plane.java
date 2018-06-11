import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import java.util.ArrayList;
import oscP5.*;

public class Plane extends Mappable {

  private PVector[] points;
  
  public JSONObject json() {
    JSONObject data = super.json();
    data.setString("type",  "Plane" );
    data.setJSONArray("points",  FlatMapper.obj2json( points ) );
    return data;
  }
  
  public boolean json( JSONObject data ) {
    if ( !data.getString("type").equals( "Plane" ) ) {
      System.err.println( "Plane::json: invalid data type for " + data );
      return false;
    }
    super.json( data );
    FlatMapper.json2obj( data.getJSONArray( "points" ), points );
    return true;
  }

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

    rgba = new float[] { 1, 1, 1, 1 };
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

  protected void compute_delta( int type, PVector xyz, PVector uv, PVector from, PVector to, PVector from_uv, PVector to_uv ) {
    
    switch( type ) {
      case DELTA_LEFT:
        from = points[0];
        to = points[3];
        from_uv = uv_quad[0];
        to_uv = uv_quad[3];
        break;
        
      case DELTA_RIGHT:
        from = points[1];
        to = points[2];
        from_uv = uv_quad[1];
        to_uv = uv_quad[2];
        break;
        
      case DELTA_DEFAULT:
        break;
        
      default:
        return;
        
    }
    
    xyz.set( 0, 0, 0 );
    xyz.add( from );
    xyz.mult( -1 );
    xyz.add( to );
    uv.set( 0, 0 );
    uv.add( from_uv );
    uv.mult( -1 );
    uv.add( to_uv );
    
    xyz.mult( 1.f / msubdivide );
    uv.mult( 1.f / msubdivide );
    
  }

  protected void generate_geometry() {

    if ( points == null ) return;

    ArrayList<float[]> tmp_faces = new ArrayList<float[]>();

    // better rendering, done by subdividing the quad in a 
    // msubdivide x msubdivide grid of quads

    PVector left_delta = new PVector();
    PVector left_delta_uv = new PVector();
    compute_delta( DELTA_LEFT, left_delta, left_delta_uv, null, null, null, null );

    PVector right_delta = new PVector();
    PVector right_delta_uv = new PVector();
    compute_delta( DELTA_RIGHT, right_delta, right_delta_uv, null, null, null, null );

    // xyz
    PVector left_top = new PVector();
    left_top.add( points[0] );
    PVector left_bottom = new PVector();
    left_bottom.add( left_top );
    left_bottom.add( left_delta );
    // uvs
    PVector left_top_uv = new PVector();
    left_top_uv.add( uv_quad[0] );
    PVector left_bottom_uv = new PVector();
    left_bottom_uv.add( left_top_uv );
    left_bottom_uv.add( left_delta_uv );

    // xyz
    PVector right_top = new PVector();
    right_top.add( points[1] );
    PVector right_bottom = new PVector();
    right_bottom.add( right_top );
    right_bottom.add( right_delta );
    // uvs
    PVector right_top_uv = new PVector();
    right_top_uv.add( uv_quad[1] );
    PVector right_bottom_uv = new PVector();
    right_bottom_uv.add( right_top_uv );
    right_bottom_uv.add( right_delta_uv );

    for ( float y = 0; y < msubdivide; ++y ) {

      PVector top_delta = new PVector();
      PVector top_delta_uv = new PVector();
      compute_delta( DELTA_DEFAULT, top_delta, top_delta_uv, left_top, right_top, left_top_uv, right_top_uv );
      
      PVector bottom_delta = new PVector();
      PVector bottom_delta_uv = new PVector();
      compute_delta( DELTA_DEFAULT, bottom_delta, bottom_delta_uv, left_bottom, right_bottom, left_bottom_uv, right_bottom_uv );

      PVector top = new PVector();
      top.add( left_top );
      PVector top_uv = new PVector();
      top_uv.add( left_top_uv );

      PVector bottom = new PVector();
      bottom.add( left_bottom );
      PVector bottom_uv = new PVector();
      bottom_uv.add( left_bottom_uv );

      float[] vdata = null;

      for ( float x = 0; x < msubdivide; ++x ) {

        vdata = new float[] { top.x, top.y, top.z, top_uv.x, top_uv.y };
        tmp_faces.add(vdata);

        vdata = new float[] { 
          top.x + top_delta.x, 
          top.y + top_delta.y, 
          top.z + top_delta.z,
          top_uv.x + top_delta_uv.x, 
          top_uv.y + top_delta_uv.y
        };
        tmp_faces.add(vdata);

        vdata = new float[] {
          bottom.x + bottom_delta.x, 
          bottom.y + bottom_delta.y, 
          bottom.z + bottom_delta.z,
          bottom_uv.x + bottom_delta_uv.x, 
          bottom_uv.y + bottom_delta_uv.y 
        };
        tmp_faces.add(vdata);

        vdata = new float[] { bottom.x, bottom.y, bottom.z, bottom_uv.x, bottom_uv.y };
        tmp_faces.add(vdata);

        top.add( top_delta );
        top_uv.add( top_delta_uv );
        bottom.add( bottom_delta );
        bottom_uv.add( bottom_delta_uv );
        
      }

      left_top.add( left_delta );
      left_top_uv.add( left_delta_uv );
      left_bottom.add( left_delta );
      left_bottom_uv.add( left_delta_uv );
      right_top.add( right_delta );
      right_top_uv.add( right_delta_uv );
      right_bottom.add( right_delta );
      right_bottom_uv.add( right_delta_uv );
    }

    int tmps = tmp_faces.size();
    geom_uv = new float[ ( tmps / 2 ) * 3 ][5];
    int gid = 0;
    for ( int i = 0; i < tmps; i += 4 ) {
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

  protected void update_vectors() {
  }

  public void edit() {
    if ( parent == null ) return;
    parent.noFill();
    for ( int i = 0; i < 4; ++i ) {
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
    for ( int i = 0; i < 4; ++i ) {
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