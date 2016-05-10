// a BlockRemover is in charge of removing blocks from the game, as well as keeping count
// of the number of blocks that were removed.
public class BlockRemover implements HitListener {
   private Game game;
   private Counter blocksCounter;

   public BlockRemover(Game game, Counter blocksCounter) {
       this.game = game;
       this.blocksCounter = blocksCounter;
   }

   // Blocks that are hit and reach 0 hit-points should be removed
   // from the game. Remember to remove this listener from the block
   // that is being removed from the game.
   public void hitEvent(Block beingHit, Ball hitter) {
       if(beingHit.getMaxHits() == 0){
           beingHit.removeHitListener(this);
           beingHit.removeFromGame(game);
           this.blocksCounter.decrease(1);
           System.out.println(blocksCounter.getValue());
       }
   }
}