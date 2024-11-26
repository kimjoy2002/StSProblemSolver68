package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.EvilDeedsAction;
import BlueArchive_ProblemSolver.characters.Aru;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;
import static java.lang.Math.abs;

public class SnsuspiciousRumor extends AbstractDynamicCard implements OnEvilDeedCards, onAddToHandCards {

    public static final String ID = DefaultMod.makeID(SnsuspiciousRumor.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("SnsuspiciousRumor.png");


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = -2;
    private static final int MAGIC = 1;

    public boolean gainEnergy = false;


    public SnsuspiciousRumor() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }
    public void onAddToHand() {
        gainEnergy = true;
        for(AbstractCard card : AbstractDungeon.player.hand.group) {
            if(card instanceof EvilDeedsCard) {
                this.addToBot(new EvilDeedsAction((EvilDeedsCard)card, 1));
            }
        }
    }
    public void onMoveToDiscard() {
        gainEnergy = true;
    }
    public void onEvilDeeds(AbstractCard c) {
        if(gainEnergy) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(upgraded?3:2));
            gainEnergy = false;
        }
    }
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
