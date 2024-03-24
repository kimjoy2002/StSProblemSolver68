package BlueArchive_ProblemSolver.patches.cards;

import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.actions.DelayAction;
import BlueArchive_ProblemSolver.cards.EvilDeedsCard;
import BlueArchive_ProblemSolver.cards.ProblemSolverDefend;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.CannotSelectedPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.stances.AbstractStance;
import javassist.CtBehavior;

public class UseCardActionPatch {
    public static AbstractPlayer originCharacter = null;

    @SpirePatch(
            clz=AbstractCard.class,
            method=SpirePatch.CLASS
    )
    public static class AbstractCardField
    {
        public static SpireField<AbstractPlayer> castPlayer = new SpireField<>(() -> null);
    }

    @SpirePatch(
            clz=PlayTopCardAction.class,
            method=SpirePatch.CLASS
    )
    public static class PlayTopCardActionField
    {
        public static SpireField<AbstractPlayer> castPlayer = new SpireField<>(() -> null);
    }


    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class getNextActionPatcher {

        public static void Prefix(GameActionManager __instance)
        {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                if (__instance.actions.isEmpty() && __instance.preTurnActions.isEmpty() && !__instance.cardQueue.isEmpty()) {
                    if(__instance.cardQueue.get(0).card != null) {
                        AbstractPlayer player_ = AbstractCardField.castPlayer.get(__instance.cardQueue.get(0).card);
                        if(player_ != null) {
                            if (originCharacter == null) {
                                originCharacter = AbstractDungeon.player;
                            }
                            AbstractCardField.castPlayer.set(__instance.cardQueue.get(0).card, null);
                            AbstractDungeon.actionManager.addToBottom(new ChangeCharacterAction(player_, false, true));
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = PlayTopCardAction.class,
            method = "update"
    )
    public static class PlayTopCardAction2Patcher {

        @SpireInsertPatch(
                locator = Locator.class

        )
        public static void Insert(PlayTopCardAction __instance)
        {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                if(AbstractDungeon.actionManager.actions.size() > 0) {
                    AbstractGameAction action_ =  AbstractDungeon.actionManager.actions.get(0);
                    if(action_ instanceof PlayTopCardAction) {
                        AbstractPlayer player_ = PlayTopCardActionField.castPlayer.get(__instance);
                        PlayTopCardActionField.castPlayer.set(action_, player_);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.NewExprMatcher(EmptyDeckShuffleAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = PlayTopCardAction.class,
            method = "update"
    )
    public static class PlayTopCardActionPatcher {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"card"}

        )
        public static void Insert(PlayTopCardAction __instance, @ByRef AbstractCard[] card)
        {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                if(card != null) {
                    AbstractPlayer player_ = PlayTopCardActionField.castPlayer.get(__instance);
                    AbstractCardField.castPlayer.set(card[0], player_);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "applyPowers");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class UseCardActionPatcher {

        public static void Postfix(UseCardAction __instance)
        {
            if(__instance.isDone) {
                if (AbstractDungeon.actionManager.cardQueue.isEmpty()) {
                    if (originCharacter != null) {
                        AbstractDungeon.actionManager.addToBottom(new ChangeCharacterAction(originCharacter, false, true));
                        originCharacter = null;
                    }
                }
                if(AbstractDungeon.player.hasPower(CannotSelectedPower.POWER_ID)) {
                    if (AbstractDungeon.actionManager.cardQueue.isEmpty()) {
                        if (AbstractDungeon.player instanceof ProblemSolver68) {
                            AbstractDungeon.actionManager.addToBottom(new ChangeCharacterAction(true));
                        } else {
                            AbstractDungeon.actionManager.addToBottom(new PressEndTurnButtonAction());
                        }
                    }
                }
            }
        }
    }
}
