/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Administrator
 */
public class InvalidIbanException extends RuntimeException 
{
    public InvalidIbanException(final String message)
    {
        super(message);
    }
    public InvalidIbanException()
    {
        super("Invalid Iban!");
    }
}
