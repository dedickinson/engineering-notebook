import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import javafx.stage.Stage

/**
 * A basic web browser
 */
class Browser extends Application {

    @Override
    void start(Stage primaryStage) {

        WebView browser = new WebView()
        WebEngine webEngine = browser.getEngine()

        TextField urlEntry = new TextField()

        urlEntry.text = 'http://www.example.com'

        def changeBrowserPage = {
            webEngine.load urlEntry.text
        }

        urlEntry.setOnKeyTyped { KeyEvent ke ->
            if (ke.code == KeyCode.ENTER) {
                changeBrowserPage()
            }
        }

        Button go = new Button('Go')
        go.setOnAction changeBrowserPage

        HBox urlBar = new HBox()

        urlBar.with {
            setHgrow urlEntry, Priority.ALWAYS
            alignment = Pos.CENTER
            spacing = 5
        }

        urlBar.getChildren().with {
            add new Label('URL: ')
            add urlEntry
            add go
        }

        VBox root = new VBox()

        root.spacing = 5

        root.getChildren().with {
            add urlBar
            add browser
        }

        primaryStage.with {
            title = 'Hello World!'
            scene = new Scene(root, 600, 600)
        }

        primaryStage.show()
    }

    static void main(args) {
        Application.launch(Browser.class, args)
    }
}

Browser.main(args)
