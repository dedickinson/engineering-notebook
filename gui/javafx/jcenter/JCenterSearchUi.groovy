import groovy.json.JsonSlurper
import groovy.util.logging.Log
@Grab('org.codehaus.groovy:groovy-all:2.3.11')
@Grab('org.glassfish.jersey.core:jersey-client:2.22.1')
import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import javafx.stage.Stage
import javafx.scene.control.ButtonBar

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.MediaType
import java.nio.file.Paths

import static javafx.geometry.Orientation.HORIZONTAL
import static javafx.geometry.Orientation.VERTICAL
import static javax.ws.rs.core.MediaType.*

@Grab('org.apache.maven:maven-model:3.3.3')
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.Model

@Log
class JCenterSearchUi extends Application {

    @Override
    void start(Stage primaryStage) {
        WebView browser = new WebView()
        Accordion resultsAccordion = new Accordion()
        TextField searchText = new TextField()
        searchText.text = ''

        def loadResultsList = {
            def items = [ ]

            def versionPopup = { event ->
                log.info "${event.source.userData}"
                browser.engine.loadContent("<html><body>Coordinate requested: ${event.source.userData.ga}:${event.source.userData.version}</body></html>")

            }

            def results = JCenterQuery.runQuery(searchText.text)
            log.info "Result count for query (${searchText.text}): ${results.size()}"

            results.each { item ->

                def descriptionLabel = new Label("${item.desc ? item.desc.trim() :'(no description provided)'}")
                descriptionLabel.wrapText = true

                descriptionLabel.alignment = Pos.TOP_LEFT

                Hyperlink latest = new Hyperlink("Latest version (${item.latest_version})")
                latest.userData = [ga: item.name,
                                   version: item.latest_version]
                latest.onAction = versionPopup

                HBox versionLinks = new HBox()

                item.versions.sort(false, { a, b -> b <=> a }).each { v ->
                    Hyperlink link = new Hyperlink(v)
                    link.userData = [ga: item.name,
                                     version: v]
                    link.onAction = versionPopup
                    versionLinks.children.add link
                }

                VBox resultInfo = new VBox()
                resultInfo.children.addAll descriptionLabel,
                        latest,
                        new Label('Other versions'),
                        versionLinks
                items << new TitledPane("${item.system_ids[0]}", resultInfo)
            }
            resultsAccordion.panes.clear()
            resultsAccordion.panes.addAll(*items)
        }

        Button go = new Button('Go')
        go.setOnAction loadResultsList

        HBox searchBar = new HBox()

        searchBar.with {
            setHgrow searchText, Priority.ALWAYS
            alignment = Pos.CENTER
            spacing = 5
        }

        searchBar.getChildren().with {
            add new Label('Search: ')
            add searchText
            add go
        }

        final Menu helpMenu = new Menu("Help")
        MenuItem helpMenuItem = new MenuItem("About")
        helpMenuItem.setOnAction {
            Alert alert = new Alert(Alert.AlertType.INFORMATION)
            alert.setTitle('About')
            alert.headerText = 'About this app'
            alert.contentText = 'This app searches BinTray\'s JCenter repository (https://bintray.com/bintray/jcenter)'
            alert.show()
        }

        helpMenu.items.add helpMenuItem

        MenuBar menuBar = new MenuBar()
        menuBar.menus.add helpMenu
        menuBar.setUseSystemMenuBar(true)

        ScrollPane scrollPane = new ScrollPane(resultsAccordion)
        scrollPane.fitToWidth = true

        TitledPane resultsPane = new TitledPane('Search results', scrollPane)
        resultsPane.collapsible = false

        VBox sideBar = new VBox()
        sideBar.spacing = 5
        sideBar.getChildren().with {
            add searchBar
            add resultsPane
        }

        SplitPane navPanel = new SplitPane()
        navPanel.with {
            orientation = HORIZONTAL
            items.add sideBar
            items.add browser
        }

        VBox root = new VBox()
        root.children.with {
            add menuBar
            add navPanel
        }

        primaryStage.with {
            title = 'JCenter Search'
            scene = new Scene(root, 1000, 800)
        }

        primaryStage.show()
    }

    static void main(args) {
        Application.launch(JCenterSearchUi.class, args)
    }

    class JCenterQuery {
        static List runQuery(String group, String artifact) {
            def slurper = new JsonSlurper()

            def json =
                    ClientBuilder.newClient().
                            target('https://api.bintray.com/search/packages/maven/').
                            queryParam('g', "$group").
                            request(MediaType.APPLICATION_JSON_TYPE).get(String)

            slurper.parseText(json)
        }

        static List runQuery(String query) {
            def slurper = new JsonSlurper()

            log.info "Query: $query"

            def json =
                    ClientBuilder.newClient().
                            target('https://api.bintray.com/search/packages/maven/').
                            queryParam('q', "*$query*").
                            request(APPLICATION_JSON_TYPE).get(String)

            slurper.parseText(json)
        }
    }
}
