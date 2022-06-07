Meta:
@author: Mikedev
@themes User operations


Narrative:
Describe the most common operation
between the user and the application

Scenario: Registration and successfull login
Given an unlogged user
When I register with username "user1", password "password1" and email "email@mail.com"
Then I login by prompting the username "user1" and password "password1"

Scenario: Login and add new task
Given the page to add a new task
When I add a new task with title "Clean" and subtasks "Bathroom", "Living room", "Bedroom"
Then it should exists a task with title "Clean" and subtasks "Bathroom", "Living room", "Bedroom"

Scenario: Delete a task
Given the page of the first task
When I click the delete button
Then the task should not exists

Scenario: Update a task
Given the update page of the first task
When I update the title with "Updated title"
Then the first task has the title "Updated title"

Scenario: Delete an user
Given the user tasks page
When I click the delete user button
Then the user should not exists anymore

Scenario: Long user interaction
Given a logged user
When I delete the first task
And I add a new task with title "Shopping" and subtasks "Bread", "Water", "Potatoes"
And I update the second task with the new title "Updated title 23"
Then the first task should not exists
And it should exists a task with title "Shopping" and subtasks "Bread", "Water", "Potatoes"
And the second task should have the title "Updated title 23"

Scenario: Create user, create a new task and then update it
Meta: this scenario is born to re-create a failure encountered during the application usage with docker containers
Given an unlogged user
When I register with username "a", password "a" and email "a@a.a"
And I login with username "a" and password "a"
And I add a new task with title "Shopping" and subtasks "Bread", "Water", "Potatoes"
And I update the first task with title "Shopping2"
Then the first task should have the title "Shopping2"