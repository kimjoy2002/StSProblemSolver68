package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.MoaiHead;
import com.megacrit.cardcrawl.events.beyond.WindingHalls;
import com.megacrit.cardcrawl.events.shrines.FaceTrader;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import javassist.CtBehavior;

public class WindingHallsPatch {
    private static final EventStrings strings;


    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:WindingHalls");
    }


    @SpirePatch(
            clz = WindingHalls.class,
            method = "buttonEffect"
    )
    public static class WindingHallsDamage {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(WindingHalls __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int hpAmt = ReflectionHacks.getPrivate(__instance, WindingHalls.class, "hpAmt");
                ProblemSolver68.damageAll(hpAmt);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "damage");
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = WindingHalls.class,
            method = "buttonEffect"
    )
    public static class WindingHallsHeal {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(WindingHalls __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int healAmt = ReflectionHacks.getPrivate(__instance, WindingHalls.class, "healAmt");
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(ps != AbstractDungeon.player) {
                        ps.heal(healAmt);
                    }
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
            clz = WindingHalls.class,
            method = "buttonEffect"
    )
    public static class WindingHallsdecreaseMaxHealth {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(WindingHalls __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int maxHPAmt = ReflectionHacks.getPrivate(__instance, WindingHalls.class, "maxHPAmt");
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(ps != AbstractDungeon.player) {
                        ps.decreaseMaxHealth(maxHPAmt);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decreaseMaxHealth");
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = WindingHalls.class,
            method = "buttonEffect"
    )
    public static class WindingHallsDescription {
        @SpireInsertPatch(
                rloc = 8
        )
        public static void Insert(WindingHalls __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int hpAmt = ReflectionHacks.getPrivate(__instance, WindingHalls.class, "hpAmt");
                int healAmt = ReflectionHacks.getPrivate(__instance, WindingHalls.class, "healAmt");
                int maxHPAmt = ReflectionHacks.getPrivate(__instance, WindingHalls.class, "maxHPAmt");
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0] + hpAmt + WindingHalls.OPTIONS[2], CardLibrary.getCopy("Madness"));
                __instance.imageEventText.updateDialogOption(1, strings.OPTIONS[1] + healAmt + WindingHalls.OPTIONS[5], CardLibrary.getCopy("Writhe"));
                __instance.imageEventText.updateDialogOption(2, strings.OPTIONS[2] + maxHPAmt + WindingHalls.OPTIONS[7]);
            }
        }
    }


    @SpirePatch(
            clz = WindingHalls.class,
            method = "<ctor>"
    )
    public static class WindingHallsConstructior {
        public static void Postfix(WindingHalls __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int hpAmt = 0;
                if (AbstractDungeon.ascensionLevel >= 15) {
                    hpAmt = 4;
                }
                else {
                    hpAmt = 3;
                }

                int healAmt = 0;
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if (AbstractDungeon.ascensionLevel >= 15) {
                        healAmt += ps.maxHealth* 0.2F;
                    }
                    else {
                        healAmt += ps.maxHealth* 0.25F;
                    }
                }
                healAmt /= ProblemSolver68.problemSolverPlayer.size();
                int maxHPAmt = 1;


                ReflectionHacks.setPrivate(__instance, WindingHalls.class, "hpAmt", hpAmt);
                ReflectionHacks.setPrivate(__instance, WindingHalls.class, "healAmt", healAmt);
                ReflectionHacks.setPrivate(__instance, WindingHalls.class, "maxHPAmt", maxHPAmt);
            }
        }
    }
}
