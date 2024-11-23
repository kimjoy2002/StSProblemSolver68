package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CleanCharacterAction extends AbstractGameAction {
    boolean change_target;
    public CleanCharacterAction(boolean change_target) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 0);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.change_target = change_target;
    }


    public static void CleanCharacter() {
        boolean change_ = false;
        for(AbstractPlayer temp : ProblemSolver68.problemSolverPlayer){
            if(temp.currentHealth > 0) {
                change_ = true;
            }
            else if(change_) {
                List<Float> originalX = new ArrayList<>();
                List<Float> originalY = new ArrayList<>();
                for (AbstractPlayer player : ProblemSolver68.problemSolverPlayer) {
                    originalX.add(player.drawX);
                    originalY.add(player.drawY);
                }

                ProblemSolver68.problemSolverPlayer.sort((p1, p2) -> {
                    if (p1.currentHealth == 0 && p2.currentHealth > 0) return -1; // p1을 앞으로
                    if (p2.currentHealth == 0 && p1.currentHealth > 0) return 1;  // p2를 앞으로
                    return 0; // 기존 순서 유지
                });

                int i =0;
                for (AbstractPlayer player : ProblemSolver68.problemSolverPlayer) {
                    ((ProblemSolver68)player).movePosition_(originalX.get(i), originalY.get(i), true);
                    i++;
                }

                return;
            }
        }
    }


    @Override
    public void update() {
        this.isDone = true;
        if(!(AbstractDungeon.player instanceof ProblemSolver68)) {
            return;
        }
        CleanCharacter();
        if(change_target) {
            AbstractDungeon.actionManager.addToTop(new ChangeCharacterAction(ProblemSolver68.problemSolverPlayer.get(ProblemSolver68.problemSolverPlayer.size() - 1)));
        }

    }
}
