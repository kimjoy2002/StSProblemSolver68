package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.PumpPanning;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class PumpPanningAction extends AbstractGameAction {
    private AbstractCard card;
    public PumpPanningAction(AbstractCard card) {
        this.setValues(this.target, this.source, 0);
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if(card != null) {
            card.baseMagicNumber++;
            card.rawDescription = PumpPanning.cardStrings.EXTENDED_DESCRIPTION[0];
            card.initializeDescription();
            card.shuffleBackIntoDrawPile = true;
        }
        this.isDone = true;
    }
}
