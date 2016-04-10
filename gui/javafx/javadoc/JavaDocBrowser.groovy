import groovy.transform.Canonical
import javafx.application.Application
import javafx.beans.value.ChangeListener
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.scene.control.TextField
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.RowConstraints
import javafx.scene.paint.Color
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import javafx.stage.Stage

@Grab('org.jsoup:jsoup:1.8.3')
import org.jsoup.Jsoup


import java.nio.file.Path
import java.nio.file.Paths

import static javafx.geometry.Orientation.HORIZONTAL
import static javafx.geometry.Orientation.VERTICAL


/**
 * A basic javadoc broswer for the Java API.
 * Expects the docs to be in "${System.getProperty('user.home')}/Development/doc/java8/api"
 */
class JavaDocBrowser extends Application {

    final TreeItem packageList = new TreeItem('Packages')
    final TreeItem classList = new TreeItem('Classes')
    final TreeItem interfaceList = new TreeItem('Interfaces')
    final TreeItem enumList = new TreeItem('Enums')

    @Override
    void init() throws Exception {
        super.init()
    }

    @Override
    void start(Stage primaryStage) {
        def baseDir = "${System.getProperty('user.home')}/Development/doc/java8/api"

        WebEngine webEngine
        TextField searchEntry = new TextField()

        Paths.get(baseDir, 'package-list').readLines().each {
            TreeItem item = new TreeItem(it)
            packageList.children.add item
        }

        String packageTitle = Jsoup.parse(Paths.get(baseDir, 'index.html').newInputStream(), 'UTF-8', '')
                .head().getElementsByTag('title')[0].text()

        TreeItem browseTree = new TreeItem(packageTitle)
        browseTree.children.addAll packageList, classList, interfaceList, enumList
        TreeView browseTreeView = new TreeView(browseTree)

        ChangeListener browseTreeViewListener = {observable, oldValue, newValue ->
            TreeItem item = newValue as TreeItem
            def path = Paths.get(baseDir, PackageListHelper.packageToPath(item.value), 'package-summary.html').toAbsolutePath().toUri()
            println path.toString()
            webEngine.load(path.toString())
        }

        browseTreeView.selectionModel.selectedItemProperty().addListener browseTreeViewListener

        SplitPane navPanel = new SplitPane()
        navPanel.with {
            orientation = VERTICAL
            items.add searchEntry
            items.add browseTreeView
            prefWidth = 200
        }

        WebView browser = new WebView()
        webEngine = browser.getEngine()

        SplitPane displayPanel = new SplitPane()
        displayPanel.with {
            orientation = HORIZONTAL
            setDividerPositions 0.3, 0.7
            items.add navPanel
            items.add browser
        }

        ColumnConstraints mainPanelColumnConstraint = new ColumnConstraints()
        mainPanelColumnConstraint.percentWidth = 100

        RowConstraints mainPanelRowConstraint = new RowConstraints()
        mainPanelRowConstraint.percentHeight = 100

        GridPane root = new GridPane()
        root.with {
            add displayPanel, 0, 0
            rowConstraints.add 0, mainPanelRowConstraint
            columnConstraints.add 0, mainPanelColumnConstraint
        }

        primaryStage.with {
            title = 'JavaDoc Browser'
            scene = new Scene(root)
            scene.fill = Color.GHOSTWHITE
            show()
        }
    }
    static void main(args) {
        Application.launch(JavaDocBrowser.class, args)
    }

}
