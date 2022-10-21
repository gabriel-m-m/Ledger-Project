package persistence;

import model.Entry;
import model.Ledger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import model.User;
import org.json.*;


// Code for read operations taken from JsonSerializationDemo example
public class JsonReader {
    private String source;
    private ArrayList<String> initList = new ArrayList<>();

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads ledger from file
    //          throws IOException if an error occurs when reading data from file
    public Ledger read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLedger(jsonObject);
    }

    // EFFECTS: reads source file as string
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses ledger from JSON object
    private Ledger parseLedger(JSONObject jsonObject) {
        JSONArray userFromJson = jsonObject.getJSONArray("users");
        ArrayList<User> users = addUsers(userFromJson);
        Ledger ledger = new Ledger(initList);
        ledger.setUsers(users);
        return ledger;
    }

    // MODIFIES: ledger
    // EFFECTS: parses users from JSON array
    private ArrayList<User> addUsers(JSONArray jsonArray) {
        ArrayList<User> users = new ArrayList<>();
        for (Object jsonUser : jsonArray) {
            JSONObject u = (JSONObject) jsonUser;
            String name = u.getString("name");
            User user = new User(name,initList);
            JSONArray jsonEntries = u.getJSONArray("entries");
            ArrayList<Entry> entries = addEntries(jsonEntries);
            user.setEntries(entries);
            users.add(user);
        }
        return users;
    }

    // MODIFIES: ledger
    // EFFECTS: parses entries from JSON array
    private ArrayList<Entry> addEntries(JSONArray jsonArray) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (Object jsonEntry : jsonArray) {
            JSONObject e = (JSONObject) jsonEntry;
            String name = e.getString("name");
            int owed = e.getInt("owed");
            int debt = e.getInt("debt");
            Entry entry = new Entry(name);
            entry.setOwed(owed);
            entry.setDebt(debt);
            entries.add(entry);
        }
        return entries;
    }
}
