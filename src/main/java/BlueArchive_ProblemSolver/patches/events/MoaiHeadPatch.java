package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import com.megacrit.cardcrawl.events.beyond.MoaiHead;
import com.megacrit.cardcrawl.localization.EventStrings;
import javassist.CtBehavior;

public class MoaiHeadPatch {
    private static final EventStrings strings;


    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:MoaiHead");
    }


    @SpirePatch(
            clz = MoaiHead.class,
            method = "buttonEffect"
    )
    public static class MoaiHeadHeal {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(MoaiHead __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {

                int hpAmt = ReflectionHacks.getPrivate(__instance, MoaiHead.class, "hpAmt");

                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(ps != AbstractDungeon.player) {
                        ps.maxHealth -= hpAmt;
                        if (ps.currentHealth > ps.maxHealth) {
                            ps.currentHealth = ps.maxHealth;
                        }
                        if (ps.maxHealth < 1) {
                            ps.maxHealth = 1;
                        }
                        ps.heal(ps.currentHealth);
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
            clz = MoaiHead.class,
            method = "<ctor>"
    )
    public static class MoaiHeadConstructior {
        public static void Postfix(MoaiHead __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int hpAmt = 0;
                if (AbstractDungeon.ascensionLevel >= 15) {
                    hpAmt = 4;
                }
                else {
                    hpAmt = 3;
                }

                ReflectionHacks.setPrivate(__instance, MoaiHead.class, "hpAmt", hpAmt);

                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0] + hpAmt + strings.OPTIONS[1]);
            }
        }
    }
}
