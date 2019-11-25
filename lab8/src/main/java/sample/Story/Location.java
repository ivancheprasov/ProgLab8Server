package sample.Story;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {
    private static final long serialVersionUID = 6885365571214696713L;
    private int x,y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return x == location.x &&
                y == location.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Location(int x, int y){
        this.setX(x);
        this.setY(y);
    }
    public String getStringLocation(){
        return  "Расположение объекта в координатах: ("+getX()+","+getY()+").";
    }
}
