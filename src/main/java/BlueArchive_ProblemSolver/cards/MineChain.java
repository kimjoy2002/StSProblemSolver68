package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.MakeTempCardInHandIndexAction;
import BlueArchive_ProblemSolver.characters.Aru;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class MineChain extends MineCard  {
    public static final String ID = DefaultMod.makeID(MineChain.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("MineChain.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 0;

    public MineChain() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
        this.cardsToPreview = new MutsukiMine();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MineChainPower(AbstractDungeon.player, 1)));
    }

    @Override
    public boolean onMine(AbstractCard c) {
        int index_target_ = AbstractDungeon.player.hand.group.indexOf(c);
        int index_ = AbstractDungeon.player.hand.group.indexOf(this);
        if(index_ >= 0 && index_ < AbstractDungeon.player.hand.group.size()) {
            if(index_target_ == -1 || index_target_ > index_) {
                //오른쪽에 있음
                this.addToTop(new MakeTempCardInHandIndexAction(cardsToPreview.makeStatEquivalentCopy(), 1, index_));
                this.addToTop(new MakeTempCardInHandIndexAction(cardsToPreview.makeStatEquivalentCopy(), 1, index_+1));
            } else {
                //왼쪽에 있음 (구)
                this.addToTop(new MakeTempCardInHandIndexAction(cardsToPreview.makeStatEquivalentCopy(), 1, index_-1));
                this.addToTop(new MakeTempCardInHandIndexAction(cardsToPreview.makeStatEquivalentCopy(), 1, index_));
            }
        }
        return true;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.cardsToPreview.upgrade();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
