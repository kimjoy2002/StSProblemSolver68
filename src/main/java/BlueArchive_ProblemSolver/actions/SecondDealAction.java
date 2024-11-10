package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.MutsukiMine;
import BlueArchive_ProblemSolver.cards.SecondDeal;
import BlueArchive_ProblemSolver.effects.ShowCardAndToHandIndexEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;

public class SecondDealAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final float DURATION;
    CardGroup original_hand = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    private SecondDeal secondDealCard;

    public SecondDealAction(SecondDeal secondDealCard) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 1);
        this.actionType = ActionType.DISCARD;
        this.secondDealCard = secondDealCard;
        this.duration = DURATION;
    }
    public void discardAction(AbstractCard target) {
        if (original_hand.contains(target) && target.cost > 0 && !target.freeToPlayOnce) {
            int index_ = original_hand.group.indexOf(target);
            secondDealCard.returnToHand = true;
            secondDealCard.return_index = index_;
            AbstractDungeon.effectList.add(new ShowCardAndToHandIndexEffect(target, index_));
            //this.addToTop(new MakeTempCardInHandIndexAction(new MutsukiMine(), amount, index_));
        }

    }

    public void update() {
        if (this.duration == DURATION) {
            secondDealCard.returnToHand = false;
            secondDealCard.return_index = -1;
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }

            if (AbstractDungeon.player.hand.size() == 1) {
                AbstractCard c = AbstractDungeon.player.hand.getTopCard();
                discardAction(c);
                AbstractDungeon.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
                AbstractDungeon.player.hand.applyPowers();
                this.isDone = true;
                return;
            } else if (AbstractDungeon.player.hand.size() > 1) {
                for(AbstractCard c : AbstractDungeon.player.hand.group) {
                    original_hand.addToTop(c);
                }
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false);
                this.tickDuration();
                return;
            } else {
                this.isDone = true;
                return;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for(AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                discardAction(c);
                AbstractDungeon.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
        TEXT = uiStrings.TEXT;
        DURATION = Settings.ACTION_DUR_XFAST;
    }
}

