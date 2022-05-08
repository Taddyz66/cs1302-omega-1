package astroid;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Bullet extends Circle{
	double vx;
	double vy;
	Game game;
	
	public Bullet(Game game, double x, double y, double rotate) {
		// TODO Auto-generated constructor stub
		super(5);
		this.setFill(Color.WHITE);
		this.game = game;
		this.vx = 5 * Math.cos(rotate * Math.PI / 180);
		this.vy = 5 * Math.sin(rotate * Math.PI / 180);
	}

	public void update() {
		Bounds astroidBounds = getBoundsInParent();
        Bounds gameBounds = game.getGameBounds();
        if (astroidBounds.getMaxX() > gameBounds.getMaxX()) {
            this.setVisible(false);
        } else if (astroidBounds.getMinX() < gameBounds.getMinX()) {
        	this.setVisible(false);
        } // if
        if (astroidBounds.getMaxY() > gameBounds.getMaxY()) {
        	this.setVisible(false);
        } else if (astroidBounds.getMinY() < gameBounds.getMinY()) {
        	this.setVisible(false);
        } // if
        setCenterX(this.getCenterX() + vx);   // move this cat!
        setCenterY(getCenterY() + vy);   // move this cat!
	}
	

}
