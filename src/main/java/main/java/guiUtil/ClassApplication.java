package main.java.main.java.guiUtil;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClassApplication extends Application {

    // Create our ProgressBar
    private ProgressBar progressBar = new ProgressBar(0.0);

    // Create a label to show current progress %
    private Label lblProgress = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Simple interface
        VBox root = new VBox(5);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.CENTER);

        // Button to start the background task
        Button button = new Button("Start");
        button.setOnAction(event -> startProcess());

        // Add our controls to the scene
        root.getChildren().addAll(
                progressBar,
                new HBox(5) {{
                    setAlignment(Pos.CENTER);
                    getChildren().addAll(
                            new Label("Current Step:"),
                            lblProgress
                    );
                }},
                button
        );

        // Here we will

        // Show the Stage
        primaryStage.setWidth(300);
        primaryStage.setHeight(300);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void startProcess() {

        // Create a background Task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                // Set the total number of steps in our process
                int steps = 1000;

                // Simulate a long running task
                for (int i = 0; i < steps; i++) {

                    Thread.sleep(10); // Pause briefly

                    // Update our progress and message properties
                    updateProgress(i, steps);
                    updateMessage(String.valueOf(i));
                }
                return null;
            }
        };

        // This method allows us to handle any Exceptions thrown by the task
        task.setOnFailed(wse -> {
            wse.getSource().getException().printStackTrace();
        });

        // If the task completed successfully, perform other updates here
        task.setOnSucceeded(wse -> {
            System.out.println("Done!");
        });

        // Before starting our task, we need to bind our UI values to the properties on the task
        progressBar.progressProperty().bind(task.progressProperty());
        lblProgress.textProperty().bind(task.messageProperty());

        // Now, start the task on a background thread
        new Thread(task).start();
    }
}
