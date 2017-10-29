package az.store.client;

import java.util.Objects;

/**
 * @author Rashad Amirjanov
 */
public class Client {

    private int id;
    private String name;
    private boolean client;
    private boolean provider;

    public Client() {
    }

    public Client(int id) {
        this.id = id;
    }

    public Client(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(int id, String name, boolean client, boolean provider) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.provider = provider;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }

    public boolean isProvider() {
        return provider;
    }

    public void setProvider(boolean provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return id + ", " + name;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof Client) {
            Client c = (Client) other;
            if (Objects.equals(c.getName(), this.name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

}
