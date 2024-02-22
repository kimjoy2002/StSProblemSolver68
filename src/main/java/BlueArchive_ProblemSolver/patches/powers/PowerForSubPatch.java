package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import BlueArchive_ProblemSolver.patches.MultiCharacterPatch;
import BlueArchive_ProblemSolver.powers.ForSubPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.Iterator;

public class PowerForSubPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class applyPowersPatch {
        @SpireInsertPatch(
                loc=3115,
                localvars = {"tmp"}
        )
        public static void Insert(AbstractCard __instance, @ByRef float[] tmp) {
            if(AbstractDungeon.player instanceof ProblemSolver68 && tmp != null) {
                for (AbstractPlayer player : ProblemSolver68.problemSolverPlayer) {
                    for(AbstractPower p : player.powers) {
                        if(p instanceof ForSubPower) {
                            ForSubPower forSubPower = (ForSubPower)p;
                            tmp[0] = forSubPower.atDamageGiveForSub(tmp[0], __instance.damageTypeForTurn);
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class applyPowersMultiPatch {
        @SpireInsertPatch(
                loc=3161,
                localvars = {"tmp","i"}
        )
        public static void Insert(AbstractCard __instance, @ByRef float[][] tmp, int i) {
            if(AbstractDungeon.player instanceof ProblemSolver68 && tmp != null) {
                for (AbstractPlayer player : ProblemSolver68.problemSolverPlayer) {
                    for(AbstractPower p : player.powers) {
                        if(p instanceof ForSubPower) {
                            ForSubPower forSubPower = (ForSubPower)p;
                            tmp[0][i] = forSubPower.atDamageGiveForSub(tmp[0][i], __instance.damageTypeForTurn);
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage",
            paramtypez= {
                    AbstractMonster.class
            }
    )
    public static class calculateCardDamagePatch {
        @SpireInsertPatch(
                loc=3244,
                localvars = {"tmp"}
        )
        public static void Insert(AbstractCard __instance, AbstractMonster damage, @ByRef float[] tmp) {
            if(AbstractDungeon.player instanceof ProblemSolver68 && tmp != null) {
                for (AbstractPlayer player : ProblemSolver68.problemSolverPlayer) {
                    for(AbstractPower p : player.powers) {
                        if(p instanceof ForSubPower) {
                            ForSubPower forSubPower = (ForSubPower)p;
                            tmp[0] = forSubPower.atDamageGiveForSub(tmp[0], __instance.damageTypeForTurn);
                        }
                    }
                }
            }
        }
    }



    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage",
            paramtypez= {
                    AbstractMonster.class
            }
    )
    public static class calculateCardMultiDamagePatch {
        @SpireInsertPatch(
                loc=3302,
                localvars = {"tmp", "i"}
        )
        public static void Insert(AbstractCard __instance, AbstractMonster damage, @ByRef float[][] tmp, int i) {
            if(AbstractDungeon.player instanceof ProblemSolver68 && tmp != null) {
                for (AbstractPlayer player : ProblemSolver68.problemSolverPlayer) {
                    for(AbstractPower p : player.powers) {
                        if(p instanceof ForSubPower) {
                            ForSubPower forSubPower = (ForSubPower)p;
                            tmp[0][i] = forSubPower.atDamageGiveForSub(tmp[0][i], __instance.damageTypeForTurn);
                        }
                    }
                }
            }
        }
    }
    @SpirePatch(
            clz = UseCardAction.class,
            method = "<ctor>",
            paramtypez= {
                    AbstractCard.class,
                    AbstractCreature.class
            }
    )
    public static class useCardActionePatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                if (!card.dontTriggerOnUseCard) {
                    for (AbstractPlayer player : ProblemSolver68.problemSolverPlayer) {
                        for (AbstractPower p : player.powers) {
                            if (p instanceof ForSubPower) {
                                ForSubPower forSubPower = (ForSubPower) p;
                                forSubPower.onUseCardForSub(card, __instance);
                            }
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Iterator.class, "hasNext");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

}
