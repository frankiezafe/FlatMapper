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
  
  public Line get_line( int i ) {
    
    if ( i < 0 || i >= ls.size() ) {
      return null;
    }
    
    return ls.get(i);
    
  }
  
  public Plane get_plane( int i ) {
    
    if ( i < 0 || i >= ps.size() ) {
      return null;
    }
    
    return ps.get(i);
    
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