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

}