import processing.core.PVector;
import processing.data.JSONObject;
import processing.data.JSONArray;

public class FlatMap implements java.io.Serializable {

  public java.util.ArrayList<Mappable> ms;
  public java.util.ArrayList<Line> ls;
  public java.util.ArrayList<Plane> ps;
  
  public FlatMap() {
    ms = new java.util.ArrayList<Mappable>();
    ls = new java.util.ArrayList<Line>();
    ps = new java.util.ArrayList<Plane>();
  };
  
  public void purge() {
    ms.clear();
    ls.clear();
    ps.clear();
  }
  
  public void add( Line l ) {
    ms.add(l);
    ls.add(l);
  }
  
  public void add( Plane p ) {
    ms.add(p);
    ps.add(p);
  }
  
  public void rmv( Line l ) {
    ms.remove(l);
    ls.remove(l);
  }
  
  public void rmv( Plane p ) {
    ms.remove(p);
    ps.remove(p);
  }

  public static JSONArray obj2json( PVector[] vs ) {
    JSONArray array = new JSONArray();
    for ( int i = 0; i < vs.length; ++i ) {
      array.setJSONObject( i, obj2json( vs[i] ) );
    }
    return array;
  }
  
  public static JSONObject obj2json( PVector v ) {
    JSONObject data = new JSONObject();
    data.setString( "type", "PVector" );
    JSONArray array = new JSONArray();
    array.setFloat( 0, v.x );
    array.setFloat( 1, v.y );
    array.setFloat( 2, v.z );
    data.setJSONArray( "values", array );
    return data;
  }
  
  public static JSONObject obj2json( float[] v ) {
    JSONObject data = new JSONObject();
    data.setString( "type", "float[]" );
    JSONArray array = new JSONArray();
    for ( int i = 0; i < v.length; ++i ) {
      array.setFloat( i, v[i] );
    }
    data.setJSONArray( "values", array );
    return data;
  }
  
  public static PVector[] json2pvector_array( JSONArray array ) {
    PVector[] out = new PVector[ array.size() ];
    for ( int i = 0; i < out.length; ++i ) {
      out[i] = json2pvector( array.getJSONObject( i ) );
    }
    return out;
  }
  
  public static PVector json2pvector( JSONObject d ) {
    if ( !d.getString( "type" ).equals( "PVector" ) ) {
      System.err.println( "json2obj: Failed to retrieve PVector from:" + d   );
    }
    JSONArray array = d.getJSONArray("values");
    PVector out = new PVector();
    out.x = array.getFloat(0);
    out.y = array.getFloat(1);
    out.z = array.getFloat(2);
    return out;
  }
  
  public static float[] json2float_array( JSONObject data ) {
    float[] out = null;
    if ( !data.getString( "type" ).equals( "float[]" ) ) {
      System.err.println( "json2obj: Failed to retrieve float[] from:" + data );
      return out;
    }
    JSONArray array = data.getJSONArray("values");
    out = new float[ array.size() ];
    for ( int i = 0; i < out.length; ++i ) {
      out[i] = array.getFloat( i );
    }
    return out;
  }

}