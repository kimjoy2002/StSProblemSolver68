package BlueArchive_ProblemSolver.patches.cards;

import BlueArchive_ProblemSolver.cards.SecondDeal;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import com.megacrit.cardcrawl.screens.mainMenu.TabBarListener;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;

public class SecondDealPatch {

    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class UseCardActionPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(UseCardAction __instance) {
            AbstractCard targetCard =  ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");
            if(targetCard instanceof SecondDeal && ((SecondDeal)targetCard).return_index != -1) {
                int index = ((SecondDeal)targetCard).return_index;
                if( AbstractDungeon.player.hand.size() -1 >= index) {
                    AbstractDungeon.player.hand.group.remove(targetCard);
                    AbstractDungeon.player.hand.group.add(index, targetCard);
                    AbstractDungeon.player.hand.refreshHandLayout();
                    AbstractDungeon.player.hand.applyPowers();
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "onCardDrawOrDiscard");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
