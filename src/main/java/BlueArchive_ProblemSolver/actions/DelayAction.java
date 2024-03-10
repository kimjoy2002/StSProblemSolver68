package BlueArchive_ProblemSolver.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DelayAction extends AbstractGameAction {
    private AbstractGameAction action;
    public DelayAction(AbstractGameAction action, int amount) {
        this.setValues(this.target, this.source, amount);
        this.action = action;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if (amount <= 0 || AbstractDungeon.actionManager.actions.size() == 1) {
            this.addToBot(action);
        }
        else {
            this.addToBot(new DelayAction(action, amount-1));
        }
        this.isDone = true;
    }
}
