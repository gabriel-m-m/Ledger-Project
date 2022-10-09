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
    public void payDebts(String name, String owed, int amount) {
        for (User user: this.users) {
            if (name.equals(user.getName())) {
                user.findEntry(owed).subtractDebt(amount);
                break;
            }
        }
        for (User user: this.users) {
            if (owed.equals(user.getName())) {
                user.findEntry(name).subtractOwed(amount);
                break;
            }
        }

    }

    // REQUIRES : amount > 0
    // MODIFIES : this
    // EFFECTS : Increases number owed to owed by name by amount
    public void increaseOwed(String name, String owed, int amount) {
        for (User user: this.users) {
            if (name.equals(user.getName())) {
                user.findEntry(owed).addDebt(amount);
                break;
            }
        }
        for (User user: this.users) {
            if (owed.equals(user.getName())) {
                user.findEntry(name).addOwed(amount);
                break;
            }
        }
    }


    // MODIFIES : this
    // EFFECTS : Balances values between users
    public void balanceLedger() {
        for (User user: this.users) {
            for (Entry e : user.getEntries()) {
                e.balanceVal();
            }
        }
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }
}
