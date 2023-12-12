package pickerchessboard;

import javafx.application.Application;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Main extends Application {

    private Stage stage;
    
    private final StackPane stackPane = new StackPane();
    private final ImageView imageView = new ImageView();
    private final Pane drawingPane = new Pane();
    private Spinner<Integer> numberOfRowsSpinner;
    private Spinner<Integer> numberOfColumnsSpinner;
    private final Button loadImageButton = new Button("Load picture");
    private final Label numberOfRowsLabel = new Label("Number of rows");
    private final Label numberOfColumnsLabel = new Label("Number of columns");


    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        
        imageView.setVisible(false);
        
        drawingPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        BorderPane root = new BorderPane();

        stackPane.getChildren().addAll(imageView, drawingPane);

        root.setCenter(stackPane);

        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.setPadding(new Insets(10));
        
        loadImageButton.setOnAction(actionEvent -> {
            loadImage();
        });
        
        numberOfRowsSpinner = new Spinner<>(1, 10, 4);

        numberOfRowsSpinner.valueProperty().addListener((observableValue, integer, t1) -> {
            draw();
        });
        
        numberOfColumnsSpinner = new Spinner<>(1, 10, 3);
        numberOfColumnsSpinner.valueProperty().addListener((observableValue, integer, t1) -> {
            draw();
        });

        buttonsHBox.getChildren().addAll(loadImageButton, numberOfRowsLabel, numberOfRowsSpinner, numberOfColumnsLabel, numberOfColumnsSpinner);
        root.setBottom(buttonsHBox);

        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty().subtract(buttonsHBox.heightProperty()));


        draw();

        stage.setTitle("Picker Chessboard");
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void draw() {
        int numberOfRows = numberOfRowsSpinner.getValue();
        int numberOfColumns = numberOfColumnsSpinner.getValue();

        drawingPane.getChildren().clear();

        for (int i = 0; i < numberOfRows; i++){
            for (int j = 0; j < numberOfColumns; j++){
                Rectangle rectangle = new Rectangle();

                rectangle.xProperty().bind(drawingPane.widthProperty().divide(numberOfColumnsSpinner.getValue()).multiply(j));
                rectangle.yProperty().bind(drawingPane.heightProperty().divide(numberOfRowsSpinner.getValue()).multiply(i));
                rectangle.widthProperty().bind(drawingPane.widthProperty().divide(numberOfColumnsSpinner.getValue()));
                rectangle.heightProperty().bind(drawingPane.heightProperty().divide(numberOfRowsSpinner.getValue()));

                rectangle.setStroke(Color.RED);
                rectangle.setStrokeWidth(1);
                rectangle.setFill(Color.BLACK);

                rectangle.setOnMouseClicked(event -> {
                    Rectangle rect = (Rectangle)event.getSource();
                    if (rect.getFill().equals(Color.BLACK)){
                        rect.setFill(Color.TRANSPARENT);
                    }
                    else {
                        rect.setFill(Color.BLACK);
                    }
                });
                drawingPane.getChildren().add(rectangle);
            }
        }
    }

    private void loadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file != null){
            try {
                imageView.setImage(new Image(new FileInputStream(file)));
                imageView.setVisible(true);

            } catch (FileNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Image load error");
                alert.setContentText("Error");
                alert.showAndWait();
            }
        }
    }


    public static void main(String[] args) {
        Application.launch();
    }
}
