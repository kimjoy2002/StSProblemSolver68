package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;
import static com.megacrit.cardcrawl.actions.common.DrawCardAction.drawnCards;

public class OneInTheChamber extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(OneInTheChamber.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("OneInTheChamber.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 3;


    public OneInTheChamber() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }
    @Override
    public void atStartOfCombat(){
        atTurnStart();
    }

    @Override
    public void atTurnStart() {
        OneInTheChamber card = this;
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction(){
            @Override
            public void update() {
                if(AbstractDungeon.player.discardPile.contains(card)) {
                    AbstractDungeon.player.discardPile.removeCard(card);
                    AbstractDungeon.player.discardPile.moveToHand(card);
                }
                if(AbstractDungeon.player.drawPile.contains(card)) {
                    AbstractDungeon.player.drawPile.removeCard(card);
                    AbstractDungeon.player.drawPile.moveToHand(card);
                }
                this.isDone = true;
            }
        });
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
