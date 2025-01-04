package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddCharacterAction;
import BlueArchive_ProblemSolver.actions.FinishAction;
import BlueArchive_ProblemSolver.actions.KeepEnergyAction;
import BlueArchive_ProblemSolver.characters.Aru;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;

import java.util.ArrayList;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class ChooseDraw extends FinishCard {
    public static final String ID = DefaultMod.makeID(ChooseDraw.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("ChooseDraw.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = -2;
    public ChooseDraw(){
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = Assault.MAGIC;
        baseSecondMagicNumber = secondMagicNumber = Assault.MAGIC2;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public String getFinishString(){
        return Assault.cardStrings.EXTENDED_DESCRIPTION[0];
    };
    @Override
    public ArrayList<AbstractGameAction> onFinish(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractGameAction> temp = new ArrayList<AbstractGameAction>();
        temp.add(new DrawCardAction(magicNumber));
        temp.add(new ApplyPowerAction(p, p, new EquilibriumPower(p, this.secondMagicNumber), this.secondMagicNumber));
        return temp;
    }

    @Override
    public void onChoseThisOption() {
        super.use(AbstractDungeon.player, null);
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

}
