package ui;

import model.Entry;
import model.Ledger;
import model.User;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// Ledger GUI application
// Code based on ListDemo and ListDialog
// via: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
public class LedgerUI extends JFrame  {
    private JList list;
    private DefaultListModel listModel;
    static JFrame startUpFrame;
    static JFrame userAddFrame;
    static JFrame mainFrame;
    static JFrame splashFrame;
    private JTextPane ledgerSummaryPane;
    private JTextPane uiConsolePane;
    private PaymentDialog paymentDialog;

    private static final String JSON_STORE_LOC = "./data/ledger.json";
    private static final String TRANSITION_GIF_LOC = "./src/main/ui/splashscreen.gif";
    private static final String oweActionString = "Owe a user";
    private static final String payActionString = "Pay a user";
    private static final String balanceActionString = "Balance ledger";
    private static final String loadActionString = "Load ledger";
    private static final String createActionString = "Create new ledger";
    private static final String saveActionString = "Save ledger";
    private JButton oweButton;
    private JButton payButton;
    private JButton balanceButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton createButton;
    private JButton doneButton;
    private JTextField username;

    protected ArrayList<String> originalNames;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    protected Ledger ledger;

    // EFFECTS : Constructs LedgerUI application
    public LedgerUI() {
        originalNames = new ArrayList<>();
        jsonReader = new JsonReader(JSON_STORE_LOC);
        jsonWriter = new JsonWriter(JSON_STORE_LOC);
        buttonInit();
        startUpGUI();
    }

    // MODIFIES : this
    // EFFECTS : Initializes buttons to be used in UI
    public void buttonInit() {
        createButton = new JButton(createActionString);
        createButton.setActionCommand(createActionString);
        createButton.addActionListener(new CreateListener());
        loadButton = new JButton(loadActionString);
        loadButton.setActionCommand(loadActionString);
        loadButton.addActionListener(new LoadListener());
        doneButton = new JButton("Done");
        doneButton.setActionCommand("Done");
        doneButton.addActionListener(new DoneListener());
        doneButton.setEnabled(false);
        oweButton = new JButton(oweActionString);
        oweButton.setActionCommand(oweActionString);
        oweButton.addActionListener(new OweListener());
        payButton = new JButton(payActionString);
        payButton.setActionCommand(payActionString);
        payButton.addActionListener(new PayListener());
        saveButton = new JButton(saveActionString);
        saveButton.setActionCommand(saveActionString);
        saveButton.addActionListener(new SaveListener());
        balanceButton = new JButton(balanceActionString);
        balanceButton.setActionCommand(balanceActionString);
        balanceButton.addActionListener(new BalanceListener());
    }

    // MODIFIES : this
    // EFFECTS : Runs the startup UI menu
    public void startUpGUI() {
        startUpFrame = new JFrame("Ledger Startup");
        startUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel buttonPane = new JPanel();
        buttonPane.setOpaque(true);
        startUpFrame.setContentPane(buttonPane);
        buttonPane.add(createButton);
        buttonPane.add(loadButton);
        startUpFrame.pack();
        startUpFrame.setVisible(true);
    }

    // MODIFIES : this
    // EFFECTS : Runs the menu to create new Ledger
    public void createLedgerUI() {
        startUpFrame.setVisible(false);
        userAddFrame = new JFrame("Add users to ledger");
        userAddFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel userPane = new AddUserUI();
        userPane.setOpaque(true);
        userAddFrame.setContentPane(userPane);
        userAddFrame.pack();
        userAddFrame.setVisible(true);
    }

    // Menu for ledger creation
    public class AddUserUI extends JPanel {

        // EFFECTS : Constructor for Ledger creation UI
        public AddUserUI() {
            super(new BorderLayout());

            listModel = new DefaultListModel();
            //listModel.addElement("");

            list = new JList(listModel);
            //list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            //list.setSelectedIndex(0);
            //list.setVisibleRowCount(5);
            JScrollPane listScrollPane = new JScrollPane(list);

            JButton addUser = new JButton("Add user");
            addUser.setActionCommand("Add user");
            AddUserListener addUserListener = new AddUserListener(addUser);
            addUser.addActionListener(addUserListener);
            addUser.setEnabled(false);

            username = new JTextField(10);
            username.addActionListener(addUserListener);
            username.getDocument().addDocumentListener(addUserListener);

            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
            buttonPane.add(doneButton);
            buttonPane.add(Box.createHorizontalStrut(5));
            buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
            buttonPane.add(Box.createHorizontalStrut(5));
            buttonPane.add(username);
            buttonPane.add(addUser);
            buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            add(listScrollPane, BorderLayout.CENTER);
            add(buttonPane, BorderLayout.PAGE_END);
            //listModel.remove(0);
        }
    }

    // MODIFIES : this
    // EFFECTS : Runs the main menu UI
    public void mainMenuGUI() {
        mainMenuInit();
        JPanel mainPane = new JPanel();
        JPanel topButtonPane = new JPanel();
        JPanel botButtonPane = new JPanel();
        StyledDocument doc = uiConsolePane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        mainPane.setOpaque(true);
        mainFrame.setContentPane(mainPane);
        botButtonPane.add(oweButton);
        botButtonPane.add(balanceButton);
        botButtonPane.add(payButton);
        topButtonPane.add(saveButton);
        topButtonPane.add(loadButton);
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
        mainPane.add(uiConsolePane, BorderLayout.CENTER);
        mainPane.add(topButtonPane, BorderLayout.PAGE_START);
        mainPane.add(ledgerSummaryPane, BorderLayout.CENTER);
        mainPane.add(botButtonPane, BorderLayout.PAGE_END);
        mainPane.setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 15));
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public void mainMenuInit() {
        startUpFrame.setVisible(false);
        mainFrame = new JFrame("Main Menu");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ledgerSummaryPane = new JTextPane();
        ledgerSummaryPane.setText(ledgerSummary());
        ledgerSummaryPane.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        ledgerSummaryPane.setEditable(false);
        uiConsolePane = new JTextPane();
        uiConsolePane.setText("Ledger Initialized!");
        uiConsolePane.setEditable(false);
    }

    // MODIFIES : this
    // EFFECTS : Runs the splashscreen
    public void splashScreenUI() {
        startUpFrame.setVisible(false);
        splashFrame = new JFrame("Ledger created successfully! \n Press to continue");
        splashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton splashButton = new JButton(new ImageIcon(TRANSITION_GIF_LOC));
        splashButton.setActionCommand("Press to continue");
        splashButton.addActionListener(new SplashListener());
        splashButton.setBackground(new Color(255,255,255));
        splashButton.setHorizontalTextPosition(SwingConstants.CENTER);
        splashButton.setVerticalTextPosition(SwingConstants.TOP);
        splashButton.setText("Press anywhere to continue");
        splashButton.setToolTipText("Press anywhere to continue");
        splashFrame.add(splashButton, BorderLayout.CENTER);
        splashFrame.setMinimumSize(new Dimension(300,300));
        splashFrame.pack();
        splashFrame.setVisible(true);
    }

    // Action listener for splashscreen
    class SplashListener implements ActionListener {
        // MODIFIES : this
        // EFFECTS : Closes splash screen and opens main menu
        public void actionPerformed(ActionEvent e) {
            splashFrame.setVisible(false);
            mainMenuGUI();
        }
    }


    // Action listener for the "Create new ledger" button
    class CreateListener implements ActionListener {
        // MODIFIES : this
        // EFFECTS : Runs the createLedgerUI
        public void actionPerformed(ActionEvent e) {
            createLedgerUI();
        }
    }

    // Action listener for the "owe a user" button
    class OweListener implements ActionListener {
        // MODIFIES : this
        // EFFECTS : Creates a new dialog with the appropriate action when button is pressed
        public void actionPerformed(ActionEvent e) {
            paymentDialog = new PaymentDialog("Owe", ledger, originalNames);
            ledgerSummaryPane.setText(ledgerSummary());
            uiConsolePane.setText("Ledger updated!");
        }
    }

    // Action listener for the "pay a user" button
    class PayListener implements ActionListener {
        // MODIFIES : this
        // EFFECTS : Creates a new dialog with the appropriate action when button is pressed
        public void actionPerformed(ActionEvent e) {
            paymentDialog = new PaymentDialog("Pay", ledger, originalNames);
            ledgerSummaryPane.setText(ledgerSummary());
            uiConsolePane.setText("Ledger updated!");
        }
    }

    // Action listener for the "balance ledger" button
    private class BalanceListener implements ActionListener {
        // MODIFIES : this
        // EFFECTS : Balances ledger when button is pressed, updates console text
        public void actionPerformed(ActionEvent e) {
            ledger.balanceLedger();
            ledgerSummaryPane.setText(ledgerSummary());
            uiConsolePane.setText("Ledger balanced!");
        }
    }

    // Action listener for the "Save ledger" button
    private class SaveListener implements ActionListener {
        // MODIFIES : this
        // EFFECTS : Saves ledger when button is pressed
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(ledger);
                jsonWriter.close();
                uiConsolePane.setText("Ledger saved!");
            } catch (FileNotFoundException fnfe) {
                System.out.println("Unable to write to file: " + JSON_STORE_LOC);
                uiConsolePane.setText("Unable to save");
            }
        }
    }

    // Action listener for the "Load ledger" button
    class LoadListener implements ActionListener {
        // MODIFIES : this
        // EFFECTS : Loads ledger, if called from startup menu, loads main menu
        public void actionPerformed(ActionEvent e) {
            try {
                ledger = jsonReader.read();
                getNames();
            } catch (IOException ex) {
                System.out.println("Unable to read from " + JSON_STORE_LOC);
            }
            if (startUpFrame.isVisible()) {
                splashScreenUI();
            } else {
                ledgerSummaryPane.setText(ledgerSummary());
                uiConsolePane.setText("Ledger loaded!");
            }
        }
    }

    // Action listener for the "done" button for the createLedgerUI
    class DoneListener implements ActionListener {
        // MODIFIES : this
        // EFFECTS : Runs the splash screen if length of names > 1
        public void actionPerformed(ActionEvent e) {
            int size = listModel.getSize();
            if (size < 2) {
                doneButton.setEnabled(false);
            } else {
                userAddFrame.setVisible(false);
                ledger = new Ledger(originalNames);
                splashScreenUI();
            }
        }
    }

    // Listener for the "Add user button" in the createLedgerUI
    class AddUserListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddUserListener(JButton button) {
            this.button = button;
        }

        // MODIFIES : this
        // EFFECTS : Adds input to originalNames if string is valid, and resets text field
        public void actionPerformed(ActionEvent e) {
            String name = username.getText();

            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                username.requestFocusInWindow();
                username.selectAll();
                return;
            }


            int index = list.getSelectedIndex();
            if (index == -1) {
                index = 0;
            } else {
                index++;
            }

            listModel.insertElementAt(username.getText(), index);
            originalNames.add(username.getText());

            int size = listModel.getSize();
            if (size >= 2) {
                doneButton.setEnabled(true);
            }

            username.requestFocusInWindow();
            username.setText("");

            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        // EFFECTS : Checks if string is already in the listmodel
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        // MODIFIES : this
        // EFFECTS : Enables button when there is an insert in the document
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        // MODIFIES : this
        // EFFECTS : Updates button when an removal in document occurs,
        //           disabling the button if the text field is empty
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        // MODIFIES : this
        // EFFECTS : Enables button if text field is not empty
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // MODIFIES : this
        // EFFECTS : Enables button
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        // MODIFIES : this
        // EFFECTS : Disables button if text field is empty and returns true
        //           returns false otherwise
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    // EFFECTS : Returns a string that summarizes Ledger
    private String ledgerSummary() {
        ArrayList<User> users = ledger.getUsers();
        String summary = "";
        for (User u : users) {
            summary += u.getName() + "\n";
            ArrayList<Entry> entries = u.getEntries();
            for (Entry e : entries) {
                summary += "owes " + e.getName() + " " + e.getDebt() + "\n";
            }
        }
        return summary;
    }

    // MODIFIES : this
    // EFFECTS : Updates originalNames with names from ledger
    public void getNames() {
        ArrayList<String> namesFromFile = new ArrayList<>();
        for (User u : ledger.getUsers()) {
            namesFromFile.add(u.getName());
        }
        originalNames = namesFromFile;
    }

    public static void main(String[] args) {
        new LedgerUI();
    }
}
