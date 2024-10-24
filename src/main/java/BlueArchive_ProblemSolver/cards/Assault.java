package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ApplyPowerToSpecificAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.powers.AssaultPower;
import BlueArchive_ProblemSolver.powers.FinishPower;
import BlueArchive_ProblemSolver.powers.ForAruSamaPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
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

public class Assault extends FinishCard {
    public static final String ID = DefaultMod.makeID(Assault.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Assault.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    public static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public Assault() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DrawCardAction(magicNumber, new AbstractGameAction() {
            @Override
            public void update() {
                ArrayList<AbstractGameAction> actions = new ArrayList<>();
                String finishstring = "";
                for(AbstractCard card : AbstractDungeon.player.hand.group) {
                    if(card instanceof FinishCard && !(card instanceof Assault)) {
                        ArrayList<AbstractGameAction> temp =((FinishCard)card).onFinish(p,m);
                        actions.addAll(temp);
                        finishstring += ((FinishCard)card).makeFinishString();
                        AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(card));
                    }
                }
                if(actions.isEmpty()) {
                    finishAfter(p, m, actions, makeFinishString());
                } else {
                    finishAfter(p, m, actions, finishstring);
                }
                this.isDone = true;
            }
        }));
    }

    @Override
    public String getFinishString() {
        return cardStrings.EXTENDED_DESCRIPTION[0];
    }

    @Override
    public ArrayList<AbstractGameAction> onFinish(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractGameAction> temp = new ArrayList<AbstractGameAction>();
        return temp;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
