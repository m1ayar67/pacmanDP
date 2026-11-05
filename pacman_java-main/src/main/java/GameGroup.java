import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class GameGroup implements GameComponent {

    private List<GameComponent> components = new ArrayList<>();

    @Override
    public void add(GameComponent component) {
        components.add(component);
    }

    @Override
    public void remove(GameComponent component) {
        components.remove(component);
    }

    @Override
    public GameComponent getChild(int i) {
        return components.get(i);
    }

    @Override
    public void draw(Graphics g) {
        for (GameComponent component : components) {
            component.draw(g);
        }
    }
}