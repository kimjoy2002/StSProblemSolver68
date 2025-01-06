package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.actions.EvilDeedsAction;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.OnEvilDeedsPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_ProblemSolver.patches.GameActionManagerPatch.evildeedThisTurn;

abstract public class EvilDeedsCard extends AbstractDynamicCard implements onAddToHandCards {

    public int require_evil = 0;
    public int evil = 0;
    public boolean limit = true;
    public EvilDeedsCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
        baseThirdMagicNumber = thirdMagicNumber = require_evil;
    }

    public void setRequireEvil(int require_evil) {
        this.require_evil = require_evil;
        baseThirdMagicNumber = thirdMagicNumber = require_evil;
    }

    public void updateRequireEvil(int amount) {
        this.require_evil = require_evil+amount;
        upgradeThirdMagicNumber(amount);
    }

    public abstract CardStrings getCardStrings();
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(evil >= require_evil) {
            onEvilDeeds(p, m);
            evildeedThisTurn++;
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for (AbstractPlayer player_ : ProblemSolver68.problemSolverPlayer) {
                    for (AbstractPower power_ : player_.powers) {
                        if(power_ instanceof OnEvilDeedsPower) {
                            ((OnEvilDeedsPower) power_).onEvilDeeds(this);
                        }
                    }
                }
            } else {
                for (AbstractPower power_ : AbstractDungeon.player.powers) {
                    if(power_ instanceof OnEvilDeedsPower) {
                        ((OnEvilDeedsPower) power_).onEvilDeeds(this);
                    }
                }
            }

            for(AbstractCard card : AbstractDungeon.player.hand.group) {
                if(card instanceof OnEvilDeedCards) {
                    ((OnEvilDeedCards) card).onEvilDeeds(this);
                }
            }
        }
    }

    public abstract void onEvilDeeds(AbstractPlayer p, AbstractMonster m);

    public void onAddToHand() {
        this.addToBot(new EvilDeedsAction(this));
        int gain_evil = 0;
        for(AbstractCard card : AbstractDungeon.player.hand.group) {
            if(card instanceof SnsuspiciousRumor) {
                gain_evil++;
            }
        }
        if(gain_evil>0) {
            this.addToBot(new EvilDeedsAction(this, gain_evil));
        }
        makeDescrption();
    }
    public void makeDescrption() {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hand.contains(this)) {
            this.rawDescription = (upgraded&&getCardStrings().UPGRADE_DESCRIPTION!=null)?getCardStrings().UPGRADE_DESCRIPTION:getCardStrings().DESCRIPTION;
        } else if(getCardStrings().UPGRADE_DESCRIPTION!=null) {
            this.rawDescription = upgraded?getCardStrings().EXTENDED_DESCRIPTION[1]:getCardStrings().EXTENDED_DESCRIPTION[0];
            this.rawDescription += getCardStrings().EXTENDED_DESCRIPTION[2] + evil + (upgraded?getCardStrings().EXTENDED_DESCRIPTION[4]:getCardStrings().EXTENDED_DESCRIPTION[3]);
        } else {
            this.rawDescription = getCardStrings().EXTENDED_DESCRIPTION[0];
            this.rawDescription += getCardStrings().EXTENDED_DESCRIPTION[1] + evil + getCardStrings().EXTENDED_DESCRIPTION[2];
        }
        initializeDescription();
    }


    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!(c instanceof Reputation)) {
            this.addToBot(new EvilDeedsAction(this, 1));
        }
    }

    public void onMoveToDiscard() {
        this.addToBot(new EvilDeedsAction(this));
        this.rawDescription = (upgraded&&getCardStrings().UPGRADE_DESCRIPTION!=null)?getCardStrings().UPGRADE_DESCRIPTION:getCardStrings().DESCRIPTION;
        this.initializeDescription();
    }
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractDynamicCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (evil >= require_evil) {
            this.glowColor = AbstractDynamicCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    public AbstractCard makeSameInstanceOf() {
        AbstractCard card = super.makeSameInstanceOf();
        if(card instanceof EvilDeedsCard) {
            ((EvilDeedsCard)card).evil = this.evil;
            ((EvilDeedsCard)card).require_evil = this.require_evil;
        }
        return card;
    }

}
