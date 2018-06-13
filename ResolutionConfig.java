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