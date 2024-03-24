package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddChellengeCountAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.patches.EnumPatch;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class TacticalChallengeMutsuki extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(TacticalChallengeMutsuki.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("TacticalChallengeMutsuki.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 3;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int MAGIC = 3;



    public TacticalChallengeMutsuki() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);

        this.tags.add(EnumPatch.TACTICAL_CHALLENGE);
    }

    public void didTacticalChallenge() {
        this.setCostForTurn(this.costForTurn - 1);
    }
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        this.setCostForTurn(this.cost - GameActionManagerPatch.tacticalChallengeCount);
    }

    public void atTurnStart() {
        this.resetAttributes();
        this.applyPowers();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0 ; i < magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                            AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        AbstractDungeon.actionManager.addToBottom(new AddChellengeCountAction());
    }


    public AbstractCard makeCopy() {
        AbstractCard tmp = new TacticalChallengeMutsuki();
        if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            this.setCostForTurn(this.cost - GameActionManagerPatch.tacticalChallengeCount);
        }

        return tmp;
    }
    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }
}
