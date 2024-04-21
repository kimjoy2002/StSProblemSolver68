package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.AllApplyedPower;
import BlueArchive_ProblemSolver.powers.OnApplyedPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

import java.util.ArrayList;

public class GloomyPastPatch {

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class applyPowerActionPatch {
        public static void Prefix(ApplyPowerAction __instance) {
            float duration = (Float) ReflectionHacks.getPrivate(__instance, AbstractGameAction.class, "duration");
            float startingDuration = (Float) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "startingDuration");
            AbstractPower powerToApply = (AbstractPower) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
            if (__instance.target != null && !__instance.target.isDeadOrEscaped()) {
                if (duration == startingDuration) {
                    if (!(powerToApply instanceof NoDrawPower && __instance.target.hasPower(powerToApply.ID))) {
                         ArrayList<AbstractPower> list_ = ProblemSolver68.getAllPowerForProblem68(AllApplyedPower.class);
                         for(AbstractPower power_ : list_) {
                             ((AllApplyedPower)power_).AllApplyed(powerToApply, __instance.target, __instance.source);
                         }

                        for(AbstractPower power_ : __instance.target.powers) {
                            if (power_ instanceof OnApplyedPower) {
                                ((OnApplyedPower)power_).OnApplyed(powerToApply, __instance.target, __instance.source);
                            }
                        }
                    }
                }
            }
        }
    }
}
