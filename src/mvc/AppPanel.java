package mvc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppPanel extends JPanel implements Subscriber, ActionListener {
    public static int FRAME_WIDTH = 830;
    public static int FRAME_HEIGHT = 500;
    private final View view;
    public JPanel controls;
    public Model model;
    private AppFactory factory;
    private JFrame frame;

    public AppPanel(AppFactory newFactory) {
        this.factory = newFactory;
        controls = new JPanel();
        model = factory.makeModel();
        view = factory.makeView(model);
        setLayout(new GridLayout(1, 3));
        add(controls);
        add(view);
        model.subscribe(this);

        frame = new SafeFrame();
        Container cp = frame.getContentPane();
        cp.add(this);
        frame.setJMenuBar(createMenuBar());
        frame.setTitle(factory.getTitle());
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    }

    protected JMenuBar createMenuBar() {
        JMenuBar result = new JMenuBar();
        JMenu fileMenu = Utilities.makeMenu("File", new String[]{"New", "Save", "SaveAs", "Open", "Quit"}, this);
        result.add(fileMenu);
        JMenu editMenu = Utilities.makeMenu("Edit", this.factory.getEditCommands(), this);
        result.add(editMenu);
        JMenu helpMenu = Utilities.makeMenu("Help", new String[]{"About", "Help"}, this);
        result.add(helpMenu);
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            switch (ae.getActionCommand()) {
                case "New" -> {
                    Utilities.saveChanges(model);
                    setModel(factory.makeModel());
                }
                case "Save" -> Utilities.save(model, false);
                case "SaveAs" -> Utilities.save(model, true);
                case "Open" -> {
                    Model model = Utilities.open(this.model);
                    if (model != null) setModel(model);
                }
                case "Quit" -> {
                    Utilities.saveChanges(model);
                    System.exit(0);
                }
                case "About" -> Utilities.inform(factory.about());
                case "Help" -> Utilities.inform(factory.getHelp());
                default -> {
                    Command command = factory.makeEditCommand(model, ae.getActionCommand(), ae.getSource());
                    if (command == null) throw new Exception("Unrecognized command " + ae.getActionCommand());
                    command.execute();
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    protected void addButtons() {
        for (String command : factory.getEditCommands()) {
            JPanel p = new JPanel();
            JButton b = new JButton(command);
            b.addActionListener(this);
            p.add(b);
            controls.add(p);
        }
    }

    public void display() {
        frame.setVisible(true);
    }

    public void update() {
    }

    public void setModel(Model newModel) {
        this.model.unsubscribe(this);
        this.model = newModel;
        this.model.subscribe(this);
        view.setModel(this.model);
        model.changed();
    }

    protected void handleException(Exception e) {
        Utilities.error(e);
    }
}