package BlueArchive_ProblemSolver.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.HealVerticalLineEffect;

public class HealOnlyEffect  extends AbstractGameEffect {
    private static final float X_JITTER;
    private static final float Y_JITTER;
    private static final float OFFSET_Y;

    public HealOnlyEffect(float x, float y) {
        int roll = MathUtils.random(0, 2);
        if (roll == 0) {
            CardCrawlGame.sound.play("HEAL_1");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("HEAL_2");
        } else {
            CardCrawlGame.sound.play("HEAL_3");
        }

        for (int i = 0; i < 18; ++i) {
            AbstractDungeon.effectsQueue.add(new HealVerticalLineEffect(x + MathUtils.random(-X_JITTER * 1.5F, X_JITTER * 1.5F), y + OFFSET_Y + MathUtils.random(-Y_JITTER, Y_JITTER)));
        }

    }

    public void update() {
        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }

    static {
        X_JITTER = 120.0F * Settings.scale;
        Y_JITTER = 120.0F * Settings.scale;
        OFFSET_Y = -50.0F * Settings.scale;
    }
}