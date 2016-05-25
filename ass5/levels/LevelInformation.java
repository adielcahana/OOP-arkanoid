package levels;

import java.util.List;

import gameObjects.Ball;
import gameObjects.Block;
import gameObjects.Sprite;
import gameObjects.Velocity;

public interface LevelInformation {
   
    int numberOfBalls();
   
   // The initial velocity of each ball
   // Note that initialBallVelocities().size() == numberOfBalls()
   List<Velocity> initialBallVelocities();
   
   int paddleSpeed();
   
   int paddleWidth();
   
   // the level name will be displayed at the top of the screen.
   String levelName();
   
   // Returns a sprite with the background of the level
   Sprite getBackground();
   
   // The Blocks that make up this level, each block contains
   // its size, color and location.
   List<Block> blocks();
   
   List<Ball> Balls();
   
   // Number of levels that should be removed
   // before the level is considered to be "cleared".
   // This number should be <= blocks.size();
   int numberOfBlocksToRemove();
}