package BlueArchive_ProblemSolver.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;

public class ZeroCostDiscardPileToHandAction  extends AbstractGameAction {
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;

    private boolean random;

    public ZeroCostDiscardPileToHandAction(int numberOfCards, boolean random) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.random = random;
    }


    public void update() {

        if (this.duration == this.startDuration) {
            CardGroup zeroGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            for(AbstractCard card : this.player.discardPile.group) {
                if(card.cost == 0 || card.costForTurn == 0 || card.freeToPlayOnce){
                    zeroGroup.addToTop(card);
                }
            }


            if (!zeroGroup.isEmpty() && this.numberOfCards > 0) {

                if(random) {
                    zeroGroup.shuffle(AbstractDungeon.cardRng);
                    for(int i = 0; i < numberOfCards; i++) {
                        if(!zeroGroup.isEmpty() && this.player.hand.size() < 10) {
                            AbstractCard c = zeroGroup.getTopCard();
                            this.player.hand.addToHand(c);
                            this.player.discardPile.removeCard(c);
                            zeroGroup.removeTopCard();
                            c.lighten(false);
                            c.applyPowers();
                        }
                    }
                    this.isDone = true;
                }
                else if (zeroGroup.size() <= this.numberOfCards) {
                    ArrayList<AbstractCard> cardsToMove = new ArrayList();
                    Iterator var5 = zeroGroup.group.iterator();

                    AbstractCard c;
                    while(var5.hasNext()) {
                        c = (AbstractCard)var5.next();
                        cardsToMove.add(c);
                    }

                    var5 = cardsToMove.iterator();

                    while(var5.hasNext()) {
                        c = (AbstractCard)var5.next();
                        if (this.player.hand.size() < 10) {
                            this.player.hand.addToHand(c);

                            this.player.discardPile.removeCard(c);
                        }

                        c.lighten(false);
                        c.applyPowers();
                    }

                    this.isDone = true;
                } else {
                    if (this.numberOfCards == 1) {
                        AbstractDungeon.gridSelectScreen.open(zeroGroup, this.numberOfCards, TEXT[0], false);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(zeroGroup, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false);
                    }

                    this.tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else {
            Iterator var1;
            AbstractCard c;
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    c = (AbstractCard)var1.next();
                    if (this.player.hand.size() < 10) {
                        this.player.hand.addToHand(c);
                        this.player.discardPile.removeCard(c);
                    }

                    c.lighten(false);
                    c.unhover();
                    c.applyPowers();
                }

                for(var1 = this.player.discardPile.group.iterator(); var1.hasNext(); c.target_y = 0.0F) {
                    c = (AbstractCard)var1.next();
                    c.unhover();
                    c.target_x = (float) CardGroup.DISCARD_PILE_X;
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
            if (this.isDone) {
                var1 = this.player.hand.group.iterator();

                while(var1.hasNext()) {
                    c = (AbstractCard)var1.next();
                    c.applyPowers();
                }
            }

        }
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("BetterToHandAction").TEXT;
    }
}