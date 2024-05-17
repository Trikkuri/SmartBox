package smartbox;

import mvc.*;
public class Add extends Command {
    private String componentName;

    public Add(Model model, Object componentName) {
        super(model);
        this.componentName = (String) componentName;
    }

    @Override
    public void execute() {
        try {
            ((Container) model).addComponent(componentName);
        } catch (Exception e) {
            Utilities.error("Error adding component: " + e.getMessage());
        }
    }
}