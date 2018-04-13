
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
  return dataPath("") + "/flatmap";
}

public synchronized void save_flatmap() {
  // https://www.tutorialspoint.com/java/java_serialization.htm
  try {
    FileOutputStream fileOut = new FileOutputStream(serialisation_path());
    ObjectOutputStream out = new ObjectOutputStream(fileOut);
    out.writeObject( map );
    out.close();
    fileOut.close();
  } 
  catch( IOException e ) {
    e.printStackTrace();
  }
}

public synchronized void load_flatmap() {
  // https://beginnersbook.com/2013/12/how-to-serialize-arraylist-in-java/
  try {
    FileInputStream fis = new FileInputStream(serialisation_path());
    ObjectInputStream ois = new ObjectInputStream(fis);
    map = (FlatMap) ois.readObject();
    ois.close();
    fis.close();
  } 
  catch( IOException e ) {
    e.printStackTrace();
    return;
  } 
  catch(ClassNotFoundException e) {
    System.out.println("Class not found");
    //e.printStackTrace();
    return;
  } 
  catch(ClassCastException e) {
    System.out.println("Wrong class");
    //e.printStackTrace();
    return;
  }
  for ( Mappable m  : map.ms ) {
    m.set_parent(this);
  }
}