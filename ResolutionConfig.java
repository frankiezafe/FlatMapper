import processing.data.JSONObject;

class ResolutionConfig {
  
  static public int width = 1280;
  static public int height = 720;
  static public boolean fullscreen = false;
  static public int screenid = 1;
  static public int offsetx = 0;
  static public int offsety = 0;
  static public boolean autoresize = false;
  
  static JSONObject json() {
    
    JSONObject data = new JSONObject();
    data.setInt("width", width);
    data.setInt("height", height);
    data.setBoolean("fullscreen", fullscreen);
    data.setInt("screenid", screenid);
    data.setInt("offsetx", offsetx);
    data.setInt("offsety", offsety);
    data.setBoolean("autoresize", autoresize);
    return data;
    
  }
  
}