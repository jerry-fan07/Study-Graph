## USER GUIDE
The program is a flashcard app that helps users study for mathematics and other conceptual subjects by putting flashcards as nodes on a graph to be reviewed. The cards are linked to one another based on conceptual relationships determined by the user. Over time, each flashcard “decays” at a rate determined by a spaced repetition algorithm using 5 parameters so that the user knows when to review the flashcard. On the sidebar, there exists a queue of flashcards in recommended study order based on the urgency of each flashcard, implemented by a priority queue.

![picture of graph](https://github.com/jerry-fan07/Study-Graph/blob/main/image1.png)

Using the mouse, you can click and drag through the screen.
Hovering over each of the nodes causes them to respond, and clicking on one of the nodes in the graph allows you to open a menu for reviewing, adding a node, or deleting the node:

IMAGE 2

The review button opens a review screen:

IMAGE 3

After showing answer you can either mark the card is correct or again:

IMAGE 4
IMAGE 5

Marking it correct results in a successful review and recalculates the priority of the card to generate the queue of cards again. The card’s color is updated as seen to return to green (lower priority). You can cancel the review by pressing the cancel button or clicking on the screen.

Within the review menu, you can also edit a card by clicking on the edit button:

IMAGE 6

Within the menu, the green plus button adds a new card that branches off the currently selected node.

IMAGE 7

You can adjust the closeness of relation between this new card and the parent card (which also adjusts the thickness of the wire connecting the two nodes.

Notice that different cards types can be chosen based on the drop down menu:

IMAGE 8
IMAGE 9
IMAGE 10

Clicking on the red delete button will delete the node as well as any child nodes it has on the graph. Thus, deleting the root node will delete the entire graph. If no nodes exist, clicking anywhere on the screen will generate a new node that can be edited.

Generation of new nodes will space each of them out from the fixed branch the node is connected to its own parent node on:

IMAGE 11

To prevent the nodes from colliding with one another and to form a more natural looking tree, the parent node’s branches will become longer for the total number of children it has (including children of its children). Thus, the root node will always have the longest branches.

Reviewing nodes on the ends of the tree will recursively review the parent nodes depending on the connection they have:

IMAGE 12

After leaf has been reviewed:

IMAGE 13

The idea behind it is that concepts building off of earlier concepts require the earlier concepts when reviewing and act as a review for those concepts as well. For example, multiplication of two numbers requires the knowledge of addition of two numbers so a review of multiplication should act as a partial review of addition as well.

Although it is not easily displayed on the graph, an incorrect review decreases the mastery of the reviewed card by 0.5 and also recursively works its way up the tree to decrement the mastery rating of parent cards as well (mastery rating can never be less than 0).

On the side bar, the cards are listed in order of the urgency of review. You can select one of the cards and directly review from the pile.

After a successful review, the card will update its priority value and move to the bottom or near the bottom of the review stack.

If more cards than can fit on the pane are on the graph, the review sidebar allows you to scroll through the list of cards to be reviewed.

IMAGE 14


## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).



