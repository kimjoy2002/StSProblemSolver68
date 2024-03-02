package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.MultiCharacterPatch;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.ShiningLight;
import com.megacrit.cardcrawl.events.shrines.Designer;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;

public class DesignerPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:Designer");
    }
    @SpirePatch(
            clz = Designer.class,
            method = "buttonEffect"
    )
    public static class DesignerHpLoss {
        @SpireInsertPatch(
            locator = Locator.class
        )
        public static void Insert(Designer __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int hpLoss = ReflectionHacks.getPrivate(__instance, Designer.class, "hpLoss");
                ProblemSolver68.damageAll(hpLoss);
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
            clz = Designer.class,
            method = "buttonEffect"
    )
    public static class DesignerDescription {
        @SpireInsertPatch(
                rloc = 42
        )
        public static void Insert(Designer __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int hpLoss = ReflectionHacks.getPrivate(__instance, Designer.class, "hpLoss");
                __instance.imageEventText.updateDialogOption(3, strings.OPTIONS[0] + hpLoss + strings.OPTIONS[1]);
            }
        }
    }

    @SpirePatch(
            clz = Designer.class,
            method = "<ctor>"
    )
    public static class DesignerConstructior {
        public static void Postfix(Designer __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
               int hpLoss = 0;
               if(AbstractDungeon.ascensionLevel >= 15) {
                   hpLoss = 2;
               } else {
                   hpLoss = 1;
               }
               ReflectionHacks.setPrivate(__instance, Designer.class, "hpLoss", hpLoss);
            }
        }
    }
}
