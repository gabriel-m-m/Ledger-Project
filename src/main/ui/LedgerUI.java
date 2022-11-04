package ui;

import model.Ledger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Code based on ListDemo and ListDialog
// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html
public class LedgerUI extends JFrame  {
    private JList list;
    private DefaultListModel listModel;
    static JFrame startUpFrame;
    static JFrame userAddFrame;

    private static final String oweActionString = "Owe a user";
    private static final String payActionString = "Pay a user";
    private static final String loadActionString = "Load ledger";
    private static final String createActionString = "Create new ledger";
    private static final String saveActionString = "Save ledger";
    private JButton oweButton;
    private JButton payButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton createButton;
    private JButton doneButton;
    private JTextField username;
    private JTextField ledgerSummary;

    private ArrayList<String> originalNames;
    private Ledger ledger;

    public LedgerUI() {
        // initialize stuff, exit on close things
        //use setVisible false to move to diff frames??
        originalNames = new ArrayList<>();
        startUpGUI();
        // mainMenuGUI();
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

            //Create the list and put it in a scroll pane.
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

            username = new JTextField(10);
            username.addActionListener(addUserListener);
            username.getDocument().addDocumentListener(addUserListener);
            String name = listModel.getElementAt(
                    list.getSelectedIndex()).toString();

            //Create a panel that uses BoxLayout.
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
    }

    class CreateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getLedgerNamesUI();
        }
    }

    class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            mainMenuGUI();
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
                mainMenuGUI();
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

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
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
}
