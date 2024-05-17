package smartbox;

import java.util.*;
import java.io.Serializable;
import java.lang.reflect.*;

public class Component implements Serializable {
    private Set<Class<?>> requiredInterfaces;
    private Set<Class<?>> providedInterfaces;
    private transient Map<Class<?>, Field> fields; // transient because Field is not serializable
    protected Container container;
    protected String name;

    public Component() {
        fields = new HashMap<>();
        providedInterfaces = new HashSet<>();
        requiredInterfaces = new HashSet<>();
        computeRequiredInterfaces();
        computeProvidedInterfaces();
        container = null;
        name = this.getClass().getSimpleName();
    }

    // Implement getters and setters as needed

    public String toString() {
        return name;
    }

    // Initializes fields and requiredInterfaces
    public void computeRequiredInterfaces() {
        Field[] fieldArray = this.getClass().getDeclaredFields();
        for (Field field : fieldArray) {
            Class<?> fieldType = field.getType();
            if (fieldType.isInterface()) {
                fields.put(fieldType, field);
                requiredInterfaces.add(fieldType);
            }
        }
    }

    // Initializes provided interfaces
    public void computeProvidedInterfaces() {
        Class<?>[] interfaces = this.getClass().getInterfaces();
        for (Class<?> intf : interfaces) {
            providedInterfaces.add(intf);
        }
    }

    // Set the field of this object to the provided component
    public void setProvider(Class<?> intf, Component provider) throws IllegalAccessException {
        Field field = fields.get(intf);
        if (field != null) {
            field.setAccessible(true); // ensure the field is accessible
            field.set(this, provider);
        }
    }

    // Needed by file/open to reinitialize transient fields
    public void initComponent() {
        fields = new HashMap<>();
        computeRequiredInterfaces();
        computeProvidedInterfaces();
    }

    // Getters and setters
    public Set<Class<?>> getProvidedInterfaces() {
        return providedInterfaces;
    }

    public Set<Class<?>> getRequiredInterfaces() {
        return requiredInterfaces;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public String getName() {
        return name;
    }
}