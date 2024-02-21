package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

public class AddMaxHPToMercenaryAction extends AbstractGameAction {

    public AddMaxHPToMercenaryAction(int amount) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = ActionType.HEAL;
    }

    public void update() {
        if (this.duration == 0.5F) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 p : ProblemSolver68.problemSolverPlayer) {
                    if(!ProblemSolver68.isProblemSolver(p.solverType)) {
                        p.increaseMaxHp(amount, true);
                    }
                }
                GameActionManagerPatch.increaseMercenaryMaxHP+=amount;
            }
        }

        this.tickDuration();
    }
}
