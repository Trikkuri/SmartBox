package smartbox;

import mvc.*;

public class Run extends Command {
    private String componentName;

    public Run(Model model, Object componentName) {
        super(model);
        this.componentName = (String )componentName;
    }

    @Override
    public void execute() {
        try {
            ((Container) model).launch(componentName);
        } catch (Exception e) {
            Utilities.error("Error running component: " + e.getMessage());
        }
    }
}
