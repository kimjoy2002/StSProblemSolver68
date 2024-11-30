package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ImpAction;
import BlueArchive_ProblemSolver.characters.Aru;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class ImpishPrank extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(ImpishPrank.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("ImpishPrank.png");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    public static final int MAGIC = 1;

    public ImpishPrank() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            float start_duration = Settings.ACTION_DUR_XFAST;
            @Override
            public void update() {
                if(start_duration == Settings.ACTION_DUR_XFAST)  {

                    if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                        this.isDone = true;
                        return;
                    }
                    if (AbstractDungeon.player.hand.size() > 0) {
                        AbstractDungeon.handCardSelectScreen.open(uiStrings.TEXT[0], 99, true, true);
                        AbstractDungeon.player.hand.applyPowers();
                        this.start_duration -= Gdx.graphics.getDeltaTime();
                        return;
                    } else {
                        this.isDone = true;
                        return;
                    }
                }

                if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                    Iterator var4 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();
                    int count = 0;
                    while(var4.hasNext()) {
                        AbstractCard c = (AbstractCard)var4.next();
                        AbstractDungeon.player.hand.moveToDiscardPile(c);
                        c.triggerOnManualDiscard();
                        GameActionManager.incrementDiscard(false);
                        count++;
                    }
                    AbstractDungeon.actionManager.addToBottom(new ImpAction(count * magicNumber));

                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                }

                this.start_duration -= Gdx.graphics.getDeltaTime();
                if (this.start_duration < 0.0F) {
                    this.isDone = true;
                }
            }
        });
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
