package model;

import java.util.ArrayList;

public class LedgerTestHelper {
    // EFFECTS : Test function to compare a ledger and the expected values for its users
    protected boolean isEqualLedger(ArrayList<User> expected, Ledger actual) {
        ArrayList<User> expUsers = expected;
        ArrayList<User> actUsers = actual.getUsers();
        for (int i = 0; i < actUsers.size(); i++) {
            if (expUsers.get(i).getName().equals(actUsers.get(i).getName())) {
                ArrayList<Entry> expEntries = expUsers.get(i).getEntries();
                ArrayList<Entry> actEntries = actUsers.get(i).getEntries();
                for (int n = 0; n < actEntries.size(); n++) {
                    if (expEntries.get(n).getName().equals(actEntries.get(n).getName()) &&
                            expEntries.get(n).getDebt() == actEntries.get(n).getDebt() &&
                            expEntries.get(n).getOwed() == actEntries.get(n).getOwed()) {
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
