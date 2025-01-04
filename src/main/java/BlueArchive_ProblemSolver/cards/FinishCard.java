package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.actions.FinishAction;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FinishPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import javax.swing.*;
import java.util.ArrayList;

abstract public class FinishCard extends AbstractDynamicCard {
    public FinishCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        finishAfter(p, m, onFinish(p, m), makeFinishString());
    }


    public void finishAfter(AbstractPlayer p, AbstractMonster m, ArrayList<AbstractGameAction> actions, FinishPower.FinishString finishstring) {
        this.addToBot(new FinishAction(this, actions, finishstring, isDebuf()));
    }

    public boolean isDebuf() {
        return false;
    }

    FinishPower.FinishString makeFinishString() {
        String temp = getFinishString();
        AbstractCard card_ = makeCopy();
        card_.drawScale = 0.3f;
        if(upgraded)
            card_.upgrade();
        return new FinishPower.FinishString() {
            AbstractCard card = card_;
            String original = temp;
            @Override
            public String getFinishString(int amount) {
                String finishString = original;
                finishString = finishString.replaceAll("!M!", Integer.toString(magicNumber * amount));
                finishString = finishString.replaceAll("!M2!", Integer.toString(secondMagicNumber * amount));
                finishString = finishString.replaceAll("!B!", Integer.toString(block * amount));
                finishString = finishString.replaceAll("!D!", Integer.toString(damage * amount));
                return finishString;
            }

            @Override
            public AbstractCard getCard() {
                return card;
            }
        };
    }

    public abstract String getFinishString();

    public abstract ArrayList<AbstractGameAction> onFinish(AbstractPlayer p, AbstractMonster m);
    public void triggerOnGlowCheck() {
        this.glowColor = BlueArchive_ProblemSolver.cards.AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        //if (false) {
        //    this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        //}
    }
}
