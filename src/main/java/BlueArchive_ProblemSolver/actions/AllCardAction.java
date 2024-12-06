package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.cards.UseCardActionPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AllCardAction extends AbstractGameAction {
    private AbstractCard card;
    AbstractPlayer exclude;
    public AllCardAction(AbstractCard card, AbstractMonster target, AbstractPlayer exclude) {
        this.setValues(target, this.source, 0);
        this.card = card;
        this.exclude = exclude;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        for(int i = 0; i < ProblemSolver68.problemSolverPlayer.size(); i++) {
            ProblemSolver68 ps = ProblemSolver68.problemSolverPlayer.get(i);
            if(ps != exclude && ps.currentHealth > 0) {
                AbstractMonster m = null;
                if (target != null) {
                    m = (AbstractMonster) target;
                }
                AbstractCard tmp = card.makeSameInstanceOf();
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float) Settings.HEIGHT / 2.0F;
                if (m != null) {
                    tmp.calculateCardDamage(m);
                }
                UseCardActionPatch.AbstractCardField.castPlayer.set(tmp, ps);
                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.cardQueue.add(0, new CardQueueItem(tmp, m, card.energyOnUse, true, true));
            }
        }
        this.isDone = true;
    }
}
