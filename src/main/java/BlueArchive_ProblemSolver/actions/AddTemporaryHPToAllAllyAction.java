package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

public class AddTemporaryHPToAllAllyAction extends AbstractGameAction {
    boolean onlyProblemSolver;
    public AddTemporaryHPToAllAllyAction( int amount, boolean onlyProblemSolver) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = AbstractGameAction.ActionType.HEAL;
        this.onlyProblemSolver = onlyProblemSolver;
    }
    public AddTemporaryHPToAllAllyAction( int amount) {
        this(amount, false);
    }

    public void update() {
        if (this.duration == 0.5F) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 p : ProblemSolver68.problemSolverPlayer) {
                    if((!onlyProblemSolver || ProblemSolver68.isProblemSolver(p.solverType)) && p.currentHealth > 0) {
                        TempHPField.tempHp.set(p, (Integer)TempHPField.tempHp.get(p) + this.amount);
                        if (this.amount > 0) {
                            AbstractDungeon.effectsQueue.add(new HealEffect(p.hb.cX - p.animX, p.hb.cY, this.amount));
                            p.healthBarUpdatedEvent();
                        }
                    }
                }
            } else {
                TempHPField.tempHp.set(this.target, (Integer)TempHPField.tempHp.get(this.target) + this.amount);
                if (this.amount > 0) {
                    AbstractDungeon.effectsQueue.add(new HealEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY, this.amount));
                    this.target.healthBarUpdatedEvent();
                }
            }
        }

        this.tickDuration();
    }
}
