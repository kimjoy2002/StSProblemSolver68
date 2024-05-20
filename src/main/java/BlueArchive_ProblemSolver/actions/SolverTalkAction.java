package BlueArchive_ProblemSolver.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

public class SolverTalkAction extends AbstractGameAction {
    private String msg;
    private boolean used;
    private float bubbleDuration;

    public SolverTalkAction(AbstractCreature source, String text, float duration, float bubbleDuration) {
        this.used = false;
        this.setValues(source, source);
        if (Settings.FAST_MODE) {
            this.duration = Settings.ACTION_DUR_MED;
        } else {
            this.duration = duration;
        }

        this.msg = text;
        this.actionType = ActionType.TEXT;
        this.bubbleDuration = bubbleDuration;
    }

    public void update() {
        if (!this.used) {
            AbstractDungeon.effectList.add(new SpeechBubble(this.source.dialogX, this.source.dialogY, this.bubbleDuration, this.msg, true));
            this.used = true;
        }

        this.tickDuration();
    }
}

