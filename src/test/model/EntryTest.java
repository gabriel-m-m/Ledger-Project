package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntryTest {
    private Entry entry;

    @BeforeEach
    public void setUp() {
        entry = new Entry("Test 1");
    }

    @Test
    public void testConstructor() {
        assertEquals("Test 1", entry.getName());
        assertEquals(0, entry.getDebt());
        assertEquals(0, entry.getOwed());
    }

    @Test
    public void testAddDebt() {
        entry.addDebt(10);
        assertEquals(10, entry.getDebt());
    }

    @Test
    public void testAddOwed() {
        entry.addOwed(15);
        assertEquals(15, entry.getOwed());
    }

    @Test
    public void testSubtractDebtExact() {
        entry.setDebt(100);
        entry.subtractDebt(100);
        assertEquals(0, entry.getDebt());

    }

    @Test
    public void testSubtractDebt() {
        entry.setDebt(200);
        entry.subtractDebt(30);
        assertEquals(170, entry.getDebt());

    }

    @Test
    public void testSubtractOwed() {
        entry.setOwed(400);
        entry.subtractOwed(400);
        assertEquals(0, entry.getOwed());
    }

    @Test
    public void testSubtractOwedExact() {
        entry.setOwed(300);
        entry.subtractOwed(250);
        assertEquals(50, entry.getOwed());
    }

    @Test
    public void testBalanceValOwedHigher() {
        entry.setDebt(100);
        entry.setOwed(300);
        entry.balanceVal();
        assertEquals(0, entry.getDebt());
        assertEquals(200, entry.getOwed());
    }

    @Test
    public void testBalanceValDebtHigher() {
        entry.setDebt(100);
        entry.setOwed(95);
        entry.balanceVal();
        assertEquals(5, entry.getDebt());
        assertEquals(0, entry.getOwed());
    }

    @Test
    public void testBalanceValSameValue() {
        entry.setDebt(100);
        entry.setOwed(100);
        entry.balanceVal();
        assertEquals(0, entry.getDebt());
        assertEquals(0, entry.getOwed());
    }

    @Test
    public void testBalanceValBothZero() {
        entry.balanceVal();
        assertEquals(0, entry.getDebt());
        assertEquals(0, entry.getOwed());
    }
}
