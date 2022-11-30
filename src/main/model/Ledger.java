package model;

import org.json.JSONArray;
import org.json.JSONObject;

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
        EventLog.getInstance().logEvent(new Event("Users: " + names + " added to ledger."));
    }

    // REQUIRES : 0 < amount <= amount owed by name to owed
    //            string contained in list of names
    // MODIFIES : this
    // EFFECTS : Decreases number owed to owed by name by amount
    public void payDebts(String name, String owed, int amount) {
        for (User user: this.users) {
            if (name.equals(user.getName())) {
                user.findEntry(owed).subtractDebt(amount);
            }
        }
        for (User user: this.users) {
            if (owed.equals(user.getName())) {
                user.findEntry(name).subtractOwed(amount);
            }
        }
        EventLog.getInstance().logEvent(new Event(name + " payed " + owed + " " + amount + "."));
    }

    // REQUIRES : amount > 0
    // MODIFIES : this
    // EFFECTS : Increases number owed to owed by name by amount
    public void increaseOwed(String name, String owed, int amount) {
        for (User user: this.users) {
            if (name.equals(user.getName())) {
                user.findEntry(owed).addDebt(amount);
            }
        }
        for (User user: this.users) {
            if (owed.equals(user.getName())) {
                user.findEntry(name).addOwed(amount);
            }
        }
        EventLog.getInstance().logEvent(new Event("Amount owed to " + owed + " by " + name
                + " increased by " + amount + "."));
    }

    // REQUIRES : string contained in original list of names
    // EFFECTS : returns User with name
    public User findUser(String name) {
        User user = null;
        for (User u: users) {
            if (name.equals(u.getName())) {
                user = u;
                break;
            }
        }
        return user;
    }


    // MODIFIES : this
    // EFFECTS : Balances values between users
    public void balanceLedger() {
        for (User user: this.users) {
            for (Entry e : user.getEntries()) {
                e.balanceVal();
            }
        }
        EventLog.getInstance().logEvent(new Event("Ledger balanced."));
    }

    // EFFECTS : returns this as a JSON object
    public JSONObject ledgerToJson() {
        JSONObject jsonLedger = new JSONObject();
        jsonLedger.put("users", usersToJson(users));
        return jsonLedger;
    }

    // EFFECTS : returns users as a JSON array
    public JSONArray usersToJson(ArrayList<User> users) {
        JSONArray usersJson = new JSONArray();
        for (User u : users) {
            usersJson.put(u.userToJson());
        }
        return usersJson;
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<String> getOriginalNames() {
        ArrayList<String> ogNames = new ArrayList<>();
        for (User u: this.users) {
            ogNames.add(u.getName());
        }
        return ogNames;
    }

    // MODIFIES : this
    // EFFECTS : removes user with string name, and its corresponding
    //           entries in all other users
    public void removeUser(String name) {
        for (User u : this.users) {
            for (Entry e : u.getEntries()) {
                if (e.getName().equals(name)) {
                    ArrayList<Entry> entries = u.getEntries();
                    entries.remove(e);
                    u.setEntries(entries);
                    break;
                }
            }
        }
        this.users.remove(findUser(name));
        EventLog.getInstance().logEvent(new Event("User: " + name + " removed."));
    }
}
