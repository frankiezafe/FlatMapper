import processing.data.JSONObject;

class ResolutionConfig {
  
  static public int width = 1280;
  static public int height = 720;
  static public boolean fullscreen = false;
  static public int screenid = 1;
  static public int offsetx = 0;
  static public int offsety = 0;
  static public boolean autoresize = false;
  
  static void json( JSONObject data ) {
    
    JSONObject sub = new JSONObject();
    sub.setInt("width", width);
    sub.setInt("height", height);
    sub.setBoolean("fullscreen", fullscreen);
    sub.setInt("screenid", screenid);
    sub.setInt("offsetx", offsetx);
    sub.setInt("offsety", offsety);
    sub.setBoolean("autoresize", autoresize);
    data.setJSONObject("ResolutionConfig", sub);
    
  }
  
}