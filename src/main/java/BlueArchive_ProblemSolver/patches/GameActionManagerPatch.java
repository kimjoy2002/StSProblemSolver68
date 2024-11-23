package BlueArchive_ProblemSolver.patches;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;


public class GameActionManagerPatch {

    public static int deadThisCombat = 0;
    public static int allyDeadThisCombat = 0;

    public static int tacticalChallengeCount = 0;
    public static int zeroCostCount = 0;
    public static int evildeedThisTurn = 0;
    public static int increaseMercenaryMaxHP = 0;
    public static int blockedThisCombat = 0;
    public static int keepEnergy = 0;

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class getNextActionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {
            zeroCostCount = 0;
            evildeedThisTurn = 0;
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnRelics");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class cardsPlayedThisTurnPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {
            AbstractCard card = ((CardQueueItem)__instance.cardQueue.get(0)).card;
            if(card.cost == 0 || card.freeToPlayOnce || card.costForTurn == 0) {
                zeroCostCount++;
            }
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "cardsPlayedThisTurn");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage",
            paramtypez = {
                    DamageInfo.class
            }
    )
    public static class blockDamagedPatcher {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"damageAmount"}
        )
        public static void Insert(AbstractPlayer __instance, DamageInfo damage, int damageAmount) {
            int block = __instance.currentBlock;
            int tempHp = TempHPField.tempHp.get(__instance);
            if(block < 0)
                block = 0;
            if(tempHp < 0)
                tempHp = 0;
            blockedThisCombat += Math.min(block+tempHp, damageAmount);
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class ThisCombatClearPatch {
        public static void Postfix(GameActionManager __instance)
        {
            evildeedThisTurn = 0;
            deadThisCombat = 0;
            allyDeadThisCombat = 0;
            increaseMercenaryMaxHP = 0;
            tacticalChallengeCount = 0;
            zeroCostCount = 0;
            blockedThisCombat = 0;
            keepEnergy = 0;
        }
    }

}
