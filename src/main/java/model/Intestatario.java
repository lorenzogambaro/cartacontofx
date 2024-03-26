
///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
package model;

import com.google.gson.JsonElement;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/**
 *
 * @author gambaro.lorenzo
 */
public class Intestatario extends User implements Serializable
{
    private final LocalDate birthdate;
    private Indirizzo address;
    private String phoneNumber;
    private String emailAddress;
    private final TipoIntestatario power;
    private final Set<Iban> contiAssociati;

    public Intestatario(final String username, final String password, final TipoIntestatario power, final String cf, final String cognome, final String nome, final LocalDate birthDate, final Indirizzo address, final String phoneNumber, final String email) 
    {
        super(username, calcolaHash(Objects.requireNonNull(password)), nome, cognome, cf);
        this.power = Objects.requireNonNull(power);
        
        if (!Objects.requireNonNull(cf).matches("^[A-Za-z]{6}[0-9]{2}[A-Za-z]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}[A-Za-z]{1}$"))
            throw new IllegalArgumentException("Fiscal code not valid!");
        
        this.birthdate = Objects.requireNonNull(birthDate);
        this.address = Objects.requireNonNull(address);
        
        if (!Objects.requireNonNull(phoneNumber).matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$"))
            throw new IllegalArgumentException("Phone number not valid!");
        this.phoneNumber = phoneNumber;
        
        if (!Objects.requireNonNull(email).matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new IllegalArgumentException("Email not valid!");
        this.emailAddress = email;
        
        this.contiAssociati = new HashSet<>();
    }
    public static Intestatario parse(final String username, final String password, final String cf, final String cognome, final String nome, final LocalDate birthDate, final Indirizzo address, final String phoneNumber, final String email, final TipoIntestatario power, final Set<Iban> contiAssociati)
    {
        final Intestatario i = new Intestatario(username, password, power, cf, cognome, nome, birthDate, address, phoneNumber, email);

        for (final Iban c : contiAssociati)
            i.addConto(c);
        
        return i;
    }
    public static Intestatario parse(final Map<String, JsonElement> user, final JsonElement tipoIntestatario)
    {
        final Set<Iban> ibans = new HashSet<>();
        for (final JsonElement i : user.get("iban_conti_associati").getAsJsonArray().asList())
            ibans.add(new Iban(i.getAsString()));
        
        final Map<String, JsonElement> tipoIntestatarioElement = tipoIntestatario.getAsJsonObject().asMap();

        return Intestatario.parse
        (                
                user.get("username").getAsString(),
                user.get("hashed_pw").getAsString(),
                user.get("fiscal_code").getAsString(),
                user.get("surname_intestatario").getAsString(),
                user.get("name_intestatario").getAsString(),
                OffsetDateTime.parse(user.get("birth_date").getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")).toLocalDate(),
                new Indirizzo(user.get("via").getAsString(), user.get("numero").getAsString(), user.get("cap").getAsString(), user.get("comune").getAsString(), user.get("provincia").getAsString()),
                user.get("phoneNumber").getAsString(),
                user.get("email_address").getAsString(),
                new TipoIntestatario(tipoIntestatarioElement.get("id_potenza").getAsInt(), tipoIntestatarioElement.get("descPerms").getAsString()),
                ibans
        );
    }
    public void addConto(final Iban c)
    {
        if (this.contiAssociati.contains(Objects.requireNonNull(c)))
            throw new IllegalArgumentException("Given Conto already marked belonging to this Intestatario!");
        
        this.contiAssociati.add(c);
    }
    public TipoIntestatario getPower()
    {
        return this.power;
    }

    public LocalDate getBirthdate() 
    {
        return this.birthdate;
    }

    public Indirizzo getAddress() 
    {
        return this.address;
    }

    public String getPhoneNumber() 
    {
        return this.phoneNumber;
    }

    public String getEmailAddress() 
    {
        return this.emailAddress;
    }
    

    public Set<Iban> getContiAssociati() {
        return contiAssociati;
    }
    
    public void setAddress(final Indirizzo address) 
    {
        this.address = Objects.requireNonNull(address);
    } 

    public void setPhoneNumber(final String phoneNumber) 
    {
        if (!Objects.requireNonNull(phoneNumber).matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$"))
            throw new IllegalArgumentException("Phone number not valid!");
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(final String emailAddress) 
    {
        if (!Objects.requireNonNull(emailAddress).matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new IllegalArgumentException("Email not valid!");
        this.emailAddress = emailAddress;
    }

    @Override 
    public String toString()
    {
        return new StringBuilder(super.getSurname()).append(' ').append(super.getName()).toString();
    }

}

