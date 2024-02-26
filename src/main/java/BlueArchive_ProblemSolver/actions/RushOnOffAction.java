package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.EvilDeedsCard;
import BlueArchive_ProblemSolver.cards.RushCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import gremlin.cards.Rush;

public class RushOnOffAction extends AbstractGameAction {
    private RushCard card;
    boolean onoff;
    public RushOnOffAction(RushCard card, boolean onoff) {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.onoff = onoff;
    }

    public void update() {
        card.rushActive = onoff;
        this.isDone = true;
    }
}
