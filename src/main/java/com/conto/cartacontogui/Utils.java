/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.conto.cartacontogui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Administrator
 */
public abstract class Utils
{
    private Utils() {}
    
    public static byte[] serializeObject(final Object o) throws IOException
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(o);
        
        return baos.toByteArray();
    }
    public static Object deserializeObject(final byte[] buffer) throws IOException, ClassNotFoundException
    {
        return new ObjectInputStream(new ByteArrayInputStream(buffer)).readObject();
    }
}
