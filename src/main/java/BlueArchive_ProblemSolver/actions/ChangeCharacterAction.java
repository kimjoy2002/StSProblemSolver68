package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.powers.PowerForSubPatch;
import BlueArchive_ProblemSolver.powers.CannotChangedPower;
import BlueArchive_ProblemSolver.powers.CannotSelectedPower;
import BlueArchive_ProblemSolver.powers.OutlawsRockPower;
import BlueArchive_ProblemSolver.powers.TauntPower;
import BlueArchive_ProblemSolver.relics.RadioTransceiverRelic;
import BlueArchive_ProblemSolver.ui.ProblemSolverTutorial;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.SilentGainPowerEffect;

import static BlueArchive_ProblemSolver.DefaultMod.*;

public class ChangeCharacterAction extends AbstractGameAction {
    AbstractPlayer targetPlayer;
    boolean manual;

    boolean unwelcome;
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
    public ChangeCharacterAction(Aru.ProblemSolver68Type type) {
        this.type = type;
        this.duration = Settings.ACTION_DUR_FAST;
        this.manual = false;
    }
    public ChangeCharacterAction() {
        this.targetPlayer = null;
        this.duration = Settings.ACTION_DUR_FAST;
        this.manual = false;
    }

    public ChangeCharacterAction(boolean unwelcome) {
        this.targetPlayer = null;
        this.duration = Settings.ACTION_DUR_FAST;
        this.manual = false;
        this.unwelcome = unwelcome;
    }

    public void changeCharacter () {

        AbstractPlayer temp = AbstractDungeon.player;
        if(targetPlayer == null) {
            if (type != Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_NONE) {
                for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if(ps.solverType == type) {
                        targetPlayer = ps;
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetPlayer, targetPlayer, new CannotChangedPower(targetPlayer)));
                        break;
                    }
                }
            }
            else {
                if (AbstractDungeon.player.hasPower(TauntPower.POWER_ID)) {
                    return;
                }
                targetPlayer = ProblemSolver68.getRandomMember(temp, true, unwelcome);
                if(targetPlayer == null && unwelcome) {
                    this.addToBot(new PressEndTurnButtonAction());
                    return;
                }
            }
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
        AbstractDungeon.player.displayGold = temp.displayGold;
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
        AbstractDungeon.onModifyPower();
        if(!manual) {
            PowerForSubPatch.prevCharacter = AbstractDungeon.player;
        }
        if(AbstractDungeon.player.hasRelic(RadioTransceiverRelic.ID)) {
            if(PowerForSubPatch.prevCharacter != AbstractDungeon.player) {
                AbstractDungeon.player.getRelic(RadioTransceiverRelic.ID).beginLongPulse();
            } else {
                AbstractDungeon.player.getRelic(RadioTransceiverRelic.ID).stopPulse();
            }
        }

        for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
            if (action instanceof DamageAction) {
                DamageAction damageAction = (DamageAction)action;
                if(damageAction.target == temp) {
                    damageAction.target = AbstractDungeon.player;
                }
            }
        }
    }

    public void update() {
        this.isDone = true;
        if(unwelcome) {
            if(!(AbstractDungeon.player instanceof ProblemSolver68)) {
                return;
            }
            if (AbstractDungeon.player.hasPower(CannotSelectedPower.POWER_ID)) {
                changeCharacter();
            }
            return;
        }
        if(!(AbstractDungeon.player instanceof ProblemSolver68) || targetPlayer == AbstractDungeon.player) {
            return;
        }
        if (AbstractDungeon.player.hasPower(CannotChangedPower.POWER_ID)) {
            AbstractDungeon.player.getPower(CannotChangedPower.POWER_ID).flashWithoutSound();
            return;
        }
        if(manual) {
            if (targetPlayer.hasPower(CannotSelectedPower.POWER_ID)) {
                targetPlayer.getPower(CannotSelectedPower.POWER_ID).flashWithoutSound();
                return;
            }
            if(AbstractDungeon.player.hasRelic(RadioTransceiverRelic.ID)) {
                if(AbstractDungeon.player.getRelic(RadioTransceiverRelic.ID).counter >= RadioTransceiverRelic.CHANGE_LIMIT) {
                    AbstractDungeon.player.getRelic(RadioTransceiverRelic.ID).flash();
                    return;
                }
            }
        }
        changeCharacter ();
    }
}
