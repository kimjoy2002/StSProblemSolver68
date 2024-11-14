package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.DiscardLeftAction;
import BlueArchive_ProblemSolver.cardmodifiers.PokerfaceModifier;
import BlueArchive_ProblemSolver.characters.Aru;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;
import static com.megacrit.cardcrawl.actions.common.DrawCardAction.drawnCards;

public class PokerFace extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(PokerFace.class.getSimpleName());
    public static final String IMG = makeCardPath("PokerFace.png");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);


    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_PLUS_BLOCK = 1;

    public static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;

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
                CardGroup highCosts = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for(AbstractCard card : drawnCards) {
                    ArrayList<AbstractCard> temp = new ArrayList<AbstractCard>();
                    temp.addAll(drawnCards);
                    CardModifierManager.addModifier(card, new PokerfaceModifier(temp));
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
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);

            initializeDescription();
        }
    }
}
