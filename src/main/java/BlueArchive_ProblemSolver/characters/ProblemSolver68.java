package BlueArchive_ProblemSolver.characters;

import BlueArchive_ProblemSolver.actions.CleanCharacterAction;
import BlueArchive_ProblemSolver.actions.RemoveCharacterAction;
import BlueArchive_ProblemSolver.cards.AbstractDynamicCard;
import BlueArchive_ProblemSolver.patches.GameActionManagerPatch;
import BlueArchive_ProblemSolver.patches.powers.PowerForSubPatch;
import BlueArchive_ProblemSolver.powers.CaliforniaGurlsPower;
import BlueArchive_ProblemSolver.powers.CannotAttackedPower;
import BlueArchive_ProblemSolver.powers.CannotSelectedPower;
import BlueArchive_ProblemSolver.powers.OnDeadPower;
import BlueArchive_ProblemSolver.relics.OnDeadRelic;
import BlueArchive_ProblemSolver.relics.RadioTransceiverRelic;
import BlueArchive_ProblemSolver.save.ProblemSolverSave;
import BlueArchive_ProblemSolver.actions.ChangeCharacterAction;
import BlueArchive_ProblemSolver.util.GifDecoder;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.*;

import static BlueArchive_ProblemSolver.DefaultMod.*;
import static BlueArchive_ProblemSolver.characters.Aru.CAT_SKELETON_GIF;
import static BlueArchive_ProblemSolver.characters.Aru.MUTSUKI_SKELETON_GIF;
import static BlueArchive_ProblemSolver.characters.Aru.ProblemSolver68Type.*;

public abstract class ProblemSolver68 extends CustomPlayer {
    public static final float PROBLEM_SOLVER_WIDTH = 120.0F;
    public static final float PROBLEM_SOLVER_HEIGHT = 290.0F;
    public static final float PROBLEM_SOLVER_INTERVAL = 200.0F;
    private static final float POWER_ICON_PADDING_Y;
    public static final int MAX_CHARACTER_NUM = 4;
    public static final ArrayList<ProblemSolver68> problemSolverPlayer = new ArrayList<>();
    public static final ArrayList<ProblemSolver68> dyingPlayer = new ArrayList<>();
    public static final ArrayList<ProblemSolver68> mayRevivePlayer = new ArrayList<>();
    private static final Texture SELECTED_IMG = TextureLoader.getTexture(makeCharPath("select.png"));
    public static ProblemSolverSave savedata;
    private boolean enabled = false;
    private static boolean inDamageAll = false;
    private static boolean changeCharacter = false;
    private boolean isDisabled = false;
    private boolean isHovered = false;
    public Aru.ProblemSolver68Type solverType = PROBLEM_SOLVER_68_NONE;

    public static Comparator<AbstractPlayer> sortByPosition;

    private static final UIStrings characterStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_ProblemSolver:CharSelectAction");

    int myCount = 0;
    static int HelmatNum = 0;
    static int RabuNum = 0;
    static int SaoriNum = 0;
    static int CatNum = 0;

    float slip_x = 0;

    float slip_y = 0;

    float slip_start_x = 0;

    float slip_start_y = 0;
    float moving_count = -1;
    static float MOVING_SPEED = 0.5F;


    public static Animation<TextureRegion> mutuski_animation;
    public static Animation<TextureRegion> cat_animation;
    float animation_elapsed = 0;
    public ProblemSolver68(String name, AbstractPlayer.PlayerClass playerClass, String[] orbTextures, String orbVfxPath, float[] layerSpeeds, AbstractAnimation animation) {
        super(name, playerClass, orbTextures, orbVfxPath, layerSpeeds, animation);

        sortByPosition = (o1, o2) -> {
            return (int)(o1.hb.cX - o2.hb.cX);
        };
    }


    public static void changeCurrentPlayer(AbstractPlayer change) {
        change.gold = AbstractDungeon.player.gold;
        change.displayGold = AbstractDungeon.player.displayGold;

        AbstractDungeon.player = change;

    }

    public static void init() {
        problemSolverPlayer.clear();
        dyingPlayer.clear();
        mayRevivePlayer.clear();
        ProblemSolverSave.currentCharacters.clear();
        savedata = new ProblemSolverSave();
        BaseMod.addSaveField("BlueArchive_ProblemSolver:Character",savedata);
        changeCharacter = false;
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
            case "Cat":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_CAT;
            case "Ironclad":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_IRONCLAD;
            case "Silent":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_SILENT;
            case "Defect":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_DEFECT;
            case "Watcher":
                return Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_WATCHER;
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
            case PROBLEM_SOLVER_68_CAT:
                return "Cat";
            case PROBLEM_SOLVER_68_IRONCLAD:
                return "Ironclad";
            case PROBLEM_SOLVER_68_SILENT:
                return "Silent";
            case PROBLEM_SOLVER_68_DEFECT:
                return "Defect";
            case PROBLEM_SOLVER_68_WATCHER:
                return "Watcher";
            default:
                return "";
        }
    }

    public boolean isProblemSolver() {
        return isProblemSolver(solverType);
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

    private static void updateCharCount(ProblemSolver68 p) {
        switch(p.solverType) {
            case PROBLEM_SOLVER_68_HELMETGANG:
                p.myCount = HelmatNum;
                HelmatNum++;
                break;
            case PROBLEM_SOLVER_68_RABU:
                p.myCount = RabuNum;
                RabuNum++;
                break;
            case PROBLEM_SOLVER_68_SAORI:
                p.myCount = SaoriNum;
                SaoriNum++;
                break;
            case PROBLEM_SOLVER_68_CAT:
                p.myCount = CatNum;
                CatNum++;
                break;
            default:
                break;
        }
    }

    public static String getCommonName() {
        return characterStrings.TEXT[9];
    }
    public static String getLocalizedName(Aru.ProblemSolver68Type ptype) {
        switch(ptype) {
            case PROBLEM_SOLVER_68_ARU:
                return characterStrings.TEXT[1];
            case PROBLEM_SOLVER_68_MUTSUKI:
                return characterStrings.TEXT[2];
            case PROBLEM_SOLVER_68_KAYOKO:
                return characterStrings.TEXT[3];
            case PROBLEM_SOLVER_68_HARUKA:
                return characterStrings.TEXT[4];
            case PROBLEM_SOLVER_68_HELMETGANG:
                return characterStrings.TEXT[5] + HelmatNum;
            case PROBLEM_SOLVER_68_RABU:
                return characterStrings.TEXT[6] + (RabuNum==1?"":RabuNum);
            case PROBLEM_SOLVER_68_SAORI:
                return characterStrings.TEXT[7] + (SaoriNum==1?"":SaoriNum);
            case PROBLEM_SOLVER_68_CAT:
                return characterStrings.TEXT[8] + CatNum;
            case PROBLEM_SOLVER_68_IRONCLAD:
                return CardCrawlGame.languagePack.getCharacterString("Ironclad").NAMES[0];
            case PROBLEM_SOLVER_68_SILENT:
                return CardCrawlGame.languagePack.getCharacterString("Silent").NAMES[0];
            case PROBLEM_SOLVER_68_DEFECT:
                return CardCrawlGame.languagePack.getCharacterString("Defect").NAMES[0];
            case PROBLEM_SOLVER_68_WATCHER:
                return CardCrawlGame.languagePack.getCharacterString("Watcher").NAMES[0];
        }
        return "";
    }
    public static String getLocalizedName(ProblemSolver68 p) {
        String name = getLocalizedName(p.solverType);
        if(name != null) {
            return name;
        } else if (AbstractDungeon.player != null) {
            return AbstractDungeon.player.getLocalizedCharacterName();
        } else {
            return "UNKNOWN";
        }
    }
    public static void damageAll(int dmg) {
        inDamageAll = true;
        for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
            if(AbstractDungeon.player != ps) {
                ps.damage(new DamageInfo((AbstractCreature) null, dmg, DamageInfo.DamageType.HP_LOSS));
            }
        }
        inDamageAll = false;
        if(AbstractDungeon.player.currentHealth <= 0) {
            changeToNextCharacter();
        }
    }

    public static ProblemSolver68 getCharacter(String name) {
        for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
            if(enumToString(ps.solverType).equals(name)) {
                return ps;
            }
        }
        return null;
    }

    public static void saveDeathCharacter(ProblemSolver68 player) {
        mayRevivePlayer.add(player);
        removeCharacter(player);
    }

    public static void reviveDeathCharacter(ProblemSolver68 player) {
        if(mayRevivePlayer.contains(player)) {
            float offset_ = PROBLEM_SOLVER_INTERVAL*Settings.scale/2;
            for( ProblemSolver68 p_ : problemSolverPlayer) {
                p_.movePosition_(p_.drawX-offset_, p_.drawY, false);
            }
            for(AbstractPower p_ : player.powers) {
                p_.onRemove();
            }
            player.powers.clear();
            player.heal(1);
            player.movePosition_(player.drawX+offset_*problemSolverPlayer.size(), player.drawY, false);

            player.isDead = false;
            mayRevivePlayer.remove(player);
            problemSolverPlayer.add(player);
            player.healthBarUpdatedEvent();
        }
    }


    public static void removeCharacter(ProblemSolver68 player) {
        float offset_ = PROBLEM_SOLVER_INTERVAL*Settings.scale/2;
        boolean isPrev = true;
        for( ProblemSolver68 p_ : problemSolverPlayer) {
            p_.movePosition_(p_.drawX+offset_*(isPrev?1:-1), p_.drawY, false);
            if(player == p_) {
                isPrev = false;
            }
        }
        for(AbstractPower p_ : player.powers) {
            p_.onRemove();
        }
        player.powers.clear();
        problemSolverPlayer.remove(player);
    }

    public static void reassignCardList() {
        HashMap<CardGroup, AbstractCard.CardRarity> checkMap = new HashMap<CardGroup, AbstractCard.CardRarity>();
        checkMap.put(AbstractDungeon.srcCommonCardPool, AbstractCard.CardRarity.COMMON);
        checkMap.put(AbstractDungeon.srcUncommonCardPool, AbstractCard.CardRarity.UNCOMMON);
        checkMap.put(AbstractDungeon.srcRareCardPool, AbstractCard.CardRarity.RARE);
        checkMap.put(AbstractDungeon.commonCardPool, AbstractCard.CardRarity.COMMON);
        checkMap.put(AbstractDungeon.uncommonCardPool, AbstractCard.CardRarity.UNCOMMON);
        checkMap.put(AbstractDungeon.rareCardPool, AbstractCard.CardRarity.RARE);

        boolean solverTypes[] = new boolean[4];

        for(ProblemSolver68 player : problemSolverPlayer) {
            if(player.solverType.ordinal() > 0 && player.solverType.ordinal() <= 4) {
                solverTypes[player.solverType.ordinal() - 1] = true;
            }
        }

        for(Map.Entry<CardGroup, AbstractCard.CardRarity> entry : checkMap.entrySet()) {
            entry.getKey().group.removeIf((card) -> {
                if(card instanceof AbstractDynamicCard) {
                    if(!((AbstractDynamicCard)card).ableSolverType(solverTypes)) {
                        return true;
                    }
                }
                return false;
            });

            ArrayList<AbstractCard> cards = CardLibrary.getCardList(Aru.Enums.LIBRARY_COLOR);

            for(AbstractCard card : cards) {
                if(card instanceof AbstractDynamicCard) {
                    if(card.isSeen &&
                        card.rarity == entry.getValue() &&
                        ((AbstractDynamicCard)card).isSpecificCard() &&
                        ((AbstractDynamicCard)card).ableSolverType(solverTypes)) {
                        boolean contain = false;
                        for(AbstractCard testCard : entry.getKey().group) {
                            if(testCard.cardID == card.cardID) {
                                contain = true;
                                break;
                            }
                        }
                        if(!contain) {
                            entry.getKey().addToBottom(card.makeCopy());
                        }
                    }
                }
            }
        }
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
                reassignCardList();
            } else {
                AbstractDungeon.player.isEndingTurn = false;
                AbstractDungeon.player.healthBarUpdatedEvent();
                AbstractDungeon.player.showHealthBar();
            }
            updateCharCount((ProblemSolver68) AbstractDungeon.player);
            return AbstractDungeon.player;
        } else {
            float offset_ = PROBLEM_SOLVER_INTERVAL*Settings.scale/2;
            for( ProblemSolver68 p_ : problemSolverPlayer) {
                p_.movePosition_(p_.drawX-offset_, p_.drawY, false);
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
            p.movePosition_(p.drawX+offset_*problemSolverPlayer.size(), p.drawY, false);
            if(hp_ != -2) {
                p.currentHealth = hp_;
            }
            if(max_hp_ != -2) {
                p.maxHealth = max_hp_;
            }
            if(ProblemSolver68.isProblemSolver(p.solverType) && hp_ == -2 && max_hp_ == -2) {
                if (AbstractDungeon.floorNum <= 1 && CardCrawlGame.dungeon instanceof Exordium) {
                    if (AbstractDungeon.ascensionLevel >= 14) {
                        p.decreaseMaxHealth(p.getAscensionMaxHPLoss());
                    }

                    if (AbstractDungeon.ascensionLevel >= 6) {
                        p.currentHealth = MathUtils.round((float) p.maxHealth * 0.9F);
                    }
                }
            }

            if(!ProblemSolver68.isProblemSolver(p.solverType) && GameActionManagerPatch.increaseMercenaryMaxHP > 0) {
                p.increaseMaxHp(GameActionManagerPatch.increaseMercenaryMaxHP, false);
            }
            problemSolverPlayer.add(p);
            if(!inBattle) {
                savedata.addCharacter(name, p.currentHealth, p.maxHealth);
                reassignCardList();
            }
            else {
                p.isEndingTurn = false;
                p.healthBarUpdatedEvent();
                p.showHealthBar();
            }
            updateCharCount((ProblemSolver68)p);
            return p;
        }
    }

    public static void afterLoad() {
        if(AbstractDungeon.player != null && AbstractDungeon.player instanceof ProblemSolver68) {
            if(ProblemSolver68.mutuski_animation == null) {
                ProblemSolver68.mutuski_animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(MUTSUKI_SKELETON_GIF).read());
            }
            if(ProblemSolver68.cat_animation == null) {
                ProblemSolver68.cat_animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(CAT_SKELETON_GIF).read());
            }


            for (ProblemSolver68 p : problemSolverPlayer) {
                if(AbstractDungeon.player != p){
                    p.energy = AbstractDungeon.player.energy;
                }
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

    public static ArrayList<AbstractPower> getAllPowerForProblem68(Class power_class) {
        ArrayList<AbstractPower> getList = new ArrayList<AbstractPower>();
        if(!(AbstractDungeon.player instanceof ProblemSolver68)) {
            for(AbstractPower power_ : AbstractDungeon.player.powers) {
                if(power_class.isInstance(power_)) {
                    getList.add(power_);
                }
            }
        }
        else {
            for (ProblemSolver68 ps : problemSolverPlayer) {
                if(ps.currentHealth > 0) {
                    for(AbstractPower power_ : ps.powers) {
                        if(power_class.isInstance(power_)) {
                            getList.add(power_);
                        }
                    }
                }
            }
        }
        return getList;
    }

    public static boolean isLive(Aru.ProblemSolver68Type problemSolverType) {
        if(!(AbstractDungeon.player instanceof ProblemSolver68)) {
            return false;
        }
        for (ProblemSolver68 ps : problemSolverPlayer) {
            if (ps.solverType == problemSolverType && ps.currentHealth > 0){
                return true;
            }
        }
        return false;
    }


    public static boolean hasCharacter(Aru.ProblemSolver68Type problemSolverType) {
        if(!(AbstractDungeon.player instanceof ProblemSolver68)) {
            return false;
        }
        for (ProblemSolver68 ps : problemSolverPlayer) {
            if (ps.solverType == problemSolverType){
                return true;
            }
        }
        return false;
    }

    public static boolean someoneHasPower(String powerID) {
        if (!(AbstractDungeon.player instanceof ProblemSolver68)) {
            return AbstractDungeon.player.hasPower(powerID);
        }
        boolean hasPower_ = false;
        for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
            if(ps.hasPower(powerID)) {
                hasPower_ = true;
                break;
            }
        }
        return hasPower_;
    }

    public static void flashPower(String powerID) {
        if (!(AbstractDungeon.player instanceof ProblemSolver68)) {
            if(AbstractDungeon.player.hasPower(powerID)){
                AbstractDungeon.player.getPower(powerID).flash();
            }
        }
        boolean hasPower_ = false;
        for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
            if(ps.hasPower(powerID)) {
                ps.getPower(powerID).flash();
            }
        }
    }

    public static <T> ArrayList<T> getPowers(String powerID, Class<T> type) {
        ArrayList<T> powers = new ArrayList<T>();
        int val = 0;
        if (!(AbstractDungeon.player instanceof ProblemSolver68)) {
            if(AbstractDungeon.player.hasPower(powerID)) {
                if (type.isInstance(AbstractDungeon.player.getPower(powerID))) {
                    powers.add(type.cast(AbstractDungeon.player.getPower(powerID)));
                }
            }
        }
        for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
            if(ps.hasPower(powerID)) {
                if (type.isInstance(ps.getPower(powerID))) {
                    powers.add(type.cast(ps.getPower(powerID)));
                }
            }
        }
        return powers;
    }

    public static int getPowerValue(String powerID) {
        int val = 0;
        if (!(AbstractDungeon.player instanceof ProblemSolver68)) {
            if(AbstractDungeon.player.hasPower(powerID)) {
                val += AbstractDungeon.player.getPower(powerID).amount;
            }
        }
        for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
            if(ps.hasPower(powerID)) {
                val += ps.getPower(powerID).amount;
            }
        }
        return val;
    }
    public void movePosition(float x, float y) {
        float offset_ = PROBLEM_SOLVER_INTERVAL*Settings.scale/2;
        int i = 0;
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.movePosition_(x-offset_*(problemSolverPlayer.size()-1-i), y, false);
            i+=2;
        }
    }
    public void movePosition_(float x, float y, boolean slip) {
        if(slip) {
            slip_start_x = this.slip_x;
            slip_start_y = this.slip_y;
            moving_count = MOVING_SPEED;
        } else {
            slip_start_x = x;
            slip_start_y = y;
            slip_x = x;
            slip_y = y;
            moving_count = -1;
        }
        super.movePosition(x,y);
    }

    public void update() {
        ArrayList<ProblemSolver68> temp_ = new ArrayList<ProblemSolver68>();
        for (ProblemSolver68 p : problemSolverPlayer)
            temp_.add(p);
        for (ProblemSolver68 p : temp_) {
            if(problemSolverPlayer.contains(p)) {
                p.update_(false);
            }
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
        if(changeCharacter) {
            ProblemSolver68 temp = problemSolverPlayer.get(0);
            float tempBeforeDrawX = temp.drawX;
            float tempAfterDrawX = temp.drawX;
            problemSolverPlayer.remove(0);
            for (ProblemSolver68 p : problemSolverPlayer) {
                tempAfterDrawX = p.drawX;
                p.movePosition_(tempBeforeDrawX, p.drawY, true);
                tempBeforeDrawX = tempAfterDrawX;
            }
            problemSolverPlayer.add(temp);
            temp.movePosition_(tempAfterDrawX, temp.drawY, true);

            changeCharacter = false;
        }
    }
    public void update_(boolean dying) {
        super.update();
        if(!dying) {
            updateForSwap();
        }
    }

    public void updatePosition() {
        moving_count-=Gdx.graphics.getDeltaTime();
        moving_count = Math.max(0, moving_count);
        if(moving_count> 0) {
            float progress = 1 - (moving_count / MOVING_SPEED); // 0부터 1로 증가하는 progress
            float easedProgress = 1 - (float) Math.pow(1 - progress, 2); // Ease-out 적용
            slip_x = MathUtils.lerp(this.slip_start_x, drawX,  easedProgress);
            slip_y = MathUtils.lerp(this.slip_start_y, drawY, easedProgress);
        } else {
            slip_x = drawX;
            slip_y = drawY;
        }
    }

    public void updateAnimations() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            p.updateAnimations_();
            p.updatePosition();
        }
        for(ProblemSolver68 p : dyingPlayer) {
            p.updateAnimations_();
            p.updatePosition();
        }
    }


    public void updateAnimations_() {
        super.updateAnimations();
    }


    public void applyStartOfTurnPowers() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p.currentHealth > 0) {
                p.applyStartOfTurnPowers_();
            }
        }
    }
    public void applyStartOfTurnPowers_(){
        super.applyStartOfTurnPowers();
    }
    public void applyTurnPowers() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p.currentHealth > 0) {
                p.applyTurnPowers_();
            }
        }
    }
    public void applyTurnPowers_(){
        super.applyTurnPowers();
    }

    public void applyStartOfTurnPostDrawPowers() {
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p.currentHealth > 0) {
                p.applyStartOfTurnPostDrawPowers_();
            }
        }
    }
    public void applyStartOfTurnPostDrawPowers_(){
        super.applyStartOfTurnPostDrawPowers();
    }

    public void applyEndOfTurnTriggers() {
        AbstractDungeon.actionManager.addToTop(new CleanCharacterAction(false));
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p.currentHealth > 0) {
                p.applyEndOfTurnTriggers_();
            }
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
                    changeCurrentPlayer(p);
                    AbstractDungeon.player.onVictory();
                    return;
                }
            }
        }
        ArrayList<ProblemSolver68> removal = new ArrayList<>();
        for (ProblemSolver68 p : problemSolverPlayer) {
            TempHPField.tempHp.set(p, 0);
            p.loseBlock();
            if (AbstractDungeon.player == p) {
                if (!this.isDying) {
                    if(currentHealth <= 0) {
                        heal(1);
                        AbstractDungeon.effectsQueue.add(new HealEffect(hb.cX - animX, hb.cY,1));
                        healthBarUpdatedEvent();
                        this.isDead = false;
                        this.isDying = false;
                    }
                    super.onVictory();
                }
            } else if (isProblemSolver(p.solverType)) {
                p.onVictorySub();
            } else {
                removal.add(p);
            }
        }
        for(ProblemSolver68 p : removal) {
            removeCharacter(p);
        }
        while (mayRevivePlayer.size() > 0) {
            ProblemSolver68 ps = mayRevivePlayer.get(0);
            reviveDeathCharacter(ps);
        }



        dyingPlayer.clear();
        changeCurrentPlayer(problemSolverPlayer.get(problemSolverPlayer.size() - 1));
        HelmatNum = 0;
        RabuNum = 0;
        SaoriNum = 0;
        CatNum = 0;

    }
    public void onVictorySub() {
        if(currentHealth <= 0) {
            heal(1);
            AbstractDungeon.effectsQueue.add(new HealEffect(hb.cX - animX, hb.cY,1));
            healthBarUpdatedEvent();
            this.isDead = false;
            this.isDying = false;
        }
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

    @Override
    public void combatUpdate() {
        super.combatUpdate();
        for (ProblemSolver68 ps : problemSolverPlayer) {
            if(ps != AbstractDungeon.player) {
                ps.stance.update();
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
            for (ProblemSolver68 p : problemSolverPlayer) {
                p.render_(sb);
            }
            for(ProblemSolver68 p : dyingPlayer) {
                p.render_(sb);
            }
        } else {
            Aru.ProblemSolver68Type orders[] = {PROBLEM_SOLVER_68_ARU,PROBLEM_SOLVER_68_HARUKA,PROBLEM_SOLVER_68_MUTSUKI, PROBLEM_SOLVER_68_KAYOKO};
            for(Aru.ProblemSolver68Type order : orders) {
                for (ProblemSolver68 p : problemSolverPlayer) {
                    if(p.solverType == order) {
                        p.render_(sb);
                    }
                }
            }
        }
    }
    public void render_(SpriteBatch sb) {
        if(this != AbstractDungeon.player && currentHealth < 1) {
            Texture temp = img;
            boolean temp_button = (boolean) ReflectionHacks.getPrivate(this, AbstractPlayer.class, "renderCorpse");
            ReflectionHacks.setPrivate(this, AbstractPlayer.class, "renderCorpse", true);
            img = corpseImg;
            super.render(sb);
            img = temp;
            ReflectionHacks.setPrivate(this, AbstractPlayer.class, "renderCorpse", temp_button);
        } else {
            super.render(sb);
        }


        if(this == AbstractDungeon.player && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            sb.setColor(Color.RED);
            sb.setColor(Color.WHITE);
            sb.draw(SELECTED_IMG, this.drawX - (float)this.SELECTED_IMG.getWidth() * Settings.scale / 2.0F, this.drawY + hb.height / 2.0F + (float)this.SELECTED_IMG.getHeight() * Settings.scale, (float)this.SELECTED_IMG.getWidth() * Settings.scale, (float)this.SELECTED_IMG.getHeight() * Settings.scale, 0, 0, SELECTED_IMG.getWidth(), SELECTED_IMG.getHeight(), false, false);
        }
        if(isHovered) {
            renderReticle(sb);
        }
    }

    public void playDeathAnimation() {
        for (ProblemSolver68 ps : problemSolverPlayer) {
            if(isProblemSolver(ps.solverType) ) {
                ps.playDeathAnimation_();
            }
        }
    }
    public void playDeathAnimation_() {
        super.playDeathAnimation();
    }

    public void renderPlayerImage(SpriteBatch sb) {
        if(solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI
                && hasPower(CaliforniaGurlsPower.POWER_ID)
                && mutuski_animation != null){
            animation_elapsed += Gdx.graphics.getDeltaTime();
            sb.setColor(Color.WHITE);
            sb.draw(mutuski_animation.getKeyFrame(animation_elapsed), this.slip_x - 306.0f * Settings.scale / 2.0F + this.animX, this.slip_y - 150.0f * Settings.scale / 2.0F+ this.animY,  256.0f/2,  256.0f/2, 256.0f, 256.0f, Settings.scale, Settings.scale, 90.0f, true);
        } else if(solverType == Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_CAT
                && cat_animation != null){
            animation_elapsed += Gdx.graphics.getDeltaTime();
            sb.setColor(Color.WHITE);
            sb.draw(cat_animation.getKeyFrame(animation_elapsed), this.slip_x - 306.0f * Settings.scale / 2.0F + this.animX, this.slip_y - 150 * Settings.scale / 2.0F+ this.animY,  256.0f/2,  256.0f/2, 256.0f, 256.0f, Settings.scale, Settings.scale, 90.0f, true);
            //sb.draw(cat_animation.getKeyFrame(animation_elapsed), this.drawX - 266.0f * Settings.scale / 2.0F + this.animX, this.drawY - 125.0f * Settings.scale / 2.0F+ this.animY,  188.0f/2,  200.0f/2, 188.0f, 200.0f, Settings.scale, Settings.scale, 90.0f, true);
        } else {
            switch (this.animation.type()) {
                case NONE:
                    if (this.atlas != null) {
                        this.state.update(Gdx.graphics.getDeltaTime());
                        this.state.apply(this.skeleton);
                        this.skeleton.updateWorldTransform();
                        this.skeleton.setPosition(this.slip_x + this.animX, this.slip_y + this.animY);
                        this.skeleton.setColor(this.tint.color);
                        this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
                        sb.end();
                        CardCrawlGame.psb.begin();
                        sr.draw(CardCrawlGame.psb, this.skeleton);
                        CardCrawlGame.psb.end();
                        sb.begin();
                    } else {
                        sb.setColor(Color.WHITE);
                        sb.draw(this.img, this.slip_x - (float)this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.slip_y, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
                    }
                    break;
                case MODEL:
                    BaseMod.publishAnimationRender(sb);
                    break;
                case SPRITE:
                    this.animation.setFlip(this.flipHorizontal, this.flipVertical);
                    this.animation.renderSprite(sb, this.slip_x + this.animX, this.slip_y + this.animY + AbstractDungeon.sceneOffsetY);
            }
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

    @SpireOverride
    protected void renderPowerIcons(SpriteBatch sb, float x, float y) {
        float offset = 10.0F * Settings.scale;
        float POWER_ICON_PADDING_X = (float) ReflectionHacks.getPrivate(this, AbstractCreature.class, "POWER_ICON_PADDING_X");

        Color hbTextColor = (Color) ReflectionHacks.getPrivate(this, AbstractCreature.class, "hbTextColor");

        Iterator var5;
        AbstractPower p;
        int count = 0;
        float x_origin_offset= offset;

        float offset_y = Settings.isMobile?(y - 53.0F * Settings.scale):(y - 48.0F * Settings.scale);
        for(var5 = this.powers.iterator(); var5.hasNext(); offset += POWER_ICON_PADDING_X) {
            if(count++ >= 4) {
                offset = x_origin_offset;
                offset_y -= POWER_ICON_PADDING_Y;
                count = 0;
            }
            p = (AbstractPower)var5.next();
            p.renderIcons(sb, x + offset, offset_y, hbTextColor);
        }

        offset = 0.0F * Settings.scale;
        count = 0;
        x_origin_offset= offset;
        offset_y = Settings.isMobile?y - 75.0F * Settings.scale:y - 66.0F * Settings.scale;
        for(var5 = this.powers.iterator(); var5.hasNext(); offset += POWER_ICON_PADDING_X) {
            if(count++ >= 4) {
                offset = x_origin_offset;
                offset_y -= POWER_ICON_PADDING_Y;
                count = 0;
            }
            p = (AbstractPower)var5.next();
            p.renderAmount(sb, x + offset + 32.0F * Settings.scale, offset_y, hbTextColor);
        }

    }


    public void preBattlePrep() {

        changeCurrentPlayer(getFrontMember());

        AbstractDungeon.actionManager.addToTop(new CleanCharacterAction(false));
        super.preBattlePrep();
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p != this) {
                p.powers.clear();
                p.isEndingTurn = false;
                p.endTurnQueued = false;
                p.healthBarUpdatedEvent();
                if (ModHelper.isModEnabled("Lethality")) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, 3), 3));
                }

                if (ModHelper.isModEnabled("Terminal")) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PlatedArmorPower(p, 5), 5));
                }
            }
        }
        PowerForSubPatch.prevCharacter = this;
    }


//    public void applyPreCombatLogic() {
//        super.preBattlePrep();
//
//        this.addToTop(new ChangeCharacterAction());
//    }


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

    public void applyStartOfTurnPreDrawCards() {
        AbstractDungeon.actionManager.addToTop(new CleanCharacterAction(true));
        super.applyStartOfTurnPreDrawCards();
    }

    public static boolean isAllDead() {
        if(!(AbstractDungeon.player instanceof ProblemSolver68))
            return true;
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p.currentHealth >= 1 && isProblemSolver(p.solverType) && !p.hasPower(CannotAttackedPower.POWER_ID)) {
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
        for (ProblemSolver68 ps : mayRevivePlayer) {
            i++;
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
        return problemSolverPlayer.size() + mayRevivePlayer.size();
    }

    public static AbstractPlayer getRandomMember(AbstractPlayer exclude, boolean forDefend, boolean unwelcome) {
        List<ProblemSolver68> ableCharacters = new ArrayList<>();
        for (ProblemSolver68 p : problemSolverPlayer) {
            if(p.currentHealth > 0 && exclude != p && (!forDefend || !p.hasPower(CannotAttackedPower.POWER_ID) ) &&
                    (!unwelcome || !p.hasPower(CannotSelectedPower.POWER_ID))) {
                ableCharacters.add(p);
            }
        }
        if(!ableCharacters.isEmpty()) {
            Collections.shuffle(ableCharacters, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
            return ableCharacters.get(0);
        }
        return null;
    }


    public static AbstractPlayer getFrontMember() {
        AbstractPlayer p = null;
        for (ProblemSolver68 ps : problemSolverPlayer) {
            if(ps.currentHealth > 0) {
                p = ps;
            }
        }
        return p;
    }


    public static ProblemSolver68 getRandomReviveMember() {
        List<ProblemSolver68> ableCharacters = new ArrayList<>();
        for (ProblemSolver68 p : mayRevivePlayer) {
                ableCharacters.add(p);
        }
        if(!ableCharacters.isEmpty()) {
            Collections.shuffle(ableCharacters, new java.util.Random(AbstractDungeon.miscRng.randomLong()));
            return ableCharacters.get(0);
        }
        return null;
    }

    public static ProblemSolver68 getRandomDeadMember() {
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
        for(AbstractRelic relic : AbstractDungeon.player.relics) {
            if(relic instanceof OnDeadRelic) {
                ((OnDeadRelic)relic).onDead(dead);
            }
        }
    }
    public static AbstractPlayer changeToRandomCharacter() {
        AbstractPlayer p = getRandomMember(null, true, false);
        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if(p != null) {
                AbstractDungeon.actionManager.addToTop(new ChangeCharacterAction(p));
            }
        } else if(!inDamageAll) {
            AbstractDungeon.player = p;
        }
        return p;
    }


    public static AbstractPlayer changeToNextCharacter() {
        if(AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return changeToRandomCharacter();
        }
        AbstractPlayer p = null;
        for (ProblemSolver68 ps : problemSolverPlayer) {
            if(ps.currentHealth > 0 && !ps.hasPower(CannotAttackedPower.POWER_ID) && !ps.hasPower(CannotSelectedPower.POWER_ID)) {
                p = ps;
            }
        }

        if(p != null) {
            AbstractDungeon.actionManager.addToTop(new ChangeCharacterAction(p));
        }
        return p;
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

        if (this.hb.clicked  && !this.isDisabled && this.enabled) {
            this.hb.clicked = false;
            if (!AbstractDungeon.isScreenUp) {
                this.disable(true);
            }
        }

        /*if (isLeftJustPressed() && !this.isDisabled && this.enabled) {
            int index = problemSolverPlayer.size()-1;
            for (int i = 0; i < problemSolverPlayer.size(); i++) {
                if(problemSolverPlayer.get(i) == AbstractDungeon.player)
                    break;
                index = i;
            }

            if (problemSolverPlayer.get(index) == this) {
                this.hb.clicked = false;
                if (!AbstractDungeon.isScreenUp) {
                    this.disable(true);
                }
            }
        }

        if (isRightJustPressed() && !this.isDisabled && this.enabled) {
            int index = 0;
            for (int i = problemSolverPlayer.size()-1; i >= 0; i--) {
                if(problemSolverPlayer.get(i) == AbstractDungeon.player)
                    break;
                index = i;
            }

            if (problemSolverPlayer.get(index) == this) {
                this.hb.clicked = false;
                if (!AbstractDungeon.isScreenUp) {
                    this.disable(true);
                }
            }
        }


        if (isRollJustPressed() && !this.isDisabled && this.enabled) {
            if (AbstractDungeon.player == this) {
                this.hb.clicked = false;
                if (!AbstractDungeon.isScreenUp) {
                    changeCharacter = true;
                }
            }
        }*/
    }
    public int getLeftKeyCode() {
        return leftKey;
    }
    public int getRightKeyCode() {
        return rightKey;
    }
    public int getRollKeyCode() {
        return rollkey;
    }
    public boolean isLeftJustPressed() {
        return Gdx.input.isKeyJustPressed(getLeftKeyCode());
    }
    public boolean isRightJustPressed() {
        return Gdx.input.isKeyJustPressed(getRightKeyCode());
    }

    public boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(getLeftKeyCode());
    }
    public boolean isRightPressed() {
        return Gdx.input.isKeyPressed(getRightKeyCode());
    }

    public boolean isRollJustPressed() {
        return Gdx.input.isKeyJustPressed(getRollKeyCode());
    }
    public boolean isRollPressed() {
        return Gdx.input.isKeyPressed(getRollKeyCode());
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
        if(AbstractDungeon.player.hasRelic(RadioTransceiverRelic.ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChangeCharacterAction(this, true, false, true));
        }
    }
    //-----여기서부터 인풋 함수끝---------

    static {
        POWER_ICON_PADDING_Y = Settings.isMobile ? 45.0F * Settings.scale : 38.0F * Settings.scale;
    }
}
