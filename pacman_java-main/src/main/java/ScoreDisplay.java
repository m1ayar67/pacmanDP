import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ScoreDisplay implements ScoreObserver {
    private static final Font SCORE_FONT = new Font("Arial", Font.PLAIN, 20);

    private int score;

    @Override
    public void update(int newScore) {
        score = newScore;
    }

    public void draw(Graphics graphics, int lives, boolean gameOver, int x, int y) {
        graphics.setFont(SCORE_FONT);
        graphics.setColor(Color.WHITE);

        String text = gameOver
                ? "Game Over " + score
                : "x" + lives + " Score: " + score;

        graphics.drawString(text, x, y);
    }
}
