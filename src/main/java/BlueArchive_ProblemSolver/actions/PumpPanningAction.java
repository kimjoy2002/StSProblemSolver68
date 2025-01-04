package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.PumpPanning;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.DeckToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PumpPanningAction extends AbstractGameAction {
    private AbstractCard card;
    public PumpPanningAction(AbstractCard card) {
        this.setValues(this.target, this.source, 1);
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if(card != null) {
            for(int i = 0; i <amount; i++) {
                card.baseMagicNumber++;
            }
            card.rawDescription = PumpPanning.cardStrings.EXTENDED_DESCRIPTION[1];

            if(AbstractDungeon.player.discardPile.contains(this.card)) {
                AbstractDungeon.player.discardPile.removeCard(this.card);
                AbstractDungeon.player.drawPile.moveToDeck(this.card, true);
            }
            card.initializeDescription();
        }
        this.isDone = true;
    }
}
