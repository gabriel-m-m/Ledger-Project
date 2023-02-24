# Personal Project

## IOU (_I owe you_)

### General Information

This application will take a list of names, and will create
a ledger. Allowing users to record and add or subtract amounts 
of money they are owed or owe to other people. The application can be used by anyone, 
from within groups of friends, to between organizations. This will allow the user
to keep a record of who owes who and how much.  

This project is of interest to me as occasionally, within my group of friends, 
the issue of someone owing someone else money can arise. With one or both people forgetting
about it, it can take a while before this is ever paid back. An application such as this could 
record these amounts so that the information regarding what is owed is easily accessible. 

### User Stories:  

- As a user, I want to be able to add names to the ledger  
- As a user, I want to be able to pay off someone (who I owe) an amount of money  
- As a user, I want to be able to set an amount of money owed to me by another person  
- As a user, I want to be able to see a summary of what is owed by which people  
- As a user, I want to be able to balance out the amounts owed  
(e.g. if U1 owes U2 $10, and U2 owes U1 $3. Then, when balanced, U1 owes U2 $7, and U2 owes U1 $0)
- As a user, I want to be able to save my ledger to file
- As a user, I want to be able to load my ledger from file

# Instructions

- You can generate a ledger by
first running the UI, then clicking on the "Create new ledger" button. 
In the new window, you will be able to type in strings to be used as usernames for users to be included in the ledger. 
This can be achieved by typing a name into the text field then pressing "Add user".
- Then, in the main menu, the ledger will be displayed in the middle. Using the "Remove user"
button you can remove any of the users and their corresponding entries from the ledger. 
Any changes which will be updated in the middle display.
- You can save the state of the application by pressing the "Save" button in the main menu, after having 
created or loaded a ledger.
- You can reload the state of the application by pressing the "Load ledger" button in the start-up 
menu, or pressing "Load ledger" in the main menu. 

### Reflection
- A small change I would like to have made is to make Ledger have a field for original names which would be also saved in JSON,
as this was originally the cause of a lot of duplicate code
- I would also have made changes to my UI, especially the AddUserUI menu, this was needlessly created as its own class
within LedgerUI. Given more time I would have simply made it into a method, similar to the other menu elements in LedgerUI.
- I would have changed PaymentDialog to also be another method in LedgerUI, this would help reduce coupling 
(but would also reduce cohesion).
- As well as in the LedgerUI class, I would have refactored the class as a whole, keeping UI elements within
the scope of individual methods as much as possible so that the class would require significantly fewer fields.


