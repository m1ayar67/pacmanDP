public class PlayingState implements GameState {

    @Override
    public void doAction(PacMan context) {
        context.move();
    }

    @Override
    public String toString() {
        return "Playing State";
    }
}
