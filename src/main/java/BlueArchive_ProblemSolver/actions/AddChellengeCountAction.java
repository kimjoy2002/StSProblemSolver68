package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.TacticalChallengeMutsuki;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class AddChellengeCountAction extends AbstractGameAction {
    AbstractCard targetcard;
    public AddChellengeCountAction() {
        this(null);
    }
    public AddChellengeCountAction(AbstractCard targetcard) {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.targetcard = targetcard;
    }

    public void update() {
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if(c instanceof TacticalChallengeMutsuki && c != targetcard) {
                ((TacticalChallengeMutsuki)c).didTacticalChallenge();
            }
        }

        var1 = AbstractDungeon.player.discardPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if(c instanceof TacticalChallengeMutsuki && c != targetcard) {
                ((TacticalChallengeMutsuki)c).didTacticalChallenge();
            }
        }

        var1 = AbstractDungeon.player.drawPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if(c instanceof TacticalChallengeMutsuki && c != targetcard) {
                ((TacticalChallengeMutsuki)c).didTacticalChallenge();
            }
        }

        if(targetcard instanceof TacticalChallengeMutsuki) {
            ((TacticalChallengeMutsuki)targetcard).didTacticalChallenge();
        }

        GameActionManagerPatch.tacticalChallengeCount++;
        this.isDone = true;
    }
}
