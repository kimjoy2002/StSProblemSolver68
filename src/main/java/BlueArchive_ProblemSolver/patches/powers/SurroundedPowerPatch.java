package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ForSubPower;
import BlueArchive_ProblemSolver.powers.OnEndOverCharacterPower;
import BlueArchive_ProblemSolver.relics.RadioTransceiverRelic;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;

import java.util.Iterator;

public class SurroundedPowerPatch {

    @SpirePatch(
            clz = SpireShield.class,
            method = "usePreBattleAction"
    )
    public static class SpireShieldPrePatch {
        public static void Prefix(SpireShield __instance) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps != AbstractDungeon.player) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(ps, __instance, new SurroundedPower(AbstractDungeon.player)));
                    }
                }
            }
        }
    }



    @SpirePatch(
            clz = SpireShield.class,
            method = "die"
    )
    public static class SpireShieldDiePatch {
        public static void Postfix(SpireShield __instance) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

                while(var1.hasNext()) {
                    AbstractMonster m = (AbstractMonster)var1.next();
                    if (!m.isDead && !m.isDying) {
                        for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                            if(ps != AbstractDungeon.player) {
                                if (ps.hasPower("Surrounded")) {
                                    ps.flipHorizontal = m.drawX < ps.drawX;
                                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(ps, ps, "Surrounded"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = SpireSpear.class,
            method = "die"
    )
    public static class SpireSpearDiePatch {
        public static void Postfix(SpireSpear __instance) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

                while(var1.hasNext()) {
                    AbstractMonster m = (AbstractMonster)var1.next();
                    if (!m.isDead && !m.isDying) {
                        for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                            if(ps != AbstractDungeon.player) {
                                if (ps.hasPower("Surrounded")) {
                                    ps.flipHorizontal = m.drawX < ps.drawX;
                                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(ps, ps, "Surrounded"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }





    @SpirePatch(
            clz = CorruptHeart.class,
            method = "takeTurn"
    )
    public static class takeTurnPatch {

        public static void Prefix(CorruptHeart __instance) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                if (__instance.nextMove == 3) {
                    for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                        if(ps != AbstractDungeon.player) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(ps, __instance, new VulnerablePower(ps, 2, true), 2));
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(ps, __instance, new WeakPower(ps, 2, true), 2));
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(ps, __instance, new FrailPower(ps, 2, true), 2));
                        }
                    }
                }
            }
        }
    }





    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "nextRoomTransition",
            paramtypez= {
                    SaveFile.class
            }
    )
    public static class nextRoomTransitionPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractDungeon __instance, SaveFile _file) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for (AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps != AbstractDungeon.player) {
                        ps.flipHorizontal = false;
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "flipHorizontal");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

}
