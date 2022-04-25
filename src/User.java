package src;

import javax.json.bind.annotation.JsonbProperty;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVorname() {
        return firstName;
    }

    public void setVorname(String vorname) {
        this.firstName = vorname;
    }

    public String getNachname() {
        return lastName;
    }

    public void setNachname(String nachname) {
        this.lastName = nachname;
    }

    public String getBenutzername() {
        return username;
    }

    public void setBenutzername(String benutzername) {
        this.username = benutzername;
    }

    public String getPasswort() {
        return password;
    }

    public void setPasswort(String passwort) {
        this.password = passwort;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    long id;
    public String firstName;
    String lastName;
    public String username;
    public String password;
    int klasse = 4;
}
