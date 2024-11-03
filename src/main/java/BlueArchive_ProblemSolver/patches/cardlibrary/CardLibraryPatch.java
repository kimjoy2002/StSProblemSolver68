package BlueArchive_ProblemSolver.patches.cardlibrary;

import BlueArchive_ProblemSolver.cards.AbstractDynamicCard;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.EverythingFix;
import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import com.megacrit.cardcrawl.screens.mainMenu.TabBarListener;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class CardLibraryPatch {

    private static Texture img;
    public static Hitbox[] problemCheckbox = null;
    public static Boolean[] problemisDisable = null;

    public static boolean changeCheck = false;


    @SpirePatch(
            clz = CardLibraryScreen.class,
            method = "didChangeTab"
    )
    public static class DidChangeTab {
        public DidChangeTab() {
        }

        @SpireInsertPatch(
                rloc = 23,
                localvars = {"visibleCards"}
        )
        public static void Insert(CardLibraryScreen __instance, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection, @ByRef CardGroup[] visibleCards) {
            ArrayList<ColorTabBarFix.ModColorTab> modColorTabs = ReflectionHacks.getPrivateStatic(ColorTabBarFix.Fields.class, "modTabs");
            if(newSelection == ColorTabBarFix.Enums.MOD &&
                    problemCheckbox != null && problemisDisable != null &&
                    basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix.Fields.getModTab().color == Aru.Enums.COLOR_RED) {
                if(!changeCheck) {
                    if(problemisDisable == null) {
                        problemisDisable = new Boolean[4];
                        for(int i = 0; i < 4; i++){
                            problemisDisable[i] = new Boolean(false);
                        }
                    } else {
                        for(int i = 0; i < 4; i++){
                            problemisDisable[i] = false;
                        }
                    }
                    return;
                }


                CardGroup tempgroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for(AbstractCard temp : visibleCards[0].group) {
                    boolean skip = false;
                    if(temp instanceof AbstractDynamicCard) {
                        AbstractDynamicCard pscard = (AbstractDynamicCard)temp;
                        for(int i = 0; i < 4; i++) {
                            if(problemisDisable[i]) {
                                if(pscard.isSolverType(Aru.ProblemSolver68Type.values()[i + 1])) {
                                    skip = true;
                                }
                            }
                        }
                    }
                    if(!skip) {
                        tempgroup.addToBottom(temp);
                    }
                }
                visibleCards[0] = tempgroup;
            }
        }
    }




    @SpirePatch(
            clz = ColorTabBar.class,
            method = "update"
    )
    public static class ColorTabBarUpdatePatch {
        public static void Postfix(ColorTabBar __instance, float y) {
            ArrayList<ColorTabBarFix.ModColorTab> modColorTabs = ReflectionHacks.getPrivateStatic(ColorTabBarFix.Fields.class, "modTabs");
            if(problemCheckbox == null) {
                problemCheckbox = new Hitbox[4];
                for(int i = 0; i < 4; i++){
                    problemCheckbox[i] = new Hitbox(235.0F * Settings.scale, 50.0F * Settings.scale);
                }
            }
            if(problemisDisable == null) {
                problemisDisable = new Boolean[4];
                for(int i = 0; i < 4; i++){
                    problemisDisable[i] = new Boolean(false);
                }
            }
            for(int i = 0; i < 4; i++){
                problemCheckbox[i].move(150.0F * Settings.scale, y - 64.0F * (float)(modColorTabs.size() + i + 1) * Settings.scale);
                problemCheckbox[i].update();
                if (InputHelper.justClickedLeft) {
                    if (problemCheckbox[i].hovered) {
                        problemisDisable[i] = !problemisDisable[i];
                        TabBarListener delegate =  ReflectionHacks.getPrivate(__instance, ColorTabBar.class, "delegate");
                        changeCheck = true;
                        delegate.didChangeTab(__instance, __instance.curTab);
                        changeCheck = false;
                    }
                }
            }
        }

    }



    @SpirePatch(
            clz = ColorTabBar.class,
            method = "render"
    )
    public static class ColorTabBarRenderPatch {
        private static final float SPACING = 64.0F;




        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"curTab"}
        )
        public static void Insert(ColorTabBar __instance, SpriteBatch sb, float y, ColorTabBar.CurrentTab curTab) {
            ArrayList<ColorTabBarFix.ModColorTab> modColorTabs = ReflectionHacks.getPrivateStatic(ColorTabBarFix.Fields.class, "modTabs");

            if(curTab == ColorTabBarFix.Enums.MOD &&
                    problemCheckbox != null && problemisDisable != null &&
                    basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix.Fields.getModTab().color == Aru.Enums.COLOR_RED) {
                //sb.setColor(Settings.GOLD_COLOR);
                //float doop = 1.0F + (1.0F + MathUtils.cosDeg((float)(System.currentTimeMillis() / 2L % 360L))) / 50.0F;

                for(int i = 0; i < 4; i ++) {
                    Color c = Settings.CREAM_COLOR;
                    if (problemCheckbox[i].hovered) {
                        c = Settings.GOLD_COLOR;
                    }

                    String name = ProblemSolver68.getLocalizedName(Aru.ProblemSolver68Type.values()[i + 1]);
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, name, 100.0F * Settings.scale, y -  (-10.0F +64.0F * (float)(modColorTabs.size() + i+1)) * Settings.scale, c);
                    Texture img = (problemisDisable[i]==false) ? ImageMaster.COLOR_TAB_BOX_TICKED : ImageMaster.COLOR_TAB_BOX_UNTICKED;
                    sb.setColor(c);
                    sb.draw(img, 30.0F * Settings.xScale, y - (96.0F + 64.0F * (float)(modColorTabs.size() + i)) * Settings.scale, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 48, 48, false, false);
                    //sb.draw(ImageMaster.COLOR_TAB_BAR, 40.0F * Settings.scale, y - 64.0F * (float)(modColorTabs.size() + i + 1) * Settings.scale, 0.0F, 0.0F, 235.0F, 102.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1334, 102, false, false);
                }


                //sb.draw(ImageMaster.COLOR_TAB_BAR, 40.0F * Settings.scale, y - 64.0F * (float)(modColorTabs.size() + 1) * Settings.scale, 0.0F, 0.0F, 235.0F, 102.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1334, 102, false, false);
                //sb.draw(img, (40.0F-100.0F) * Settings.scale, (y - 64.0F * (float)(modColorTabs.size() + 1)-43.0F) * Settings.scale, 100.0F, 43.0F, 200.0F, 86.0F, Settings.scale * doop * (100 / 150.0F / Settings.scale), Settings.scale * doop, 0.0F, 0, 0, 200, 86, false, false);
            }

        }

        private static class Locator extends SpireInsertLocator {
            private Locator() {
            }

            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ColorTabBar.class, "getBarColor");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList(), finalMatcher);
            }
        }
    }

    static {
        img = ImageMaster.loadImage("images/ui/cardlibrary/selectBox.png");
    }
}
