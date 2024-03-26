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
public class TipoMovimento implements Serializable
{   
    private static long id = 0;
    private final long code;
    private final String desc;
    private final double cost;
    private final double amount;
    private final int min_power_requried;

    public TipoMovimento(final long code, final String desc, final double cost, final double amount, final int min_power_required) 
    {
        this.code = code;
        this.desc = Objects.requireNonNull(desc);
        this.cost = cost;
        if (amount < -1 || amount > 1)
            throw new IllegalArgumentException("Segno must be -1 for negative, +1 for positive!");
        this.amount = amount;
        this.min_power_requried = min_power_required;
    }
    
    public TipoMovimento(final String desc, final double cost, final double amount, final int min_power_required)
    {
        this(id++, desc, cost, amount, min_power_required);
    }

    public long getCode() 
    {
        return this.code;
    }

    public double getCost() 
    {
        return this.cost;
    }

    public double getAmount() 
    {
        return this.amount;
    }

    public String getDesc() 
    {
        return this.desc;
    }  

    public int getMin_power_requried() {
        return min_power_requried;
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Movimento ").append(this.desc).append(": { ");
        sb.append("Code:").append(this.code).append(", ");
        sb.append("Cost: ").append(this.cost).append(", ");
        sb.append("Sign: ").append(this.amount > 0 ? "+" : "-");
        sb.append(" }");
        return sb.toString();
    }
    
}
