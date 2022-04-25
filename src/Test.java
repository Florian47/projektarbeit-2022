package src;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Test {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String username;
    public String password;
}
