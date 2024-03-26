/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


/**
 *
 * @author Administrator
 */
public class Administrator extends User
{
    private final int powerLevel;
    
    public Administrator(final String username, final String password, final String name, final String surname, final String fiscal_code, final int powerLevel) 
    {
        super(username, password, name, surname, fiscal_code);
        this.powerLevel = powerLevel;
    }

    @Override
    public String toString() 
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("Administrator{");
        sb.append("powerLevel=").append(powerLevel);
        sb.append('}');
        return sb.toString();
    }

    

    public int getPowerLevel() 
    {
        return powerLevel;
    }
}
