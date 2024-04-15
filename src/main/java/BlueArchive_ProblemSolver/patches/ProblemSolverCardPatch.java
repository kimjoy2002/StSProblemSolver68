package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.cards.AbstractDynamicCard;
import BlueArchive_ProblemSolver.cards.AddDeck;
import BlueArchive_ProblemSolver.cards.SideDeck;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.save.SideDeckSave;
import BlueArchive_ProblemSolver.screens.SideDeckViewScreen;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import javassist.CtBehavior;

import java.lang.reflect.Field;

public class ProblemSolverCardPatch {
    private static final Texture aruTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_aru.png");
    private static final Texture mutsukiTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_mutsuki.png");
    private static final Texture kayokoTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_kayoko.png");
    private static final Texture harukaTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_haruka.png");
    private static final Texture arumutsukiTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_arumutsuki.png");
    private static final Texture arukayokoTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_arukayoko.png");
    private static final Texture aruharukaTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_aruharuka.png");
    private static final Texture mutsukikayokoTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_mutsukikayoko.png");
    private static final Texture mutsukiharukaTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_mutsukiharuka.png");
    private static final Texture kayokoharukaTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_kayokoharuka.png");
    private static final Texture allTex512 = new Texture("BlueArchive_ProblemSolverResources/images/512/card_all.png");

    private static final Texture aruTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_aru.png");
    private static final Texture mutsukiTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_mutsuki.png");
    private static final Texture kayokoTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_kayoko.png");
    private static final Texture harukaTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_haruka.png");
    private static final Texture arumutsukiTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_arumutsuki.png");
    private static final Texture arukayokoTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_arukayoko.png");
    private static final Texture aruharukaTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_aruharuka.png");
    private static final Texture mutsukikayokoTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_mutsukikayoko.png");
    private static final Texture mutsukiharukaTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_mutsukiharuka.png");
    private static final Texture kayokoharukaTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_kayokoharuka.png");
    private static final Texture allTex1024 = new Texture("BlueArchive_ProblemSolverResources/images/1024/card_all.png");

    private static Texture getTexture512ForCard(AbstractDynamicCard card) {
        if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU)) {
            if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI)) {
                if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO) &&
                        card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)) {
                    return allTex512;
                }
                else {
                    return arumutsukiTex512;
                }
            }
            else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO)) {
                return arukayokoTex512;
            }
            else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)) {
                return aruharukaTex512;
            }
            else {
                return aruTex512;
            }
        }
        else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI)) {
            if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO)) {
                return mutsukikayokoTex512;
            }
            else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)) {
                return mutsukiharukaTex512;
            }
            else {
                return mutsukiTex512;
            }
        }
        else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO)) {
            if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)) {
                return kayokoharukaTex512;
            }
            else {
                return kayokoTex512;
            }
        }
        else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)) {
            return harukaTex512;
        }
        else {
            return null;
        }
    }
    private static Texture getTexture1024ForCard(AbstractDynamicCard card) {
        if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU)) {
            if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI)) {
                if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO) &&
                        card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)) {
                    return allTex1024;
                }
                else {
                    return arumutsukiTex1024;
                }
            }
            else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO)) {
                return arukayokoTex1024;
            }
            else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)) {
                return aruharukaTex1024;
            }
            else {
                return aruTex1024;
            }
        }
        else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI)) {
            if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO)) {
                return mutsukikayokoTex1024;
            }
            else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)) {
                return mutsukiharukaTex1024;
            }
            else {
                return mutsukiTex1024;
            }
        }
        else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO)) {
            if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)) {
                return kayokoharukaTex1024;
            }
            else {
                return kayokoTex1024;
            }
        }
        else if(card.isSolverType(Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA)) {
            return harukaTex1024;
        }
        else {
            return null;
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCard",
            paramtypez = {SpriteBatch.class, boolean.class, boolean.class}
    )
    public static class card512Patcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected) {
            if(__instance != null) {
                if (__instance instanceof AbstractDynamicCard && AbstractDungeon.player != null  && !__instance.isLocked && __instance.isSeen) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                    Texture tex = getTexture512ForCard((AbstractDynamicCard)__instance);
                    if(tex != null) {
                        float w = tex.getWidth();
                        float h = tex.getHeight();
                        sb.draw(tex, __instance.current_x-256, __instance.current_y-256, w/2.0f, h/2.0f, w, h, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, (int)w, (int)h, false, false);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "renderEnergy");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderInLibrary",
            paramtypez = {SpriteBatch.class}
    )
    public static class cardLibraryPatcher {

        public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
            if(__instance != null) {
                ReflectionHacks.RMethod isOnScreen = ReflectionHacks.privateMethod(AbstractCard.class, "isOnScreen");
                boolean result = isOnScreen.invoke(__instance);
                if (result &&
                    __instance instanceof AbstractDynamicCard) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                    Texture tex = getTexture512ForCard((AbstractDynamicCard)__instance);
                    if(tex != null) {
                        float w = tex.getWidth();
                        float h = tex.getHeight();
                        sb.draw(tex, __instance.current_x-256, __instance.current_y-256, w/2.0f, h/2.0f, w, h, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, (int)w, (int)h, false, false);
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz=SingleCardViewPopup.class,
            method=SpirePatch.CLASS
    )
    public static class singleCardViewPopupField
    {
        public static SpireField<Hitbox> sideDeckHb = new SpireField<>(() -> null);
    }


    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "open",
            paramtypez = {AbstractCard.class,
                    CardGroup.class}
    )
    public static class singleCardOpenPatcher {

        public static void Postfix(SingleCardViewPopup __instance, AbstractCard card, CardGroup group) {
            singleCardViewPopupField.sideDeckHb.set(__instance, null);
            if(card instanceof SideDeck) {
                singleCardViewPopupField.sideDeckHb.set(__instance, new Hitbox(128.0F * Settings.scale, 128.0F * Settings.scale));
                Hitbox sideDeckHb = singleCardViewPopupField.sideDeckHb.get(__instance);
                sideDeckHb.move((float) Settings.WIDTH / 2.0F + 350.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F + 300.0F * Settings.scale);
            }
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "updateArrows"
    )
    public static class updateArrowsPatcher {

        public static void Postfix(SingleCardViewPopup __instance) {
            Hitbox sideDeckHb = singleCardViewPopupField.sideDeckHb.get(__instance);
            if (sideDeckHb != null && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW) {

                sideDeckHb.update();
                if (sideDeckHb.justHovered) {
                    CardCrawlGame.sound.play("UI_HOVER");
                }

                if (sideDeckHb.clicked) {
                    sideDeckHb.clicked =  false;

                    AbstractCard card =  ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
                    if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW) {
                        if (SideDeckSave.decks.containsKey(card.misc)) {
                            if (SideDeckSave.decks.get(card.misc).size() > 0) {
                                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                                group.group = SideDeckSave.decks.get(card.misc);
                                BaseMod.openCustomScreen(EnumPatch.SIDEDECK_VIEW_SCREEN, group);
                                __instance.close();
                                ReflectionHacks.setPrivate(__instance, SingleCardViewPopup.class, "fadeTimer", 0.0F);
                                Color fadeColor = (Color) ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "fadeColor");
                                fadeColor.a = 0.9F;
                            }
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "closeCurrentScreen"
    )
    public static class closeCurrentScreenPatcher {

        public static void Postfix() {
            if(SideDeckViewScreen.preprevScreen != AbstractDungeon.CurrentScreen.NONE && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW) {
                AbstractDungeon.previousScreen = SideDeckViewScreen.preprevScreen;
                SideDeckViewScreen.preprevScreen = AbstractDungeon.CurrentScreen.NONE;
            }
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "updateInput"
    )
    public static class updateInputPatcher {

        public static SpireReturn Prefix(SingleCardViewPopup __instance) {
            Hitbox sideDeckHb = singleCardViewPopupField.sideDeckHb.get(__instance);
            if(sideDeckHb != null && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW) {
                if (InputHelper.justClickedLeft) {
                    if (sideDeckHb.hovered) {
                        sideDeckHb.clickStarted = true;
                        CardCrawlGame.sound.play("UI_CLICK_1");
                        return SpireReturn.Return();
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }



    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "render",
            paramtypez = {SpriteBatch.class}
    )
    public static class card1024Patcher {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(SingleCardViewPopup __instance, SpriteBatch sb) {
            if(__instance != null) {
                Hitbox sideDeckHb = singleCardViewPopupField.sideDeckHb.get(__instance);
                AbstractCard card =  ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
                if (card instanceof AbstractDynamicCard) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                    Texture tex = getTexture1024ForCard((AbstractDynamicCard)card);
                    if(tex != null) {
                        sb.draw(tex, (float)Settings.WIDTH / 2.0F - 512.0F, (float)Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);
                    }

                    if(sideDeckHb != null && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW) {
                        sb.setColor(Color.WHITE);
                        sb.draw(ImageMaster.DECK_BTN_BASE, sideDeckHb.cX - 64.0F, sideDeckHb.cY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0f, 0, 0, 128, 128, false, false);
                        if (sideDeckHb.hovered) {
                            sb.setBlendFunction(770, 1);
                            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.25F));
                            sb.draw(ImageMaster.DECK_BTN_BASE, sideDeckHb.cX - 64.0F, sideDeckHb.cY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0f, 0, 0, 128, 128, false, false);
                            sb.setBlendFunction(770, 771);
                        }
                        int size_ = 0;
                        if(SideDeckSave.decks.containsKey(card.misc)) {
                            size_ = SideDeckSave.decks.get(card.misc).size();
                        }
                        Color tmpColor = Color.WHITE.cpy();
                        FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, Integer.toString(size_), sideDeckHb.cX + 48.0F * Settings.scale, sideDeckHb.cY + -25.0F * Settings.scale, tmpColor);
                    }
                }
                if (sideDeckHb != null) {
                    sideDeckHb.render(sb);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderArrows");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
