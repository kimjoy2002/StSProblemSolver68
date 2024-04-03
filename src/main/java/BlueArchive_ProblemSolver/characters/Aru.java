package BlueArchive_ProblemSolver.characters;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.cards.DeliveryRequest;
import BlueArchive_ProblemSolver.cards.MultiBlock;
import BlueArchive_ProblemSolver.cards.ProblemSolverDefend;
import BlueArchive_ProblemSolver.cards.ProblemSolverStrike;
import BlueArchive_ProblemSolver.relics.ProblemSolverBaseRelic;
import BlueArchive_ProblemSolver.util.GifDecoder;
import basemod.animations.SpineAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static BlueArchive_ProblemSolver.characters.Aru.Enums.COLOR_RED;
import static BlueArchive_ProblemSolver.characters.Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_NONE;

public class Aru extends ProblemSolver68 {
    public static final Logger logger = LogManager.getLogger(Aru.class.getName());

    public static final String BASE_ANIMATION = "base_animation";

    public enum ProblemSolver68Type {
        PROBLEM_SOLVER_68_NONE,
        PROBLEM_SOLVER_68_ARU,
        PROBLEM_SOLVER_68_MUTSUKI,
        PROBLEM_SOLVER_68_KAYOKO,
        PROBLEM_SOLVER_68_HARUKA,
        PROBLEM_SOLVER_68_HELMETGANG,
        PROBLEM_SOLVER_68_RABU,
        PROBLEM_SOLVER_68_SAORI,
        PROBLEM_SOLVER_68_CAT,
        PROBLEM_SOLVER_68_IRONCLAD,
        PROBLEM_SOLVER_68_SILENT,
        PROBLEM_SOLVER_68_DEFECT,
        PROBLEM_SOLVER_68_WATCHER
    }

    public static class Enums {
        @SpireEnum
        public static PlayerClass PROBLEM_SOLVER;
        @SpireEnum(name = "BA_PROBLEMSOLVER68_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor COLOR_RED;
        @SpireEnum(name = "BA_PROBLEMSOLVER68_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 25;
    public static final int MAX_HP = 25;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;
    public static float update_timer = 0;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================
    private static final String ID = DefaultMod.makeID("ProblemSolver68");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    public static final String ARU_SKELETON_ATLAS = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Aru.atlas";
    public static final String ARU_SKELETON_JSON = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Aru.json";
    public static final String MUTSUKI_SKELETON_ATLAS = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Mutsuki.atlas";
    public static final String MUTSUKI_SKELETON_JSON = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Mutsuki.json";
    public static final String MUTSUKI_SKELETON_GIF = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Mutsuki.gif";
    public static final String KAYOKO_SKELETON_ATLAS = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Kayoko.atlas";
    public static final String KAYOKO_SKELETON_JSON = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Kayoko.json";
    public static final String HARUKA_SKELETON_ATLAS = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Haruka.atlas";
    public static final String HARUKA_SKELETON_JSON = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Haruka.json";

    //--- non Problem Solver 68
    public static final String HELMETGANG_SKELETON_ATLAS = "BlueArchive_ProblemSolverResources/images/char/problemsolver/HelmetGang.atlas";
    public static final String HELMETGANG_SKELETON_JSON = "BlueArchive_ProblemSolverResources/images/char/problemsolver/HelmetGang.json";

    public static final String RABU_SKELETON_ATLAS = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Rabu.atlas";
    public static final String RABU_SKELETON_JSON = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Rabu.json";

    public static final String SAORI_SKELETON_ATLAS = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Saori.atlas";
    public static final String SAORI_SKELETON_JSON = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Saori.json";
    public static final String CAT_SKELETON_ATLAS = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Saori.atlas";
    public static final String CAT_SKELETON_JSON = "BlueArchive_ProblemSolverResources/images/char/problemsolver/Saori.json";
    public static final String CAT_SKELETON_GIF = "BlueArchive_ProblemSolverResources/images/char/problemsolver/dance-cat.gif";

    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer1.png",
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer2.png",
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer3.png",
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer4.png",
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer5.png",
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer6.png",
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer1d.png",
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer2d.png",
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer3d.png",
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer4d.png",
            "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/layer5d.png",};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public Aru(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/vfx.png", null,
                new SpineAnimation(ARU_SKELETON_ATLAS,
                        ARU_SKELETON_JSON, 1f));

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                DefaultMod.PROBLEMSOLVER_SHOULDER_2, // campfire pose
                DefaultMod.PROBLEMSOLVER_SHOULDER_1, // another campfire pose
                DefaultMod.PROBLEMSOLVER_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, PROBLEM_SOLVER_WIDTH, PROBLEM_SOLVER_HEIGHT, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        init();
    }


    public Aru(String name, PlayerClass setClass, ProblemSolver68Type type) {
        super(name, setClass, orbTextures,
                "BlueArchive_ProblemSolverResources/images/char/problemsolver/orb/vfx.png", null,
                new SpineAnimation(ARU_SKELETON_ATLAS,
                        ARU_SKELETON_JSON, 1f));

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                DefaultMod.PROBLEMSOLVER_SHOULDER_2, // campfire pose
                DefaultMod.PROBLEMSOLVER_SHOULDER_1, // another campfire pose
                DefaultMod.PROBLEMSOLVER_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, PROBLEM_SOLVER_WIDTH, PROBLEM_SOLVER_HEIGHT, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        setSolverType(type);
    }

    public void setSolverType(ProblemSolver68Type solverType) {
        this.solverType = solverType;
        newAnimation("");
    }

    // =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");

        retVal.add(ProblemSolverStrike.ID);
        retVal.add(ProblemSolverStrike.ID);
        retVal.add(ProblemSolverStrike.ID);
        retVal.add(ProblemSolverStrike.ID);

        retVal.add(ProblemSolverDefend.ID);
        retVal.add(ProblemSolverDefend.ID);
        retVal.add(ProblemSolverDefend.ID);
        retVal.add(ProblemSolverDefend.ID);

        return retVal;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(ProblemSolverBaseRelic.ID);

        // Mark relics as seen - makes it visible in the compendium immediately
        // If you don't have this it won't be visible in the compendium until you see them in game
        UnlockTracker.markRelicAsSeen(ProblemSolverBaseRelic.ID);

        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        //AbstractDungeon.actionManager.addToBottom(new SFXAction("BlueArchive_Aris:Reload"));
        CardCrawlGame.sound.playA("BlueArchive_ProblemSolver:ProblemSolverSelect", 0.0F); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 0;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return COLOR_RED;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return new Color(0.78f, 0.0f, 0.0f, 1.0f);
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new ProblemSolverStrike();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new Aru(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return DefaultMod.DEFAULT_BLACK_RED;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return DefaultMod.DEFAULT_BLACK_RED;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.LIGHTNING,
                AbstractGameAction.AttackEffect.LIGHTNING,
                AbstractGameAction.AttackEffect.LIGHTNING};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    public void newAnimation(String animation) {
        String atlas = "";
        String json = "";
        String defaultAnimationName = BASE_ANIMATION;

        switch(solverType) {
            case PROBLEM_SOLVER_68_ARU:
                atlas = ARU_SKELETON_ATLAS;
                json = ARU_SKELETON_JSON;
                break;
            case PROBLEM_SOLVER_68_MUTSUKI:
                atlas = MUTSUKI_SKELETON_ATLAS;
                json = MUTSUKI_SKELETON_JSON;
                break;
            case PROBLEM_SOLVER_68_KAYOKO:
                atlas = KAYOKO_SKELETON_ATLAS;
                json = KAYOKO_SKELETON_JSON;
                break;
            case PROBLEM_SOLVER_68_HARUKA:
                atlas = HARUKA_SKELETON_ATLAS;
                json = HARUKA_SKELETON_JSON;
                break;
            case PROBLEM_SOLVER_68_HELMETGANG:
                atlas = HELMETGANG_SKELETON_ATLAS;
                json = HELMETGANG_SKELETON_JSON;
                break;
            case PROBLEM_SOLVER_68_RABU:
                atlas = RABU_SKELETON_ATLAS;
                json = RABU_SKELETON_JSON;
                break;
            case PROBLEM_SOLVER_68_SAORI:
                atlas = SAORI_SKELETON_ATLAS;
                json = SAORI_SKELETON_JSON;
                break;
            case PROBLEM_SOLVER_68_CAT:
                atlas = CAT_SKELETON_ATLAS;
                json = CAT_SKELETON_JSON;
                break;
            case PROBLEM_SOLVER_68_IRONCLAD:
                atlas ="images/characters/ironclad/idle/skeleton.atlas";
                json = "images/characters/ironclad/idle/skeleton.json";
                defaultAnimationName ="Idle";
                break;
            case PROBLEM_SOLVER_68_SILENT:
                atlas ="images/characters/theSilent/idle/skeleton.atlas";
                json = "images/characters/theSilent/idle/skeleton.json";
                defaultAnimationName ="Idle";
                break;
            case PROBLEM_SOLVER_68_DEFECT:
                atlas ="images/characters/defect/idle/skeleton.atlas";
                json = "images/characters/defect/idle/skeleton.json";
                defaultAnimationName ="Idle";
                break;
            case PROBLEM_SOLVER_68_WATCHER:
                atlas ="images/characters/watcher/idle/skeleton.atlas";
                json = "images/characters/watcher/idle/skeleton.json";
                defaultAnimationName ="Idle";
                break;
        }

        if(!atlas.isEmpty() && !json.isEmpty()) {
            loadAnimation(
                    atlas,
                    json,
                    0.9f);
            AnimationState.TrackEntry e = state.setAnimation(0,animation.isEmpty()?defaultAnimationName:animation, true);
            e.setTime(e.getEndTime() * MathUtils.random());
        }
    }

    public Texture getCutsceneBg() {
        return ImageMaster.loadImage("images/scenes/purpleBg.jpg");
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList();
        if(AbstractDungeon.player.hasRelic("BlueArchive_Hoshino:PeroroRelic")) {
            panels.add(new CutscenePanel("BlueArchive_ProblemSolverResources/images/ending/ending_1_1.png"));
            panels.add(new CutscenePanel("BlueArchive_ProblemSolverResources/images/ending/ending_2_2.png"));
            panels.add(new CutscenePanel("BlueArchive_ProblemSolverResources/images/ending/ending_3_3.png"));
        }
        else {
            panels.add(new CutscenePanel("BlueArchive_ProblemSolverResources/images/ending/ending_1.png"));
            panels.add(new CutscenePanel("BlueArchive_ProblemSolverResources/images/ending/ending_2.png"));
            panels.add(new CutscenePanel("BlueArchive_ProblemSolverResources/images/ending/ending_3.png"));
        }
        return panels;
    }


    public void updateVictoryVfx(ArrayList<AbstractGameEffect> effects) {
        update_timer += Gdx.graphics.getDeltaTime();

        for (float i = 0; i + (1.0 / 10.0) <= update_timer; update_timer -= (1.0 / 10.0)) {
            //effects.add(new VictoryPeroroGoodEffect());
        }
    }
}
