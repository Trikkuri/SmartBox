package smartbox;

import mvc.*;

public class Rem extends Command {
    private String componentName;

    public Rem(Model model, Object componentName) {
        super(model);
        this.componentName = (String) componentName;
    }

    @Override
    public void execute() throws Exception {
        try {
            ((Container) model).remComponent(componentName);
        } catch (Exception e) {
            Utilities.error("Error removing component: " + e.getMessage());
        }
    }
}
