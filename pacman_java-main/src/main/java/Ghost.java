import java.awt.Image;

public class Ghost extends Block {

    // Strategy Pattern
    private MovementStrategy strategy;

    public void setStrategy(MovementStrategy strategy) {
        this.strategy = strategy;
    }

    public void applyStrategy(Block pacman, int tileSize) {
        if (strategy != null) {
            strategy.updateDirection(this, pacman, tileSize);
        }
    }

    public Ghost(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }

    // Prototype: create a copy of this ghost
    public Ghost clone(int x, int y) {
        Ghost copy = new Ghost(
            x,
            y,
            this.width,
            this.height,
            this.image
        );
        
        copy.startX = x;
        copy.startY = y;

        copy.direction = this.direction;
        copy.velocityX = this.velocityX;
        copy.velocityY = this.velocityY;

        return copy;
    }
}
