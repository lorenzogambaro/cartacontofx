/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package properties;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import model.Conto;

/**
 *
 * @author Administrator
 */
public class ContoProperty extends ObjectProperty<Conto> implements Comparable<ContoProperty>
{
    private Conto c;
    private final Set<ChangeListener<? super Conto>> listeners;
    private final Set<InvalidationListener> invalidationListeners;
    private final ChangeListener<? super Conto> listener = (ob, oldValue, newValue) -> { this.c = newValue; };
    private ObservableValue<? extends Conto> observable;
    
    public ContoProperty(final Conto c)
    {
        this.c = c;
        this.listeners = new HashSet<>();
        this.invalidationListeners = new HashSet<>();
    }
    public ContoProperty()
    {
        this(null);
    }

    @Override
    public Conto get() 
    {
        return this.c;
    }

    @Override
    public void addListener(ChangeListener<? super Conto> listener) 
    {
        this.listeners.add(Objects.requireNonNull(listener));
    }

    @Override
    public void removeListener(ChangeListener<? super Conto> listener) 
    {
        this.listeners.remove(Objects.requireNonNull(listener));
    }

    @Override
    public void addListener(InvalidationListener listener) 
    {
        this.invalidationListeners.remove(Objects.requireNonNull(listener));
        
    }

    @Override
    public void removeListener(InvalidationListener listener)
    {
        this.invalidationListeners.remove(Objects.requireNonNull(listener));
        
    }

    @Override
    public Object getBean()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return c.getClass().getName();
    }

    @Override
    public void bind(ObservableValue<? extends Conto> observable) 
    {
        this.observable = Objects.requireNonNull(observable);
        observable.addListener(this.listener);
    }

    @Override
    public void unbind()
    {
        if (this.observable == null)
            return;
        this.observable.removeListener(this.listener);
        this.observable = null;
    }

    @Override
    public boolean isBound() 
    {
        return this.observable != null;
    }

    @Override
    public void set(Conto value) 
    {
        for (final var listener : this.listeners)
            listener.changed(this.observable, this.c, value);
        this.c = Objects.requireNonNull(value);
    }

    @Override
    public int compareTo(final ContoProperty o)
    {
        if (o == null)
            return -1;
        if (o == this)
            return 0;
        if (o.get() == this.get())
            return 0;
        if (o.get() == null && this.get() != null)
            return -1;
        return o.get().compareTo(this.get());
    }
    
}
