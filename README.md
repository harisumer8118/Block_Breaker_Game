# Block_Breaker_Game
My first project in Java: a classic Block Breaker game where you control a paddle to bounce a ball and break blocks at the top of the screen. The game features scoring and basic collision detection for a fun, arcade-style experience.
Functionalities
Game Window Initialization and Full-Screen Mode:

The game initializes in full-screen mode by using the screen dimensions to set the width and height of the game panel.
The main method sets up the game window using JFrame and makes it full-screen with no window borders.
Background and Watermark:

A background image (background.jpg) is loaded, displayed, and stretched to fill the game screen.
A watermark text, “Game Design by Haris Umer,” is displayed in the center with transparency.
Paddle Control:

The paddle is controlled using the left and right arrow keys.
The paddle moves within the screen boundaries and cannot go off-screen.
The paddle moves by 15 pixels with each key press.
Ball Movement and Collisions:

The ball moves automatically within the game window.
It bounces off the left, right, and top edges of the screen, reversing direction upon collision.
When the ball hits the paddle, it bounces upward to remain in play.
Block Setup and Breaking:

Blocks are arranged in rows and columns at the top of the screen.
When the ball hits a visible block, the block becomes invisible (breaks) and the ball bounces back.
Game States (Pause, Resume, and Game Over):

The player can pause and resume the game by pressing the P key.
The game pauses, showing a “Paused” message in the center of the screen.
If the ball touches the bottom of the screen, the game stops, displaying a “Game Over” message with instructions to press R to retry.
Restarting the Game:

When the game is over, pressing the R key resets the game to its initial state with all blocks visible and the ball and paddle repositioned.
Supporting Classes
Ball: Controls the ball’s position, movement, and bounce logic.
Paddle: Controls the paddle’s position, size, and movement limits within the game window.
Block: Represents each breakable block, with properties for visibility and collision bounds.
