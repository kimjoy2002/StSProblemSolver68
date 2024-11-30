package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Colosseum;
import com.megacrit.cardcrawl.events.exordium.Cleric;
import com.megacrit.cardcrawl.localization.EventStrings;
import javassist.CtBehavior;

public class ColosseumPatch {
    @SpirePatch(
            clz = Colosseum.class,
            method = "reopen"
    )
    public static class ColosseumPatchMovePatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Postfix(Colosseum __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                ((ProblemSolver68)AbstractDungeon.player).movePosition((float) Settings.WIDTH * 0.25F,
                        AbstractDungeon.player.drawY);
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {

                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "preBattlePrep");

                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);

            }
        }
    }
}
