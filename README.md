# *Scrabble* in Java

## Proposal
- **What will the application do?**

This project will be a re-creation of the game *Scrabble*, made within Java. *Scrabble* is a turn based game where players draw letter tiles at random until they have 7 tiles on their tile rack or the bag of tiles is empty. Players take turns placing their letters on a board to form words and score points. Each letter has a preassigned value, and there are special spaces on the board which can multiply either letter or word scores the first time a tile is played on them. Play ends when any player has no tiles left on their tile rack to play, and the bag of random tiles to draw from is empty. Any unplayed tiles from the remaining players are then totalled and subtracted from their corresponding player's score. The combined value of unplayed tiles is then added to the score of the player who made the final move.

The game will be played between two or more players locally. Initially, the game will be playable through the command line, where players will select where to place their tiles on the board using a (row, column) coordinate system. Once this is fully functional, a graphical user interface will be developed.

- **Why did I choose to create this?**

This project is of interest to me since *Scrabble* was one of my favorite board games as a kid, and I still enjoy it. Furthermore, I dislike how the online or mobile application versions of *Scrabble* I have used automatically indicate to the user whether the word they have played is valid. This fundamentally changes how the game is played compared to the version with a physical board since it allows players to guess and check words as many times as they would like without any consequence.

## User Stories
1. As a user I want to be able to create a new board.

2. As a user I want to be able to move letters from my tile rack onto the board.

3. As a user I want to have the appropriate number of letters added to my rack at the start of my first turn, or after I play a word.

4. As a user I want to be able to view the remaining quantity of each letter tile, combined between my opponents' tile racks and the draw bag.

5. As a user, when I play a word I want to have it added to my history of words played, including its letters, start and end coordinates and its points.

6. As a user I'd like to have the option to quit my game, and be prompted to see if I want to save it.

7. As a user when I run the application I'd like to have the choice between continuing a previous, unfinished game, or starting a new game.