package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.SideDeckAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.powers.FearPower;
import BlueArchive_ProblemSolver.save.SideDeckSave;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Random;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class SideDeck extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(SideDeck.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("SideDeck.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private int viewCount = 0;
    private int viewIndex = 0;
    private static final int COST = 1;
    private static final int VIEW_INTERVAL = 60;

    public SideDeck() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
        misc = new Random().nextInt();
    }
    public void changePreview() {
        viewCount++;
        if(viewCount > VIEW_INTERVAL) {
            viewCount = 0;
            viewIndex++;
            if(SideDeckSave.decks.containsKey(misc)) {
                int size_ = SideDeckSave.decks.get(misc).size();
                if(size_ > 0) {
                    this.cardsToPreview = SideDeckSave.decks.get(misc).get(viewIndex%size_);
                    return;
                }
            }
            this.cardsToPreview = null;
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new SideDeckAction(this));
    }

    @Override
    public void update() {
        changePreview();
        super.update();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(0);
            initializeDescription();
        }
    }
}
