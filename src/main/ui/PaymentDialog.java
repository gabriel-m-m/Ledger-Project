package ui;

import model.Ledger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

    public PaymentDialog(String command, Ledger ledger, ArrayList<String> ogNames) {
        this.command = command;
        this.originalNamesCopy = new ArrayList<>(ogNames);
        firstChoices = originalNamesCopy.toArray(new String[0]);
        this.ledger = ledger;
        dialogUI();
    }

    public void dialogUI() {
        firstUserChoice = new JComboBox<>(firstChoices);
        firstUserChoice.setSize(20, 10);
        secondUserChoice = new JComboBox<>(firstChoices);
        secondUserChoice.setSize(20, 10);
        paymentPane = new JPanel();
        paymentDialog = new JDialog();
        paymentDialog.setTitle(command + " a user");
        paymentDialog.setModal(true);
        finishButton = new JButton(finishActionString);
        finishButton.addActionListener(new FinishListener());
        amountField = new JTextField(12);
        actionDescription = new JTextPane();
        actionDescription.setText(command.equals("owe") ? " owes " : " is paying ");
        actionDescription.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        actionDescription.enableInputMethods(false);
        actionDescription.setEditable(false);
        paymentDialog.setContentPane(paymentPane);
        paymentDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        paymentPane.add(firstUserChoice);
        paymentPane.add(actionDescription);
        paymentPane.add(secondUserChoice);
        paymentPane.add(amountField);
        paymentPane.add(finishButton);
        paymentDialog.pack();
        paymentDialog.setVisible(true);
    }

    private class FinishListener implements ActionListener {
        @Override
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
                if (command.equals("owe")) {
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
