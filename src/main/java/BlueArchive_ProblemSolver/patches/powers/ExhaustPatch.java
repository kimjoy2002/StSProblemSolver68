package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FearPower;
import BlueArchive_ProblemSolver.powers.OnExhaustForProblemPower;
import BlueArchive_ProblemSolver.powers.OnManualDiscaedPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

public class ExhaustPatch {

    @SpirePatch(
            clz = CardGroup.class,
            method = "moveToExhaustPile"
    )
    public static class moveToExhaustPilePatch {
        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(CardGroup __instance, AbstractCard c) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    for (AbstractPower power_ : ps.powers) {
                        if(power_ instanceof OnExhaustForProblemPower) {
                            ((OnExhaustForProblemPower)power_).onExhaustForProblem(c);
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "triggerOnExhaust");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
