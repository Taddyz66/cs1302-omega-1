package astroid;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;


public class AstroidFinalApp  extends Application {
	/**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public AstroidFinalApp() {}

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
    	
    	Dialog<ButtonType> startDialog = new Dialog<ButtonType>();
    	startDialog.setTitle("Welcome to Astroid!!!");
    	
    	ButtonType startButtonType = new ButtonType("PLAY", ButtonData.OK_DONE);
    	ButtonType cancelButtonType = new ButtonType("CANCEL", ButtonData.CANCEL_CLOSE);
    	startDialog.setContentText("Click PLAY button to start the game! or CANCEL to quit!");
    	startDialog.getDialogPane().getButtonTypes().add(startButtonType);
    	startDialog.getDialogPane().getButtonTypes().add(cancelButtonType);
    	
    	Optional<ButtonType> result = startDialog.showAndWait();
    	if (result.isPresent() && result.get().getText() == "CANCEL") {
    		System.exit(0);
    	}
    	

        // demonstrate how to load local asset using "file:resources/"
        //Image bannerImage = new Image("file:resources/readme-banner.png");
        //ImageView banner = new ImageView(bannerImage);
        //banner.setPreserveRatio(true);
        //banner.setFitWidth(640);

        // some labels to display information
        Label notice = new Label("Modify the starter code to suit your needs.");
        Label instructions
            = new Label("Move left/right with arrow keys; click rectangle to teleport.");

        // demo game provided with the starter code
        AstroidFinal game = new AstroidFinal(640, 640, stage);

        // setup scene
        VBox root = new VBox(game);//banner, notice, instructions, 
        Scene scene = new Scene(root);

        // setup stage
        stage.setTitle("AstroidApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

        // play the game
        game.play();
    	

    } // start
    
    public static void main(String[] args) {
        try {
            Application.launch(AstroidFinalApp.class, args);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            System.err.println();
            System.err.println(e);
            System.err.println("If this is a DISPLAY problem, then your X server connection");
            System.err.println("has likely timed out. This can generally be fixed by logging");
            System.err.println("out and logging back in.");
            System.exit(1);
        } catch (RuntimeException re) {
            re.printStackTrace();
            System.err.println();
            System.err.println(re);
            System.err.println("A runtime exception has occurred somewhere in the application,");
            System.err.println("and it propagated all the way up to the main method. Please");
            System.err.println("inspect the backtrace above for more information.");
            System.exit(1);
        } // try
    } // main
}
