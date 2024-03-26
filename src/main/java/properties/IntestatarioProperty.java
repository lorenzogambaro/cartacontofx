/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package properties;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import model.Intestatario;

/**
 *
 * @author Administrator
 */
public class IntestatarioProperty extends ObjectProperty<Intestatario> implements Comparable<IntestatarioProperty>
{
    private Intestatario i;
    private final Set<ChangeListener<? super Intestatario>> listeners;
    private final Set<InvalidationListener> invalidationListeners;
    private final ChangeListener<? super Intestatario> listener = (ob, oldValue, newValue) -> { this.i = newValue; };
    private ObservableValue<? extends Intestatario> observable;
    
    public IntestatarioProperty(final Intestatario i)
    {
        this.i = i;
        this.listeners = new HashSet<>();
        this.invalidationListeners = new HashSet<>();
    }
    public IntestatarioProperty()
    {
        this(null);
    }

    @Override
    public Intestatario get() 
    {
        return this.i;
    }

    @Override
    public void addListener(ChangeListener<? super Intestatario> listener) 
    {
        this.listeners.add(Objects.requireNonNull(listener));
    }

    @Override
    public void removeListener(ChangeListener<? super Intestatario> listener) 
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
        return i.getClass().getName();
    }

    @Override
    public void bind(ObservableValue<? extends Intestatario> observable) 
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
    public void set(Intestatario value) 
    {
        for (final var listener : this.listeners)
            listener.changed(this.observable, this.i, value);
        this.i = Objects.requireNonNull(value);
    }

    @Override
    public int compareTo(final IntestatarioProperty o)
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
