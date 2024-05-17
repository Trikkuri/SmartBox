package smartbox;

import mvc.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class ContainerView extends View {
    private List componentsList;

    public ContainerView(Model model) {
        super(model);
        componentsList = new List(10);
        this.add(componentsList);
    }

    @Override
    public void update() {
        updateComponentList();  // Refresh the component list whenever the model changes
    }

    private void updateComponentList() {
        componentsList.removeAll();
        for (Component component : ((Container) model).getComponents()) {
            componentsList.add(component.getName());
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        Container container = (Container) model;
        componentsList.removeAll();
        for (Component component : container.getComponents()) {
            componentsList.add(component.toString());
        }
    }
}