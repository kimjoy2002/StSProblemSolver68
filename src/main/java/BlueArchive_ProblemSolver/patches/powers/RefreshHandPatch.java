package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ForSubPower;
import BlueArchive_ProblemSolver.powers.OnRefreshHandPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;

import java.util.Iterator;

public class RefreshHandPatch {

    @SpirePatch(
            clz = CardGroup.class,
            method = "refreshHandLayout"
    )
    public static class RefreshHandLayoutPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(CardGroup __instance) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for (AbstractPlayer player : ProblemSolver68.problemSolverPlayer) {
                    for (AbstractPower p : player.powers) {
                        if (p instanceof OnRefreshHandPower) {
                            OnRefreshHandPower onRefreshHandPower = (OnRefreshHandPower) p;
                            onRefreshHandPower.onRefreshHand();
                        }
                    }
                }
            } else {
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    if (p instanceof OnRefreshHandPower) {
                        OnRefreshHandPower onRefreshHandPower = (OnRefreshHandPower) p;
                        onRefreshHandPower.onRefreshHand();
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher( CardGroup.class,"size");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
