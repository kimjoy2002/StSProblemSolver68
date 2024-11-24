package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
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

public class OldMaid extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(OldMaid.class.getSimpleName());
    public static final String IMG = makeCardPath("OldMaid.png");
    public static final String IMG_ARU = makeCardPath("OldMaidAru.png");
    public static final String IMG_MUTSUKI = makeCardPath("OldMaidMutsuki.png");
    public static final String IMG_KAYOKO = makeCardPath("OldMaidKayoko.png");
    public static final String IMG_HARUKA = makeCardPath("OldMaidHaruka.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CAN_ALLY_TARGETING;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 1;
    private static final int MAGIC = 2;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public OldMaid() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCreature c = getTarget(this);
        if(!(c instanceof AbstractPlayer)) {
            c = p!=null?p:AbstractDungeon.player;
        }

        ProblemSolver68 ps68 = (c instanceof ProblemSolver68)?(ProblemSolver68)c:null;
        this.addToBot(new AbstractGameAction() {
            public void update() {
                CardGroup drawPiles = AbstractDungeon.player.drawPile;
                ArrayList<AbstractCard> cards_ = new ArrayList<AbstractCard>();
                Aru.ProblemSolver68Type type = Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_NONE;
                if(ps68 != null) {
                    type = ps68.solverType;
                }

                if (type != Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_NONE) {
                    for(AbstractCard card : drawPiles.group) {
                        if(card instanceof AbstractDynamicCard) {
                            if(((AbstractDynamicCard)card).isSolverType(type)) {
                                cards_.add(card);
                            }
                        }
                    }
                }
                if(cards_.size() > 0) {
                    for(int i = 0; i< magicNumber && i< cards_.size(); i++) {
                        AbstractDungeon.player.drawPile.removeCard(cards_.get(i));
                        AbstractDungeon.player.drawPile.addToTop(cards_.get(i));
                    }
                }
                this.addToBot(new DrawCardAction(magicNumber));
                this.isDone = true;
            }
        });
    }

    private static String getImgPath(AbstractPlayer p) {
        if(p instanceof ProblemSolver68) {

            switch(((ProblemSolver68)p).solverType) {
                case PROBLEM_SOLVER_68_ARU:
                    return IMG_ARU;
                case PROBLEM_SOLVER_68_MUTSUKI:
                    return IMG_MUTSUKI;
                case PROBLEM_SOLVER_68_KAYOKO:
                    return IMG_KAYOKO;
                case PROBLEM_SOLVER_68_HARUKA:
                    return IMG_HARUKA;
                default:
                    return IMG;
            }
        }
        else {
            return IMG;
        }
    }

    public void applyPowers() {
        AbstractCreature c = getTarget(this);
        if(!(c instanceof AbstractPlayer)) {
            c = AbstractDungeon.player;
        }

        ProblemSolver68 target = (c instanceof ProblemSolver68)?(ProblemSolver68)c:null;

        super.applyPowers();
        /*if(target != null && ProblemSolver68.isProblemSolver(target.solverType)) {
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0] + cardStrings.EXTENDED_DESCRIPTION[1] + ProblemSolver68.getLocalizedName(target) + cardStrings.EXTENDED_DESCRIPTION[2] ;
        } else {
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        }*/
        this.textureImg = getImgPath(AbstractDungeon.player);
        if (textureImg != null) {
            try {
                this.loadCardImage(textureImg);
            } catch (Throwable ignore) {

            }
        }
        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        if(textureImg != IMG) {
            this.textureImg = getImgPath(null);
            if (textureImg != null) {
                try {
                    this.loadCardImage(textureImg);
                } catch (Throwable ignore) {

                }
            }
        }
        this.initializeDescription();
    }


    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
