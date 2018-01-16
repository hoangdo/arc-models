import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentationTemplate;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * This is a simple example of how to get started with Structurizr for Java.
 */
public class Structurizr {

    private static final long WORKSPACE_ID = 38067;
    private static final String API_KEY = "e2d229b2-314c-46d5-a8ff-9bf6b646b2ad";
    private static final String API_SECRET = "4a4162b4-82c3-4f01-b285-130c4cd0acd1";

    public static void main(String[] args) throws Exception {
        // a Structurizr workspace is the wrapper for a software architecture model, views and documentation
        Workspace workspace = new Workspace("System context", "This is a model of Y");
        Model model = workspace.getModel();

        // add some elements to your software architecture model
        Person user = model.addPerson("Consumer", "An end consumer");
        SoftwareSystem cooperate = model.addSoftwareSystem("Cooperate", "A cooperate consumer");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Y", "Personal information central");
        user.uses(softwareSystem, "Enter personal information ");
        cooperate.uses(softwareSystem, "Retrieve or manage personal information");

        // define some views (the diagrams you would like to see)
        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext", "Y");
        contextView.setPaperSize(PaperSize.A5_Landscape);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        Container webApplication = softwareSystem.addContainer("Web Application", "Personal information central",
                "Spring Boot");
        Container relationalDatabase = softwareSystem.addContainer("Relational Database",
                "Stores information regarding the personal data",
                "Relational Database Schema");

        user.uses(webApplication, "Uses", "HTTPS");
        cooperate.uses(webApplication, "Uses", "HTTPS");
        webApplication.uses(relationalDatabase, "Reads from and writes to", "JDBC");

        ContainerView containerView = views.createContainerView(softwareSystem, "Y", "Y description");
        containerView.setPaperSize(PaperSize.A5_Landscape);
        containerView.addAllContainers();
        containerView.addAllPeople();

        // add some documentation
        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);
        template.addContextSection(softwareSystem, Format.Markdown,
                "Here is some context about the software system...\n" +
                        "\n" +
                        "![](embed:SystemContext)");

        // add some styling
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);
        styles.addElementStyle(Tags.CONTAINER).background("#1168bd").color("#ffffff");

        uploadWorkspaceToStructurizr(workspace);
    }

    private static void uploadWorkspaceToStructurizr(Workspace workspace) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}