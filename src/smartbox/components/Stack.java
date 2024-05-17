package smartbox.components;

import smartbox.*;

import java.util.EmptyStackException;

public class Stack extends Component implements IStack {

    private java.util.Stack<Double> stack;

    public Stack() {
        super();  // Calling the constructor of Component if needed
        this.stack = new java.util.Stack<Double>();
    }

    @Override
    public void push(Double num) {
        stack.push(num);
    }

    @Override
    public void pop() {
        if (!stack.isEmpty()) {
            stack.pop();
        } else {
            throw new EmptyStackException();
        }
    }

    @Override
    public Double top() {
        if (!stack.isEmpty()) {
            return stack.peek();
        }
        throw new EmptyStackException();
    }

    @Override
    public void clear() {
        stack.clear();
    }

    @Override
    public Boolean isEmpty() {
        return stack.isEmpty();
    }
}