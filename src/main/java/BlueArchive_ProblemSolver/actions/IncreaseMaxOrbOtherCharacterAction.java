package BlueArchive_ProblemSolver.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class IncreaseMaxOrbOtherCharacterAction extends AbstractGameAction {
    private AbstractPlayer p;
    public IncreaseMaxOrbOtherCharacterAction(AbstractPlayer p, int slotIncrease) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = slotIncrease;
        this.actionType = ActionType.BLOCK;
        this.p = p;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            for(int i = 0; i < this.amount; ++i) {
                p.increaseMaxOrbSlots(1, true);
            }
        }

        this.tickDuration();
    }
}
