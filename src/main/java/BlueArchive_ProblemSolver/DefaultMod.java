package BlueArchive_ProblemSolver;

import BlueArchive_ProblemSolver.actions.ProblemSolverTutorialAction;
import BlueArchive_ProblemSolver.cards.ChooseAru;
import BlueArchive_ProblemSolver.cards.ChooseHaruka;
import BlueArchive_ProblemSolver.cards.ChooseKayoko;
import BlueArchive_ProblemSolver.cards.ChooseMutsuki;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.patches.GridSelectScreenPatch;
import BlueArchive_ProblemSolver.relics.*;
import BlueArchive_ProblemSolver.util.GifDecoder;
import BlueArchive_ProblemSolver.variables.SecondMagicNumber;
import BlueArchive_ProblemSolver.variables.ThirdMagicNumber;
import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import BlueArchive_ProblemSolver.util.IDCheckDontTouchPls;
import BlueArchive_ProblemSolver.util.TextureLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static BlueArchive_ProblemSolver.characters.Aru.CAT_SKELETON_GIF;
import static BlueArchive_ProblemSolver.characters.Aru.Enums.PROBLEM_SOLVER;
import static BlueArchive_ProblemSolver.characters.Aru.MUTSUKI_SKELETON_GIF;
import static BlueArchive_ProblemSolver.characters.ProblemSolver68.problemSolverPlayer;

//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "theDefault" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 4 places.
// I comment those places below, under the place where you set your ID.

//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault", and change to "yourmodname" rather than "thedefault".
// You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories, and press alt+c to make the replace case sensitive (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class DefaultMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber,
        OnStartBattleSubscriber,
        StartGameSubscriber,
        PostUpdateSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(DefaultMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties problemSolverSettings = new Properties();
    public static final String ACTIVE_TUTORIAL = "activeTutorial";
    public static boolean activeTutorial = true;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "BlueArchive Problem Solver 68";
    private static final String AUTHOR = "joy1999"; // And pretty soon - You!
    private static final String DESCRIPTION = "BlueArchive Problem Solver 68 mod";
    ModLabeledToggleButton activeTutorialButton = null;
    // =============== INPUT TEXTURE LOCATION =================


    // =============== CHOOSE PRE GAME (REFERENCE SNEKO MOD) =================
    public static int choosingCharacters = -1;
    public static CardGroup choosePS68;
    public static boolean pureRandomMode = false;
    public static boolean openedStarterScreen = true;

    // Colors (RGB)
    // Character Color
    public static final Color DEFAULT_BLACK_RED = CardHelper.getColor(200.0f, 0.0f, 0.0f);
    
    // Potion Colors in RGB
    public static final Color TEMP_POTION_LIQUID = CardHelper.getColor(240.0f, 240.0f, 240.0f);
    public static final Color TEMP_POTION_HYBRID = CardHelper.getColor(240.0f, 240.0f, 240.0f);
    public static final Color TEMP_POTION_SPOTS = CardHelper.getColor(30.0f, 30.0f, 30.0f);

    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!


    private static final String ATTACK_PROBLEMSOLVER = "BlueArchive_ProblemSolverResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_PROBLEMSOLVER = "BlueArchive_ProblemSolverResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_PROBLEMSOLVER = "BlueArchive_ProblemSolverResources/images/512/bg_power_default_gray.png";


    private static final String ENERGY_ORB_PROBLEMSOLVER = "BlueArchive_ProblemSolverResources/images/512/card_default_gray_orb.png";
    private static final String CARD_PROBLEMSOLVER_ENERGY_ORB = "BlueArchive_ProblemSolverResources/images/512/card_small_orb.png";

    private static final String ATTACK_PROBLEMSOLVER_PORTRAIT = "BlueArchive_ProblemSolverResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_PROBLEMSOLVER_PORTRAIT = "BlueArchive_ProblemSolverResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_PROBLEMSOLVER_PORTRAIT = "BlueArchive_ProblemSolverResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_PROBLEMSOLVER_PORTRAIT = "BlueArchive_ProblemSolverResources/images/1024/card_default_gray_orb.png";

    // Character assets
    private static final String PROBLEMSOLVER_BUTTON = "BlueArchive_ProblemSolverResources/images/charSelect/ProblemSolverButton.png";
    private static final String PROBLEMSOLVER_PORTRAIT = "BlueArchive_ProblemSolverResources/images/charSelect/ProblemSolverPortraitBG.png";
    public static final String PROBLEMSOLVER_SHOULDER_1 = "BlueArchive_ProblemSolverResources/images/char/problemsolver/shoulder.png";
    public static final String PROBLEMSOLVER_SHOULDER_2 = "BlueArchive_ProblemSolverResources/images/char/problemsolver/shoulder2.png";
    public static final String PROBLEMSOLVER_CORPSE = "BlueArchive_ProblemSolverResources/images/char/problemsolver/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "BlueArchive_ProblemSolverResources/images/Badge.png";


    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    public static String makeCharPath(String resourcePath) {
        return getModID() + "Resources/images/char/other/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public DefaultMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */
      
        setModID("BlueArchive_ProblemSolver");
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project) and press alt+c (or mark the match case option)
        // replace all instances of theDefault with yourModID, and all instances of thedefault with yourmodid (the same but all lowercase).
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        // It's important that the mod ID prefix for keywords used in the cards descriptions is lowercase!

        // 3. Scroll down (or search for "ADD CARDS") till you reach the ADD CARDS section, and follow the TODO instructions

        // 4. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");

        logger.info("Creating the color " + Aru.Enums.COLOR_RED.toString());
        BaseMod.addColor(Aru.Enums.COLOR_RED, DEFAULT_BLACK_RED, DEFAULT_BLACK_RED, DEFAULT_BLACK_RED,
                DEFAULT_BLACK_RED, DEFAULT_BLACK_RED, DEFAULT_BLACK_RED, DEFAULT_BLACK_RED,
                ATTACK_PROBLEMSOLVER, SKILL_PROBLEMSOLVER, POWER_PROBLEMSOLVER, ENERGY_ORB_PROBLEMSOLVER,
                ATTACK_PROBLEMSOLVER_PORTRAIT, SKILL_PROBLEMSOLVER_PORTRAIT, POWER_PROBLEMSOLVER_PORTRAIT,
                ENERGY_ORB_PROBLEMSOLVER_PORTRAIT, CARD_PROBLEMSOLVER_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        problemSolverSettings.setProperty(ACTIVE_TUTORIAL, "TRUE");


        try {
            SpireConfig config = new SpireConfig("BlueArchive_ProblemSolver", "BlueArchiveConfig", problemSolverSettings);
            config.load();
            activeTutorial = config.getBool(ACTIVE_TUTORIAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = DefaultMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = DefaultMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======
    
    
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        DefaultMod defaultmod = new DefaultMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + PROBLEM_SOLVER.toString());

        BaseMod.addCharacter(new Aru("Aru", PROBLEM_SOLVER),
                PROBLEMSOLVER_BUTTON, PROBLEMSOLVER_PORTRAIT, PROBLEM_SOLVER);
        logger.info("Added " + PROBLEM_SOLVER.toString());
        receiveEditPotions();
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        activeTutorialButton = new ModLabeledToggleButton("Active Problem Solver Tutorial.",
                350.0f, 650.0f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                activeTutorial,
                settingsPanel,
                (label) -> {},
                (button) -> {
                    activeTutorial = button.enabled;
                    try {
                        SpireConfig config = new SpireConfig("BlueArchive_ProblemSolver", "BlueArchiveConfig", problemSolverSettings);
                        config.setBool(ACTIVE_TUTORIAL, activeTutorial);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


        settingsPanel.addUIElement(activeTutorialButton);

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        // =============== EVENTS =================
        // https://github.com/daviscook477/BaseMod/wiki/Custom-Events


        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }

    @Override
    public void receiveStartGame() {
        if (!CardCrawlGame.loadingSave) {
            openedStarterScreen = false;
        }

    }

    public static void ChooseAtGameStart() {
        if (AbstractDungeon.player instanceof Aru && !pureRandomMode) {
            if(ProblemSolver68.mutuski_animation == null) {
                ProblemSolver68.mutuski_animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(MUTSUKI_SKELETON_GIF).read());
            }
            if(ProblemSolver68.cat_animation == null) {
                ProblemSolver68.cat_animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(CAT_SKELETON_GIF).read());
            }


            choosingCharacters = 0;
            choosePS68 = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);


            choosePS68.addToTop(new ChooseAru());
            choosePS68.addToTop(new ChooseMutsuki());
            choosePS68.addToTop(new ChooseKayoko());
            choosePS68.addToTop(new ChooseHaruka());
            GridSelectScreenPatch.centerGridSelect = true;
            AbstractDungeon.gridSelectScreen.open(choosePS68, 1, false, CardCrawlGame.languagePack.getUIString("BlueArchive_ProblemSolver:CharSelectAction").TEXT[0]);
        }

    }
    @Override
    public void receivePostUpdate() {
        if (!openedStarterScreen && CardCrawlGame.isInARun()) {
            ChooseAtGameStart();
            openedStarterScreen = true;
        }

        if (choosingCharacters > -1 && choosingCharacters <= 1 && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && !pureRandomMode) {
            AbstractCard c = (AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            choosePS68.removeCard(c);
            c.onChoseThisOption();
            if (choosingCharacters == 1) {
                choosingCharacters = 2;
                GridSelectScreenPatch.centerGridSelect = false;

            } else if (choosingCharacters < 1) {
                ++choosingCharacters;
                AbstractDungeon.gridSelectScreen.open(choosePS68, 1, false, CardCrawlGame.languagePack.getUIString("BlueArchive_ProblemSolver:CharSelectAction").TEXT[0]);
            }
        }

    }



    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        if (AbstractDungeon.player instanceof Aru) {
            if (activeTutorial) {
                AbstractDungeon.actionManager.addToBottom(new ProblemSolverTutorialAction());
            }
        }
    }

    // =============== / POST-INITIALIZE/ =================
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        //BaseMod.addPotion(PeroroPotion.class, PERORO_POTION_LIQUID, PERORO_POTION_HYBRID, PERORO_POTION_SPOTS, PeroroPotion.POTION_ID, Aru.Enums.HIFUMI);


        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        BaseMod.addRelicToCustomPool(new ProblemSolverBaseRelic(), Aru.Enums.COLOR_RED);

        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variables");
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new ThirdMagicNumber());
        
        logger.info("Adding cards");
        // Add the cards
        // Don't delete these default cards yet. You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        // This method automatically adds any cards so you don't have to manually load them 1 by 1
        // For more specific info, including how to exclude cards from being added:
        // https://github.com/daviscook477/BaseMod/wiki/AutoAdd

        // The ID for this function isn't actually your modid as used for prefixes/by the getModID() method.
        // It's the mod id you give MTS in ModTheSpire.json - by default your artifact ID in your pom.xml

        //TODO: Rename the "DefaultMod" with the modid in your ModTheSpire.json file
        //TODO: The artifact mentioned in ModTheSpire.json is the artifactId in pom.xml you should've edited earlier
        new AutoAdd("BlueArchive_ProblemSolver") // ${project.artifactId}
                .packageFilter("BlueArchive_ProblemSolver.cards") // filters to any class in the same package as AbstractDefaultCard, nested packages included
                .setDefaultSeen(true)
                .cards();
        // .setDefaultSeen(true) unlocks the cards
        // This is so that they are all "seen" in the library,
        // for people who like to look at the card list before playing your mod

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        String pathByLanguage;
        switch(Settings.language) {
            case KOR:
                pathByLanguage = getModID() + "Resources/localization/" + "kor/";
                break;
            case ZHS:
                pathByLanguage = getModID() + "Resources/localization/" + "zhs/";
                break;
            default:
                pathByLanguage = getModID() + "Resources/localization/" + "eng/";
        }

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                pathByLanguage + "ProblemSolver-Card-Strings.json");
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                pathByLanguage + "ProblemSolver-Power-Strings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                pathByLanguage + "ProblemSolver-Relic-Strings.json");
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                pathByLanguage + "ProblemSolver-Event-Strings.json");
        
        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                pathByLanguage + "ProblemSolver-Potion-Strings.json");
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                pathByLanguage + "ProblemSolver-Character-Strings.json");

        BaseMod.loadCustomStringsFile(UIStrings.class,
                pathByLanguage + "ProblemSolver-UI-Strings.json");

        BaseMod.loadCustomStringsFile(TutorialStrings.class,
                pathByLanguage + "ProblemSolver-Tutorials-Strings.json");

        
        logger.info("Done edittting strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String pathByLanguage;
        switch (Settings.language) {
            case KOR:
                pathByLanguage = getModID() + "Resources/localization/" + "kor/";
                break;
            case ZHS:
                pathByLanguage = getModID() + "Resources/localization/" + "zhs/";
                break;
            default:
                pathByLanguage = getModID() + "Resources/localization/" + "eng/";
        }

        {
            String json = Gdx.files.internal(pathByLanguage + "ProblemSolver-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
            com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

            if (keywords != null) {
                for (Keyword keyword : keywords) {
                    BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("BlueArchive_ProblemSolver:ProblemSolverSelect", getModID() + "Resources/sound/ProblemSolver_select.mp3");
    }
}
