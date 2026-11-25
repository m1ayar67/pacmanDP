import java.util.List;

public class ChaseStrategy implements MovementStrategy {

    @Override
    public void updateDirection(Ghost ghost, Block pacman, int tileSize) {

        int ghostRow = ghost.y / tileSize;
        int ghostCol = ghost.x / tileSize;
        int pacmanRow = pacman.y / tileSize;
        int pacmanCol = pacman.x / tileSize;

        List<int[]> path = AStar.aStar(
            ghostRow, ghostCol, pacmanRow, pacmanCol
        );

        if (path != null && path.size() > 1) {

            int[] nextStep = path.get(1);

            int nextRow = nextStep[0];
            int nextCol = nextStep[1];

            if (nextRow > ghostRow) {
                ghost.updateDirection('D');
            } else if (nextRow < ghostRow) {
                ghost.updateDirection('U');
            } else if (nextCol > ghostCol) {
                ghost.updateDirection('R');
            } else if (nextCol < ghostCol) {
                ghost.updateDirection('L');
            }
        }
    }
}
