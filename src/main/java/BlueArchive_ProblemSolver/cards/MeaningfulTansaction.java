package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.effects.HealOnlyEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class MeaningfulTansaction extends EvilDeedsCard {
    public static final String ID = DefaultMod.makeID(MeaningfulTansaction.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("MeaningfulTansaction.png");


    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;

    public MeaningfulTansaction() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        setSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU);
        setRequireEvil(4);
        exhaust = true;
    }

    @Override
    public CardStrings getCardStrings() {
        return cardStrings;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new RemoveDebuffsAction(p));
        AbstractDungeon.effectsQueue.add(new HealOnlyEffect(p.hb.cX - p.animX, p.hb.cY));
        super.use(p, m);
    }

    @Override
    public void onEvilDeeds(AbstractPlayer p, AbstractMonster m) {
        for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
            if (ps != AbstractDungeon.player) {
                this.addToBot(new RemoveDebuffsAction(ps));
                AbstractDungeon.effectsQueue.add(new HealOnlyEffect(ps.hb.cX - ps.animX, ps.hb.cY));
            }
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            updateRequireEvil(-1);
            makeDescrption();
        }
    }
}
