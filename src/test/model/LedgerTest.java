package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class LedgerTest extends LedgerTestHelper {
    private Ledger ledger;
    private ArrayList<User> users;
    private ArrayList<String> names;
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
        names = new ArrayList<>();
        names.add("User 1");
        names.add("User 2");
        names.add("User 3");
        ledger = new Ledger(names);
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
        u1 = new User("User 1", names);
        u2 = new User("User 2", names);
        u3 = new User("User 3", names);
        users = new ArrayList<>();
        u1.setEntries(u1Entries);
        u2.setEntries(u2Entries);
        u3.setEntries(u3Entries);
        users.add(u1);
        users.add(u2);
        users.add(u3);
    }


    @Test
    public void testConstructor() {
        assertTrue(isEqualLedger(users, ledger));
        assertEquals(names, ledger.getOriginalNames());
    }

    @Test
    public void testIncreaseOwed() {
        ledger.increaseOwed("User 1", "User 2", 40);
        U2toU1.setOwed(40);
        U1toU2.setDebt(40);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testIncreaseOwedMultiple() {
        ledger.increaseOwed("User 1", "User 2", 40);
        ledger.increaseOwed("User 1", "User 2", 20);
        U2toU1.setOwed(60);
        U1toU2.setDebt(60);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testIncreaseOwedMultipleDifferent() {
        ledger.increaseOwed("User 1", "User 2", 30);
        ledger.increaseOwed("User 3", "User 2", 90);
        U2toU1.setOwed(30);
        U1toU2.setDebt(30);
        U2toU3.setOwed(90);
        U3toU2.setDebt(90);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testPayDebts() {
        // This finds the entry between user 1 and user 2 and sets the debt
        ledger.getUsers().get(0).getEntries().get(0).setDebt(30);
        ledger.getUsers().get(1).getEntries().get(0).setOwed(30);
        ledger.payDebts("User 1", "User 2", 20);
        U1toU2.setDebt(10);
        U2toU1.setOwed(10);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testPayDebtsExact() {
        // This finds the entry between user 1 and user 2 and sets the debt
        ledger.getUsers().get(0).getEntries().get(1).setOwed(250);
        ledger.getUsers().get(2).getEntries().get(0).setDebt(250);
        ledger.payDebts("User 3", "User 1", 250);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testPayDebtsMultiple() {
        // This finds the entry between user 1 and user 2 and sets the debt
        ledger.getUsers().get(0).getEntries().get(0).setDebt(300);
        ledger.getUsers().get(1).getEntries().get(0).setOwed(300);
        ledger.payDebts("User 1", "User 2", 230);
        ledger.payDebts("User 1", "User 2", 30);
        U1toU2.setDebt(40);
        U2toU1.setOwed(40);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testPayDebtsMultipleDifferent() {
        // This finds the entry between user 1 and user 2 and sets the debt
        ledger.getUsers().get(0).getEntries().get(0).setDebt(300);
        ledger.getUsers().get(1).getEntries().get(0).setOwed(300);
        ledger.payDebts("User 1", "User 2", 230);
        ledger.getUsers().get(0).getEntries().get(1).setOwed(250);
        ledger.getUsers().get(2).getEntries().get(0).setDebt(250);
        ledger.payDebts("User 3", "User 1", 130);
        U1toU2.setDebt(70);
        U2toU1.setOwed(70);
        U1toU3.setOwed(120);
        U3toU1.setDebt(120);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testBalanceLedgerDebtHigher() {
        ledger.getUsers().get(0).getEntries().get(1).setDebt(300);
        ledger.getUsers().get(0).getEntries().get(1).setOwed(250);
        ledger.getUsers().get(2).getEntries().get(0).setOwed(300);
        ledger.getUsers().get(2).getEntries().get(0).setDebt(250);
        ledger.balanceLedger();
        U1toU3.setDebt(50);
        U1toU3.setOwed(0);
        U3toU1.setOwed(50);
        U3toU1.setDebt(0);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testBalanceLedgerOwedHigher() {
        ledger.getUsers().get(0).getEntries().get(0).setDebt(100);
        ledger.getUsers().get(0).getEntries().get(0).setOwed(600);
        ledger.getUsers().get(1).getEntries().get(0).setOwed(100);
        ledger.getUsers().get(1).getEntries().get(0).setDebt(600);
        ledger.balanceLedger();
        U1toU2.setDebt(0);
        U1toU2.setOwed(500);
        U2toU1.setOwed(0);
        U2toU1.setDebt(500);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testBalanceLedgerSame() {
        ledger.getUsers().get(1).getEntries().get(1).setDebt(1200);
        ledger.getUsers().get(1).getEntries().get(1).setOwed(1200);
        ledger.getUsers().get(2).getEntries().get(1).setOwed(1200);
        ledger.getUsers().get(2).getEntries().get(1).setDebt(1200);
        ledger.balanceLedger();
        U2toU3.setDebt(0);
        U2toU3.setOwed(0);
        U3toU2.setOwed(0);
        U3toU2.setDebt(0);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testBalanceLedgerMultiple() {
        ledger.getUsers().get(1).getEntries().get(1).setDebt(1200);
        ledger.getUsers().get(1).getEntries().get(1).setOwed(1000);
        ledger.getUsers().get(2).getEntries().get(1).setOwed(1200);
        ledger.getUsers().get(2).getEntries().get(1).setDebt(1000);
        ledger.getUsers().get(0).getEntries().get(0).setDebt(750);
        ledger.getUsers().get(0).getEntries().get(0).setOwed(730);
        ledger.getUsers().get(1).getEntries().get(0).setOwed(750);
        ledger.getUsers().get(1).getEntries().get(0).setDebt(730);
        ledger.balanceLedger();
        U1toU2.setDebt(20);
        U1toU2.setOwed(0);
        U2toU1.setOwed(20);
        U2toU1.setDebt(0);
        U2toU3.setDebt(200);
        U2toU3.setOwed(0);
        U3toU2.setOwed(200);
        U3toU2.setDebt(0);
        assertTrue(isEqualLedger(users, ledger));
    }

    @Test
    public void testFindUser() {
        ArrayList<User> expected = new ArrayList<>();
        expected.add(u1);
        ArrayList<User> actual = new ArrayList<>();
        actual.add(ledger.findUser("User 1"));
        ledger.setUsers(actual);
        assertTrue(isEqualLedger(expected, ledger));
    }

    @Test
    public void testFindUserOther() {
        ArrayList<User> expected = new ArrayList<>();
        expected.add(u3);
        ArrayList<User> actual = new ArrayList<>();
        actual.add(ledger.findUser("User 3"));
        ledger.setUsers(actual);
        assertTrue(isEqualLedger(expected, ledger));
    }

    @Test
    public void testFindUserNull() {
        assertEquals(null, ledger.findUser("User 0"));
    }
}