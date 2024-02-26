package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.EvilDeedsCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class EvilDeedsAction extends AbstractGameAction {
    private EvilDeedsCard card;

    public EvilDeedsAction(EvilDeedsCard card) {
        this(card, -999);
    }

    public EvilDeedsAction(EvilDeedsCard card, int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.card = card;
    }

    public void update() {
        card.evil += amount;
        if(card.limit && card.evil > card.require_evil)
            card.evil = card.require_evil;
        if(card.evil > 999)
            card.evil = 999;
        if(card.evil < 0)
            card.evil = 0;
        card.makeDescrption();
        this.isDone = true;
    }
}
