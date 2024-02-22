package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.patches.AbstractMonsterPatch;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import BlueArchive_ProblemSolver.powers.FearPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class FearPatch {

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "updateEscapeAnimation"
    )
    public static class MonsterDiePatch {
        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(AbstractMonster __instance) {
            if(__instance.hasPower(FearPower.POWER_ID)) {
                if(__instance.getPower(FearPower.POWER_ID).amount >= __instance.currentHealth) {
                    __instance.escaped = false;
                    __instance.isDead = true;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getMonsters");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
