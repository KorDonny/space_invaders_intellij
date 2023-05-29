package spaceinvaders.entity;

import spaceinvaders.Game;
import spaceinvaders.Sprite;
import spaceinvaders.SpriteStore;
import spaceinvaders.entity.Entity;

import java.util.TimerTask;

public class MeteorEntity extends Entity {
    private Game game;
    private Entity target;
    private int idx;
    private double xDup;
    private TimerTask meteorTask;
    private long lastFrameChange;
    private long frameDuration = 50;
    private int frameNumber;
    private static int howMuch;
    private Sprite[] frames = new Sprite[4];

    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param ref The reference to the image to be displayed for this entity
     * @param x   The initial x location of this entity
     * @param y   The initial y location of this entity
     */
    public MeteorEntity(Game game,int idx) {
        super("sprites/meteor1.gif", 10 + 26*idx, 40);
        this.frames[0]= sprite;
        this.frames[1]= SpriteStore.get().getSprite("sprites/meteor2.gif");
        this.frames[2]= SpriteStore.get().getSprite("sprites/meteor3.gif");
        this.frames[3]= SpriteStore.get().getSprite("sprites/meteor4.gif");
        dx = 0;
        dy = 0;
        this.target = null;
        this.game = game;
        this.xDup = 0;
        this.idx = idx;
        howMuch=game.isMulti()?20:10;
        MeteorEntity meteor = this;
        meteorTask = new TimerTask(){
            @Override
            public void run() {
                if(game.isMulti()&&idx>9&&game.get2P()!=null){
                    target = game.get2P();
                }
                else{
                    target = game.get1P();
                }
                xDup+=2;
                meteor.setHorizontalMovement((target.getX()-meteor.getX())/(100-xDup));
                meteor.setVerticalMovement(meteor.getVerticalMovement()+1);
                if(howMuch==0){
                    game.removeTask(meteorTask);
                }
            }
        };
        game.addTask(meteorTask,3,5);
    }

    public void move(long delta) {
        // since the move tells us how much time has passed
        // by we can use it to drive the animation, however
        // its the not the prettiest solution
        lastFrameChange += delta;
        // if we need to change the frame, update the frame number
        // and flip over the sprite in use
        if (lastFrameChange > frameDuration) {
            // reset our frame change time counter
            lastFrameChange = 0;
            // update the frame
            frameNumber++;
            if (frameNumber >= frames.length) {
                frameNumber = 0;
            }
            sprite = frames[frameNumber];
        }
        // proceed with normal move
        super.move(delta);
        if (y > 780||x<0||x>800) {
            game.removeEntity(this);
            --howMuch;
        }
    }
    @Override
    public void collidedWith(Entity other) {
    }
}
