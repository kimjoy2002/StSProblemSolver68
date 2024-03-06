package BlueArchive_ProblemSolver.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class LoseRelicEffect extends AbstractGameEffect {
    String relicId;
    public LoseRelicEffect(String relicId) {
        this.relicId = relicId;
    }

    public void update() {
        AbstractDungeon.player.loseRelic(relicId);
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    public void dispose() {
    }
}
