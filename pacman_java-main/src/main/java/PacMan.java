import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;


import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PacMan extends JPanel implements ActionListener, KeyListener {

    private static PacMan instance;
    
    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;

    private int width = columnCount * tileSize;
    private int height = rowCount * tileSize;

    private Image wallImage;
    private Image powerFood;

    private Image redGhostImage;
    private Image pinkGhostImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;

    private Image pacmanRight;
    private Image pacmanLeft;
    private Image pacmanUp;
    private Image pacmanDown;

    // Prototype objects
    private Ghost redGhostPrototype;
    private Ghost pinkGhostPrototype;
    private Ghost blueGhostPrototype;
    private Ghost orangeGhostPrototype;
    
    // Composite objects
    private GameGroup gameObjects;
    private GameGroup wallsGroup;
    private GameGroup foodsGroup;
    private GameGroup ghostsGroup;

    // X = wall, O = skip, P = Pac-Man, space = food
    // Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX         X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Ghost> ghosts;
    Block pacman;

    Timer gameLoop;

    // Strategy object
    private final MovementStrategy chaseStrategy = new ChaseStrategy();
    private final MovementStrategy randomStrategy = new RandomStrategy();

    private GameState state;
    int score = 0;
    int lives = 3;
    

    public static PacMan getInstance() {
        if (instance == null) {
            instance = new PacMan();
        }
        return instance;
    }
    
    public void setState(GameState state){
        this.state = state;
    }
    public GameState getState(){
        return state;
    }
    public void stopGame(){
        gameLoop.stop();
    }

    private PacMan() {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);

        addKeyListener(this);
        setFocusable(true);

        // Load images
        wallImage = new ImageIcon(
            getClass().getResource("/wall.png")
        ).getImage();

        powerFood = new ImageIcon(
            getClass().getResource("/powerFood.png")
        ).getImage();

        redGhostImage = new ImageIcon(
            getClass().getResource("/redGhost.png")
        ).getImage();

        pinkGhostImage = new ImageIcon(
            getClass().getResource("/pinkGhost.png")
        ).getImage();

        blueGhostImage = new ImageIcon(
            getClass().getResource("/blueGhost.png")
        ).getImage();

        orangeGhostImage = new ImageIcon(
            getClass().getResource("/orangeGhost.png")
        ).getImage();

        pacmanRight = new ImageIcon(
            getClass().getResource("/pacmanRight.png")
        ).getImage();

        pacmanLeft = new ImageIcon(
            getClass().getResource("/pacmanLeft.png")
        ).getImage();

        pacmanUp = new ImageIcon(
            getClass().getResource("/pacmanUp.png")
        ).getImage();

        pacmanDown = new ImageIcon(
            getClass().getResource("/pacmanDown.png")
        ).getImage();

        // Create Prototype objects
        redGhostPrototype = new Ghost(
            0, 0, tileSize, tileSize, redGhostImage
        );

        pinkGhostPrototype = new Ghost(
            0, 0, tileSize, tileSize, pinkGhostImage
        );

        blueGhostPrototype = new Ghost(
            0, 0, tileSize, tileSize, blueGhostImage
        );

        orangeGhostPrototype = new Ghost(
            0, 0, tileSize, tileSize, orangeGhostImage
        );

        // Build the map using the prototypes
        loadMap();

        // Set walls for Pac-Man and all ghosts
        pacman.setWalls(walls);

        for (Ghost ghost : ghosts) {
            ghost.setWalls(walls);
        }

// Strategy Pattern: Set the initial movement strategy for each ghost
for (Ghost ghost : ghosts) {
    ghost.setStrategy(chaseStrategy);
    ghost.applyStrategy(pacman, tileSize);
}
        setState(new PlayingState());

        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    public void loadMap() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Ghost>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {

                String row = tileMap[r];
                char tile = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tile == 'X') {
                    walls.add(
                        new Block(
                            x, y, tileSize, tileSize, wallImage
                        )
                    );

                } else if (tile == 'O') {
                    foods.add(
                        new Block(x, y, tileSize, tileSize, null)
                    );

                } else if (tile == 'P') {
                    pacman = new Block(
                        x, y, tileSize, tileSize, pacmanRight
                    );

                } else if (tile == ' ') {
                    foods.add(
                        new Block(
                            x + 14, y + 14, 4, 4, powerFood
                        )
                    );
                    } else if (tile == 'r') {
                        ghosts.add(redGhostPrototype.clone(x, y));
                    } else if (tile == 'p') {
                        ghosts.add(pinkGhostPrototype.clone(x, y));
                    } else if (tile == 'b') {
                        ghosts.add(blueGhostPrototype.clone(x, y));
                    } else if (tile == 'o') {
                        ghosts.add(orangeGhostPrototype.clone(x, y));
                    }
            }
        }
         // Create the main Composite and subgroups
        gameObjects = new GameGroup();

        wallsGroup = new GameGroup();
        foodsGroup = new GameGroup();
        ghostsGroup = new GameGroup();

        // Add walls to walls group
        for (Block wall : walls) {
            wallsGroup.add(wall);
        }

        // Add foods to foods group
        for (Block food : foods) {
            foodsGroup.add(food);
        }

        // Add ghosts to ghosts group
        for (Ghost ghost : ghosts) {
            ghostsGroup.add(ghost);
        }

        // Add groups and Pac-Man to the main group
        gameObjects.add(wallsGroup);
        gameObjects.add(foodsGroup);
        gameObjects.add(ghostsGroup);
        gameObjects.add(pacman);
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
    gameObjects.draw(g); // Composite: يرسم عناصر اللعبة

    g.setFont(new Font("Arial", Font.PLAIN, 20));
    g.setColor(Color.WHITE);

    if (state instanceof  GameOverState) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial",Font.BOLD,30));
        
        String message= "GAME OVER";
    int x = (width - g.getFontMetrics().stringWidth(message)) / 2;
    int y = height / 2;

    g.drawString(message, x, y);
    } else {
        g.drawString("x" + lives + " Score: " + score,
                     tileSize / 2 + 2, tileSize / 2 + 2);
    }
}

    void move() {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        // Check collision between Pac-Man and walls
        for (Block wall : walls) {
            if (checkCollision(pacman, wall)
                    || pacman.x < 0
                    || pacman.x + pacman.width > width) {

                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
            }
        }

        for (Ghost ghost : ghosts) {

            // Check collision between Pac-Man and a ghost
            if (checkCollision(pacman, ghost)) {
                lives--;

                if (lives == 0) {
                    setState(new GameOverState());

                } else {
                    pacman.reset();
                    pacman.velocityX = 0;
                    pacman.velocityY = 0;


// Strategy Pattern: Reset ghosts and apply chase strategy
for (Ghost ghost2 : ghosts) {
    ghost2.reset();
    ghost2.setStrategy(chaseStrategy);
    ghost2.applyStrategy(pacman, tileSize);
}
                }
            }

            if (ghost.y == tileSize * 9
                    && ghost.direction != 'U'
                    && ghost.direction != 'D') {

                ghost.updateDirection('U');
            }

ghost.x += ghost.velocityX;
ghost.y += ghost.velocityY;


            for (Block wall : walls) {
                if (checkCollision(ghost, wall)
                        || ghost.x < 0
                        || ghost.x + ghost.width > width) {

                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;

// Strategy Pattern: Apply random movement when hitting a wall
ghost.setStrategy(randomStrategy);
ghost.applyStrategy(pacman, tileSize);

                }
            }
        }

        // Check food collision
        Block eatenFood = null;

        for (Block food : foods) {
            if (checkCollision(pacman, food)) {
                eatenFood = food;
                break;
            }
        }

        if (eatenFood != null) {
    foods.remove(eatenFood);
    foodsGroup.remove(eatenFood); // Composite: يشيل النقطة من مجموعة الرسم
    score += 10;
}
    }

    public boolean checkCollision(Block a, Block b) {
        return a.x < b.x + b.width
            && a.x + a.width > b.x
            && a.y < b.y + b.height
            && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       state.doAction(this);
        repaint();

        if(foods.size()==0){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        if(!(state instanceof PlayingState)){
            return ;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');

        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        if (pacman.direction == 'U') {
            pacman.image = pacmanUp;

        } else if (pacman.direction == 'D') {
            pacman.image = pacmanDown;

        } else if (pacman.direction == 'L') {
            pacman.image = pacmanLeft;

        } else if (pacman.direction == 'R') {
            pacman.image = pacmanRight;
        }
    }
}
