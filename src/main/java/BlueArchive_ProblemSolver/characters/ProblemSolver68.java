package BlueArchive_ProblemSolver.characters;

import BlueArchive_ProblemSolver.actions.RemoveCharacterAction;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import BlueArchive_ProblemSolver.powers.OnDeadPower;
import BlueArchive_ProblemSolver.save.ProblemSolverSave;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

import java.util.*;

import static BlueArchive_ProblemSolver.DefaultMod.makeCharPath;
import static BlueArchive_ProblemSolver.characters.Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_NONE;

public abstract class ProblemSolver68 extends CustomPlayer {
    public static final float PROBLEM_SOLVER_WIDTH = 120.0F;
    public static final float PROBLEM_SOLVER_HEIGHT = 290.0F;
    public static final float PROBLEM_SOLVER_INTERVAL = 200.0F;
    public static final ArrayList<ProblemSolver68> problemSolverPlayer = new ArrayList<>();
    public static final ArrayList<ProblemSolver68> dyingPlayer = new ArrayList<>();
    private static final Texture SELECTED_IMG = TextureLoader.getTexture(makeCharPath("select.png"));
    public static ProblemSolverSave savedata;
    private float holdProgress = 0.0f;
    private boolean enabled = false;
    private boolean isDisabled = false;
    private boolean isHovered = false;
    public Aru.ProblemSolver68Type solverType = PROBLEM_SOLVER_68_NONE;
    public ProblemSolver68(String name, AbstractPlayer.PlayerClass playerClass, String[] orbTextures, String orbVfxPath, float[] layerSpeeds, AbstractAnimation animation) {
        super(name, playerClass, orbTextures, orbVfxPath, layerSpeeds, animation);
    }
    public static void init() {
        problemSolverPlayer.clear();
        dyingPlayer.clear();
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
            case "Helmet Gang":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HELMETGANG;
            case "Rabu":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_RABU;
            case "Saori":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_SAORI;
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
            case PROBLEM_SOLVER_68_HELMETGANG:
                return "Helmet Gang";
            case PROBLEM_SOLVER_68_RABU:
                return "Rabu";
            case PROBLEM_SOLVER_68_SAORI:
                return "Saori";
            default:
                return "";
        }
    }

    public static boolean isProblemSolver(Aru.ProblemSolver68Type type) {
        switch(type) {
            case PROBLEM_SOLVER_68_ARU:
            case PROBLEM_SOLVER_68_MUTSUKI:
            case PROBLEM_SOLVER_68_KAYOKO:
            case PROBLEM_SOLVER_68_HARUKA:
                return true;
            default:
                return false;
        }
    }

    public static void removeCharacter(ProblemSolver68 player) {
        float offset_ = PROBLEM_SOLVER_INTERVAL*Settings.scale/2;
        boolean isPrev = true;
        for( ProblemSolver68 p_ : problemSolverPlayer) {
            p_.movePosition_(p_.drawX+offset_*(isPrev?1:-1), p_.drawY);
            if(player == p_) {
                isPrev = false;
            }
        }
        problemSolverPlayer.remove(player);
    }
    public static void addCharacter(Aru.ProblemSolver68Type type) {
        addCharacter(type, -2, -2, false);
    }
    public static AbstractPlayer addCharacter(Aru.ProblemSolver68Type type, int hp_, int max_hp_, boolean inBattle) {
        if(!(AbstractDungeon.player instanceof Aru)) {
            return null;
        }
        String name = enumToString(type);

        if (name.isEmpty()) {
            return null;
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
            if(!inBattle) {
                savedata.addCharacter(name, AbstractDungeon.player.currentHealth, AbstractDungeon.player.maxHealth);
            } else {
                AbstractDungeon.player.isEndingTurn = false;
                AbstractDungeon.player.healthBarUpdatedEvent();
                AbstractDungeon.player.showHealthBar();
            }
            return AbstractDungeon.player;
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
            p.cardInUse = null; //불필요
            p.movePosition_(p.drawX+offset_*problemSolverPlayer.size(), p.drawY);
            if(hp_ != -2) {
                p.currentHealth = hp_;
            }
            if(max_hp_ != -2) {
                p.maxHealth = max_hp_;
            }
            if(!ProblemSolver68.isProblemSolver(p.solverType) && GameActionManagerPatch.increaseMercenaryMaxHP > 0) {
                p.increaseMaxHp(GameActionManagerPatch.increaseMercenaryMaxHP, false);
            }
            problemSolverPlayer.add(p);
            if(!inBattle) {
                savedata.addCharacter(name, p.currentHealth, p.maxHealth);
            }
            else {
                p.isEndingTurn = false;
                p.healthBarUpdatedEvent();
                p.showHealthBar();
            }
            return p;
        }
    }

    public static void afterLoad() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(AbstractDungeon.player != p){
                p.energy = AbstractDungeon.player.energy;
            }
        }
    }

    public static void loseBlockForOther() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(AbstractDungeon.player != p){
                if (!p.hasPower("Barricade") && !p.hasPower("Blur")) {
                    if (!p.hasRelic("Calipers")) {
                        p.loseBlock();
                    } else {
                        p.loseBlock(15);
                    }
                }
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
            p.update_(false);
        }
        AbstractList<AbstractPlayer> delete_ = new ArrayList<>();
        for(ProblemSolver68 p : dyingPlayer) {
            p.update_(true);
            if(p.escapeTimer == 0.0f){
                delete_.add(p);
            }
        }
        for(AbstractPlayer p : delete_) {
            problemSolverPlayer.remove(p);
        }

    }
    public void update_(boolean dying) {
        super.update();
        if(!dying) {
            updateForSwap();
        }
    }

    public void updateAnimations() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.updateAnimations_();
        }
        for(ProblemSolver68 p : dyingPlayer) {
            p.updateAnimations_();
        }
    }


    public void updateAnimations_() {
        super.updateAnimations();
    }


    public void applyStartOfTurnPowers() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.applyStartOfTurnPowers_();
        }
    }
    public void applyStartOfTurnPowers_(){
        super.applyStartOfTurnPowers();
    }
    public void applyTurnPowers() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.applyTurnPowers_();
        }
    }
    public void applyTurnPowers_(){
        super.applyTurnPowers();
    }

    public void applyStartOfTurnPostDrawPowers() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.applyStartOfTurnPostDrawPowers_();
        }
    }
    public void applyStartOfTurnPostDrawPowers_(){
        super.applyStartOfTurnPostDrawPowers();
    }

    public void applyEndOfTurnTriggers() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.applyEndOfTurnTriggers_();
        }
    }

    public void applyEndOfTurnTriggers_() {
        super.applyEndOfTurnTriggers();
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

    public void onVictory() {
        if (!isProblemSolver(solverType)) {
            for (ProblemSolver68 p : problemSolverPlayer) {
                if (isProblemSolver(p.solverType) && !this.isDying) {
                    AbstractDungeon.player = p;
                    break;
                }
            }
        }
        if (!this.isDying) {
            ArrayList<ProblemSolver68> removal = new ArrayList<>();
            for (ProblemSolver68 p : problemSolverPlayer) {
                TempHPField.tempHp.set(p, 0);
                if (AbstractDungeon.player == p) {
                    super.onVictory();
                } else if (isProblemSolver(p.solverType)) {
                    p.onVictorySub();
                } else {
                    removal.add(p);
                }
            }
            for(ProblemSolver68 p : removal) {
                removeCharacter(p);
            }
        }
        dyingPlayer.clear();
        AbstractDungeon.player = problemSolverPlayer.get(0);
    }
    public void onVictorySub() {
        heal(1);
        AbstractDungeon.effectsQueue.add(new HealEffect(hb.cX - animX, hb.cY,1));
        healthBarUpdatedEvent();
        if (!this.isDying) {
            Iterator var1 = this.blights.iterator();

            while(var1.hasNext()) {
                AbstractBlight b = (AbstractBlight)var1.next();
                b.onVictory();
            }

            var1 = this.powers.iterator();

            while(var1.hasNext()) {
                AbstractPower p = (AbstractPower)var1.next();
                p.onVictory();
            }
        }
    }

    public void render(SpriteBatch sb) {
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.render_(sb);
        }
        for(ProblemSolver68 p : dyingPlayer) {
            p.render_(sb);
        }
    }
    public void render_(SpriteBatch sb) {
        super.render(sb);
        if(this == AbstractDungeon.player && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            sb.setColor(Color.RED);
            sb.setColor(Color.WHITE);
            sb.draw(SELECTED_IMG, this.drawX - (float)this.SELECTED_IMG.getWidth() * Settings.scale / 2.0F, this.drawY + hb.height / 2.0F + (float)this.SELECTED_IMG.getHeight() * Settings.scale, (float)this.SELECTED_IMG.getWidth() * Settings.scale, (float)this.SELECTED_IMG.getHeight() * Settings.scale, 0, 0, SELECTED_IMG.getWidth(), SELECTED_IMG.getHeight(), false, false);
        }
        if(isHovered) {
            renderReticle(sb);
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
    public static void battleStartEffectForProblemSolver68(AbstractPlayer exclude) {
        for (ProblemSolver68 p : problemSolverPlayer) {
            if (p != exclude) {
                p.showHealthBar();
            }
        }
    }

    public static void endOfTurnPowersForProblemSolver68(AbstractPlayer exclude) {
        for (ProblemSolver68 p : problemSolverPlayer) {
            if (p != exclude && p.currentHealth > 0) {
                for(AbstractPower power : p.powers) {
                    power.atEndOfRound();
                }
            }
        }
    }


    public static boolean isAllDead() {
        if(!(AbstractDungeon.player instanceof ProblemSolver68))
            return true;
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p.currentHealth >= 1) {
                return false;
            }
        }
        return true;
    }
    public static int getDeadMember() {
        int i = 0;
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p.currentHealth < 1) {
                i++;
            }
        }
        return i;
    }

    public static int getMemberNum(boolean onlyProblemSolver, boolean onlyLiving) {
        if (onlyLiving || onlyProblemSolver) {
            int num = 0;
            for (ProblemSolver68 p : problemSolverPlayer) {
                if((!onlyLiving || p.currentHealth >= 1) &&
                  (!onlyProblemSolver || isProblemSolver(p.solverType))
                   ) {
                    num++;
                }
            }
            return num;
        }
        return problemSolverPlayer.size();
    }

    public static AbstractPlayer getRandomMember(AbstractPlayer exclude) {
        List<ProblemSolver68> ableCharacters = new ArrayList<>();
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p.currentHealth > 0 && exclude != p) {
                ableCharacters.add(p);
            }
        }
        if(!ableCharacters.isEmpty()) {
            Collections.shuffle(ableCharacters, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
            return ableCharacters.get(0);
        }
        return null;
    }


    public static AbstractPlayer getRandomDeadMember() {
        List<ProblemSolver68> ableCharacters = new ArrayList<>();
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p.currentHealth <= 0 && isProblemSolver(p.solverType)) {
                ableCharacters.add(p);
            }
        }
        if(!ableCharacters.isEmpty()) {
            Collections.shuffle(ableCharacters, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
            return ableCharacters.get(0);
        }
        return null;
    }

    public static void onDead(AbstractPlayer dead) {
        for (ProblemSolver68 p : problemSolverPlayer) {
            for(AbstractPower power_ : p.powers) {
                if(power_ instanceof OnDeadPower) {
                    ((OnDeadPower)power_).onDead(dead);
                }
            }
        }
    }
    public static void changeToRandomCharacter() {
        AbstractPlayer p = getRandomMember(null);
        if(p != null) {
            AbstractDungeon.actionManager.addToTop(new ChangeCharacterAction(p));
        }
    }


    @SpireOverride
    protected void updateEscapeAnimation() {
        if (this.escapeTimer != 0.0F) {
            this.escapeTimer -= Gdx.graphics.getDeltaTime();
            if (this.flipHorizontal) {
                this.drawX -= Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
            } else {
                this.drawX += Gdx.graphics.getDeltaTime() * 500.0F * Settings.scale;
            }
        }

        if (this.escapeTimer < 0.0F) {
            if(currentHealth <= 0 && !isProblemSolver(solverType)) {
                if (problemSolverPlayer.contains(this)) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveCharacterAction(this));
                }
            } else {
                AbstractDungeon.getCurrRoom().endBattle();
            }
            this.flipHorizontal = false;
            this.isEscaping = false;
            this.escapeTimer = 0.0F;
        }

    }

    //-----여기서부터 캐릭터 전환용 인풋 함수들---------
    public void updateForSwap() {
        if (AbstractDungeon.overlayMenu != null &&
            AbstractDungeon.overlayMenu.endTurnButton != null &&
            AbstractDungeon.overlayMenu.endTurnButton.enabled) {
            if(ableUse()) {
                this.enable();
            }
            else {
                this.disable();
            }
        }

        if (!(AbstractDungeon.overlayMenu != null &&
                AbstractDungeon.overlayMenu.endTurnButton != null &&
                AbstractDungeon.overlayMenu.endTurnButton.enabled)) {
            this.disable();
        }


        updateHoldProgress();
        if(enabled) {
            isDisabled = false;
            if (AbstractDungeon.isScreenUp || AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.inSingleTargetMode) {
                this.isDisabled = true;
            }
            if(hb.hovered && AbstractDungeon.player.hoveredCard == null)
                isHovered = true;
            else
                isHovered = false;

            if (AbstractDungeon.player.hoveredCard == null) {
                this.hb.update();
            }
            if (!Settings.USE_LONG_PRESS && InputHelper.justClickedLeft && this.hb.hovered && !this.isDisabled && !AbstractDungeon.isScreenUp) {
                this.hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }
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

    public void disable() {
        this.enabled = false;
        this.isHovered = false;
    }

    public boolean ableUse() {
        if(currentHealth > 0) {
            return true;
        }
        return false;
    }

    public void disable(boolean isEnemyTurn) {
        AbstractDungeon.actionManager.addToBottom(new ChangeCharacterAction(this));
    }
    //-----여기서부터 인풋 함수끝---------

}
