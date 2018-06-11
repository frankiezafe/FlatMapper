
// #####################################
// ################ OSC ################
// #####################################

public void start_osc() {
  oscP5 = new OscP5( this, oscP5_port );
}

public void oscEvent(OscMessage msg) {

  if ( editmode != ControlFrame.EDITMODE_NORMAL ) {
    return;
  }
  
  if ( msg.checkAddrPattern("/tube/length") ) {
    demo_rt_length  = msg.get(0).floatValue();
  }
  
  for( Mappable m : map.ms ) {
    m.parse( msg );
  }
  
}

// #####################################
// ############# TEXTURES ##############
// #####################################

public PImage get_default_texture() {
  return get_texture( default_texture_path );
}

public PImage get_texture( String path ) {
  if ( !texture_atlas.containsKey( path ) && !load_texture( path ) ) {
    return null;
  }
  return texture_atlas.get( path );
}

public PImage get_texture( int id ) {
    String[] names = new String[texture_atlas.keySet().size()];
    texture_atlas.keySet().toArray(names);
    return texture_atlas.get( names[ id ] );
}

private void load_default_texture() {
  texture_atlas = new java.util.HashMap<String,PImage>();
  load_texture( default_texture_path );
}

private boolean load_texture( String path ) {
  try{ 
    PImage tmp = loadImage( path );
    texture_atlas.put( path, tmp );
    return true;
  } catch( Exception e ) {
    
  }
  return false;
}

private void register_movie( String name, Movie mov ) {
  texture_atlas.put( name, mov );
}

private void register_pgraphic( String name, PGraphics pg ) {
  texture_atlas.put( name, pg );
}

// #####################################
// ########### SERIALISATION ###########
// #####################################

public String serialisation_path() {
  return dataPath("") + "/flatmap.json";
}

public synchronized void save_flatmap() {
  
  try {
    
    JSONObject _data = new JSONObject();
    _data.setJSONObject("ResolutionConfig",  ResolutionConfig.json() );
    int i;
    JSONArray mappables = new JSONArray();
    i = 0;
    for ( Mappable m : map.ms ) {
      mappables.setJSONObject( i, m.json() );
      ++i;
    }
    _data.setJSONArray( "Mappable", mappables );
    saveJSONObject( _data, serialisation_path() );
    
  } catch ( Exception e ) {
  
  }
  
}

public synchronized void load_flatmap() {

  if ( map == null ) {
    map = new FlatMap();
  } else {
    map.purge();
  }
  
  JSONObject _data = loadJSONObject( serialisation_path() );
  JSONArray mappables_data = _data.getJSONArray("Mappable");
  for (int i = 0; i < mappables_data.size(); i++) {
    JSONObject m_data = mappables_data.getJSONObject(i);
    if ( m_data.getString( "type" ).equals( "Line" ) ) {
      Line l = new Line();
      if ( l.json( m_data ) ) {
        map.ms.add( l );
        map.ls.add( l );
      }
    } else if ( m_data.getString( "type" ).equals( "Plane" ) ) {
      Plane p = new Plane();
      if ( p.json( m_data ) ) {
        System.out.println( "Plane sucessfully decompressed" );
        map.ms.add( p );
        map.ps.add( p );
      }
    }
    
    for ( Mappable m : map.ms ) {
      m.set_parent( this );
    }
    
  }
  //println( mappables );

  // https://beginnersbook.com/2013/12/how-to-serialize-arraylist-in-java/
  //try {
  //  FileInputStream fis = new FileInputStream(serialisation_path());
  //  ObjectInputStream ois = new ObjectInputStream(fis);
  //  map = (FlatMap) ois.readObject();
  //  ois.close();
  //  fis.close();
  //} 
  //catch( IOException e ) {
  //  e.printStackTrace();
  //  return;
  //} 
  //catch(ClassNotFoundException e) {
  //  System.out.println("Class not found");
  //  //e.printStackTrace();
  //  return;
  //} 
  //catch(ClassCastException e) {
  //  System.out.println("Wrong class");
  //  //e.printStackTrace();
  //  return;
  //}
  //for ( Mappable m  : map.ms ) {
  //  m.set_parent(this);
  //}
}