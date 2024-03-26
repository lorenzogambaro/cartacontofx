/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author gambaro.lorenzo
 */
public class Banca implements Comparable<Banca>, Serializable
{
    private final String name;
    private final String location;
    private final String cab;
    private final String abi;
    private final Map<Iban, Conto> conti;
    private final Map<String, Intestatario> utenti;
    private final Map<String, TipoMovimento> tipiMovimento;
    
    public Banca(final String name, final String location, final String abi, final String cab, final Map<Iban, Conto> conti, final Map<String, Intestatario> utenti, final Map<String, TipoMovimento> tipiMovimento)
    {
        this.name = Objects.requireNonNull(name);

        if (Objects.requireNonNull(location).length() != 2)
            throw new IllegalArgumentException("Location characters not valid!");

        for (int i = 0; i < location.length(); i++)
            if (!Character.isUpperCase(location.charAt(i)))
                throw new IllegalArgumentException("Location characters not valid!");
        this.location = location;

        if (Objects.requireNonNull(cab).length() != 5)
            throw new IllegalArgumentException("Cab code not valid!");
        for (int i = 0; i < cab.length(); i++)
            if (!Character.isDigit(cab.charAt(i)) && !Character.isUpperCase(cab.charAt(i)) && !Character.isLowerCase(cab.charAt(i)))
                throw new IllegalArgumentException("Cab code not valid!");
        this.cab = cab;

        if (Objects.requireNonNull(abi).length() != 5)
            throw new IllegalArgumentException("Abi code not valid!");
        for (int i = 0; i < abi.length(); i++)
            if (!Character.isDigit(abi.charAt(i)) && !Character.isUpperCase(abi.charAt(i)) && !!Character.isLowerCase(abi.charAt(i)))
                throw new IllegalArgumentException("Abi code not valid!");
        this.abi = abi;

        this.conti = Objects.requireNonNull(conti);
        this.utenti = Objects.requireNonNull(utenti);
        this.tipiMovimento = Objects.requireNonNull(tipiMovimento);
    }
    public Banca(final String name, final String location, final String abi, final String cab)
    {
        this(name, location, abi, cab, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }
    public TipoMovimento newTipoMovimento(final String desc, final double cost, final double amount, final int min_power_id)
    {
        final TipoMovimento newMovimento = new TipoMovimento(desc, cost, amount, min_power_id);
        
        if (!this.tipiMovimento.containsKey(desc))
        {
            this.tipiMovimento.put(desc, newMovimento);
            return newMovimento;
        }
        return null;
    }
    
    public Map<String, TipoMovimento> getTipiMovimento() { return this.tipiMovimento; }
    
    public Conto newConto(final Intestatario... intestatari)
    {
        final Conto c = new Conto(generateIban(this.location, this.abi, this.cab, String.valueOf(this.conti.size() + 1)), LocalDate.now(), Objects.requireNonNull(intestatari));
        
        this.conti.put(c.getIban(), c);
        
        for (final Intestatario temp : intestatari)
            temp.addConto(c.getIban());
        
        return c;
    }
    public void registerUser(final Intestatario... i)
    {
        for (final Intestatario temp : Objects.requireNonNull(i))
        {
            if (this.utenti.containsKey(temp.getUsername()))
                throw new IllegalArgumentException("User already registered!");
            
            this.utenti.put(temp.getUsername(), temp);
        }
    }
    public Intestatario searchIntestatario(final String username, final String password)
    {
        final Intestatario found = this.utenti.get(Objects.requireNonNull(username));
        if (found == null)
            return null;
        
        if (found.getPassword().equals(password))
            return found;
        
        return null;
    }
    public void extinguishConto(final Iban iban)
    {
        final Conto c = this.findConto(iban);
        
        if (c == null)
            throw new IllegalArgumentException("Account not found for iban " + iban.toString() + "!");
        
        if (c.isOpen())
           c.extinguish();
    }
    public Conto findConto(final Iban iban)
    {
        return this.conti.get(iban);
    }
    private static Iban generateIban(final String location, final String abi, final String cab, final String cc)
    {
        final StringBuilder ccn = new StringBuilder();
        
        for (int i = 0; i < 12 - Objects.requireNonNull(cc).length(); i++)
            ccn.append(0);
        ccn.append(cc);
        
        final StringBuilder bbanSb = new StringBuilder().append(calculateCIN(Objects.requireNonNull(abi), Objects.requireNonNull(cab), ccn.toString())).append(abi).append(cab).append(ccn.toString());
                
        return new Iban(new StringBuilder(Objects.requireNonNull(location)).append(calculateCheckDigits(location, bbanSb.toString())).append(bbanSb).toString());
    }
    private static char calculateCIN(final String abi, final String cab, final String ccn)
    {
        final String bban = new StringBuilder(Objects.requireNonNull(abi)).append(Objects.requireNonNull(cab)).append(Objects.requireNonNull(ccn)).toString();
        
        final String aa = "A0B1C2D3E4F5G6H7I8J9K#L#M#N#O#P#Q#R#S#T#U#V#W#X#Y#Z#-#.# #";
        final String bb = "B1A0K#P#L#C2Q#D3R#E4V#O#S#F5T#G6U#H7M#I8N#J9W#Z#Y#X# #-#.#";
        
        int sum = 0;
        
        for (int i = 0; i < bban.length(); i += 2)
        {
            sum += Math.floorDiv(aa.indexOf(bban.charAt(i + 1)), 2);
            sum += Math.floorDiv(bb.indexOf(bban.charAt(i)), 2);
        }
        sum -= Math.floor(sum / 26) * 26;
        
        return aa.charAt(sum * 2);
    }
    private static String calculateCheckDigits(final String countryCode, final String bban) 
    {
        String fakeIban = Objects.requireNonNull(countryCode) + "00" + Objects.requireNonNull(bban);

        fakeIban = fakeIban.toUpperCase();
        fakeIban = fakeIban.substring(4) + fakeIban.substring(0, 4);
        
        final StringBuilder sb = new StringBuilder();
        
        for (char c : fakeIban.toCharArray())
            if (Character.isLetter(c))
                sb.append((int) c - 55);
            else
                sb.append(c);
           

        final BigInteger remainder = new BigInteger(sb.toString()).mod(BigInteger.valueOf(97));
        final int checkDigits = 98 - remainder.intValue();

        return String.format("%02d", checkDigits);
    }
    public String getName()
    {
        return this.name;
    }

    public String getLocation()
    {
        return this.location;
    }

    public String getCab()
    {
        return this.cab;
    }

    public String getAbi()
    {
        return this.abi;
    }

    @Override
    public String toString() 
    {
        final StringBuilder sb = new StringBuilder("Banca ").append(this.name).append(": {");
        
        sb.append("\n\tLocation: ").append(this.location);
        sb.append("\n\nCAB: ").append(this.cab);
        sb.append("\n\nABI: ").append(this.abi);
        sb.append("\n\tConti: ").append(this.conti);
        
        return sb.toString();
    }

    @Override
    public int hashCode() 
    {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.cab);
        hash = 29 * hash + Objects.hashCode(this.abi);
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) 
            return true;
        if (getClass() == obj.getClass())
            return ((Banca) obj).compareTo(this) == 0;
        return false;
    }

    @Override
    public int compareTo(Banca o) 
    {
        if (o == null)
            return -1;
        return o.getAbi().compareTo(this.getAbi()) + o.getCab().compareTo(this.getCab());
    }
}
