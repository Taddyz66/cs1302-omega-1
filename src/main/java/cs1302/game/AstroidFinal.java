package astroid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;

public class AstroidFinal  extends Game {
	private Random rng;       // random number generator
    private ImageView player; // some rectangle to represent the player
    private HashMap<Integer, AstroidRock> astroidMap = new HashMap<Integer, AstroidRock>();
    private Vector<Bullet> bulletQueue = new Vector<Bullet>();
    private int numLives;
    private int score;
    private int player_width;
    private int player_height;
    
    private Stage stage;
    
    // Todo:
    // done: 1. detect whether a bullet hit one rock
    // done: 2. rock broken into small rocks if it was a big rock, else if it was a small one, the rock got destroyed.
    // 3. detect whether the player was hit by a rock
    // 4. If player hit by the rock, lose current life/or game over if no more life remaining
    // 5. Determine how to calculate the score by counting the number of destroyed rocks
    // 6. Refining the relative location between the bullet and the player
    // 7. Refining the bullet amount to one at a time the space key was pressed
    
    
    //private IdleCat cat;      // the not so idle cat (see IdleCat.java)

    /**
     * Construct a {@code DemoGame} object.
     * @param width scene width
     * @param height scene height
     */
    public AstroidFinal(int width, int height, Stage stage) {
    	
        super(width, height, 60);            // call parent constructor
        setLogLevel(Level.INFO);             // enable logging
        this.rng = new Random();             // random number generator
        this.player = new ImageView("file:resources/spaceship.jpg"); // some rectangle to represent the player
        //this.player_height = Image("file:resources/spaceship.jpg")
        //this.cat  = new IdleCat(this);       // the not so idle cat (see IdleCat.java)
        this.astroidMap.put(0, new AstroidRock(this, true));
        
        
        setNumLives(5); // initial 5 lives for a game
        setScore(0); // initial score is 0
        
        this.stage = stage;
        
        
    } // DemoGame

    /** {@inheritDoc} */
    @Override
    protected void init() {
        // setup subgraph for this component
        getChildren().addAll(player);     //, cat    // add to main container
     // pass lambda expression to forEach()
        this.astroidMap.forEach((key, value) -> {
        	getChildren().addAll(value);
        });
        
        
        // setup player
        player.setX(300);                           // 50px in the x direction (right)
        player.setY(300);                           // 50ps in the y direction (down)
        //player.setOnMouseClicked(event -> handleClickPlayer(event));
        // setup the cat
        this.astroidMap.get(0).setX(100);
        this.astroidMap.get(0).setY(100);
        
        for (Bullet b : this.bulletQueue) {
        	getChildren().addAll(b);
        	b.setCenterX(300);
        	b.setCenterY(300);
        }
        //cat.setX(0);
        //cat.setY(0);
    } // init

    /** {@inheritDoc} */
    @Override
    protected void update() {

        // (x, y)         In computer graphics, coordinates along an x-axis and
        // (0, 0) -x--->  y-axis are used. When compared to the standard
        // |              Cartesian plane that most students are familiar with,
        // y              the x-axis behaves the same, but the y-axis increases
        // |              in the downward direction! Keep this in mind when
        // v              adjusting the x and y positions of child nodes.

        // update player position
        isKeyPressed( KeyCode.LEFT, () -> player.setRotate(player.getRotate() + 2)); //  moving spaceship conter-clockwise rotate
        isKeyPressed( KeyCode.RIGHT, () -> player.setRotate(player.getRotate() - 2)); //  moving spaceship clockwise rotation
        isKeyPressed(KeyCode.SPACE, () -> shootBullet(player.getRotate())); // spaceship shoot bullet
        
        isKeyPressed(KeyCode.A, () -> player.setX(player.getX() - 5.0));	// moving spaceship left
        isKeyPressed(KeyCode.W, () -> player.setY(player.getY() - 5.0));	// moving spaceship up
        isKeyPressed(KeyCode.S, () -> player.setY(player.getY() + 5.0));	// moving spaceship down
        isKeyPressed(KeyCode.D, () -> player.setX(player.getX() + 5.0));	// moving spaceship right
        
        
        System.out.println("Current score: " + this.getScore() + " Available lives: " + this.getNumLives());
        
        // ToDo: detect whether the player got hit by the rock -- check the lives and terminate the game if needed
        this.astroidMap.forEach((key, value) -> {
        	if (Math.abs(player.getX() - value.getX()) < 20 && Math.abs(player.getY() - value.getY()) < 20) {
        		this.numLives--;
        		if (this.numLives == 0) {
        			System.out.println("Game Over!!!!");
        			Platform.exit();
        		} else {
        			this.astroidMap.clear();
        			getChildren().clear();
        			this.astroidMap.put(0, new AstroidRock(this, true));
        			this.astroidMap.get(0).setX(rng.nextInt(0, 600));
        			this.astroidMap.get(0).setY(rng.nextInt(0, 600));
        			getChildren().addAll(this.astroidMap.get(0));
        			getChildren().addAll(this.player);
        			player.setX(300);
        			player.setY(300);
        		}
        	}
        });
        
        // <--------------------------------------------------------------------
        // try adding the code to make the player move up and down!
        // <--------------------------------------------------------------------

        // update idle cat
        //cat.update();
        
        Vector<Integer> crashId = new Vector<Integer>();
        this.astroidMap.forEach((key, value) -> {
        	int temp = value.update();
        	if (temp != -1) {
        		crashId.add(temp);
				
        	}
        });
        
        for (int id : crashId) {
        	
        	if (this.astroidMap.get(id).bigRock) {
        		for (int i = 0; i < 4; i++) {
    				this.astroidMap.put(AstroidRock.createdRocks, new AstroidRock(this, false));
    				getChildren().addAll(this.astroidMap.get(AstroidRock.createdRocks-1));
    			}
        	}
        	
        	this.astroidMap.remove(id);
        }
        
        if (this.astroidMap.size() == 0) {
        	
        	this.stop();
        	Alert  newPopup = new Alert(Alert.AlertType.CONFIRMATION);
        	
        	newPopup.setTitle("You win! Press Enter to play again, other keys to quit");
        	
        	newPopup.getDialogPane().addEventFilter(KeyEvent.KEY_PRESSED, event-> {
        		if(event.getCode().equals(KeyCode.ENTER)) {
        			event.consume();
        			astroidMap.clear();
        			getChildren().clear();
        			astroidMap.put(0, new AstroidRock(AstroidFinal.this, true));
        			astroidMap.get(0).setX(rng.nextInt(0, 600));
        			astroidMap.get(0).setY(rng.nextInt(0, 600));
        			getChildren().addAll(astroidMap.get(0));
        			getChildren().addAll(player);
        			player.setX(300);
        			player.setY(300);
        			
        			//newPopup.hide();
        			stage.requestFocus();
        			stage.show();
        		}
        	});
        	
        	
        	ButtonType againButton = new ButtonType("PLAY AGAIN", ButtonData.OK_DONE);
        	ButtonType quitButton = new ButtonType("QUIT", ButtonData.CANCEL_CLOSE);
        	
        	ArrayList<ButtonType> buttons = new ArrayList<>();
        	buttons.add(quitButton);
        	buttons.add(againButton);
        	
        	//newPopup.getContent().add(popupPane);
        	newPopup.getButtonTypes().setAll(buttons);
        	
        	
        	newPopup.show();
        	
        	
        	
        	
        	//quitButton.setOnAction(eventQuit);
        	//againButton.setOnAction(eventAgain);
        	
        	//newPopup.show(stage);
        	//newPopup.show();
        	        	
        }
        
        for(Bullet b : this.bulletQueue) {
        	b.update();
        }

    } // update
    
    public void gameReset() {
    	
    }
    
    public void shootBullet(double dir) {
    	Bullet newBullet = new Bullet(this, player.getX(), player.getY(), player.getRotate());
    	bulletQueue.add(newBullet);
    	getChildren().addAll(newBullet);
    	newBullet.setCenterX(this.player.getX());
    	newBullet.setCenterY(this.player.getY());
    	//System.out.println("player size: " + this.player.get + " " + this.player.getFitHeight());
    }


    public Vector<Bullet> getBulletQueue() {
    	return this.bulletQueue;
    }
    
	public HashMap<Integer, AstroidRock> getAstroidMap() {
		return astroidMap;
	}

	public void setAstroidMap(HashMap<Integer, AstroidRock> astroidMap) {
		this.astroidMap = astroidMap;
	}

	public int getNumLives() {
		return numLives;
	}

	public void setNumLives(int numLives) {
		this.numLives = numLives;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPlayer_width() {
		return player_width;
	}

	public void setPlayer_width(int player_width) {
		this.player_width = player_width;
	}
}
