package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;
    private ArrayList<String> names;
    private ArrayList<Entry> entries;
    private Entry e1;
    private Entry e2;
    private Entry e3;

    @BeforeEach
    public void setUp() {
        names = new ArrayList<>();
        names.add("Tester 1");
        names.add("Tester 2");
        names.add("Tester 3");
        user = new User("Testing", names);
        e1 = new Entry(names.get(0));
        e2 = new Entry(names.get(1));
        e3 = new Entry(names.get(2));
        entries = new ArrayList<>();
        entries.add(e1);
        entries.add(e2);
        entries.add(e3);
    }

    @Test
    public void testConstructor() {
        assertEquals("Testing", user.getName());
        assertEquals(e1.getName(), user.getEntries().get(0).getName());
        assertEquals(e1.getDebt(), user.getEntries().get(0).getDebt());
        assertEquals(e1.getOwed(), user.getEntries().get(0).getOwed());
        assertEquals(e2.getName(), user.getEntries().get(1).getName());
        assertEquals(e2.getDebt(), user.getEntries().get(1).getDebt());
        assertEquals(e2.getOwed(), user.getEntries().get(1).getOwed());
        assertEquals(e3.getName(), user.getEntries().get(2).getName());
        assertEquals(e3.getDebt(), user.getEntries().get(2).getDebt());
        assertEquals(e3.getOwed(), user.getEntries().get(2).getOwed());
    }

    @Test
    public void testFindEntry() {
        Entry finde1 = user.findEntry("Tester 1");
        Entry finde2 = user.findEntry("Tester 2");
        Entry finde3 = user.findEntry("Tester 3");
        Entry nulle = user.findEntry("Not in List");
        assertEquals(null, nulle);
        assertEquals(e1.getName(), finde1.getName());
        assertEquals(e1.getDebt(), finde1.getDebt());
        assertEquals(e1.getOwed(), finde1.getOwed());
        assertEquals(e2.getName(), finde2.getName());
        assertEquals(e2.getDebt(), finde2.getDebt());
        assertEquals(e2.getOwed(), finde2.getOwed());
        assertEquals(e3.getName(), finde3.getName());
        assertEquals(e3.getDebt(), finde3.getDebt());
        assertEquals(e3.getOwed(), finde3.getOwed());
    }
}


