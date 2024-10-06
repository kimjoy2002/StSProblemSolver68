package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.FinishCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class RushOnOffAction extends AbstractGameAction {
    private FinishCard card;
    boolean onoff;
    public RushOnOffAction(FinishCard card, boolean onoff) {
        this.setValues(this.target, this.source, 0);
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.onoff = onoff;
    }

    public void update() {
        if(card != null) {
            //card.rushActive = onoff;
        }
        this.isDone = true;
    }
}
