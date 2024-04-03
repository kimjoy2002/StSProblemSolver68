package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class CreateTacticalChallengeAction extends AbstractGameAction {
    boolean upgrade;
    public CreateTacticalChallengeAction(boolean upgrade) {
        this.setValues(this.target, this.source, 0);
        this.upgrade = upgrade;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        cardGroup.addToRandomSpot(new TacticalChallengeAru());
        cardGroup.addToRandomSpot(new TacticalChallengeMutsuki());
        cardGroup.addToRandomSpot(new TacticalChallengeKayoko());
        cardGroup.addToRandomSpot(new TacticalChallengeHaruka());

        if(!upgrade) {
            cardGroup.removeTopCard();
        }


        while(cardGroup.size()>0) {
            AbstractCard card = cardGroup.getTopCard();
            card.setCostForTurn(0);

            card.current_x = -1000.0F * Settings.xScale;
            if (AbstractDungeon.player.hand.size() < 10) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            } else {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            }
            cardGroup.removeTopCard();
        }


        this.isDone = true;
    }
}
