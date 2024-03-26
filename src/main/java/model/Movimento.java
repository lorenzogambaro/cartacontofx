/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

 
/**
 *
 * @author gambaro.lorenzo
 */
public class Movimento implements Comparable<Movimento>, Serializable
{
    private static long globalId = 0;
    private final long id;
    private final Iban ibanRichiedente;
    private final LocalDate operationDate;
    private final LocalDate valuteDate;
    private final String descr;
    private final Iban ibanDestinatario;
    private final double importo;
    private final TipoMovimento type;

    public Movimento(final long id, final Iban ibanRichiedente, final LocalDate operationDate, final LocalDate valuteDate, final String descr, final Iban ibanDestinatario, final double importo, final TipoMovimento type)
    {
        this.id = id;
        this.operationDate = Objects.requireNonNull(operationDate);
        
        if (Objects.requireNonNull(valuteDate).isBefore(operationDate))
            throw new IllegalArgumentException("Valute date cannot be before operation date!");
        
        this.valuteDate = Objects.requireNonNull(valuteDate);
        this.descr = Objects.requireNonNull(descr);
        this.ibanRichiedente = Objects.requireNonNull(ibanRichiedente);
        this.ibanDestinatario = Objects.requireNonNull(ibanDestinatario);
        
        if (importo <= 0)
            throw new IllegalArgumentException("Import must be grater than 0");
        
        this.importo = importo;
        this.type = Objects.requireNonNull(type);
    }


    public Movimento(final Iban ibanRichiedente, final LocalDate operationDate, final LocalDate valuteDate, final String descr, final Iban ibanDestinatario, final double importo, final TipoMovimento t)
    {
        this(globalId++, ibanRichiedente, operationDate, valuteDate, descr, ibanDestinatario, importo, t);
    }

    public TipoMovimento getType() 
    {
        return this.type;
    }

    public long getId() 
    {
        return this.id;
    }
    
    public LocalDate getOperationDate() 
    {
        return this.operationDate;
    }

    public LocalDate getValuteDate() 
    {
        return this.valuteDate;
    }
    public Iban getIbanRichiedente()
    {
        return this.ibanRichiedente;
    }
    public Iban getIbanDestinatario() 
    {
        return this.ibanDestinatario;
    }

    public double getImporto() 
    {
        return this.importo;
    }

    public String getDescr() {
        return this.descr;
    }
    
    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("Movimento: \n{\n\t");
        
        sb.append("Id: ").append(this.id);
        sb.append("\n\tDescription: ").append(this.type.getDesc()).append(" -> ").append(this.descr);
        sb.append("\n\tOperation Date: ").append(this.operationDate);
        sb.append("\n\tValute Date: ").append(this.valuteDate);
        sb.append("\n\tIban richiedente: ").append(this.ibanRichiedente);
        sb.append("\n\tIban destinatario: ").append(this.ibanDestinatario);
        sb.append("\n\tImport: ").append(this.type.getAmount() > 0 ? "+" : "-").append(this.importo);
        sb.append("\n\tOperation Cost: ").append(this.type.getCost()).append("\n]");
        
        return sb.toString();
    }

    @Override
    public int compareTo(Movimento o) 
    {
        if (o == null)
            return -1;
        return Math.toIntExact(o.getId() - this.getId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (int) (79 * hash + this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movimento other = (Movimento) obj;
        return this.id == other.id;
    }

    
    


    
}
