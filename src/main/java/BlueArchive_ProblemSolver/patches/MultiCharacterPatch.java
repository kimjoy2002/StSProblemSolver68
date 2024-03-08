package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import javassist.CtBehavior;
public class MultiCharacterPatch {

    @SpirePatch(
            clz = BattleStartEffect.class,
            method = "update"
    )
    public static class GetPurgeablePatch {
        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(BattleStartEffect __instance) {
            ProblemSolver68.battleStartEffectForProblemSolver68(AbstractDungeon.player);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "showHealthBar");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "loadPlayerSave",
            paramtypez= {
                AbstractPlayer.class
            }
    )
    public static class loadPlayerSavePatch {
        public static void Postfix(CardCrawlGame __instance, AbstractPlayer _3) {
            ProblemSolver68.afterLoad();
        }
    }
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "initializeCardPools"
    )
    public static class initializeCardPoolsPatch {
        public static void Postfix(AbstractDungeon __instance)
        {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                ProblemSolver68.reassignCardList();
            }
        }
    }



    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage",
            paramtypez= {
                    DamageInfo.class
            }
    )
    public static class PlayerDeadPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn Insert(AbstractPlayer __instance, DamageInfo info) {
            if (!ProblemSolver68.isAllDead()) {
                __instance.currentHealth = 0;
                ProblemSolver68.changeToRandomCharacter();
                if(__instance instanceof ProblemSolver68 &&
                   !ProblemSolver68.isProblemSolver(((ProblemSolver68)__instance).solverType)) {

                    ProblemSolver68.dyingPlayer.add((ProblemSolver68)__instance);
                    ProblemSolver68.removeCharacter((ProblemSolver68)__instance);
                    __instance.isEscaping = true;
                    __instance.powers.clear();
                    __instance.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
                    __instance.escapeTimer = 2.5F;
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isDead");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage",
            paramtypez= {
                    DamageInfo.class
            }
    )
    public static class OnDeadPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractPlayer __instance, DamageInfo info) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                if (!ProblemSolver68.isAllDead()) {
                    ProblemSolver68.onDead(__instance);
                    GameActionManagerPatch.deadThisCombat++;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }



    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class getNextActionPatcher {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {
            ProblemSolver68.loseBlockForOther();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasPower");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = MonsterGroup.class,
            method = "applyEndOfTurnPowers"
    )
    public static class endOfTurnPowersPatch {
        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(MonsterGroup __instance) {
            ProblemSolver68.endOfTurnPowersForProblemSolver68(AbstractDungeon.player);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "player");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
