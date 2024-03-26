/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
public abstract class User implements Comparable<User>, Serializable
{
    private String username;
    private String password;
    private final String name;
    private final String surname;
    private final String fiscal_code;
    
    public User(final String username, final String password, final String name, final String surname, final String fiscal_code)
    {
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
        this.name = Objects.requireNonNull(name);
        this.surname = Objects.requireNonNull(surname);
        this.fiscal_code = Objects.requireNonNull(fiscal_code);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(final String username) {
        this.username = Objects.requireNonNull(username);
    }

    public void setPassword(final String password) {
        this.password = Objects.requireNonNull(password);
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFiscal_code() {
        return fiscal_code;
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.surname);
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        
        if (obj instanceof User u)
            return this.compareTo(u) == 0;
        
        return false;
    }
    
    @Override
    public int compareTo(final User other)
    {
        if (other == null)
            return -1;
        if (other == this)
            return 0;
        return this.getFiscal_code().compareTo(other.getFiscal_code());
    }

    @Override
    public String toString() 
    {
        final StringBuilder sb = new StringBuilder();
        sb.append("User{");
        sb.append("username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", name=").append(name);
        sb.append(", surname=").append(surname);
        sb.append(", fiscal_code=").append(fiscal_code);
        sb.append('}');
        return sb.toString();
    }
    
    
    
    public static String calcolaHash(final String password) 
    {
        try 
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            final StringBuilder sb = new StringBuilder();
            for (byte b : md.digest(password.getBytes()))
                sb.append(String.format("%02x", b));
            
            return sb.toString();
        } 
        catch (final NoSuchAlgorithmException ex) 
        {
            System.err.println(ex);
            return null;
        }
    }
    
    
}
