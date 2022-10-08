package model;

import java.util.ArrayList;

// Represents a ledger with users
public class Ledger {
    private ArrayList<User> users;

    // REQUIRES : list of unique names with length > 1
    // EFFECTS : Creates a ledger with list of users for each name
    public Ledger(ArrayList<String> names) {
        users = new ArrayList<>();
        for (String s : names) {
            User user = new User(s, names);
            users.add(user);
        }
    }

    // REQUIRES : amount <= amount owed by name to owed
    //            string contained in list of names
    // MODIFIES : this
    // EFFECTS : Decreases number owed to owed by name by amount
    private void payDebts(String name, String owed, int amount) {}

    // REQUIRES : amount > 0
    // MODIFIES : this
    // EFFECTS : Increases number owed to owed by name by amount
    private void increaseOwed(String name, String owed, int amount) {}


    // MODIFIES : this
    // EFFECTS : Balances values between users
    private void balanceLedger() {}

    // EFFECTS : Prints a summary of the information in the ledger
    private void ledgerSummary() {}

    public ArrayList<User> getUsers() {
        return this.users;
    }
}
