package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.characters.Aru;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import java.util.Iterator;

public class SelectScreenPatch {

    @SpirePatch(
            clz = HandCardSelectScreen.class,
            method = "selectHoveredCard"
    )
    public static class SelectScreen1Patch {
        public static CardGroup handClone;
        public static CardGroup handCloneb;

        @SpirePostfixPatch
        public static void SelectionPostPatch(HandCardSelectScreen reg) {
            if (AbstractDungeon.player.chosenClass == Aru.Enums.PROBLEM_SOLVER && !InputHelper.justClickedLeft && CInputActionSet.select.isJustPressed()) {
            }

        }

        public static void ResetHand() {
            Iterator var1 = handClone.group.iterator();

            AbstractCard c;
            for(handCloneb = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED); var1.hasNext(); AbstractDungeon.player.hand.removeCard(c)) {
                c = (AbstractCard)var1.next();
                Iterator var3 = AbstractDungeon.player.hand.group.iterator();

                while(var3.hasNext()) {
                    AbstractCard d = (AbstractCard)var3.next();
                    if (c.equals(d)) {
                        handCloneb.addToBottom(c);
                        break;
                    }
                }
            }

            Iterator var2 = handCloneb.group.iterator();

            while(var2.hasNext()) {
                AbstractCard c2 = (AbstractCard)var2.next();
                AbstractDungeon.player.hand.addToTop(c2);
            }

            AbstractDungeon.player.hand.refreshHandLayout();
        }
    }


    @SpirePatch(
            clz = HandCardSelectScreen.class,
            method = "prep"
    )
    public static class SelectScreenPatch2 {

        @SpirePrefixPatch
        public static SpireReturn SelectionPrePatch(HandCardSelectScreen reg) {
            if (AbstractDungeon.player.chosenClass == Aru.Enums.PROBLEM_SOLVER) {
                SelectScreenPatch.SelectScreen1Patch.handClone = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                Iterator var1 = AbstractDungeon.player.hand.group.iterator();

                while(var1.hasNext()) {
                    AbstractCard c = (AbstractCard)var1.next();
                    SelectScreenPatch.SelectScreen1Patch.handClone.addToBottom(c);
                }
            }

            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
            clz = HandCardSelectScreen.class,
            method = "updateSelectedCards"
    )
    public static class SelectScreenPatch3 {
        @SpireInsertPatch(
                rloc = 34,
                localvars = {}
        )
        public static void Insert() {
            if (AbstractDungeon.player.chosenClass == Aru.Enums.PROBLEM_SOLVER) {
                SelectScreenPatch.SelectScreen1Patch.ResetHand();
            }

        }
    }

    @SpirePatch(
            clz = HandCardSelectScreen.class,
            method = "selectHoveredCard"
    )
    public static class SelectScreenPatch5 {
        public SelectScreenPatch5() {
        }

        @SpirePostfixPatch
        public static void SelectionPostPatch(HandCardSelectScreen reg) {
            if (AbstractDungeon.player.chosenClass == Aru.Enums.PROBLEM_SOLVER) {
                SelectScreenPatch.SelectScreen1Patch.ResetHand();
            }

        }
    }

}
