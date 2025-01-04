package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.cardmodifiers.DamegeModifier;
import BlueArchive_ProblemSolver.cardmodifiers.PokerfaceModifier;
import BlueArchive_ProblemSolver.characters.Aru;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class Claymore extends MineCard {

    public static final String ID = DefaultMod.makeID(Claymore.class.getSimpleName());
    public static final String IMG = makeCardPath("Claymore.png");
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);


    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;

    private static final int DAMAGE = 4;

    private static final int MAGIC = 6;
    private static final int UPGRADE_PLUS_MAGIC = 2;

    public Claymore() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        this.isMultiDamage = true;
        baseMagicNumber = magicNumber = MAGIC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }
    public boolean onMine(AbstractCard c) {
        AbstractCard mine_card = this;
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if(CardModifierManager.hasModifier(mine_card, DamegeModifier.MODIFIER_ID)) {
                    DamegeModifier modifer = (DamegeModifier) CardModifierManager.getModifiers(mine_card, DamegeModifier.MODIFIER_ID).get(0);
                    modifer.damage += magicNumber;
                } else {
                    CardModifierManager.addModifier(mine_card, new DamegeModifier(magicNumber));
                }
                this.isDone = true;
            }
        });
        return true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }

}
