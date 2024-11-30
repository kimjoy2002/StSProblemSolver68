package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.characters.Aru;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;
import static BlueArchive_ProblemSolver.targeting.AllyTargeting.CAN_ALLY_TARGETING;
import static com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting.getTarget;

public class Lure extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(Lure.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Lure.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CAN_ALLY_TARGETING;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 0;


    public Lure() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature c = getTarget(this);
        if(!(c instanceof AbstractPlayer)) {
            c = p!=null?p:AbstractDungeon.player;
        }
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, 1, false));
        AbstractDungeon.actionManager.addToBottom(new ChangeCharacterAction((AbstractPlayer)c, false, false, true));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
