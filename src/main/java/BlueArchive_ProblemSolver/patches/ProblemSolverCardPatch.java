package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.cards.AbstractDynamicCard;
import BlueArchive_ProblemSolver.characters.Aru;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
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
                AbstractCard card =  ReflectionHacks.getPrivate(__instance, SingleCardViewPopup.class, "card");
                if (card instanceof AbstractDynamicCard) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                    Texture tex = getTexture1024ForCard((AbstractDynamicCard)card);
                    if(tex != null) {
                        sb.draw(tex, (float)Settings.WIDTH / 2.0F - 512.0F, (float)Settings.HEIGHT / 2.0F - 512.0F, 512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 1024, 1024, false, false);
                    }
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
