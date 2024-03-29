package deprecate;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.cards.AbstractDynamicCard;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.relics.peroro.PeroroGoodsRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class WhyBeginners extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(WhyBeginners.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("WhyBeginners.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 3;


    public WhyBeginners() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for(AbstractRelic relic : AbstractDungeon.player.relics) {
            if(relic instanceof PeroroGoodsRelic) {
                count++;
            }
        }
        for(int i = 0; i < count; i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
    }

    public void applyPowers() {
        int count = 0;
        for(AbstractRelic relic : AbstractDungeon.player.relics) {
            if(relic instanceof PeroroGoodsRelic) {
                count++;
            }
        }
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] + count + cardStrings.EXTENDED_DESCRIPTION[1];
        this.initializeDescription();
    }
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
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
