package BlueArchive_ProblemSolver.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FearEscapeAction extends AbstractGameAction {
    public FearEscapeAction(AbstractMonster source) {
    this.setValues(source, source);
    this.duration = 0.5F;
    this.actionType = ActionType.TEXT;
}

    public void update() {
        if (this.duration == 0.5F && !source.isEscaping && source.currentHealth > 0) {
            AbstractMonster m = (AbstractMonster)this.source;
            m.escape();
        }

        this.tickDuration();
    }
}
