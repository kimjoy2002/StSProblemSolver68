package BlueArchive_ProblemSolver.patches.cards;

import BlueArchive_ProblemSolver.cards.EvilDeedsCard;
import BlueArchive_ProblemSolver.cards.HardboiledShot;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class CardUiPatch {

    private static Texture evildeed_on = new Texture("BlueArchive_ProblemSolverResources/images/ui/evildeed_on.png");

    private static Texture evildeed_glow = new Texture("BlueArchive_ProblemSolverResources/images/ui/evildeed_glow.png");

    private static Texture evildeed_off = new Texture("BlueArchive_ProblemSolverResources/images/ui/evildeed_off.png");


    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderCard",
            paramtypez = {SpriteBatch.class, boolean.class, boolean.class}
    )
    public static class collectedCardPatcher {


        public static float calculateMaxLength(double evil) {
            int c = 200;
            int d = 5;
            return (float) (c * (1 - Math.exp(-evil / d)));
        }


        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected) {
            if(__instance != null) {
                if (__instance instanceof EvilDeedsCard && AbstractDungeon.player != null && AbstractDungeon.player.hand != null && AbstractDungeon.player.hand.contains(__instance) && !__instance.isLocked && __instance.isSeen) {
                    int require_evil = ((EvilDeedsCard)__instance).require_evil;
                    int evil = ((EvilDeedsCard)__instance).evil;
                    int draw_evil = Math.min(30, Math.max(evil,require_evil));
                    float max_length = calculateMaxLength(draw_evil-1);
                    float offset = max_length/(draw_evil-1);
                    float offset_x = -max_length/2;
                    float offset_y = 220;

                    for(int i = 0; i < draw_evil; i++) {
                        Texture draw_tex = i>=evil?evildeed_off:evildeed_on;
                        float w = draw_tex.getWidth();
                        float h = draw_tex.getHeight();
                        sb.setBlendFunction(770, 771);
                        sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                        sb.draw(draw_tex, __instance.current_x-w/2.0f+offset_x, __instance.current_y-h/2.0f+offset_y, w/2.0f-offset_x, h/2.0f-offset_y, w, h, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, (int)w, (int)h, false, false);

                        if(require_evil <= evil) {
                            sb.setBlendFunction(770, 1);
                            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.7F));
                            w = evildeed_glow.getWidth();
                            h = evildeed_glow.getHeight();
                            sb.draw(evildeed_glow, __instance.current_x-w/2.0f+offset_x, __instance.current_y-h/2.0f+offset_y, w/2.0f-offset_x, h/2.0f-offset_y, w, h, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, (int)w, (int)h, false, false);
                        }

                        offset_x+=offset;
                    }
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
                    sb.setBlendFunction(770, 771);
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
}
