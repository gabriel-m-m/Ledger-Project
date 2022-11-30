# CPSC210 Personal Project

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

# Instructions for Grader

- You can generate the first required event relating adding Xs to a Y by
first running the UI, then clicking on the "Create new ledger" button. 
In the new window, you will be able to type in strings to be used as usernames for users to be included in the ledger. 
This can be achieved by typing a name into the text field then pressing "Add user".
- You can generate the second required event relating adding Xs to a Y by proceeding from the first step,
and creating a ledger. Then, in the main menu, the ledger will be displayed in the middle. Using the "Remove user"
button you can remove any of the users and their corresponding entries from the ledger. 
Any changes which will be updated in the middle display.
- You can locate my visual component by loading a ledger from the start menu or by creating a 
new ledger then pressing "Done" (when at least 2 users have been added). Both of these actions will bring you 
to a splashscreen, which contains the visual component.
- You can save the state of my application by pressing the "Save" button in the main menu, after having 
created or loaded a ledger.
- You can reload the state of my application by pressing the "Load ledger" button in the start-up 
menu, or pressing "Load ledger" in the main menu. 

### Phase 4 : Task 2
Wed Nov 23 08:39:52 PST 2022  
Users: [User 1, User 2, User 3, User 4] added to ledger.

Wed Nov 23 08:39:57 PST 2022  
User: User 3 removed.

Wed Nov 23 08:40:13 PST 2022  
Amount owed to User 2 by User 1 increased by 13202.

Wed Nov 23 08:40:24 PST 2022  
Amount owed to User 4 by User 1 increased by 2229138.

Wed Nov 23 08:40:34 PST 2022  
Amount owed to User 1 by User 2 increased by 213090.

Wed Nov 23 08:40:41 PST 2022  
User 1 payed User 4 12313.

Wed Nov 23 08:40:43 PST 2022  
Ledger balanced.

