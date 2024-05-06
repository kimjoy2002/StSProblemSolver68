package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.Collections;

public class CreateTacticalChallengeAction extends AbstractGameAction {
    boolean upgrade;
    public CreateTacticalChallengeAction(boolean upgrade) {
        this.setValues(this.target, this.source, 0);
        this.upgrade = upgrade;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();
        cards.add(new TacticalChallengeAru());
        cards.add(new TacticalChallengeMutsuki());
        cards.add(new TacticalChallengeKayoko());
        cards.add(new TacticalChallengeHaruka());
        Collections.shuffle(cards, new java.util.Random(AbstractDungeon.miscRng.randomLong()));

        if(!upgrade) {
            cards.remove(3);
        }


        for(AbstractCard card : cards) {
            card.setCostForTurn(0);

            card.current_x = -1000.0F * Settings.xScale;
            if (AbstractDungeon.player.hand.size() < 10) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            } else {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(card, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            }
        }


        this.isDone = true;
    }
}
