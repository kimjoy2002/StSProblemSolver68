package BlueArchive_ProblemSolver.patches.relics;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.AbstractMonsterPatch;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import BlueArchive_ProblemSolver.powers.OneTurnConfusionPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;

import java.util.Iterator;

public class SnekoEyePatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "draw",
            paramtypez= {
                    int.class
            }
    )
    public static class DrawPatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"c"}

        )
        public static void Insert(AbstractPlayer __instance, int numCards, AbstractCard c) {
            if(__instance instanceof ProblemSolver68) {
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(ps != __instance) {
                        for(AbstractPower power_ : ps.powers) {
                            if(power_ instanceof ConfusionPower || power_ instanceof OneTurnConfusionPower) {
                                power_.onCardDraw(c);
                            }
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "removeTopCard");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
