package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import javassist.CtBehavior;

public class AbstractMonsterPatch {

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez= {
                    boolean.class
            }
    )
    public static class MonsterDiePatch {
        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(AbstractMonster __instance, boolean triggerRelics) {
            GameActionManagerPatch.deadThisCombat++;
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "currentHealth");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


}
