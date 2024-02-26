package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.HireHelmetLeader;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.CannotAttackedPower;
import BlueArchive_ProblemSolver.powers.ChangeWhenHitPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AddCharacterAction extends AbstractGameAction {
    Aru.ProblemSolver68Type type;
    public AddCharacterAction(Aru.ProblemSolver68Type type, int amount) {
        this.type = type;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        this.isDone = true;
        if(ProblemSolver68.getMemberNum(false ,false) >= 5)
            return;
        AbstractPlayer p = ProblemSolver68.addCharacter(type, amount, amount, true);
        if(p != null) {
            if (type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_RABU) {
                this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, HireHelmetLeader.MAGIC2), HireHelmetLeader.MAGIC2));
            } else if (type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_SAORI) {
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, HireHelmetLeader.MAGIC3), HireHelmetLeader.MAGIC3));
            } else if (type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_CAT) {
                this.addToBot(new ApplyPowerAction(p, p, new CannotAttackedPower(p)));
            }
        }
    }
}
