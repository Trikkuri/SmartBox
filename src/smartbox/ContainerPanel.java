package smartbox;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import mvc.*;

public class ContainerPanel extends AppPanel {
    private AppFactory localFactory;
    public ContainerPanel(AppFactory factory) {
        super(factory);
        this.localFactory = factory;
        JButton addButton = new JButton("Add");
        addButton.setActionCommand("Add");
        addButton.addActionListener(this);

        JButton removeButton = new JButton("Rem");
        removeButton.setActionCommand("Rem");
        removeButton.addActionListener(this);

        JButton runButton = new JButton("Run");
        runButton.setActionCommand("Run");
        runButton.addActionListener(this);

        controls.add(addButton);
        controls.add(removeButton);
        controls.add(runButton);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        try {
            switch (command) {
                case "Add":
                case "Rem":
                case "Run":
                    performAction(command);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Unrecognized command: " + command, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            Utilities.error("Error processing command: " + command + " - " + ex.getMessage());
        }
    }

    private void performAction(String action) throws Exception {
        String componentName = JOptionPane.showInputDialog(this, "Component name for " + action + "?", action + " Component", JOptionPane.QUESTION_MESSAGE);
        if (componentName != null && !componentName.isEmpty()) {
            Command command = localFactory.makeEditCommand(model, action, componentName);
            if (command != null) {
                command.execute();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid action or component name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void setModel(Model m) {
        super.setModel(m);
        ((Container) m).initContainer();
    }

    public static void main(String[] args) {
        AppFactory factory = new ContainerFactory();
        AppPanel panel = new ContainerPanel(factory);
        panel.display();
    }
}