package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Ghosts;
import com.megacrit.cardcrawl.events.city.KnowingSkull;
import com.megacrit.cardcrawl.localization.EventStrings;

public class GhostsPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:Ghosts");
    }


    @SpirePatch(
            clz = Ghosts.class,
            method = "buttonEffect"
    )
    public static class GhostsLoss {
        @SpireInsertPatch(
                rloc = 8
        )
        public static void Insert(Ghosts __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps != AbstractDungeon.player) {
                        int hpLoss = MathUtils.ceil((float)ps.maxHealth * 0.5F);
                        if (hpLoss >= ps.maxHealth) {
                            hpLoss = ps.maxHealth - 1;
                        }
                        ps.decreaseMaxHealth(hpLoss);
                    }
                }
            }
        }
    }


    @SpirePatch(
            clz = Ghosts.class,
            method = "<ctor>"
    )
    public static class GhostsConstructior {
        public GhostsConstructior() {
        }

        public static void Postfix(Ghosts __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                if (AbstractDungeon.ascensionLevel >= 15) {
                    __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0], new Apparition());
                } else {
                    __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[1], new Apparition());
                }
            }
        }
    }
}
