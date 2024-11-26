package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.powers.PowerForSubPatch;
import BlueArchive_ProblemSolver.powers.*;
import BlueArchive_ProblemSolver.relics.RadioTransceiverRelic;
import BlueArchive_ProblemSolver.ui.ProblemSolverTutorial;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.SilentGainPowerEffect;

import java.util.AbstractList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static BlueArchive_ProblemSolver.DefaultMod.*;
import static BlueArchive_ProblemSolver.actions.CleanCharacterAction.CleanCharacter;
import static BlueArchive_ProblemSolver.actions.ImpAction.resetImp;

public class ChangeCharacterAction extends AbstractGameAction {
    AbstractPlayer targetPlayer;
    boolean manual;
    boolean force = false;

    boolean unwelcome = false;
    boolean moving = false;
    boolean to_front = false;
    Aru.ProblemSolver68Type type = Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_NONE;
    public ChangeCharacterAction(AbstractPlayer targetPlayer) {
        this.targetPlayer = targetPlayer;
        this.duration = Settings.ACTION_DUR_FAST;
        this.manual = false;
    }
    public ChangeCharacterAction(AbstractPlayer targetPlayer, boolean manual) {
        this.targetPlayer = targetPlayer;
        this.duration = Settings.ACTION_DUR_FAST;
        this.manual = manual;
    }
    public ChangeCharacterAction(AbstractPlayer targetPlayer, boolean manual, boolean force) {
        this.targetPlayer = targetPlayer;
        this.duration = Settings.ACTION_DUR_FAST;
        this.manual = manual;
        this.force = force;
    }
    public ChangeCharacterAction(AbstractPlayer targetPlayer, boolean manual, boolean force, boolean to_front) {
        this.targetPlayer = targetPlayer;
        this.duration = Settings.ACTION_DUR_FAST;
        this.manual = manual;
        this.force = force;
        this.moving = true;
        this.to_front = to_front;
    }
    public ChangeCharacterAction(Aru.ProblemSolver68Type type, boolean to_front) {
        this.type = type;
        this.duration = Settings.ACTION_DUR_FAST;
        this.manual = false;
        this.moving = true;
        this.to_front = to_front;
    }
    public ChangeCharacterAction() {
        this.targetPlayer = null;
        this.duration = Settings.ACTION_DUR_FAST;
        this.manual = false;
    }


    public void onMovingCharacter (AbstractPlayer p) {
        if(manual) {
            return;
        }
        for(AbstractPower power : p.powers) {
            if(power instanceof OnFrontPower) {
                ((OnFrontPower)power).OnMoving();
            }
        }
    }
    public void onFrontCharacter (AbstractPlayer p) {
        if(manual) {
            return;
        }
        for(AbstractPower power : p.powers) {
            if(power instanceof OnFrontPower) {
                ((OnFrontPower)power).OnFront();
            }
        }
    }
    public void movingCharacter () {
        CleanCharacter();
        int prev_index = ProblemSolver68.problemSolverPlayer.lastIndexOf(targetPlayer);

        Set<AbstractPlayer> movingChar = new HashSet<AbstractPlayer>();

        if(to_front) {
            int next_index = ProblemSolver68.problemSolverPlayer.size() - 1;

            if(prev_index != -1 && next_index != -1 && prev_index != next_index) {
                for(int start_  = prev_index; start_ < next_index; start_++) {
                    AbstractPlayer next_charecter = ProblemSolver68.problemSolverPlayer.get(start_+1);
                    float temp_x = targetPlayer.drawX;
                    float temp_y = targetPlayer.drawY;
                    ((ProblemSolver68)targetPlayer).movePosition_(next_charecter.drawX, next_charecter.drawY, true);
                    ((ProblemSolver68)next_charecter).movePosition_(temp_x, temp_y, true);
                    Collections.swap(ProblemSolver68.problemSolverPlayer, start_, start_+1);

                    if(start_+1 == next_index) {
                        onFrontCharacter(targetPlayer);
                    }
                    movingChar.add(targetPlayer);
                    movingChar.add(next_charecter);
                }
            }
        } else {
            int next_index = 0;

            if(prev_index != -1 && prev_index != next_index) {
                for(int start_  = prev_index; start_ > next_index; start_--) {
                    AbstractPlayer next_charecter = ProblemSolver68.problemSolverPlayer.get(start_-1);
                    if(next_charecter.currentHealth <= 0)
                        return;
                    float temp_x = targetPlayer.drawX;
                    float temp_y = targetPlayer.drawY;
                    ((ProblemSolver68)targetPlayer).movePosition_(next_charecter.drawX, next_charecter.drawY, true);
                    ((ProblemSolver68)next_charecter).movePosition_(temp_x, temp_y, true);
                    Collections.swap(ProblemSolver68.problemSolverPlayer, start_, start_-1);


                    if(start_ == ProblemSolver68.problemSolverPlayer.size() - 1) {
                        onFrontCharacter(next_charecter);
                    }
                    movingChar.add(targetPlayer);
                    movingChar.add(next_charecter);
                }
            }
        }
        for(AbstractPlayer p : movingChar) {
            onMovingCharacter(p);
        }
    }



    public void changeCharacter () {

        AbstractPlayer temp = AbstractDungeon.player;
        AbstractPlayer changeCharacter = targetPlayer;
        if(changeCharacter == null) {
            if (type != Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_NONE) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps.solverType == type && ps.currentHealth > 0) {
                        changeCharacter = ps;
                        targetPlayer = ps;
                        //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(changeCharacter, changeCharacter, new CannotChangedPower(changeCharacter)));
                        break;
                    }
                }
            }
            else {
                changeCharacter = ProblemSolver68.getRandomMember(temp, true);
                targetPlayer = changeCharacter;
                if(changeCharacter == null && unwelcome) {
                    this.addToBot(new PressEndTurnButtonAction());
                    return;
                }
            }
            if(changeCharacter == null || changeCharacter == temp) {
                return;
            }

        } else if(moving && !to_front) {
            //뒤로 옮기는 경우 다음 앞으로 오게되는 캐릭터가 선택되어야함
            for (AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                if(ps.currentHealth > 0 && ps != targetPlayer) {
                    changeCharacter = ps;
                }

            }

            if(changeCharacter == null || changeCharacter == temp) {
                movingCharacter ();
                return;
            }
        }
        if(AbstractDungeon.player.endTurnQueued) {
            changeCharacter.endTurnQueued = true;
            AbstractDungeon.player.endTurnQueued = false;
        }
        AbstractDungeon.player.cardInUse = null;
        AbstractDungeon.player = changeCharacter;
        AbstractDungeon.player.gold = temp.gold;
        AbstractDungeon.player.displayGold = temp.displayGold;
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
        AbstractDungeon.onModifyPower();
        if(!manual) {
            PowerForSubPatch.prevCharacter = AbstractDungeon.player;
        }

        for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (action instanceof DamageAction) {
                DamageAction damageAction = (DamageAction)action;
                if(damageAction.target == temp) {
                    damageAction.target = AbstractDungeon.player;
                }
            }
        }

        if(moving && targetPlayer instanceof ProblemSolver68) {
            movingCharacter ();
        }
    }

    public void update() {
        this.isDone = true;
        if(unwelcome) {
            if(!(AbstractDungeon.player instanceof ProblemSolver68)) {
                return;
            }
            resetImp();
            return;
        }
        if(!(AbstractDungeon.player instanceof ProblemSolver68) || (targetPlayer == AbstractDungeon.player && (!moving || to_front))) {
            return;
        }
        if (AbstractDungeon.player.hasPower(CannotChangedPower.POWER_ID) && !force) {
            AbstractDungeon.player.getPower(CannotChangedPower.POWER_ID).flashWithoutSound();
            return;
        }
        changeCharacter ();
        resetImp();
        if (AbstractDungeon.player instanceof ProblemSolver68
                && ProblemSolver68.getFrontMember() != null) {
            AbstractPlayer front = ProblemSolver68.getFrontMember();
            if(front.hasPower(CaliforniaGurlsPower.POWER_ID)) {
                this.addToBot(new RemoveSpecificPowerAction(front, front, CaliforniaGurlsPower.POWER_ID));
            }
        }
    }
}
