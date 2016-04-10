import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage

//Just displays a very basic GUI with a button

class HelloWorld extends Application {
    @Override
    void start(Stage primaryStage) {
        Button btn = new Button()
        btn.text = "Say 'Hello World'"

        btn.setOnAction({
            println 'Hello World!'
        })

        StackPane root = new StackPane()
        root.getChildren().add(btn)

        primaryStage.title = 'Hello World!'
        primaryStage.scene = new Scene(root, 300, 250)
        primaryStage.show()
    }

    static void main(args) {
        Application.launch(HelloWorld, args)
    }
}
