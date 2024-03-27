package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.RushCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DiscardLeftAction extends AbstractGameAction {
    public DiscardLeftAction(int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.DISCARD;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }

            if (this.amount > 0) {
                if(AbstractDungeon.player.hand.size() == 0) {
                    this.isDone = true;
                    return;
                }
                if(amount > AbstractDungeon.player.hand.size())
                    amount = AbstractDungeon.player.hand.size();
                for(int i = 0; i < amount; i++) {
                    AbstractCard c = AbstractDungeon.player.hand.getBottomCard();
                    AbstractDungeon.player.hand.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(false);
                }
                AbstractDungeon.player.hand.applyPowers();
            }
        }
        this.tickDuration();
    }
}
