package ui;

import model.Entry;
import model.Ledger;
import model.User;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

// Code based on ListDemo and ListDialog
// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
public class LedgerUI extends JFrame  {
    private JList list;
    private DefaultListModel listModel;
    static JFrame startUpFrame;
    static JFrame userAddFrame;
    static JFrame mainFrame;
    private JTextPane ledgerSummaryPane;

    private static final String JSON_STORE_LOC = "./data/ledger.json";
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

    private ArrayList<String> originalNames;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Ledger ledger;

    public LedgerUI() {
        // initialize stuff, exit on close things
        //use setVisible false to move to diff frames??
        originalNames = new ArrayList<>();
        jsonReader = new JsonReader(JSON_STORE_LOC);
        jsonWriter = new JsonWriter(JSON_STORE_LOC);
        startUpGUI();
    }

    public void startUpGUI() {
        startUpFrame = new JFrame("Ledger Startup");
        startUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createButton = new JButton(createActionString);
        createButton.setActionCommand(createActionString);
        createButton.addActionListener(new CreateListener());
        loadButton = new JButton(loadActionString);
        loadButton.setActionCommand(loadActionString);
        loadButton.addActionListener(new LoadListener());
        JPanel buttonPane = new JPanel();
        JComponent newContentPane = buttonPane;
        newContentPane.setOpaque(true); //content panes must be opaque
        startUpFrame.setContentPane(newContentPane);
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
        JComponent newContentPane = userPane;
        newContentPane.setOpaque(true); //content panes must be opaque
        userAddFrame.setContentPane(newContentPane);
        userAddFrame.pack();
        userAddFrame.setVisible(true);
    }


    public class AddUserUI extends JPanel {

        public AddUserUI() {
            super(new BorderLayout());

            listModel = new DefaultListModel();
            listModel.addElement("");

            list = new JList(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setSelectedIndex(0);
            list.setVisibleRowCount(5);
            JScrollPane listScrollPane = new JScrollPane(list);

            JButton addUser = new JButton("Add user");
            addUser.setActionCommand("Add user");
            AddUserListener addUserListener = new AddUserListener(addUser);
            addUser.addActionListener(addUserListener);
            addUser.setEnabled(false);

            doneButton = new JButton("Done");
            doneButton.setActionCommand("Done");
            doneButton.addActionListener(new DoneListener());
            doneButton.setEnabled(false);

            username = new JTextField(10);
            username.addActionListener(addUserListener);
            username.getDocument().addDocumentListener(addUserListener);
            String name = listModel.getElementAt(list.getSelectedIndex()).toString();

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
            listModel.remove(0);
        }
    }

    public void mainMenuGUI() {
        startUpFrame.setVisible(false);
        mainFrame = new JFrame("Main Menu");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        JPanel mainPane = new JPanel();
        JPanel topButtonPane = new JPanel();
        JPanel botButtonPane = new JPanel();
        ledgerSummaryPane = new JTextPane();
        ledgerSummaryPane.setText(ledgerSummary());
        ledgerSummaryPane.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        ledgerSummaryPane.enableInputMethods(false);
        ledgerSummaryPane.setEditable(false);
        JComponent newContentPane = mainPane;
        newContentPane.setOpaque(true); //content panes must be opaque
        mainFrame.setContentPane(newContentPane);
        botButtonPane.add(oweButton);
        botButtonPane.add(balanceButton);
        botButtonPane.add(payButton);
        topButtonPane.add(saveButton);
        topButtonPane.add(loadButton);
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
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

        JButton splashButton = new JButton(new ImageIcon("./src/main/ui/splashscreen.gif"));
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

        }
    }

    class PayListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class BalanceListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ledger.balanceLedger();
            ledgerSummaryPane.setText(ledgerSummary());
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
        }
    }


    class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                ledger = jsonReader.read();
            } catch (IOException ex) {
                System.out.println("Unable to read from " + JSON_STORE_LOC);
            }
            if (startUpFrame.isVisible()) {
                splashScreenUI();
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


            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            listModel.insertElementAt(username.getText(), index);
            originalNames.add(username.getText());
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());
            int size = listModel.getSize();
            if (size >= 2) {
                doneButton.setEnabled(true);
            }

            //Reset the text field.
            username.requestFocusInWindow();
            username.setText("");

            //Select the new item and make it visible.
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

    public static void main(String[] args) {
        new LedgerUI();
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
}
