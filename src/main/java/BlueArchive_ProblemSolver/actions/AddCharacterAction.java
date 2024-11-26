package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.HireHelmetLeader;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.PerfectPresidentsIntegiblePower;
import BlueArchive_ProblemSolver.powers.PerfectPresidentsStrPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
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
        if(!(AbstractDungeon.player instanceof ProblemSolver68)) {
            return;
        }

        if(ProblemSolver68.getMemberNum(false ,true) >= ProblemSolver68.MAX_CHARACTER_NUM)
            return;
        if(ProblemSolver68.problemSolverPlayer.size() >= ProblemSolver68.MAX_CHARACTER_NUM) {
            ProblemSolver68 ps = ProblemSolver68.getRandomDeadMember();
            ProblemSolver68.saveDeathCharacter(ps);
            if (ProblemSolver68.problemSolverPlayer.size() >= ProblemSolver68.MAX_CHARACTER_NUM) {
                return;
            }
        }


        AbstractPlayer p = ProblemSolver68.addCharacter(type, amount, amount, true);
        if(p != null) {
            if (type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_RABU) {
                this.addToBot(new ChangeCharacterAction(p, false, true, true));
                this.addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, HireHelmetLeader.MAGIC2), HireHelmetLeader.MAGIC2));
            } else if (type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_SAORI) {
                this.addToBot(new ChangeCharacterAction(p, false, true, true));
                this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, HireHelmetLeader.MAGIC3), HireHelmetLeader.MAGIC3));
            } else if (type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_CAT) {
                this.addToBot(new ChangeCharacterAction(p, true, true, false));
                //this.addToBot(new ApplyPowerAction(p, p, new CannotAttackedPower(p)));
            } else if (type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HELMETGANG) {
                this.addToBot(new ChangeCharacterAction(p, false, true, true));
            } else if (type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_DEFECT || type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_WATCHER || type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_IRONCLAD || type == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_SILENT) {
                this.addToBot(new ChangeCharacterAction(p, false, true, true));
            }
            for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                for (AbstractPower power_ : ps.powers) {
                    if(power_ instanceof PerfectPresidentsStrPower) {
                        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, power_.amount), power_.amount));
                    }
                    if(power_ instanceof PerfectPresidentsIntegiblePower) {
                        this.addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, power_.amount), power_.amount));
                    }
                }
            }
        }
    }
}
