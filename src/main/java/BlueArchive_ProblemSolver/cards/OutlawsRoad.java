package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.OutlawsRockPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class OutlawsRoad extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(OutlawsRoad.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("OutlawsRoad.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    public static final int MAGIC = 1;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public OutlawsRoad() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        //baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(upgraded?2:1));
        this.addToBot(new ChangeStanceAction("Wrath"));
        //AbstractDungeon.actionManager.addToBottom(new ChangeCharacterAction(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU, true));
        //AbstractDungeon.actionManager.addToBottom(new DrawCardAction(magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            //upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            //this.upgradeBaseCost(0);
            initializeDescription();
        }
    }

//    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
//        if(!ProblemSolver68.isLive(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU)) {
//            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
//            return false;
//        }
//        return super.canUse(p, m);
//    }
}
