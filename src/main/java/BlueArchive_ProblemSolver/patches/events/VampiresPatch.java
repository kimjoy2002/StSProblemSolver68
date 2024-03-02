package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.cards.colorless.Bite;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Ghosts;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.localization.EventStrings;

public class VampiresPatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:Vampires");
    }


    @SpirePatch(
            clz = Vampires.class,
            method = "buttonEffect"
    )
    public static class VampiresLoss {
        @SpireInsertPatch(
                rloc = 9
        )
        public static void Insert(Vampires __instance, int buttonPressed) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps != AbstractDungeon.player) {
                        int hpLoss = MathUtils.ceil((float)ps.maxHealth * 0.3F);
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
            clz = Vampires.class,
            method = "<ctor>"
    )
    public static class VampiresConstructior {
        public static void Postfix(Vampires __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                __instance.imageEventText.updateDialogOption(0, strings.OPTIONS[0], new Bite());
            }
        }
    }
}
