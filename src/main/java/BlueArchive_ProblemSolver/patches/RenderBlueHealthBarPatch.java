package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.powers.FearPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "renderRedHealthBar"
)
public class RenderBlueHealthBarPatch {

    private static final float HEALTH_BAR_HEIGHT;
    private static final float HEALTH_BAR_OFFSET_Y;
    private static final Color COLOR_OF_FEAR;
    public static void Postfix(AbstractCreature __instance, SpriteBatch sb, float x, float y) {

        if (__instance.hasPower(FearPower.POWER_ID)) {
            sb.setColor(COLOR_OF_FEAR);
            float targetHealthBarWidth = (float)ReflectionHacks.getPrivate(__instance, AbstractCreature.class, "targetHealthBarWidth");
            int fearAmt = __instance.getPower(FearPower.POWER_ID).amount;
            if(__instance.currentHealth < fearAmt) {
                fearAmt = __instance.currentHealth;
            }
            float w = (float)(fearAmt) / (float)__instance.currentHealth;
            w *= targetHealthBarWidth;
            if (__instance.currentHealth > 0) {
                sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            }

            sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, w, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_R, x + w, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        }
    }

    static {
        HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
        HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;
        COLOR_OF_FEAR = Color.valueOf("9c00b8ff");
    }
}
