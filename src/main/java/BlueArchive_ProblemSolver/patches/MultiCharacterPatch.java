package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.actions.CheckApplyPowerAction;
import BlueArchive_ProblemSolver.actions.CleanCharacterAction;
import BlueArchive_ProblemSolver.actions.ImpAction;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ImmotalPower;
import BlueArchive_ProblemSolver.powers.ImpPower;
import BlueArchive_ProblemSolver.powers.SharedPower;
import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.KnowingSkull;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import javassist.CtBehavior;

import java.util.ArrayList;

import static BlueArchive_ProblemSolver.actions.CleanCharacterAction.CleanCharacter;
import static BlueArchive_ProblemSolver.actions.ImpAction.resetImp;

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
            clz = AbstractRoom.class,
            method = "applyEndOfTurnPreCardPowers"
    )
    public static class applyEndOfTurnPreCardPowersPatch {
        public static void Postfix(AbstractRoom __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps != AbstractDungeon.player) {
                        for(AbstractPower p_ : ps.powers) {
                            p_.atEndOfTurnPreEndTurnCards(true);
                        }
                    }
                }
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


    public static void onDead_(AbstractPlayer __instance) {
        if (AbstractDungeon.player instanceof ProblemSolver68) {
            if (!ProblemSolver68.isAllDead()) {
                ProblemSolver68.onDead(__instance);
                GameActionManagerPatch.deadThisCombat++;
                GameActionManagerPatch.allyDeadThisCombat++;
            }
        }
    }


    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class SkullDamage {
        public static SpireReturn Prefix(ApplyPowerAction __instance)
        {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                if (__instance.target != null && __instance.target.isPlayer){
                    if(!ProblemSolver68.problemSolverPlayer.contains(__instance.target)) {
                        __instance.isDone = true;
                        return SpireReturn.Return();
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }

    public static boolean onDamage_(AbstractPlayer __instance) {
        if(__instance instanceof ProblemSolver68) {

            if (!ProblemSolver68.isAllDead()) {
                __instance.isDead = true;
                __instance.currentHealth = 0;
                for(AbstractPower p_ : __instance.powers) {
                    p_.onRemove();
                }

                AbstractPlayer next_ = null;
                if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                    next_ = ProblemSolver68.changeToNextCharacter();
                } else {
                    next_ = ProblemSolver68.changeToRandomCharacter();
                }

                ArrayList<AbstractPower> newPowers = new ArrayList<AbstractPower>();
                for(AbstractPower power_ : __instance.powers) {
                    if(power_ instanceof CloneablePowerInterface && power_ instanceof SharedPower){
                        newPowers.add(((CloneablePowerInterface)power_).makeCopy());
                    }
                    else {
                        SharedPower.moreShared(power_, next_, newPowers);
                    }
                }

                int imp_value = 0;
                int imp_amount = 0;
                if(__instance.hasPower(ImpPower.POWER_ID)) {
                    imp_value = __instance.getPower(ImpPower.POWER_ID).amount;
                    imp_amount = ((ImpPower)__instance.getPower(ImpPower.POWER_ID)).amount_imp;
                }


                if (!ProblemSolver68.isProblemSolver(((ProblemSolver68) __instance).solverType) &&
                        __instance.hasPower(ImmotalPower.POWER_ID)) {
                    __instance.getPower(ImmotalPower.POWER_ID).onDeath();
                }

                __instance.powers.clear();

                if (!__instance.stance.ID.equals("Neutral")) {
                    __instance.stance.onExitStance();
                    __instance.stance = new NeutralStance();
                    __instance.onStanceChange("Neutral");
                }

                if(next_ != null) {
                    for(AbstractPower power_ : newPowers) {
                        AbstractDungeon.actionManager.addToBottom(new CheckApplyPowerAction(next_, next_, power_));
                    }
                }

                if(imp_value > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ImpAction(imp_value, imp_amount, true));
                }

                if (!ProblemSolver68.isProblemSolver(((ProblemSolver68) __instance).solverType)) {

                    ProblemSolver68.dyingPlayer.add((ProblemSolver68) __instance);
                    ProblemSolver68.removeCharacter((ProblemSolver68) __instance);
                    __instance.isEscaping = true;
                    __instance.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
                    __instance.escapeTimer = 2.5F;
                }

                CleanCharacter();
                return true;
            }
        }
        return false;
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
            if (MultiCharacterPatch.onDamage_(__instance)) {
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
    public static class MercernyDeadPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn Insert(AbstractPlayer __instance, DamageInfo info) {
            MultiCharacterPatch.onDead_(__instance);
            if (__instance instanceof ProblemSolver68 && !((ProblemSolver68)__instance).isProblemSolver()) {
                if (MultiCharacterPatch.onDamage_(__instance)) {
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = InstantKillAction.class,
            method = "update"
    )
    public static class InstantKillActionPatch {
        public static void Postfix(InstantKillAction __instance)
        {
            if(__instance.target instanceof ProblemSolver68) {
                MultiCharacterPatch.onDead_((AbstractPlayer)__instance.target);
                MultiCharacterPatch.onDamage_((AbstractPlayer)__instance.target);
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





    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class getNextActionPatch {
        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(GameActionManager __instance) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                if (__instance.monsterQueue.isEmpty()) {
                    AbstractDungeon.actionManager.addToBottom(new CleanCharacterAction(true));
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.NewExprMatcher(WaitAction.class);
                return new int[]{LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0] + 1};
            }
        }
    }
}
