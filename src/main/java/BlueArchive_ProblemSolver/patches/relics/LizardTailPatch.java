package BlueArchive_ProblemSolver.patches.relics;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.MultiCharacterPatch;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FairyPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.LizardTail;
import com.megacrit.cardcrawl.relics.PotionBelt;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;
import javassist.CtBehavior;

public class LizardTailPatch {


    @SpirePatch(
            clz = FairyPotion.class,
            method = "use",
            paramtypez= {
                    AbstractCreature.class
            }
    )
    public static class FairyPotionPatch {
        public static SpireReturn Prefix(FairyPotion __instance, AbstractCreature target) {
            if (target instanceof ProblemSolver68) {
                int potency = (int) ReflectionHacks.getPrivate(__instance, AbstractPotion.class, "potency");
                float percent = (float)potency / 100.0F;
                int healAmt = (int)((float)target.maxHealth * percent);
                if (healAmt < 1) {
                    healAmt = 1;
                }

                target.heal(healAmt, true);
                AbstractDungeon.topPanel.destroyPotion(__instance.slot);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage",
            paramtypez= {
                    DamageInfo.class
            }
    )
    public static class LizardTailInsertPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn Insert(AbstractPlayer __instance, DamageInfo info) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                if (__instance.hasRelic("Lizard Tail") && ((LizardTail)__instance.getRelic("Lizard Tail")).counter == -1) {
                    AbstractRelic relic = __instance.getRelic("Lizard Tail");
                    relic.flash();
                    AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
                    int healAmt = __instance.maxHealth / 2;
                    if (healAmt < 1) {
                        healAmt = 1;
                    }
                    __instance.heal(healAmt, true);
                    relic.setCounter(-2);
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "onTrigger");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

}
