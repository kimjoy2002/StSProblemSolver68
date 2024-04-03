package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class OldMaid extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(OldMaid.class.getSimpleName());
    public static final String IMG = makeCardPath("OldMaid.png");
    public static final String IMG_ARU = makeCardPath("OldMaidAru.png");
    public static final String IMG_MUTSUKI = makeCardPath("OldMaidMutsuki.png");
    public static final String IMG_KAYOKO = makeCardPath("OldMaidKayoko.png");
    public static final String IMG_HARUKA = makeCardPath("OldMaidHaruka.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
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
        this.addToBot(new AbstractGameAction() {
            public void update() {
                CardGroup drawPiles = AbstractDungeon.player.drawPile;
                AbstractCard card_ = null;
                Aru.ProblemSolver68Type type = Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_NONE;
                if(p instanceof ProblemSolver68) {
                    type = ((ProblemSolver68)AbstractDungeon.player).solverType;
                }

                if (type != Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_NONE) {
                    for(AbstractCard card : drawPiles.group) {
                        if(card instanceof AbstractDynamicCard) {
                            if(((AbstractDynamicCard)card).isSolverType(type)) {
                                card_ = card;
                                break;
                            }
                        }
                    }
                }
                if(card_ != null) {
                    AbstractDungeon.player.drawPile.removeCard(card_);
                    AbstractDungeon.player.drawPile.addToTop(card_);
                }
                this.addToBot(new DrawCardAction(magicNumber));
                this.isDone = true;
            }
        });
    }

    private static String getImgPath(AbstractPlayer p) {
        if(p instanceof ProblemSolver68) {

            switch(((ProblemSolver68)AbstractDungeon.player).solverType) {
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
        super.applyPowers();
        if(AbstractDungeon.player instanceof ProblemSolver68 && ProblemSolver68.isProblemSolver(((ProblemSolver68)AbstractDungeon.player).solverType)) {
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0] + cardStrings.EXTENDED_DESCRIPTION[1] + ProblemSolver68.getLocalizedName((ProblemSolver68)AbstractDungeon.player) + cardStrings.EXTENDED_DESCRIPTION[2] ;
        } else {
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        }
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
