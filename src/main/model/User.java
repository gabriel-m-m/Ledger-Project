package model;

import java.util.ArrayList;

// Represents a user in the ledger with name and entries
public class User {
    private String name;
    private ArrayList<Entry> entries;


    // REQUIRES : list of unique names with length > 0
    // EFFECTS : Constructs a user with name and entries for names in list
    public User(String name, ArrayList<String> names) {
        this.name = name;
        entries = new ArrayList<>();
        for (String s : names) {
            if (!s.equals(name)) {
                Entry entry = new Entry(s);
                entries.add(entry);
            }
        }
    }

    // REQUIRES : string contained in original list of names
    // EFFECTS : returns Entry with name
    public Entry findEntry(String name) {
        Entry entry = null;
        for (Entry e: entries) {
            if (name.equals(e.getName())) {
                entry = e;
                break;
            }
        }
        return entry;
    }

    public ArrayList<Entry> getEntries() {
        return this.entries;
    }

    public String getName() {
        return this.name;
    }

    public void setEntries(ArrayList<Entry> e) {
        this.entries = e;
    }

}
