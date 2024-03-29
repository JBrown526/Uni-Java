package game.entities.control;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import game.levels.GameLevel;
import game.entities.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The controller object takes keyboard inputs and outputs interprets them as actions that the player character can take.
 */
public class Controller extends KeyAdapter implements StepListener {

    // ---------------------- FIELDS ----------------------

    // Movement variables
    private static final float WALKING_SPEED = 4;
    private static final float JUMP_SPEED = 11;
    private static final int BARK_TIME = 40;

    private GameLevel world;
    private Player player;
    /**
     * The number of steps since timing started.
     */
    private int stepSum;
    /**
     * Whether or not the player is currently barking.
     */
    private boolean barking;

    // ---------------------- CONSTRUCTOR ----------------------
    public Controller(GameLevel world) {
        this.world = world;
        player = world.getPlayer();
        barking = false;
    }

    // ---------------------- METHODS ----------------------
    // ---------------------- GENERAL ----------------------
    public void setWorld(GameLevel world) {
        this.world = world;
        player = world.getPlayer();
    }

    // ---------------------- MOVEMENT ----------------------

    /**
     * This method splits the inputs based on the key pressed and executes other methods based on the key
     *
     * @param e The key event when a key is pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        final int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE) {
            bark();
        } else if (code == KeyEvent.VK_W) {
            jump();
        } else if (code == KeyEvent.VK_A) {
            move(0);
        } else if (code == KeyEvent.VK_D) {
            move(1);
        }
    }

    private void bark() {
        // if given the instruction to bark and the previous bark is not still ongoing it will trigger another one
        if (!barking) {
            player.updateImage(0);
            barking = true;
            stepSum = 0;
            world.addBark(player.getDirectionIndex());
        }
    }

    private void jump() {
        // allows the player to jump if not already in the air
        if (!player.getInAir()) {
            player.updateImage(1);
            player.setInAir(true);
            player.jump(JUMP_SPEED);
        }
    }

    private void move(int dirIndex) {
        // move the player in a direction based off of keyboard input
        player.setMoving(true);
        player.setDirectionIndex(dirIndex);
        player.updateImage(player.getInAir() ? 1 : 2);
        player.startWalking((dirIndex == 0 ? -1 : 1) * WALKING_SPEED);
    }

    /**
     * This method governs the action to be taken when a key is releases.
     *
     * @param e The key event when a key is released.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_D) {
            stopMove();
        }
    }

    private void stopMove() {
        // stops player movement in the x-axis
        player.updateImage(player.getInAir() ? 1 : 3);
        player.setMoving(false);
        player.stopWalking();
    }

    // ---------------------- TIMING ----------------------

    /**
     * This method governs the time a bark image will go for and the interval between barks, as well as what image
     * to update with after the bark time has finished.
     *
     * @param e The step event that adds to the stepSum for working out when the barking is finished.
     */
    @Override
    public void preStep(StepEvent e) {
        // barking timer
        if (barking) {
            if (stepSum < BARK_TIME) {
                stepSum++;
            }
            // after timing finished check player action and update image as required
            else {
                postBark();
            }
        }
    }

    @Override
    public void postStep(StepEvent e) {
        // Unneeded
    }

    private void postBark() {
        if (player.getInAir()) {
            player.updateImage(1);
        } else if (player.getMoving()) {
            player.updateImage(2);
        } else {
            player.updateImage(3);
        }
        barking = false;
    }

    public void resetBark() {
        stepSum = 0;
        barking = false;
    }
}
