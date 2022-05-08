package astroid;

import java.util.HashMap;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AstroidRock  extends ImageView{
	
	static int createdRocks = 0;
	int id;
	double vx, vy;
	boolean bigRock;
	boolean crashed;
	private AstroidFinal game;
	
	public AstroidRock(AstroidFinal game, boolean bigRock) {
		super();
		
		if (bigRock) {
			this.setImage(new Image("file:resources/astroid-big-1.jpg"));
		} else {
			this.setImage(new Image("file:resources/astroid-small-1.jpg"));
		}
		
        this.setPreserveRatio(true);
        this.setFitWidth(getImage().getWidth());
        
		vx = (int) (Math.random() * 5 ); // [-50, 50)- 1
		vy = (int) (Math.random() * 5 ); // [-50, 50)- 1
		
		crashed = false;
		this.bigRock = bigRock;
		id = AstroidRock.createdRocks++;
		
		this.game = game;
		
	}

	public int update() {
		double curX = getX();
		double curY = getY();
		
		for (Bullet b : this.game.getBulletQueue()) {
			if(Math.abs(curX - b.getCenterX()) < 20 
					&& Math.abs(curY - b.getCenterY()) < 20) {
				this.setVisible(false);
				this.crashed = true;
				if (this.bigRock) {
					this.game.setScore(this.game.getScore() + 5);
					return this.id;
				} else  {
					this.game.setScore(this.game.getScore() + 10);
					return this.id;
				}
				
			}
		}
		
		Bounds astroidBounds = getBoundsInParent();
        Bounds gameBounds = game.getGameBounds();
        if (astroidBounds.getMaxX() > gameBounds.getMaxX()) {
            vx *= -1.0;      // change x direction
            setScaleX(-1.0); // flip this image view horizontally
        } else if (astroidBounds.getMinX() < gameBounds.getMinX()) {
            vx *= -1.0;      // change x direction
            setScaleX(1.0);  // flip this image view back
        } // if
        if (astroidBounds.getMaxY() > gameBounds.getMaxY()) {
            vy *= -1.0;      // change x direction
            setScaleY(-1.0); // flip this image view horizontally
        } else if (astroidBounds.getMinY() < gameBounds.getMinY()) {
            vy *= -1.0;      // change x direction
            setScaleY(1.0);  // flip this image view back
        } // if
        setX(getX() + vx);   // move this cat!
        setY(getY() + vy);   // move this cat!
        
        return -1;
			
	}

}
