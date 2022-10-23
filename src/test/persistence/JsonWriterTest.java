package persistence;

import model.Entry;
import model.Ledger;
import model.LedgerTestHelper;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;


// Test for exception and empty case based on JsonSerializationDemo example
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriterTest extends LedgerTestHelper {
    private ArrayList<String> emptyNameList = new ArrayList<>();
    private ArrayList<User> emptyUserList = new ArrayList<>();
    private ArrayList<User> expectedUsers;
    private User u1;
    private User u2;
    private User u3;
    private ArrayList<Entry> u1Entries;
    private ArrayList<Entry> u2Entries;
    private ArrayList<Entry> u3Entries;
    private Entry U1toU2;
    private Entry U1toU3;
    private Entry U2toU1;
    private Entry U2toU3;
    private Entry U3toU1;
    private Entry U3toU2;

    @BeforeEach
    public void setUp() {
        U1toU2 = new Entry("User 2");
        U1toU3 = new Entry("User 3");
        U2toU1 = new Entry("User 1");
        U2toU3 = new Entry("User 3");
        U3toU1 = new Entry("User 1");
        U3toU2 = new Entry("User 2");
        u1Entries = new ArrayList<>();
        u1Entries.add(U1toU2);
        u1Entries.add(U1toU3);
        u2Entries = new ArrayList<>();
        u2Entries.add(U2toU1);
        u2Entries.add(U2toU3);
        u3Entries = new ArrayList<>();
        u3Entries.add(U3toU1);
        u3Entries.add(U3toU2);
        u1 = new User("User 1", emptyNameList);
        u2 = new User("User 2", emptyNameList);
        u3 = new User("User 3", emptyNameList);
        expectedUsers = new ArrayList<>();
        u1.setEntries(u1Entries);
        u2.setEntries(u2Entries);
        u3.setEntries(u3Entries);
        expectedUsers.add(u1);
        expectedUsers.add(u2);
        expectedUsers.add(u3);
        U1toU2.setDebt(496);
        U1toU3.setDebt(21102);
        U2toU1.setOwed(496);
        U2toU3.setOwed(513);
        U3toU2.setDebt(513);
        U3toU1.setOwed(21102);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            Ledger ledger = new Ledger(emptyNameList);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("Exception was expected");
        } catch (IOException e) {}
    }

    @Test
    void testWriterEmptyLedger() {
        try {
            Ledger emptyLedger = new Ledger(emptyNameList);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyLedger.json");
            writer.open();
            writer.write(emptyLedger);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyLedger.json");
            emptyLedger = reader.read();
            assertTrue(isEqualLedger(emptyUserList, emptyLedger));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterFilledLedger() {
        try {
            Ledger ledger = new Ledger(emptyNameList);
            ledger.setUsers(expectedUsers);
            JsonWriter writer = new JsonWriter("./data/testWriterFilledLedger.json");
            writer.open();
            writer.write(ledger);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterFilledLedger.json");
            ledger = reader.read();
            assertTrue(isEqualLedger(expectedUsers, ledger));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
