package smartbox;

import mvc.*;

public class ContainerFactory implements AppFactory {

    @Override
    public Model makeModel() {
        return new Container();  // Create and return a new Container instance
    }

    @Override
    public View makeView(Model m) {
        if (!(m instanceof Container)) {
            throw new IllegalArgumentException("Model must be instance of Container");
        }
        return new ContainerView(m);  // Create and return a new ContainerView instance
    }

    @Override
    public String getTitle() {
        return "SmartBox Container";  // Return the title of the application
    }

    @Override
    public String[] getHelp() {
        String[] helpMessages = {
                "Add Component: Add a new component to the container.",
                "Remove Component: Remove an existing component from the container.",
                "Run Component: Execute a component's main functionality."
        };
        return helpMessages;  // Return help messages relevant to the application
    }

    @Override
    public String about() {
        return "SmartBox is a Component-Container framework designed to dynamically manage components.";
    }

    @Override
    public String[] getEditCommands() {
        String[] commands = {"Add", "Remove", "Run"};
        return commands;  // Return the commands that can be executed in this application
    }

    @Override
    public Command makeEditCommand(Model model, String name, Object object) {
        // Implementation depends on commands; as a placeholder:
        switch (name) {
            case "Add":
                return new Add(model, object);
            case "Remove":
                return new Rem(model, object);
            case "Run":
                return new Run(model, object);
            default:
                return null;  // Return null if no command matches
        }
    }
}