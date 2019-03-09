package game;

import city.cs.engine.BoxShape;
import org.jbox2d.common.Vec2;

public class LevelDebug extends GameLevel {

    @Override
    public void populate(Game game) {
        super.populate(game);

        Platform ground = new Platform(this, new BoxShape(11, 0.5f), 0, -11.5f);
        ground.addCollisionListener(super.getCollisionHandler());

        BreakablePlatform wall = new BreakablePlatform(this, new BoxShape(0.5f, 0.5f), -5, -8);
        wall.addCollisionListener(super.getCollisionHandler());

        Spike spike = new Spike(this);
        spike.setPosition(new Vec2(-8, -10.8f));
        spike.addCollisionListener(super.getCollisionHandler());
    }

    @Override
    public Vec2 startPosition() {
        return new Vec2(0, -9);
    }

    @Override
    public Vec2 bonePosition() {
        return new Vec2(8, -8);
    }
}