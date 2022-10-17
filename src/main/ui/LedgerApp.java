package ui;

import model.Ledger;
import model.User;
import model.Entry;

import java.util.ArrayList;
import java.util.Scanner;

// Ledger application
public class LedgerApp {
    private Ledger ledger;
    private ArrayList<String> originalNames;
    private Scanner input;
    private String command;


    // EFFECTS : Runs the teller application
    public LedgerApp() {
        runLedger();
    }

    // MODIFIES : this
    // EFFECTS : process user inputs
    private void runLedger() {
        originalNames = new ArrayList<>();
        input = new Scanner(System.in);
        makeNameList();
        ledger = new Ledger(originalNames);
        System.out.println("Ledger has been created!");
        while (true) {
            displayOptions();
            command = input.next();
            if (command.equals("q")) {
                break;
            } else {
                processCommand(command);
            }
        }
        System.out.println("Program quit");
    }

    // EFFECTS : Process user input
    private void processCommand(String command) {
        if (command.equals("v")) {
            ledgerSummary();
        } else if (command.equals("o")) {
            doDebtIncrease();
        } else if (command.equals("p")) {
            makePayment();
        } else if (command.equals("b")) {
            balanceTellerApp();
        } else {
            System.out.println("Please enter a valid command");
        }
    }

    // REQUIRES : a non-empty string
    // MODIFIES : this
    // EFFECTS : get a valid name from user input that isn't contained in originalNames
    //           uses string for printed message
    private void addValidName(String message) {
        while (true) {
            System.out.println(message);
            command = input.next();
            if (command.equals("") || originalNames.contains(command)) {
                System.out.println("Invalid name");
            } else {
                originalNames.add(command);
                break;
            }
        }
    }

    // MODIFIES : this
    // EFFECTS : takes user input to create a list of names for ledger
    private void makeNameList() {
        addValidName("Please enter a username for your ledger");
        addValidName("Please enter a another unique username for your ledger");
        while (true) {
            System.out.println("Would you like to add more users ('yes'/'no')");
            command = input.next();
            if (command.equals("yes")) {
                addValidName("Please enter a another unique username for your ledger");
            } else if (command.equals("no")) {
                break;
            } else {
                System.out.println("Please enter a valid command");
            }
        }
    }

    // MODIFIES : this
    // EFFECTS : Makes a payment between users
    //           Quit (fails) if user doesn't owe any money
    private void makePayment() {
        ArrayList<String> bothUsers = getTwoDifferentNames("are you paying", "pay");
        String payer = bothUsers.get(0);
        String recipient = bothUsers.get(1);
        if (ledger.findUser(recipient).findEntry(payer).getOwed() == 0) {
            System.out.println("User doesn't owe any money\n");
            return;
        }
        System.out.println("How much is user paying?");
        int max = ledger.findUser(payer).findEntry(recipient).getDebt();
        int amount = getValidPaymentNumber(max);
        ledger.payDebts(payer, recipient, amount);
    }

    // REQUIRES : max > 0
    // MODIFIES : this
    // EFFECTS : gets valid number to be used in a payment transaction
    //           0 < amount <= max
    private int getValidPaymentNumber(int max) {
        while (true) {
            if (input.hasNextInt()) {
                int amount = input.nextInt();
                if (amount > max) {
                    System.out.println("User doesn't owe this much");
                } else if (amount > 0) {
                    return amount;
                } else {
                    System.out.println("Please enter a valid number");
                }
            } else {
                System.out.println("Please enter a number");
                input.next();
            }
        }
    }

    // MODIFIES : this
    // EFFECTS : Performs an increase in debt between users
    private void doDebtIncrease() {
        ArrayList<String> bothUsers = getTwoDifferentNames("do you owe", "owe");
        String payer = bothUsers.get(0);
        String recipient = bothUsers.get(1);
        System.out.println("How much does user owe?");
        while (true) {
            if (input.hasNextInt()) {
                int amount;
                amount = input.nextInt();
                if (amount > 0) {
                    ledger.increaseOwed(payer, recipient, amount);
                    break;
                } else {
                    System.out.println("Please enter a valid number");
                }
            } else {
                System.out.println("Please enter a number");
                input.next();
            }
        }
    }

    // REQUIRES : non-empty string action and message
    // MODIFIES : this
    // EFFECTS : Gets two names in originalNames to be used for operations
    //           Uses both string inputs for printed messages
    private ArrayList<String> getTwoDifferentNames(String message, String action) {
        System.out.println("What is your user name\n" + originalNames);
        ArrayList<String> bothUsers = new ArrayList<>();
        while (true) {
            command = input.next();
            if (originalNames.contains(command)) {
                bothUsers.add(command);
                break;
            } else {
                System.out.println("Invalid username");
            }
        }
        System.out.println("Which user " + message + " (cannot be yourself)\n" + originalNames);
        while (true) {
            command = input.next();
            if (command.equals(bothUsers.get(0))) {
                System.out.println("You can't " + action + " yourself!");
            } else if (originalNames.contains(command)) {
                bothUsers.add(command);
                return bothUsers;
            } else {
                System.out.println("Invalid username");
            }
        }
    }

    // MODIFIES : this
    // EFFECTS : Balances values in ledger
    private void balanceTellerApp() {
        ledger.balanceLedger();
        System.out.println("Values have been balanced!");
    }

    // EFFECTS : Prints a summary of the information in the ledger
    private void ledgerSummary() {
        ArrayList<User> users = ledger.getUsers();
        for (User u : users) {
            System.out.println(u.getName());
            ArrayList<Entry> entries = u.getEntries();
            for (Entry e : entries) {
                System.out.println("owes " + e.getName() + " " + e.getDebt());
            }
        }
    }

    // EFFECTS : displays a menu with options for user input
    private void displayOptions() {
        System.out.println("What would you like to do");
        System.out.println("'v' = View ledger summary");
        System.out.println("'o' = Owe another user money");
        System.out.println("'b' = Balance out values");
        System.out.println("'p' = Pay another user\n" + "'q' = quit");
    }
}


