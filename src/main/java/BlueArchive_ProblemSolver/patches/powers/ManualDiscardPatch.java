package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.OnManualDiscaedPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

public class ManualDiscardPatch {

    @SpirePatch(
            clz = GameActionManager.class,
            method = "incrementDiscard"
    )
    public static class incrementDiscardPatch {
        public static void Postfix(boolean endOfTurn) {
            if (!AbstractDungeon.actionManager.turnHasEnded && !endOfTurn) {
                if(AbstractDungeon.player instanceof ProblemSolver68) {
                    for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                        for (AbstractPower power_ : ps.powers) {
                            if(power_ instanceof OnManualDiscaedPower) {
                                ((OnManualDiscaedPower)power_).onManualDiscard();
                            }
                        }
                    }
                } else {
                    for (AbstractPower power_ : AbstractDungeon.player.powers) {
                        if(power_ instanceof OnManualDiscaedPower) {
                            ((OnManualDiscaedPower)power_).onManualDiscard();
                        }
                    }
                }
            }
        }
    }
}
