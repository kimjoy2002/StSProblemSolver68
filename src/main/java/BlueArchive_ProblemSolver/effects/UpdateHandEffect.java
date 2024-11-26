package BlueArchive_ProblemSolver.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class UpdateHandEffect extends AbstractGameEffect {
    public UpdateHandEffect() {

    }

    public void update() {
        AbstractDungeon.player.hand.refreshHandLayout();
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    public void dispose() {
    }
}
