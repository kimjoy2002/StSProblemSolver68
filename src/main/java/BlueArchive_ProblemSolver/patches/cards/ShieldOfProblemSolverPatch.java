package BlueArchive_ProblemSolver.patches.cards;

import BlueArchive_ProblemSolver.cards.ShieldOfProblemSolver;
import BlueArchive_ProblemSolver.cards.onAddToHandCards;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class ShieldOfProblemSolverPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onVictory"
    )
    public static class addToHandPatcher {

        public static void Postfix(AbstractPlayer __instance)
        {
            int number_of_shieldOfProblemSolver = 0;
            for(AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if(card instanceof ShieldOfProblemSolver) {
                    number_of_shieldOfProblemSolver += card.magicNumber;
                }
            }
            if(number_of_shieldOfProblemSolver > 0) {
                if(AbstractDungeon.player instanceof ProblemSolver68) {
                    for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                        if(ps.solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA) {
                            ps.increaseMaxHp(number_of_shieldOfProblemSolver, true);
                        }
                    }
                } else {
                    AbstractDungeon.player.increaseMaxHp(number_of_shieldOfProblemSolver, true);
                }
            }
        }
    }
}
