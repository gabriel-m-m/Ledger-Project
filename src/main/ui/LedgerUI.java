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

// Code based on ListDemo and ListDialog
// via: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
public class LedgerUI extends JFrame  {
    private JList list;
    private DefaultListModel listModel;
    static JFrame startUpFrame;
    static JFrame userAddFrame;
    static JFrame mainFrame;
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

    public LedgerUI() {
        originalNames = new ArrayList<>();
        jsonReader = new JsonReader(JSON_STORE_LOC);
        jsonWriter = new JsonWriter(JSON_STORE_LOC);
        elementInit();
        startUpGUI();
    }

    public void elementInit() {
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

    public void getLedgerNamesUI() {
        startUpFrame.setVisible(false);
        userAddFrame = new JFrame("Add users to ledger");
        userAddFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel userPane = new AddUserUI();
        userPane.setOpaque(true);
        userAddFrame.setContentPane(userPane);
        userAddFrame.pack();
        userAddFrame.setVisible(true);
    }


    public class AddUserUI extends JPanel {

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

    public void mainMenuGUI() {
        startUpFrame.setVisible(false);
        mainFrame = new JFrame("Main Menu");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPane = new JPanel();
        JPanel topButtonPane = new JPanel();
        JPanel botButtonPane = new JPanel();
        ledgerSummaryPane = new JTextPane();
        ledgerSummaryPane.setText(ledgerSummary());
        ledgerSummaryPane.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        ledgerSummaryPane.setEditable(false);
        uiConsolePane = new JTextPane();
        uiConsolePane.setText("Ledger Initialized!");
        uiConsolePane.setEditable(false);
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
        mainPane.setVisible(true);
    }

    public void splashScreenUI() {
        startUpFrame.setVisible(false);
        JFrame splashFrame = new JFrame("Ledger created successfully! \n Press to continue");
        splashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        class SplashListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                splashFrame.setVisible(false);
                mainMenuGUI();
            }
        }

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



    class CreateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getLedgerNamesUI();
        }
    }

    class OweListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            paymentDialog = new PaymentDialog("Owe", ledger, originalNames);
            ledgerSummaryPane.setText(ledgerSummary());
            uiConsolePane.setText("Ledger updated!");
        }
    }

    class PayListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            paymentDialog = new PaymentDialog("Pay", ledger, originalNames);
            ledgerSummaryPane.setText(ledgerSummary());
            uiConsolePane.setText("Ledger updated!");
        }
    }

    private class BalanceListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ledger.balanceLedger();
            ledgerSummaryPane.setText(ledgerSummary());
            uiConsolePane.setText("Ledger balanced!");
        }
    }

    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(ledger);
                jsonWriter.close();
            } catch (FileNotFoundException fnfe) {
                System.out.println("Unable to write to file: " + JSON_STORE_LOC);
            }
            uiConsolePane.setText("Ledger saved!");
        }
    }


    class LoadListener implements ActionListener {
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

    class DoneListener implements ActionListener {
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

    class AddUserListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddUserListener(JButton button) {
            this.button = button;
        }

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

        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

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
