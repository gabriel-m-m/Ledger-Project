package model;

import org.json.JSONArray;
import org.json.JSONObject;

// Represents an entry for a user with owed and debt for other user with name
public class Entry {
    private String name;
    private int owed;
    private int debt;

    // REQUIRES : non-empty string
    // EFFECTS : Constructs entry with name, and owed and debt = 0 (cents)
    public Entry(String name) {
        this.name = name;
        this.owed = 0;
        this.debt = 0;
    }

    // REQUIRES : amount > 0
    // MODIFIES : this
    // EFFECTS : increases owed by amount
    public void addOwed(int amount) {
        this.owed += amount;
    }

    // REQUIRES : owed >= amount > 0
    // MODIFIES : this
    // EFFECTS : decreases owed by amount
    public void subtractOwed(int amount) {
        this.owed -= amount;
    }

    // REQUIRES : amount > 0
    // MODIFIES : this
    // EFFECTS : increases debt by amount
    public void addDebt(int amount) {
        this.debt += amount;
    }

    // REQUIRES : debt >= amount > 0
    // MODIFIES : this
    // EFFECTS : decreases debt by amount
    public void subtractDebt(int amount) {
        this.debt -= amount;
    }

    // MODIFIES : this
    // EFFECTS : balances out debt and owed values
    public void balanceVal() {
        if (owed >= debt) {
            this.owed -= debt;
            this.debt = 0;
        } else {
            this.debt -= owed;
            this.owed = 0;
        }
    }

    // EFFECTS : Returns entry as a JSON object
    public JSONObject entryToJson() {
        JSONObject entryJson = new JSONObject();
        entryJson.put("name", name);
        entryJson.put("owed", owed);
        entryJson.put("debt", debt);
        return entryJson;
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

    public void setOwed(int amount) {
        this.owed = amount;
    }

    public void setDebt(int amount) {
        this.debt = amount;
    }
}
