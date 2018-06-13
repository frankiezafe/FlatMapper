// #####################################
// ############## PROJECT ##############
// #####################################

public void project_selector() {
  flatmap_path = "";
  JFileChooser fc = new JFileChooser();
  int i = fc.showDialog( null, "select flatmap" );
  if ( i == 0 ) {
    flatmap_path = fc.getSelectedFile().getPath();
    if ( !flatmap_path.substring( flatmap_path.length() - 8, flatmap_path.length() ).equals( ".flatmap" ) ) {
      flatmap_path += ".flatmap";
    }
    System.out.println( flatmap_path );
  }
}

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

class TextureRef {
   public PImage im = null;
   public Object src = null;
   public String type = "undefined";
}

public PImage get_default_texture() {
  return get_texture( default_texture_path );
}

public PImage get_texture( String path ) {
  if ( !texture_atlas.containsKey( path ) && !load_texture( path ) ) {
    return null;
  }
  return (PImage) texture_atlas.get( path ).src;
}

public TextureRef get_texture_ref( int id ) {
  String[] names = new String[texture_atlas.keySet().size()];
  texture_atlas.keySet().toArray(names);
  if ( id < 0 || id >= names.length ) {
    return null;
  }
  return texture_atlas.get( names[ id ] );
}

public TextureRef get_texture_ref( Object src ) {
  String[] names = new String[texture_atlas.keySet().size()];
  texture_atlas.keySet().toArray(names);
  for ( int i = 0; i < names.length; ++i ) {
    if ( texture_atlas.get( names[ i ] ).src == src ) {
      return texture_atlas.get( names[ i ] );
    }
  }
  return null;
}

public PImage get_texture( int id ) {
  TextureRef tr = get_texture_ref( id );
  if ( tr == null ) {
    return null;
  }
  return (PImage) tr.src;
}

public PImage get_texture_tumb( int id ) {
  TextureRef tr = get_texture_ref( id );
  if ( tr == null ) {
    return null;
  }
  return tr.im;
}

private void load_default_texture() {
  texture_atlas = new java.util.HashMap<String,TextureRef>();
  load_texture( default_texture_path );
}

private boolean load_texture( String path ) {
  try{ 
    TextureRef tr = new TextureRef();
    tr.src = loadImage( path );
    tr.im = (PImage) tr.src;
    tr.type = "PImage";
    texture_atlas.put( path, tr );
    return true;
  } catch( Exception e ) {
    
  }
  return false;
}

private void texture_register_movie( String name, Movie mov ) {
  TextureRef tr = new TextureRef();
  tr.im = mov;
  tr.src = mov;
  tr.type = "Movie";
  texture_atlas.put( name, tr );
}

private void texture_register_pgraphic( String name, PGraphics pg ) {
  TextureRef tr = new TextureRef();
  tr.src = pg;
  tr.type = "PGraphics";
  texture_atlas.put( name, tr );
}

private PImage texture_thumb( PGraphics src ) {
  PImage thumb = createImage( src.width, src.height, RGB );
  src.loadPixels();
  thumb.loadPixels();
  for( int i = 0; i < src.pixels.length; ++i ) {
    thumb.pixels[i] = src.pixels[i];
  }
  thumb.updatePixels();
  TextureRef tr = get_texture_ref( src );
  if ( tr != null ) {
    tr.im = thumb;
  }
  return thumb;
}

// #####################################
// ########### SERIALISATION ###########
// #####################################

public String serialisation_path() {
  if ( flatmap_path == null || flatmap_path.equals("") ) {
    flatmap_path = dataPath("") + "/flatmap.flatmap";
  }
  return flatmap_path;
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
  
  JSONObject _data;
  try { 
    _data = loadJSONObject( serialisation_path() );
  } catch ( Exception e ) {
    e.printStackTrace();
    return;
  }
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
  
}