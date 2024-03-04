package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.CorrectAdvice;
import BlueArchive_ProblemSolver.cards.RushCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CorrectAdviceAction extends AbstractGameAction {
    private CorrectAdvice card;
    private AbstractPlayer use_player;
    public CorrectAdviceAction(CorrectAdvice card, AbstractPlayer p) {
        this.setValues(p, p, 0);
        use_player = p;
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if(card != null) {
            if(!card.used_character.contains(use_player)) {
                card.used_character.add(use_player);
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(card.magicNumber));
                card.makeDescrption();
            }
        }
        this.isDone = true;
    }
}
