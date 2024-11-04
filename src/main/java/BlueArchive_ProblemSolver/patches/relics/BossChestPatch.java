package BlueArchive_ProblemSolver.patches.relics;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.relics.MoreProblemSolverRelic;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;

import java.util.ArrayList;

public class BossChestPatch {



    @SpirePatch(
            clz = BossChest.class,
            method = "<ctor>"
    )
    public static class BossChestCtorPatch {
        public static void Postfix(BossChest __instance) {
            if (AbstractDungeon.player instanceof Aru) {
                if (!(AbstractDungeon.actNum >= 4 && AbstractPlayer.customMods.contains("Blight Chests"))) {
                    if(AbstractDungeon.actNum == 2 && ProblemSolver68.problemSolverPlayer.size() == 3) {
                        ArrayList<Aru.ProblemSolver68Type> able = new ArrayList<>();
                        if(!ProblemSolver68.hasCharacter(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU))
                            able.add(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
                        if(!ProblemSolver68.hasCharacter(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI))
                            able.add(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
                        if(!ProblemSolver68.hasCharacter(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO))
                            able.add(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO);
                        if(!ProblemSolver68.hasCharacter(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA))
                            able.add(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA);

                        if(able.size() > 0) {
                            __instance.relics.remove(0);
                            __instance.relics.add(new MoreProblemSolverRelic(able.get(AbstractDungeon.relicRng.random(able.size() - 1))));
                        }

                    }
                }
            }
        }
    }


//    @SpirePatch(
//            clz = BossRelicSelectScreen.class,
//            method = "open"
//    )
    public static class BossRelicSelectScreenPatch {
        public static void Postfix(BossRelicSelectScreen __instance, ArrayList<AbstractRelic> chosenRelics) {
            if (AbstractDungeon.player instanceof Aru) {
                if (chosenRelics.size() > 3) {
                    AbstractRelic r4 = (AbstractRelic)chosenRelics.get(3);
                    float SLOT_4_X = (float)Settings.WIDTH / 2.0F + 4.0F * Settings.scale;
                    float SLOT_4_Y = AbstractDungeon.floorY + 150.0F * Settings.scale;
                    r4.spawn(SLOT_4_X, SLOT_4_Y);
                    r4.hb.move(r4.currentX, r4.currentY);
                    __instance.relics.add(r4);
                }
            }
        }
    }
}
