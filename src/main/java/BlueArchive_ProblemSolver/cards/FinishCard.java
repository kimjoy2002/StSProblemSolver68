package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.actions.FinishAction;
import BlueArchive_ProblemSolver.actions.RushOnOffAction;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ProperGreetingPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

abstract public class FinishCard extends AbstractDynamicCard {
    public FinishCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new FinishAction(this, p, m, getFinishString()));
    }
    String getFinishString(){return name;};

    public abstract void onFinish(AbstractPlayer p, AbstractMonster m);
    public void triggerOnGlowCheck() {
        this.glowColor = BlueArchive_ProblemSolver.cards.AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        //if (false) {
        //    this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        //}
    }
}
