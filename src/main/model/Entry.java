package model;

// Represents an entry for a user with owed and debt for other user with name
public class Entry {
    private String name;
    private int owed;
    private int debt;

    // REQUIRES : non-empty string
    // EFFECTS : Constructs entry with name, and owed and debt = 0
    public Entry(String name) {
        this.name = name;
        this.owed = 0;
        this.debt = 0;
    }

    // REQUIRES : amount > 0
    // MODIFIES : this
    // EFFECTS : increases owed by amount
    private void addOwed(int amount) {
        this.owed += amount;
    }

    // REQUIRES : owed >= amount > 0
    // MODIFIES : this
    // EFFECTS : decreases owed by amount
    private void subtractOwed(int amount) {
        this.owed -= amount;
    }

    // REQUIRES : amount > 0
    // MODIFIES : this
    // EFFECTS : increases debt by amount
    private void addDebt(int amount) {
        this.owed += amount;
    }

    // REQUIRES : debt >= amount > 0
    // MODIFIES : this
    // EFFECTS : decreases debt by amount
    private void subtractDebt(int amount) {
        this.owed -= amount;
    }

    public int getDebt() {
        return this.debt;
    }

    public int getOwed() {
        return this.owed;
    }

    public String getName() {
        return this.name;
    }
}
