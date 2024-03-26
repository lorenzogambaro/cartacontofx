/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Administrator
 */
public abstract class TipoUtente 
{
    private final int powerCode;
    private final String descr;
    public TipoUtente(final int powerCode, final String descr)
    {
        this.powerCode = powerCode;
        this.descr = descr;
    }

    public int getPowerCode() {
        return powerCode;
    }

    public String getDescr() {
        return descr;
    }
    
}
