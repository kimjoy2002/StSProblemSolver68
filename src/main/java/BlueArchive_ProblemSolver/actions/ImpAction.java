package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.RushCard;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ImpPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ImpAction extends AbstractGameAction {
    public ImpAction(int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if(AbstractDungeon.player instanceof ProblemSolver68 && amount > 0) {
            for(ProblemSolver68 p : ProblemSolver68.problemSolverPlayer) {
                if(p.solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ImpPower(p, amount), amount));
                }
            }
        }
        this.isDone = true;
    }
}
