package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.AbstractMonsterPatch;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import BlueArchive_ProblemSolver.powers.FearPower;
import BlueArchive_ProblemSolver.powers.OnUsePotionPower;
import BlueArchive_ProblemSolver.relics.OnUsePotionRelic;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;

import java.util.Iterator;

public class FearPatch {


    @SpirePatch(
            clz = AbstractMonster.class,
            method = "updateEscapeAnimation"
    )
    public static class ShieldPatch {
        public static SpireReturn Prefix(AbstractMonster __instance) {
            if(__instance instanceof SpireShield) {
                if (__instance.escapeTimer != 0.0F) {
                    __instance.flipHorizontal = true;
                    __instance.escapeTimer -= Gdx.graphics.getDeltaTime();
                    __instance.drawX -= Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
                    if(__instance.escapeTimer > 0.0F) {
                        return SpireReturn.Return();
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = AbstractMonster.class,
            method = "updateEscapeAnimation"
    )
    public static class MonsterDiePatch {
        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(AbstractMonster __instance) {
            if(__instance.hasPower(FearPower.POWER_ID)) {
                if(__instance.getPower(FearPower.POWER_ID).amount >= __instance.currentHealth) {
                    __instance.escaped = false;
                    __instance.isDead = true;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getMonsters");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    private static void onUsePotionPower(AbstractPotion potion, AbstractCreature target) {

        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster) var1.next();
            if (!m.isDead && !m.isDying) {
                for(AbstractPower power_ : m.powers) {
                    if(power_ instanceof OnUsePotionPower) {
                        ((OnUsePotionPower)power_).OnUsePotion(potion, target);
                    }
                }
            }
        }

        for(AbstractRelic relic_ : AbstractDungeon.player.relics) {
            if(relic_ instanceof OnUsePotionRelic) {
                ((OnUsePotionRelic)relic_).OnUsePotion(potion, target);
            }
        }

        if(AbstractDungeon.player instanceof ProblemSolver68) {
            for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                if(ps.currentHealth > 0) {
                    for(AbstractPower power_ : ps.powers) {
                        if(power_ instanceof OnUsePotionPower) {
                            ((OnUsePotionPower)power_).OnUsePotion(potion, target);
                        }
                    }
                }
            }
        } else {
            for(AbstractPower power_ : AbstractDungeon.player.powers) {
                if(power_ instanceof OnUsePotionPower) {
                    ((OnUsePotionPower)power_).OnUsePotion(potion, target);
                }
            }
        }
    }


    @SpirePatch(
            clz = PotionPopUp.class,
            method = "updateInput"
    )
    public static class PotionPopUpPatch {
        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(PotionPopUp __instance) {
            AbstractPotion potion = (AbstractPotion) ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "potion");
            AbstractMonster hoveredMonster = (AbstractMonster) ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "hoveredMonster");
            onUsePotionPower(potion, hoveredMonster);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "destroyPotion");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }



    @SpirePatch(
            clz = PotionPopUp.class,
            method = "updateTargetMode"
    )
    public static class PotionPopUpPatch2 {
        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(PotionPopUp __instance) {
            AbstractPotion potion = (AbstractPotion) ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "potion");
            AbstractMonster hoveredMonster = (AbstractMonster) ReflectionHacks.getPrivate(__instance, PotionPopUp.class, "hoveredMonster");
            onUsePotionPower(potion, hoveredMonster);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "destroyPotion");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
