package BlueArchive_ProblemSolver.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.Iterator;

public class ImprishAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;

    public ImprishAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
            } else if (this.p.hand.size() == 1) {
                if (this.p.hand.getBottomCard().costForTurn == -1) {
                    this.addToTop(new ImpAction(EnergyPanel.getCurrentEnergy()));
                } else if (this.p.hand.getBottomCard().costForTurn > 0) {
                    this.addToTop(new ImpAction(this.p.hand.getBottomCard().costForTurn));
                }

                this.p.hand.moveToExhaustPile(this.p.hand.getBottomCard());
                this.tickDuration();
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                AbstractCard c;
                for(Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator(); var1.hasNext(); this.p.hand.moveToExhaustPile(c)) {
                    c = (AbstractCard)var1.next();
                    if (c.costForTurn == -1) {
                        this.addToTop(new ImpAction(EnergyPanel.getCurrentEnergy()));
                    } else if (c.costForTurn > 0) {
                        this.addToTop(new ImpAction(c.costForTurn));
                    }
                }

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("RecycleAction");
        TEXT = uiStrings.TEXT;
    }
}
