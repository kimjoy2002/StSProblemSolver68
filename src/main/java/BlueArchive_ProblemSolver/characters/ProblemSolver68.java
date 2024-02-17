package BlueArchive_ProblemSolver.characters;

import BlueArchive_ProblemSolver.save.ProblemSolverSave;
import BlueArchive_ProblemSolver.actions.ApplyPowerToAllAllyAction;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;

import static BlueArchive_ProblemSolver.DefaultMod.makeCharPath;

public abstract class ProblemSolver68 extends CustomPlayer {
    public static final float PROBLEM_SOLVER_WIDTH = 120.0F;
    public static final float PROBLEM_SOLVER_HEIGHT = 290.0F;
    public static final float PROBLEM_SOLVER_INTERVAL = 200.0F;
    public static final ArrayList<ProblemSolver68> problemSolverPlayer = new ArrayList<>();
    private static final Texture SELECTED_IMG = TextureLoader.getTexture(makeCharPath("select.png"));
    public static ProblemSolverSave savedata;
    private float holdProgress = 0.0f;
    private boolean enabled = false;
    private boolean isDisabled = false;

    public ProblemSolver68(String name, AbstractPlayer.PlayerClass playerClass, String[] orbTextures, String orbVfxPath, float[] layerSpeeds, AbstractAnimation animation) {
        super(name, playerClass, orbTextures, orbVfxPath, layerSpeeds, animation);
    }
    public static void init() {
        problemSolverPlayer.clear();
        savedata = new ProblemSolverSave();
        BaseMod.addSaveField("BlueArchive_ProblemSolver:Character",savedata);
    }

    public static Aru.ProblemSolver68Type stringToEnum(String name) {
        switch(name) {
            case "Aru":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU;
            case "Mutsuki":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI;
            case "Kayoko":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO;
            case "Haruka":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA;
            default:
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_NONE;
        }
    }
    public static String enumToString(Aru.ProblemSolver68Type type) {
        switch(type) {
            case PROBLEM_SOLVER_68_ARU:
                return "Aru";
            case PROBLEM_SOLVER_68_MUTSUKI:
                return "Mutsuki";
            case PROBLEM_SOLVER_68_KAYOKO:
                return "Kayoko";
            case PROBLEM_SOLVER_68_HARUKA:
                return "Haruka";
            default:
                return "";
        }
    }

    public static void addCharacter(Aru.ProblemSolver68Type type) {
        addCharacter(type, -2, -2);
    }
    public static void addCharacter(Aru.ProblemSolver68Type type, int hp_, int max_hp_) {
        if(!(AbstractDungeon.player instanceof Aru)) {
            return;
        }
        String name = enumToString(type);

        if (name.isEmpty()) {
            return;
        } else if(problemSolverPlayer.isEmpty()) {
            ((Aru)AbstractDungeon.player).setSolverType(type);
            AbstractDungeon.player.name = name;
            if(hp_ != -2) {
                AbstractDungeon.player.currentHealth = hp_;
            }
            if(max_hp_ != -2) {
                AbstractDungeon.player.maxHealth = max_hp_;
            }
            problemSolverPlayer.add(((Aru)AbstractDungeon.player));
            savedata.addCharacter(name, AbstractDungeon.player.currentHealth, AbstractDungeon.player.maxHealth);
        } else {
            float offset_ = PROBLEM_SOLVER_INTERVAL*Settings.scale/2;
            for( ProblemSolver68 p_ : problemSolverPlayer) {
                p_.movePosition_(p_.drawX-offset_, p_.drawY);
            }
            Aru p = new Aru(name, Aru.Enums.PROBLEM_SOLVER, type);
            AbstractPlayer main = AbstractDungeon.player;
            p.hand = main.hand;
            p.masterDeck = main.masterDeck;
            p.drawPile = main.drawPile;
            p.discardPile = main.discardPile;
            p.exhaustPile = main.exhaustPile;
            p.limbo = main.limbo;
            p.relics = main.relics;
            p.potionSlots = main.potionSlots;
            p.potions = main.potions;
            p.energy = main.energy;
            p.hoveredCard = main.hoveredCard;
            p.toHover = main.toHover;
            p.cardInUse = main.cardInUse;
            p.movePosition_(p.drawX+offset_*problemSolverPlayer.size(), p.drawY);
            if(hp_ != -2) {
                p.currentHealth = hp_;
            }
            if(max_hp_ != -2) {
                p.maxHealth = max_hp_;
            }
            problemSolverPlayer.add(p);
            savedata.addCharacter(name, p.currentHealth, p.maxHealth);
        }
    }

    public static void afterLoad() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(AbstractDungeon.player != p){
                p.energy = AbstractDungeon.player.energy;
            }
        }
    }
    public void movePosition(float x, float y) {
        float offset_ = PROBLEM_SOLVER_INTERVAL*Settings.scale/2;
        int i = 0;
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.movePosition_(x-offset_*(problemSolverPlayer.size()-1-i), y);
            i+=2;
        }
    }
    public void movePosition_(float x, float y) {
        super.movePosition(x,y);
    }

    public void update() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.update_();
        }
    }
    public void update_() {
        super.update();
        updateForSwap();
    }

    public void renderHand(SpriteBatch sb) {
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p == AbstractDungeon.player) {
                p.renderHand_(sb);
            }
        }
    }

    public void renderHand_(SpriteBatch sb) {
        super.renderHand(sb);
    }

    public void render(SpriteBatch sb) {
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.render_(sb);
        }
    }
    public void render_(SpriteBatch sb) {
        super.render(sb);
        if(this == AbstractDungeon.player) {
            sb.setColor(Color.RED);
            sb.setColor(Color.WHITE);
            sb.draw(SELECTED_IMG, this.drawX - (float)this.SELECTED_IMG.getWidth() * Settings.scale / 2.0F, this.drawY + hb.height / 2.0F + (float)this.SELECTED_IMG.getHeight() * Settings.scale, (float)this.SELECTED_IMG.getWidth() * Settings.scale, (float)this.SELECTED_IMG.getHeight() * Settings.scale, 0, 0, SELECTED_IMG.getWidth(), SELECTED_IMG.getHeight(), false, false);
        }
    }

    public void renderPlayerBattleUi(SpriteBatch sb) {
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.renderPlayerBattleUi_(sb);
        }
    }

    public void renderPlayerBattleUi_(SpriteBatch sb) {
        super.renderPlayerBattleUi(sb);
    }

    public void preBattlePrep() {
        super.preBattlePrep();
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p != this) {
                p.powers.clear();
                p.isEndingTurn = false;
                p.healthBarUpdatedEvent();
            }
        }
    }
    public static void BattleStartEffectForProblemSolver68(AbstractPlayer exclude) {
        for (ProblemSolver68 p : problemSolverPlayer) {
            if (p != exclude) {
                p.showHealthBar();
            }
        }
    }

    //-----여기서부터 캐릭터 전환용 인풋 함수들---------
    public void updateForSwap() {
        updateHoldProgress();
        isDisabled = false;
        if (AbstractDungeon.isScreenUp || AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.inSingleTargetMode) {
            this.isDisabled = true;
        }

        if (AbstractDungeon.player.hoveredCard == null) {
            this.hb.update();
        }
        if (!Settings.USE_LONG_PRESS && InputHelper.justClickedLeft && this.hb.hovered && !this.isDisabled && !AbstractDungeon.isScreenUp) {
            this.hb.clickStarted = true;
            CardCrawlGame.sound.play("UI_CLICK_1");
        }

        if (this.holdProgress == 0.4F && !this.isDisabled && !AbstractDungeon.isScreenUp) {
            this.disable(true);
            this.holdProgress = 0.0F;
        }

        if ((!Settings.USE_LONG_PRESS || !Settings.isControllerMode && !isPressed())
                && (this.hb.clicked || (isJustPressed() || CInputActionSet.proceed.isJustPressed()) && !this.isDisabled && this.enabled)) {
            this.hb.clicked = false;
            if (!this.isDisabled && !AbstractDungeon.isScreenUp) {
                this.disable(true);
            }
        }
    }
    public int getKeyCode() {
        return 0; //TODO
    }
    public boolean isJustPressed() {
        return Gdx.input.isKeyJustPressed(getKeyCode());
    }

    public boolean isPressed() {
        return Gdx.input.isKeyPressed(getKeyCode());
    }

    private void updateHoldProgress() {
        if (!Settings.USE_LONG_PRESS || !Settings.isControllerMode && !InputHelper.isMouseDown) {
            this.holdProgress -= Gdx.graphics.getDeltaTime();
            if (this.holdProgress < 0.0F) {
                this.holdProgress = 0.0F;
            }

        } else {
            if ((this.hb.hovered && InputHelper.isMouseDown) && !this.isDisabled && this.enabled) {
                this.holdProgress += Gdx.graphics.getDeltaTime();
                if (this.holdProgress > 0.4F) {
                    this.holdProgress = 0.4F;
                }
            } else {
                this.holdProgress -= Gdx.graphics.getDeltaTime();
                if (this.holdProgress < 0.0F) {
                    this.holdProgress = 0.0F;
                }
            }

        }
    }
    public void enable() {
        this.enabled = true;
    }

    public boolean ableUse() {
        if(true) {
            return true;
        }
        return false;
    }

    public void disable(boolean isEnemyTurn) {
        AbstractDungeon.actionManager.addToBottom(new ChangeCharacterAction(this));
    }
    //-----여기서부터 인풋 함수끝---------

}
