package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.ui.ProblemSolverTutorial;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static BlueArchive_ProblemSolver.DefaultMod.*;

public class ChangeCharacterAction extends AbstractGameAction {
    AbstractPlayer targetPlayer;
    public ChangeCharacterAction(AbstractPlayer targetPlayer) {
        this.targetPlayer = targetPlayer;
        this.duration = Settings.ACTION_DUR_FAST;
    }
    public ChangeCharacterAction() {
        this.targetPlayer = null;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        this.isDone = true;
        if(!(AbstractDungeon.player instanceof ProblemSolver68) || targetPlayer == AbstractDungeon.player) {
            return;
        }
        AbstractPlayer temp = AbstractDungeon.player;
        if(targetPlayer == null) {
            targetPlayer = ProblemSolver68.getRandomMember(temp, true);
            if(targetPlayer == null || targetPlayer == temp) {
                return;
            }
        }
        if(AbstractDungeon.player.endTurnQueued) {
            targetPlayer.endTurnQueued = true;
            AbstractDungeon.player.endTurnQueued = false;
        }
        AbstractDungeon.player.cardInUse = null;
        AbstractDungeon.player = targetPlayer;
        AbstractDungeon.player.gold = temp.gold;
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
        AbstractDungeon.onModifyPower();
        for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (action instanceof DamageAction) {
                DamageAction damageAction = (DamageAction)action;
                if(damageAction.target == temp) {
                    damageAction.target = AbstractDungeon.player;
                }
            }
        }
    }
}
