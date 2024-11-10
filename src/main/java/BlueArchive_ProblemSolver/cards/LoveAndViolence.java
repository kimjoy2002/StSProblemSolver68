package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class LoveAndViolence extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(LoveAndViolence.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("LoveAndViolence.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final UIStrings uiStrings;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 2;
    private static final int DAMAGE = 7;

    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_DAM = 1;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public LoveAndViolence() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            private boolean start_turn = false;
            @Override
            public void update() {
                if (!start_turn) {
                    AbstractDungeon.handCardSelectScreen.open(uiStrings.TEXT[0], magicNumber, true, true);
                    this.addToBot(new WaitAction(0.25F));
                    start_turn = true;
                } else {
                    if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                        int multi_ =  0;
                        if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                            Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

                            while(var1.hasNext()) {
                                AbstractCard c = (AbstractCard)var1.next();
                                AbstractDungeon.player.hand.moveToDiscardPile(c);
                                GameActionManager.incrementDiscard(false);
                                c.triggerOnManualDiscard();
                                multi_++;
                            }
                        }
                        for(int i = 0; i < multi_; i++) {
                            AbstractDungeon.actionManager.addToBottom(
                                    new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                                            AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                        }

                        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                        this.isDone = true;
                    }
                }
            }
        });
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAM);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }


    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
    }
}
