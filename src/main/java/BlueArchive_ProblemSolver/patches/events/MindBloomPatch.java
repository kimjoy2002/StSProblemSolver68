package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.MindBloom;
import com.megacrit.cardcrawl.events.exordium.Cleric;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import javassist.CtBehavior;

public class MindBloomPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:MindBloom");
    }

    @SpirePatch(
            clz = MindBloom.class,
            method = "buttonEffect"
    )
    public static class MindBloomHeal {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(MindBloom __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(ps != AbstractDungeon.player) {
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
            clz = MindBloom.class,
            method = "<ctor>"
    )
    public static class MindBloomConstructior {
        public static void Postfix(MindBloom __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                if (AbstractDungeon.floorNum % 50 > 40) {
                    __instance.imageEventText.updateDialogOption(2, strings.OPTIONS[0], CardLibrary.getCopy("Doubt"));
                }
            }
        }
    }
}
