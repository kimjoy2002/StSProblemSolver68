package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.city.ForgottenAltar;
import com.megacrit.cardcrawl.events.exordium.ScrapOoze;
import com.megacrit.cardcrawl.events.shrines.WomanInBlue;
import com.megacrit.cardcrawl.localization.EventStrings;
import javassist.CtBehavior;

public class ForgottenAltarPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:ForgottenAltar");
    }

    @SpirePatch(
            clz = ForgottenAltar.class,
            method = "buttonEffect"
    )
    public static class ForgottenAltarDamage {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn Insert(ForgottenAltar __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int hp_loss = (Integer) ReflectionHacks.getPrivate(__instance, ForgottenAltar.class, "hpLoss");
                String DIALOG_3 = (String) ReflectionHacks.getPrivate(__instance, ForgottenAltar.class, "DIALOG_3");
                for(AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                    ps.increaseMaxHp(2, false);
                    ps.damage(new DamageInfo(null, hp_loss));
                }
                CardCrawlGame.sound.play("HEAL_3");
                __instance.showProceedScreen(DIALOG_3);
                AbstractEvent.logMetricDamageAndMaxHPGain("Forgotten Altar", "Shed Blood", hp_loss, 2);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "increaseMaxHp");
                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = ForgottenAltar.class,
            method = "<ctor>"
    )
    public static class ForgottenAltarConstructior {
        public static void Postfix(ForgottenAltar __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int hp_loss = 0;
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(AbstractDungeon.ascensionLevel >= 15) {
                        hp_loss += ps.maxHealth* 0.35F;
                    } else {
                        hp_loss += ps.maxHealth* 0.25F;
                    }
                }
                hp_loss /= ProblemSolver68.problemSolverPlayer.size();
                ReflectionHacks.setPrivate(__instance, ForgottenAltar.class, "hpLoss", hp_loss);
                __instance.imageEventText.updateDialogOption(1, strings.OPTIONS[0] + 2 + strings.OPTIONS[1] + hp_loss + strings.OPTIONS[2]);
            }
        }
    }
}
