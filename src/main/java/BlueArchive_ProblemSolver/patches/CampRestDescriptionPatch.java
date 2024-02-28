package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.RestOption;

@SpirePatch(
        clz = RestOption.class,
        method = "<ctor>"
)
public class CampRestDescriptionPatch {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public CampRestDescriptionPatch() {
    }

    @SpireInsertPatch(
            rloc = 27
    )
    public static void Insert(RestOption __instance, boolean active) {
        if (AbstractDungeon.player instanceof Aru) {
            int healAmt_mod;

            int max_health = 0;
            for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                if (ProblemSolver68.isProblemSolver(ps.solverType)) {
                    max_health += ps.maxHealth;
                }
            }

            if (ModHelper.isModEnabled("Night Terrors")) {
                healAmt_mod = (int)((float)max_health * 1.0F);
            } else {
                healAmt_mod = (int)((float)max_health * 0.3F);
            }

            if (Settings.isEndless && AbstractDungeon.player.hasBlight("FullBelly")) {
                healAmt_mod /= 2;
            }

            String desc;
            if (ModHelper.isModEnabled("Night Terrors")) {
                desc = TEXT[1] + healAmt_mod + ").";
                if (AbstractDungeon.player.hasRelic("Regal Pillow")) {
                    desc = desc + "\n+15" + TEXT[2] + AbstractDungeon.player.getRelic("Regal Pillow").name + LocalizedStrings.PERIOD;
                }
            } else {
                desc = TEXT[0] + healAmt_mod + ").";
                if (AbstractDungeon.player.hasRelic("Regal Pillow")) {
                    desc = desc + "\n+15" + TEXT[2] + AbstractDungeon.player.getRelic("Regal Pillow").name + LocalizedStrings.PERIOD;
                }
            }
            ReflectionHacks.setPrivateInherited(__instance, RestOption.class, "description", desc);
        }

    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_ProblemSolver:RestOption");
        TEXT = uiStrings.TEXT;
    }
}
