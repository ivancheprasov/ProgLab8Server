package sample.Story;

import java.io.Serializable;

public class Zipper implements Serializable {
    private static final long serialVersionUID = 6885365571214696713L;
    private boolean fasteness;
    public Zipper(){
        this.fasteness=true;
    }
    public boolean checkFasteness(){
        return this.fasteness;
    }
    public boolean interractWithZipper(boolean fasteness){
        this.fasteness=fasteness;
        return this.fasteness;
    }
}
