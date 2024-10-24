package BlueArchive_ProblemSolver.cardmodifiers;

import BlueArchive_ProblemSolver.cards.PokerFace;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class PokerfaceModifier  extends AbstractCardModifier {
    ArrayList<AbstractCard> others;

    public PokerfaceModifier(ArrayList<AbstractCard> others) {
        this.others = others;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        String temp = rawDescription;
        temp += " NL ";
        boolean first = true;
        for(AbstractCard card_ : others) {
            if(card_ != card) {
                if(first) {
                    first = false;
                } else {
                    temp += ", ";
                }
                temp += "*" + card_.name.replace(" ", " *");
            }
        }
        temp += PokerFace.cardStrings.EXTENDED_DESCRIPTION[0];
        return temp;
    }

    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    public boolean removeAtEndOfTurn(AbstractCard card) {
        return true;
    }

    public void onOtherCardPlayed(AbstractCard card, AbstractCard otherCard, CardGroup group) {
        if(group == AbstractDungeon.player.hand) {
            if(others.contains(otherCard)){
                AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(card));
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new PokerfaceModifier(others);
    }
}
