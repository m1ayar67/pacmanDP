import java.awt.Graphics;

public interface GameComponent {

    void draw(Graphics g);

    void add(GameComponent component);

    void remove(GameComponent component);

    GameComponent getChild(int i);
}
