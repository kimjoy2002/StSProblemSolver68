package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.TacticalChallengeMutsuki;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public class AddChellengeCountAction extends AbstractGameAction {
    public AddChellengeCountAction() {
        this.setValues(this.target, this.source, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        Iterator var1 = AbstractDungeon.player.hand.group.iterator();

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if(c instanceof TacticalChallengeMutsuki) {
                ((TacticalChallengeMutsuki)c).didTacticalChallenge();
            }
        }

        var1 = AbstractDungeon.player.discardPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if(c instanceof TacticalChallengeMutsuki) {
                ((TacticalChallengeMutsuki)c).didTacticalChallenge();
            }
        }

        var1 = AbstractDungeon.player.drawPile.group.iterator();

        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if(c instanceof TacticalChallengeMutsuki) {
                ((TacticalChallengeMutsuki)c).didTacticalChallenge();
            }
        }

        GameActionManagerPatch.tacticalChallengeCount++;
        this.isDone = true;
    }
}
