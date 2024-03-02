package BlueArchive_ProblemSolver.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SaveEffect extends AbstractGameEffect {
    public SaveEffect() {
        if (Settings.FAST_MODE) {
            this.duration = 0.5F;
        } else {
            this.duration = 2.0F;
        }
        this.duration += 0.5F;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            SaveHelper.saveIfAppropriate(SaveFile.SaveType.ENTER_ROOM);
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    public void dispose() {
    }
}
