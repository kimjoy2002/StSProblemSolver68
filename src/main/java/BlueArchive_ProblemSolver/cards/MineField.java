package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.MakeTempCardInHandIndexAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.SelectScreenPatch;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class MineField extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(MineField.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("MineField.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 1;



    public MineField() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
        this.cardsToPreview = new MutsukiMine();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DiscardAction(p, p, 1, false) {
            boolean first = true;
            CardGroup prev_hand = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            boolean create = false;
            @Override
            public void update() {
                if(!create) {
                    if(first) {
                        for(AbstractCard c : AbstractDungeon.player.hand.group) {
                            prev_hand.addToTop(c);
                        }

                        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead() && p.hand.size() <= this.amount) {
                            this.addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy(), magicNumber));
                            create = true;
                        }
                        first = false;
                    }
                    if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved &&
                            AbstractDungeon.handCardSelectScreen.selectedCards.group.size() == 1) {
                        AbstractCard card = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);

                        int index_ = prev_hand.group.indexOf(card);
                        if(index_ >= 0) {
                            this.addToBot(new MakeTempCardInHandIndexAction(cardsToPreview.makeStatEquivalentCopy(), magicNumber, index_));
                        } else {
                            this.addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy(), magicNumber));
                        }
                        create = true;
                    }
                }
                super.update();

            }

        });

        //this.addToBot(new MakeTempCardInHandAction(cardsToPreview.makeStatEquivalentCopy(), this.magicNumber));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
