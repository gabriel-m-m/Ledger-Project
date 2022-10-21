package persistence;

import model.Entry;
import model.Ledger;
import model.LedgerTestHelper;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.ArrayList;



import static org.junit.jupiter.api.Assertions.*;

// Test for exception and empty case based on JsonSerializationDemo example
public class JsonReaderTest extends LedgerTestHelper {
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
        U1toU2.setOwed(123123);
        U1toU3.setDebt(99999);
        U2toU1.setDebt(123123);
        U2toU3.setOwed(4820);
        U3toU2.setDebt(4820);
        U3toU1.setOwed(99999);
    }

    @Test
    void testReaderFileNonExistent() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Ledger ledger = reader.read();
            fail("Exception expected");
        } catch (IOException e) {}
    }

    @Test
    void testReaderEmptyLedger() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyLedger.json");
        try {
            Ledger ledger = reader.read();
            assertTrue(isEqualLedger(emptyUserList, ledger));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderFilledLedger() {
        JsonReader reader = new JsonReader("./data/testReaderFilledLedger.json");
        try {
            Ledger ledger = reader.read();
            assertTrue(isEqualLedger(expectedUsers, ledger));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
