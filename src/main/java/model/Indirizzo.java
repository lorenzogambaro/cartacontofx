/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author gambaro.lorenzo
 */
public class Indirizzo implements Comparable<Indirizzo>, Serializable
{
    private final String via;
    private final String numero;
    private final String cap;
    private final String comune;
    private final String provincia;
    
    public Indirizzo(final String via, final String numero, final String cap, final String comune, final String provincia)
    {
        this.via = Objects.requireNonNull(via);
        this.numero = Objects.requireNonNull(numero);
        if (!Objects.requireNonNull(cap).matches("^\\d+$"))
            throw new IllegalArgumentException("CAP cannot have letters!");
        this.cap = cap;
        if (Objects.requireNonNull(provincia).length() != 2)
            throw new IllegalArgumentException("Provincia must have exactly 2 upper-case letters!");
        
        this.provincia = provincia.toUpperCase();
        this.comune = Objects.requireNonNull(comune);
    }

    public String getVia() 
    {
        return this.via;
    }

    public String getNumero() 
    {
        return this.numero;
    }

    public String getCap() 
    {
        return this.cap;
    }

    public String getProvincia() 
    {
        return this.provincia;
    }

    public String getComune() 
    {
        return this.comune;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.via);
        hash = 79 * hash + Objects.hashCode(this.numero);
        hash = 79 * hash + Objects.hashCode(this.cap);
        hash = 79 * hash + Objects.hashCode(this.comune);
        hash = 79 * hash + Objects.hashCode(this.provincia);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (getClass() == obj.getClass())
            return ((Indirizzo) obj).compareTo(this) == 0;
        
        return false;
    }

    @Override
    public int compareTo(Indirizzo o) 
    {
        if (o == null)
            return -1;
        if (o == this)
            return 0;
        
        return o.getProvincia().compareTo(this.getProvincia()) 
                + 
                o.getComune().compareTo(this.getComune()) 
                +
                o.getCap().compareTo(this.getCap())
                +
                o.getVia().compareTo(this.getVia())
                +
                o.getNumero().compareTo(this.getNumero());
    }
}
