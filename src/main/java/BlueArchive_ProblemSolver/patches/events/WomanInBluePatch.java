package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.Mushrooms;
import com.megacrit.cardcrawl.events.shrines.FaceTrader;
import com.megacrit.cardcrawl.events.shrines.WomanInBlue;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import javassist.CtBehavior;

public class WomanInBluePatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:WomanInBlue");
    }

    @SpirePatch(
            clz = WomanInBlue.class,
            method = "buttonEffect"
    )
    public static class WomanInBlueDamage {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(WomanInBlue __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int damage = MathUtils.ceil(AbstractDungeon.player.maxHealth * 0.05f);
                ProblemSolver68.damageAll(damage);
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
            clz = WomanInBlue.class,
            method = "<ctor>"
    )
    public static class WomanInBlueConstructior {
        public static void Postfix(WomanInBlue __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                if(AbstractDungeon.ascensionLevel >= 15) {
                    int damage = MathUtils.ceil(AbstractDungeon.player.maxHealth * 0.05f);
                    __instance.imageEventText.updateDialogOption(3, strings.OPTIONS[0] + damage + WomanInBlue.OPTIONS[6]);
                }
            }
        }
    }
}
