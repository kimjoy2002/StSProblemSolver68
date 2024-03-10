package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.patches.cards.UseCardActionPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UnwelcomeSchoolAction extends AbstractGameAction {
    AbstractPlayer owner;
    public UnwelcomeSchoolAction(AbstractPlayer owner, int amount) {
        this.setValues(owner, owner, amount);
        this.owner = owner;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        for(int i = 0; i < amount; ++i) {
            PlayTopCardAction action = new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng), false);
            UseCardActionPatch.PlayTopCardActionField.castPlayer.set(action, owner);
            this.addToBot(action);
        }
        this.isDone = true;
    }
}
