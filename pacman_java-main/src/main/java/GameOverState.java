public class GameOverState implements GameState {

    @Override
    public void doAction(PacMan context) {
        context.stopGame();
    }

    @Override
    public String toString() {
        return "Game Over State";
    }
}
