/**
 * Created by Trist on 22-5-2017.
 */
public class Soldier {
    private int x;
    private int y;
    private boolean isHit;

    public Soldier(int x, int y, boolean isHit) {
        this.x = x;
        this.y = y;
        this.isHit = isHit;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isHit() {
        return isHit;
}
}
