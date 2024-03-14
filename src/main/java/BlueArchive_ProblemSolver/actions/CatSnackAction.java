package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CatSnackAction extends AbstractGameAction {
    public CatSnackAction(int drawAmount) {
        this.amount = drawAmount;
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
    }
    @Override
    public void update() {
        this.isDone = true;
        if(!(AbstractDungeon.player instanceof ProblemSolver68)) {
            return;
        }

        List<ProblemSolver68> ableCharacters = new ArrayList<>();
        for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
            if (ps.currentHealth > 0 && ps.solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_CAT) {
                ableCharacters.add(ps);
            }
        }

        if(!ableCharacters.isEmpty()) {
            Collections.shuffle(ableCharacters, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
            ProblemSolver68 cat = ableCharacters.get(0);
            AbstractDungeon.actionManager.addToBottom(new InstantKillAction(cat));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(amount));
        }
    }
}
