package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import javassist.CtBehavior;

public class GremlinWheelGamePatch {
    private static final EventStrings strings;


    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:GremlinWheelGame");
    }

    @SpirePatch(
            clz = GremlinWheelGame.class,
            method = "applyResult"
    )
    public static class GremlinWheelGameHpLoss {
        @SpireInsertPatch(
            locator = Locator.class
        )
        public static SpireReturn Insert(GremlinWheelGame __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                float hpLossPercent = ReflectionHacks.getPrivate(__instance, GremlinWheelGame.class, "hpLossPercent");
                int dmg = 0;
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    dmg += ps.maxHealth * hpLossPercent;
                }
                dmg /= ProblemSolver68.problemSolverPlayer.size();
                ((ProblemSolver68)AbstractDungeon.player).damageAll(dmg);
                AbstractDungeon.player.damage(new DamageInfo(null, dmg, DamageInfo.DamageType.HP_LOSS));
                AbstractEvent.logMetricTakeDamage("Wheel of Change", "Damaged", dmg);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "damage");
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = GremlinWheelGame.class,
            method = "applyResult"
    )
    public static class GremlinWheelGameHealAll {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GremlinWheelGame __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    ps.heal(ps.maxHealth);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "heal");
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
            }
        }
    }




    @SpirePatch(
            clz = GremlinWheelGame.class,
            method = "preApplyResult"
    )
    public static class GremlinWheelGameDescription {
        public static void Postfix(GremlinWheelGame __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int result = ReflectionHacks.getPrivate(__instance, GremlinWheelGame.class, "result");

                if(result == 2) {
                    __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0]);
                }
                else if(result == 5) {
                    float hpLossPercent = ReflectionHacks.getPrivate(__instance, GremlinWheelGame.class, "hpLossPercent");
                    int dmg = 0;
                    for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                        dmg += ps.maxHealth * hpLossPercent;
                    }
                    dmg /= ProblemSolver68.problemSolverPlayer.size();
                    __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[1] + dmg + GremlinWheelGame.OPTIONS[7]);
                }
            }
        }
    }
}
