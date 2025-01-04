package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FinishPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.lang.reflect.Method;
import java.util.Iterator;

public class OnFinishPatch {

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "onModifyPower"
    )
    public static class onModifyPowerPatch {
        public static void Prefix() {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for(AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                    for(AbstractPower power_ : ps.powers) {
                        if(power_ instanceof FinishPower) {
                            ((FinishPower)power_).setFinishCount();
                            ((FinishPower)power_).setSelfFinishCount();
                        }
                    }
                }

            } else {
                for(AbstractPower power_ : AbstractDungeon.player.powers) {
                    if(power_ instanceof FinishPower) {
                        ((FinishPower)power_).setFinishCount();
                        ((FinishPower)power_).setSelfFinishCount();
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "renderHealth",
            paramtypez= {
                    SpriteBatch.class
            }
    )
    public static class incrementDiscardPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractCreature __instance, SpriteBatch sb) {
            for(int i = __instance.powers.size()-1; i >= 0; i--) {
                if(__instance.powers.get(i) instanceof FinishPower) {
                    float x = __instance.hb.cX - __instance.hb.width / 2.0F;
                    float hbYOffset = (float)ReflectionHacks.getPrivate(__instance, AbstractCreature.class, "hbYOffset");
                    float y = __instance.hb.cY - __instance.hb.height / 2.0F + hbYOffset;
                    ((FinishPower)__instance.powers.get(i)).renderFinish(sb, x, y);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "renderPowerIcons");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
