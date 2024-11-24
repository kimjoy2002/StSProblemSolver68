package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.characters.Aru;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;
import static BlueArchive_ProblemSolver.targeting.AllyTargeting.CAN_ALLY_TARGETING;
import static com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting.getTarget;

public class Obstinate extends AbstractDynamicCard {
    public static final String ID = DefaultMod.makeID(Obstinate.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Obstinate.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CAN_ALLY_TARGETING;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int UPGRADE_BLOCK = 3;

    public Obstinate() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature c = getTarget(this);
        if(!(c instanceof AbstractPlayer)) {
            c = p!=null?p: AbstractDungeon.player;
        }

        if(c != AbstractDungeon.player) {

            AbstractPlayer target_p = (AbstractPlayer)c;
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    if (p.currentBlock != 0) {
                        int bamount = p.currentBlock;
                        p.loseBlock(bamount);
                        AbstractDungeon.actionManager.addToTop(new GainBlockAction(target_p, bamount));
                    }

                }
            });
        }
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction((AbstractPlayer)c, block));
        AbstractDungeon.actionManager.addToBottom(new ChangeCharacterAction((AbstractPlayer)c, false, false, true));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            initializeDescription();
        }
    }
}
