public interface MovementStrategy {
    void updateDirection(Ghost ghost, Block pacman, int tileSize);
}
