package BlueArchive_ProblemSolver.patches.cards;

import BlueArchive_ProblemSolver.cards.Snipe;
import BlueArchive_ProblemSolver.cards.onAddToHandCards;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;

public class KeepEnergyPatch {
    static int temp_energy = 0;

    @SpirePatch(
            clz = EnergyPanel.class,
            method = "setEnergy",
            paramtypez= {
                    int.class
            }
    )
    public static class setEnergyPatch {
        public static void Postfix(int energy) {
            if(GameActionManagerPatch.keepEnergy > EnergyPanel.totalCount) {
                GameActionManagerPatch.keepEnergy = EnergyPanel.totalCount;
            }
            if(GameActionManagerPatch.keepEnergy < 0) {
                GameActionManagerPatch.keepEnergy = 0;
            }
        }
    }

    @SpirePatch(
            clz = EnergyPanel.class,
            method = "useEnergy",
            paramtypez= {
                    int.class
            }
    )
    public static class useEnergyPatch {
        public static void Postfix(int energy) {
            if(GameActionManagerPatch.keepEnergy > EnergyPanel.totalCount) {
                GameActionManagerPatch.keepEnergy = EnergyPanel.totalCount;
            }
            if(GameActionManagerPatch.keepEnergy < 0) {
                GameActionManagerPatch.keepEnergy = 0;
            }
        }
    }

    @SpirePatch(
            clz = EnergyManager.class,
            method = "recharge"
    )
    public static class EnergyManagerPrePatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(EnergyManager __instance) {
            if(GameActionManagerPatch.keepEnergy > EnergyPanel.totalCount) {
                GameActionManagerPatch.keepEnergy = EnergyPanel.totalCount;
            }
            if(GameActionManagerPatch.keepEnergy > 0) {
                __instance.energy += GameActionManagerPatch.keepEnergy;
                temp_energy = GameActionManagerPatch.keepEnergy;
            }
            GameActionManagerPatch.keepEnergy = 0;
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(EnergyPanel.class, "setEnergy");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = EnergyManager.class,
            method = "recharge"
    )
    public static class EnergyManagerPostPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(EnergyManager __instance) {
            if(temp_energy > 0) {
                __instance.energy -= temp_energy;
                temp_energy = 0;
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "updateEnergyGain");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
