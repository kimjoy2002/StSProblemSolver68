package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ApplyPowerToAllAllyAction;
import BlueArchive_ProblemSolver.actions.CorrectAdviceAction;
import BlueArchive_ProblemSolver.actions.GainBlockToAllAllyAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ChangeWhenHitPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashSet;
import java.util.Set;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class CorrectAdvice extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(CorrectAdvice.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("CorrectAdvice.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 0;
    public static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public Set<AbstractPlayer> used_character = new HashSet<>();

    public CorrectAdvice() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        isEthereal = true;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new CorrectAdviceAction(this, p));
    }

    public void makeDescrption() {
        this.rawDescription = cardStrings.DESCRIPTION;
        if(used_character.size() > 0) {
            boolean first = true;
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[1];
            for (AbstractPlayer p : used_character) {
                if (!first) {
                    this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[2];
                }
                if(p instanceof ProblemSolver68) {
                    this.rawDescription += ProblemSolver68.getLocalizedName((ProblemSolver68)p);
                } else {
                    this.rawDescription += p.getLocalizedCharacterName();
                }
                first = false;
            }
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[3];
        }
        initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            makeDescrption();
        }
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if(used_character.contains(p)){
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return super.canUse(p,m);
    }
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        if(card instanceof CorrectAdvice) {
            ((CorrectAdvice)card).used_character.addAll(this.used_character);
        }
        return card;
    }
}
