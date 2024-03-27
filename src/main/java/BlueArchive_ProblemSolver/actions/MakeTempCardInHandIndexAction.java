package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.effects.ShowCardAndToHandIndexEffect;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class MakeTempCardInHandIndexAction extends AbstractGameAction {
    private AbstractCard c;
    private static final float PADDING;
    private boolean sameUUID;

    private int index;


    public MakeTempCardInHandIndexAction(AbstractCard card, int amount, int index) {
        this.sameUUID = false;
        this.index = index;
        UnlockTracker.markCardAsSeen(card.cardID);
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.c = card;
        if (this.c.type != AbstractCard.CardType.CURSE && this.c.type != AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower("MasterRealityPower")) {
            this.c.upgrade();
        }

    }

    public void update() {
        if (this.amount == 0) {
            this.isDone = true;
        } else {
            int discardAmount = 0;
            int handAmount = this.amount;
            if (this.amount + AbstractDungeon.player.hand.size() > 10) {
                AbstractDungeon.player.createHandIsFullDialog();
                discardAmount = this.amount + AbstractDungeon.player.hand.size() - 10;
                handAmount -= discardAmount;
            }

            this.addToHand(handAmount);
            if (this.amount > 0) {
                this.addToTop(new WaitAction(0.8F));
            }

            this.isDone = true;
        }
    }

    private void addToHand(int handAmt) {
        int i;
        switch (this.amount) {
            case 0:
                break;
            case 1:
                if (handAmt == 1) {
                    AbstractDungeon.effectList.add(new ShowCardAndToHandIndexEffect(this.makeNewCard(), (float) Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F, index));
                }
                break;
            case 2:
                if (handAmt == 1) {
                    AbstractDungeon.effectList.add(new ShowCardAndToHandIndexEffect(this.makeNewCard(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH * 0.5F), (float)Settings.HEIGHT / 2.0F, index));
                } else if (handAmt == 2) {
                    AbstractDungeon.effectList.add(new ShowCardAndToHandIndexEffect(this.makeNewCard(), (float)Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT / 2.0F, index));
                    AbstractDungeon.effectList.add(new ShowCardAndToHandIndexEffect(this.makeNewCard(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F, index));
                }
                break;
            case 3:
                if (handAmt == 1) {
                    AbstractDungeon.effectList.add(new ShowCardAndToHandIndexEffect(this.makeNewCard(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F, index));
                } else if (handAmt == 2) {
                    AbstractDungeon.effectList.add(new ShowCardAndToHandIndexEffect(this.makeNewCard(), (float)Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, (float)Settings.HEIGHT / 2.0F, index));
                    AbstractDungeon.effectList.add(new ShowCardAndToHandIndexEffect(this.makeNewCard(), (float)Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), (float)Settings.HEIGHT / 2.0F, index));
                } else if (handAmt == 3) {
                    for(i = 0; i < this.amount; ++i) {
                        AbstractDungeon.effectList.add(new ShowCardAndToHandIndexEffect(this.makeNewCard(), index));
                    }
                }
                break;
            default:
                for(i = 0; i < handAmt; ++i) {
                    AbstractDungeon.effectList.add(new ShowCardAndToHandIndexEffect(this.makeNewCard(), MathUtils.random((float)Settings.WIDTH * 0.2F, (float)Settings.WIDTH * 0.8F), MathUtils.random((float)Settings.HEIGHT * 0.3F, (float)Settings.HEIGHT * 0.7F), index));
                }
        }
    }

    private AbstractCard makeNewCard() {
        return this.sameUUID ? this.c.makeSameInstanceOf() : this.c.makeStatEquivalentCopy();
    }

    static {
        PADDING = 25.0F * Settings.scale;
    }
}
