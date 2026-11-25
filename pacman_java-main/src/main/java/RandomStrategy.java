import java.util.Random;

public class RandomStrategy implements MovementStrategy {

    private final char[] directions = {'U', 'D', 'L', 'R'};
    private final Random random = new Random();

    @Override
    public void updateDirection(Ghost ghost, Block pacman, int tileSize) {

        ghost.direction = directions[
            random.nextInt(directions.length)
        ];

        ghost.updateVelocity();
    }
}
