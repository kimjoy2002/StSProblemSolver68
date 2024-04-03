package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.DiscardLeftAction;
import BlueArchive_ProblemSolver.characters.Aru;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;
import static com.megacrit.cardcrawl.actions.common.DrawCardAction.drawnCards;

public class PokerFace extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(PokerFace.class.getSimpleName());
    public static final String IMG = makeCardPath("PokerFace.png");


    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public static final int MAGIC = 2;

    public PokerFace() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        this.addToBot(new DrawCardAction(magicNumber, new AbstractGameAction() {
            @Override
            public void update() {
                int current_cost = -1;
                CardGroup highCosts = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for(AbstractCard card : drawnCards) {
                    int cost_ = card.cost;
                    if(card.freeToPlayOnce || cost_ < 0)
                        cost_ = 0;
                    if(current_cost < cost_) {
                        current_cost = cost_;
                        highCosts.clear();
                    }
                    if(current_cost == cost_) {
                        highCosts.addToRandomSpot(card);
                    }
                }
                if(highCosts.size()> 0) {
                    AbstractCard card_ = highCosts.getTopCard();
                    this.addToBot(new DiscardSpecificCardAction(card_));
                }
                this.isDone = true;
            }
        }));
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
