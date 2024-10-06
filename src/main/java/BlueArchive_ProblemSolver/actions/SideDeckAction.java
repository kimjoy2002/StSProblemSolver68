package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.AddDeck;
import BlueArchive_ProblemSolver.rewards.SideDeckReward;
import BlueArchive_ProblemSolver.save.SideDeckSave;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.Iterator;

public class SideDeckAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractCard card;

    int misc = 0;
    public SideDeckAction(AbstractCard card) {
        this.setValues(this.target, this.source, 0);
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }


    private void addReward() {
        AbstractDungeon.topLevelEffectsQueue.add(new PurgeCardEffect(new AddDeck()));
        if(misc != 0) {
            boolean already_has_reward = false;
            for(RewardItem reward : AbstractDungeon.getCurrRoom().rewards) {
                if(reward instanceof SideDeckReward) {
                    if(((SideDeckReward)reward).card_misc == misc) {
                        already_has_reward = true;
                        break;
                    }
                }
            }
            if(already_has_reward == false) {
                RewardItem rewardItem = new SideDeckReward(misc);
                AbstractDungeon.getCurrRoom().rewards.add(rewardItem);
            }
        }
    }

    public void update() {
        if (this.duration == this.startDuration) {
            misc = card.misc;

            CardGroup deck_ = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            if(SideDeckSave.decks.containsKey(misc)) {
                for(AbstractCard card_in_side_deck :  SideDeckSave.decks.get(misc)) {
                    deck_.addToTop(card_in_side_deck.makeStatEquivalentCopy());
                }
            }
            if (deck_.isEmpty()) {
                addReward();
            } else {
                deck_.addToBottom(new AddDeck());
                AbstractDungeon.gridSelectScreen.open(deck_, 1, TEXT[0], false, false, false, false);
            }
            this.tickDuration();
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();


                while (var1.hasNext()) {
                    AbstractCard c = (AbstractCard) var1.next();
                    if(c instanceof AddDeck) {
                        addReward();
                    } else {
                        if (AbstractDungeon.player.hand.size() < 10) {
                            this.addToBot(new MakeTempCardInHandAction(c.makeStatEquivalentCopy()));
                        } else {
                            this.addToBot(new MakeTempCardInDiscardAction(c.makeStatEquivalentCopy(), 1));
                        }

                    }
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }
            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_ProblemSolver:SideDeck");
        TEXT = uiStrings.TEXT;
    }
}
