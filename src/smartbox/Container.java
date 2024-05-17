package smartbox;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import mvc.*;

public class Container extends Model {
    private Map<Class<?>, Component> providedInterfaces = new HashMap<>();
    private Map<Class<?>, Component> requiredInterfaces = new HashMap<>();
    private Map<String, Component> components = new HashMap<>();

    public Collection<Component> getComponents() {
        return components.values();
    }

    public void addComponent(String name) throws Exception {
        try {
            String qualName = "smartbox.components." + name;
            Class<?> cls = Class.forName(qualName);
            Constructor<?> ctor = cls.getConstructor();
            Component obj = (Component) ctor.newInstance();
            addComponent(obj);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Failed to add component: " + e.getMessage());
            throw e;
        }
    }

    private void addComponent(Component component) {
        component.setContainer(this);
        components.put(component.getName(), component);

        for (Class<?> intf : component.getProvidedInterfaces()) {
            providedInterfaces.put(intf, component);
        }

        for (Class<?> intf : component.getRequiredInterfaces()) {
            requiredInterfaces.put(intf, component);
        }

        findProviders();
        changed(); // MVC update
    }

    public void remComponent(String name) throws Exception {
        Component component = components.remove(name);
        if (component != null) {
            providedInterfaces.values().remove(component);
            requiredInterfaces.values().remove(component);

            // Invalidate the providers for all components requiring the interfaces provided by the removed component
            for (Class<?> intf : component.getProvidedInterfaces()) {
                for (Component client : components.values()) {
                    if (client.getRequiredInterfaces().contains(intf)) {
                        client.setProvider(intf, null);
                    }
                }
            }
            changed();
        }
    }

    private void findProviders() {
        requiredInterfaces.forEach((intf, client) -> {
            if (providedInterfaces.containsKey(intf)) {
                try {
                    client.setProvider(intf, providedInterfaces.get(intf));
                } catch (Exception e) {
                    System.err.println("Failed to set provider: " + e.getMessage());
                }
            }
        });
    }

    public void launch(String name) throws Exception {
        Component component = components.get(name);
        if (component instanceof App) {
            ((App) component).main();
        }
    }

    public void initContainer() {
        for (Component c : components.values()) {
            c.initComponent();
        }
        changed();
    }
}