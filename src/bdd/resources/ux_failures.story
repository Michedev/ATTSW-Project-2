Meta:
@author Mikedev
@themes User interaction with errors


Narrative:
Describe the most common scenarios where
the interaction between the user and
the application goes wrong

Scenario: Bad registration
Given an unlogged user
When I register with username "user1!", password "password1" and email "email"
Then the user with username "user1!" should not exists

Scenario: Make a new task without a title
Given the page to add a new task
When I add a new task with title "" and subtasks "Bread", "Water", "Potatoes"
Then the new task should not exists

Scenario: Update a task with an empty title
Given the update page of the first task
When I update with the new title ""
Then the first task should have the old title

Scenario: Delete a not existing task
Given the page of the first task
And the first task is deleted from the Database
When the user clicks the Delete button
Then it should show an error message saying that the task doesn't exists anymore

Scenario: Delete a not existing user
Given the user tasks page
When I delete the user from the Database
And I click the delete user button
Then login page should show an errthe user should not exists anymoreor message saying that the user doesn't exists


Scenario: Update a deleted task
Given the update page of the first task
And the first task is deleted from the Database
When I update with the new title "Updated title 1"
Then it should show an error message saying that the task doesn't exists anymore