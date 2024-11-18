package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.MineCard;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.ImpPower;
import BlueArchive_ProblemSolver.powers.OnMinePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MineAction extends AbstractGameAction {
    MineCard card;
    public MineAction(MineCard card) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.card = card;
    }

    public void update() {
        if(AbstractDungeon.player.hand.contains(card) || AbstractDungeon.player.discardPile.contains(card) || AbstractDungeon.player.drawPile.contains(card)){
            if(card.onMine(null)) {
                if(AbstractDungeon.player instanceof ProblemSolver68) {
                    for(AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                        for(AbstractPower p : ps.powers) {
                            if(p instanceof OnMinePower) {
                                ((OnMinePower)p).onMine(null);
                            }
                        }
                    }
                } else {
                    for(AbstractPower p : AbstractDungeon.player.powers) {
                        if(p instanceof OnMinePower) {
                            ((OnMinePower)p).onMine(null);
                        }
                    }
                }
            }
        }
        this.isDone = true;
    }
}
