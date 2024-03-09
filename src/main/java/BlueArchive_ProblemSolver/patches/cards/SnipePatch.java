package BlueArchive_ProblemSolver.patches.cards;

import BlueArchive_ProblemSolver.cards.Snipe;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.relics.SnekoEyePatch;
import BlueArchive_ProblemSolver.powers.ForSubPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import javassist.CtBehavior;

public class SnipePatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class DrawPatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"tmp"}

        )
        public static void Insert(AbstractCard __instance, @ByRef float[] tmp) {
            if(AbstractDungeon.player instanceof ProblemSolver68 &&
                    __instance instanceof Snipe &&
                tmp != null && tmp.length > 0) {
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(ps != AbstractDungeon.player) {
                        for(AbstractPower power_ : ps.powers) {
                            if(power_ instanceof StrengthPower) {
                                StrengthPower sp = (StrengthPower)power_;
                                if(sp.amount > 0) {
                                    tmp[0] = sp.atDamageGive(tmp[0], __instance.damageTypeForTurn, __instance);
                                }
                            }
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractStance.class, "atDamageGive");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
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
            if(AbstractDungeon.player instanceof ProblemSolver68 &&
                    __instance instanceof Snipe &&
                    tmp != null && tmp.length > 0) {
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(ps != AbstractDungeon.player) {
                        for(AbstractPower power_ : ps.powers) {
                            if(power_ instanceof StrengthPower) {
                                StrengthPower sp = (StrengthPower)power_;
                                if(sp.amount > 0) {
                                    tmp[0] = sp.atDamageGive(tmp[0], __instance.damageTypeForTurn, __instance);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
