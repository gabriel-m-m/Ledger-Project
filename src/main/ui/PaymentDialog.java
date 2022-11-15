package ui;

import model.Ledger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Dialog for transaction in Ledger
public class PaymentDialog {
    private String command;
    static JPanel paymentPane;
    private JDialog paymentDialog;
    private JTextPane actionDescription;
    private JTextField amountField;
    private Ledger ledger;
    private ArrayList<String> originalNamesCopy;
    static String[] firstChoices;

    protected JComboBox<String> firstUserChoice;
    protected JComboBox<String> secondUserChoice;
    private JButton finishButton;
    private static final String finishActionString = "Finish";

    // EFFECTS : Constructors a dialog for the command, and updates the Ledger
    public PaymentDialog(String command, Ledger ledger, ArrayList<String> ogNames) {
        this.command = command;
        this.originalNamesCopy = new ArrayList<>(ogNames);
        firstChoices = originalNamesCopy.toArray(new String[0]);
        this.ledger = ledger;
        dialogUI();
    }

    // MODIFIES : this
    // EFFECTS : Runs the dialog UI
    public void dialogUI() {
        firstUserChoice = new JComboBox<>(firstChoices);
        secondUserChoice = new JComboBox<>(firstChoices);
        paymentPane = new JPanel();
        paymentDialog = new JDialog();
        paymentDialog.setTitle(command + " a user");
        paymentDialog.setModal(true);
        finishButton = new JButton(finishActionString);
        finishButton.addActionListener(new FinishListener());
        amountField = new JTextField(12);
        actionDescription = new JTextPane();
        actionDescription.setText(command.equals("Owe") ? " owes " : " is paying ");
        actionDescription.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        actionDescription.setEditable(false);
        paymentDialog.setContentPane(paymentPane);
        paymentDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        paymentPane.add(firstUserChoice);
        paymentPane.add(actionDescription);
        paymentPane.add(secondUserChoice);
        paymentPane.add(amountField);
        paymentPane.add(finishButton);
        paymentDialog.setBounds(50,80, 0, 0);
        paymentDialog.pack();
        paymentDialog.setVisible(true);
    }

    // Action listener for the "Finish" button
    private class FinishListener implements ActionListener {
        // MODIFIES : this
        // EFFECTS : If inputs are valid: two different name chosen, valid integer input
        //           Updates ledger and closes dialog
        public void actionPerformed(ActionEvent e) {
            if (firstUserChoice.getSelectedItem() == null || secondUserChoice.getSelectedItem() == null) {
                return;
            }
            if (!isValidInteger(amountField.getText())) {
                return;
            }
            String user1 = firstUserChoice.getSelectedItem().toString();
            String user2 = secondUserChoice.getSelectedItem().toString();
            int amount = Integer.parseInt(amountField.getText());
            if (!user1.equals(user2)) {
                if (command.equals("Owe")) {
                    ledger.increaseOwed(user1, user2, amount);
                    paymentDialog.setVisible(false);
                    paymentPane.setVisible(false);
                } else if (ledger.findUser(user1).findEntry(user2).getDebt() >= amount) {
                    ledger.payDebts(user1, user2, amount);
                    paymentDialog.setVisible(false);
                    paymentPane.setVisible(false);
                }
            }
        }
    }

    // EFFECTS : Returns true if integer input is valid
    public boolean isValidInteger(String text) {
        if (text == null || text.length() <= 0) {
            return false;
        } else {
            try {
                Integer.parseInt(text);
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        if (Integer.parseInt(text) <= 0) {
            return false;
        }
        return true;
    }
}
