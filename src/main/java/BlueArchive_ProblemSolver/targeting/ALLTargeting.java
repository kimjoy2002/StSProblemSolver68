package BlueArchive_ProblemSolver.targeting;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Iterator;

public class ALLTargeting  extends TargetingHandler<AbstractCreature> {
    @SpireEnum
    public static AbstractCard.CardTarget CAN_ALL_TARGETING;
    private AbstractCreature hovered = null;

    public ALLTargeting() {
    }

    public static AbstractCreature getTarget(AbstractCard card) {
        return (AbstractCreature) CustomTargeting.getCardTarget(card);
    }

    public boolean hasTarget() {
        return this.hovered != null;
    }

    public void updateHovered() {
        this.hovered = null;
        AbstractDungeon.player.hb.update();
        if (AbstractDungeon.player instanceof ProblemSolver68) {
            for(AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                if(ps.currentHealth > 0 && ps.hb.hovered) {
                    this.hovered = ps;
                    return;
                }
            }
        }

        if (AbstractDungeon.player.hb.hovered) {
            this.hovered = AbstractDungeon.player;
        }
        else {
            Iterator var1 = AbstractDungeon.getMonsters().monsters.iterator();

            while(var1.hasNext()) {
                AbstractMonster m = (AbstractMonster)var1.next();
                if (!m.isDeadOrEscaped()) {
                    m.hb.update();
                    if (m.hb.hovered) {
                        this.hovered = m;
                        break;
                    }
                }
            }
        }

    }

    public AbstractCreature getHovered() {
        return this.hovered;
    }

    public void clearHovered() {
        this.hovered = null;
    }

    public void renderReticle(SpriteBatch sb) {
        if (this.hovered != null) {
            this.hovered.renderReticle(sb);
        }

    }

    public void setDefaultTarget() {
        this.hovered = AbstractDungeon.player;
    }

    public int getDefaultTargetX() {
        return (int)AbstractDungeon.player.hb.cX;
    }

    public int getDefaultTargetY() {
        return (int)AbstractDungeon.player.hb.cY;
    }

    public void updateKeyboardTarget() {
        int directionIndex = 0;
        ArrayList<AbstractPlayer> sortedPlayer = new ArrayList<>();
        if (AbstractDungeon.player instanceof ProblemSolver68) {
            for ( AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                if(ps.currentHealth > 0) {
                    sortedPlayer.add(ps);
                    sortedPlayer.sort(ProblemSolver68.sortByPosition);
                }
            }

        }
        else {
            sortedPlayer.add(AbstractDungeon.player);
        }
        if (InputActionSet.left.isJustPressed() || CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
            --directionIndex;
        }

        if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
            ++directionIndex;
        }

        if (directionIndex != 0) {
            ArrayList<AbstractMonster> sortedMonsters = new ArrayList(AbstractDungeon.getCurrRoom().monsters.monsters);
            sortedMonsters.removeIf(AbstractCreature::isDeadOrEscaped);
            AbstractCreature newTarget = null;
            if (sortedMonsters.isEmpty()) {
                if (this.hovered != null) {
                    return;
                }

                newTarget = AbstractDungeon.player;
            } else {
                sortedMonsters.sort(AbstractMonster.sortByHitbox);
                if (this.hovered == null) {
                    if (directionIndex == 1) {
                        newTarget = (AbstractCreature)sortedMonsters.get(0);
                    } else {
                        newTarget = sortedPlayer.get(sortedPlayer.size()-1);
                    }
                } else if (this.hovered instanceof AbstractPlayer) {
                    int currentTargetIndex = sortedPlayer.indexOf(this.hovered);
                    int newTargetIndex = currentTargetIndex + directionIndex;
                    if (newTargetIndex == -1) {
                        newTarget = (AbstractCreature)sortedMonsters.get(sortedMonsters.size() - 1);
                    }
                    else if(newTargetIndex >= sortedPlayer.size()) {
                        newTarget = (AbstractCreature)sortedMonsters.get(0);
                    }
                    else {
                        newTargetIndex = (newTargetIndex + sortedPlayer.size()) % sortedPlayer.size();
                        newTarget = (AbstractCreature)sortedPlayer.get(newTargetIndex);
                    }
                } else if (this.hovered instanceof AbstractMonster) {
                    int currentTargetIndex = sortedMonsters.indexOf(this.hovered);
                    int newTargetIndex = currentTargetIndex + directionIndex;
                    if (newTargetIndex == -1) {
                        newTarget = sortedPlayer.get(sortedPlayer.size()-1);
                    }
                    else if(newTargetIndex >= sortedMonsters.size()) {
                        newTarget = (AbstractCreature)sortedPlayer.get(0);
                    } else {
                        newTargetIndex = (newTargetIndex + sortedMonsters.size()) % sortedMonsters.size();
                        newTarget = (AbstractCreature)sortedMonsters.get(newTargetIndex);
                    }
                }
            }

            if (newTarget != null) {
                Hitbox target = ((AbstractCreature)newTarget).hb;
                Gdx.input.setCursorPosition((int)target.cX, Settings.HEIGHT - (int)target.cY);
                this.hovered = (AbstractCreature)newTarget;
                ReflectionHacks.setPrivate(AbstractDungeon.player, AbstractPlayer.class, "isUsingClickDragControl", true);
                AbstractDungeon.player.isDraggingCard = true;
            }

            if (this.hovered instanceof AbstractMonster && this.hovered.halfDead) {
                this.hovered = null;
            }
        }

    }
}
