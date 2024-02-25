package BlueArchive_ProblemSolver.cards;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

abstract public class RushCard extends AbstractDynamicCard {

    boolean rushActive = false;
    public RushCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(rushActive) {
            onRush(p, m);
        }
    }

    abstract void onRush(AbstractPlayer p, AbstractMonster m);

    public void triggerWhenDrawn() {
        rushActive = true;
    }

    public void triggerOnOtherCardPlayed(AbstractCard c) {
        rushActive = false;
    }
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        rushActive = false;
    }
    public void triggerOnGlowCheck() {
        this.glowColor = BlueArchive_ProblemSolver.cards.AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (rushActive) {
            this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }
}
