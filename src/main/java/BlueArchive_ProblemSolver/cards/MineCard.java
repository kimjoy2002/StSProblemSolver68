package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.actions.DelayAction;
import BlueArchive_ProblemSolver.actions.EvilDeedsAction;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.OnEvilDeedsPower;
import BlueArchive_ProblemSolver.powers.OnMinePower;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static BlueArchive_ProblemSolver.patches.GameActionManagerPatch.evildeedThisTurn;
import static java.lang.Math.abs;

abstract public class MineCard extends AbstractDynamicCard {

    public MineCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if(AbstractDungeon.player.hand.contains(c)) {
            int index_ = AbstractDungeon.player.hand.group.indexOf(c);
            int index2_ = AbstractDungeon.player.hand.group.indexOf(this);
            if(index2_ >= 0 && index_ >= 0 && abs(index_ - index2_)<=1) {
                if(onMine(c)) {
                    if(AbstractDungeon.player instanceof ProblemSolver68) {
                        for(AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                            for(AbstractPower p : ps.powers) {
                                if(p instanceof OnMinePower) {
                                    ((OnMinePower)p).onMine(c);
                                }
                            }
                        }
                    } else {
                        for(AbstractPower p : AbstractDungeon.player.powers) {
                            if(p instanceof OnMinePower) {
                                ((OnMinePower)p).onMine(c);
                            }
                        }
                    }
                }
            }
        }
    }

    public abstract boolean onMine(AbstractCard c);

}
