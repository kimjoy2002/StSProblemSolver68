package BlueArchive_ProblemSolver.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class KayokoKickAction extends AbstractGameAction {
    public KayokoKickAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.setValues(target, source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if(target.hasPower(ArtifactPower.POWER_ID)) {
        }
        this.isDone = true;
    }
}
