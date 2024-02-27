package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.DefaultMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;


import java.util.ArrayList;

public class ProblemSolverBtnPatch {
    public static final Hitbox challengeDownHitbox;
    public static final ArrayList<PowerTip> challengeTips;
    public static final UIStrings uiStrings;

    public ProblemSolverBtnPatch() {
    }

    static {
        challengeDownHitbox = new Hitbox(40.0F * Settings.scale * 0.991F, 40.0F * Settings.scale);
        challengeTips = new ArrayList();
        uiStrings = CardCrawlGame.languagePack.getUIString("BlueArchive_ProblemSolver:ChallengeMode");
    }

    @SpirePatch(
            clz = CharacterOption.class,
            method = "updateHitbox"
    )
    public static class UpdateHitbox {
        public UpdateHitbox() {
        }

        public static void Postfix(CharacterOption obj) {
            if (obj.name.contains("68") && obj.selected) {
                ProblemSolverBtnPatch.challengeDownHitbox.update();
                if (ProblemSolverBtnPatch.challengeDownHitbox.hovered) {
                    if (ProblemSolverBtnPatch.challengeTips.isEmpty()) {
                        ProblemSolverBtnPatch.challengeTips.add(new PowerTip(ProblemSolverBtnPatch.uiStrings.TEXT[0], ProblemSolverBtnPatch.uiStrings.TEXT[0]));
                    }

                    if ((float) InputHelper.mX < 1400.0F * Settings.scale) {
                        TipHelper.queuePowerTips((float)InputHelper.mX + 60.0F * Settings.scale, (float)InputHelper.mY - 50.0F * Settings.scale, ProblemSolverBtnPatch.challengeTips);
                    } else {
                        TipHelper.queuePowerTips((float)InputHelper.mX - 350.0F * Settings.scale, (float)InputHelper.mY - 50.0F * Settings.scale, ProblemSolverBtnPatch.challengeTips);
                    }

                    if (InputHelper.justClickedLeft) {
                        CardCrawlGame.sound.playA("UI_CLICK_1", -0.4F);
                        ProblemSolverBtnPatch.challengeDownHitbox.clickStarted = true;
                    }

                    if (ProblemSolverBtnPatch.challengeDownHitbox.clicked) {
                        DefaultMod.pureRandomMode = !DefaultMod.pureRandomMode;
                        ProblemSolverBtnPatch.challengeDownHitbox.clicked = false;
                    }
                }
            }

        }
    }

    @SpirePatch(
            clz = CharacterOption.class,
            method = "renderRelics"
    )
    public static class RenderBtn {
        public RenderBtn() {
        }

        public static void Postfix(CharacterOption obj, SpriteBatch sb) {
            float allTextInfoX = 0;
            if (obj.name.contains("68") && obj.selected) {
                ProblemSolverBtnPatch.challengeDownHitbox.move(190.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F - 190.0F * Settings.scale);
                ProblemSolverBtnPatch.challengeDownHitbox.render(sb);
                sb.setColor(Color.WHITE);
                sb.draw(ImageMaster.CHECKBOX, ProblemSolverBtnPatch.challengeDownHitbox.cX - 32.0F + allTextInfoX, ProblemSolverBtnPatch.challengeDownHitbox.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.991F, Settings.scale * 0.991F, 0.0F, 0, 0, 64, 64, false, false);
                if (DefaultMod.pureRandomMode) {
                    sb.draw(ImageMaster.TICK, ProblemSolverBtnPatch.challengeDownHitbox.cX - 32.0F + allTextInfoX, ProblemSolverBtnPatch.challengeDownHitbox.cY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.991F, Settings.scale * 0.991F, 0.0F, 0, 0, 64, 64, false, false);
                }

                FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, ProblemSolverBtnPatch.uiStrings.TEXT[0], ProblemSolverBtnPatch.challengeDownHitbox.cX + 25.0F * Settings.scale + allTextInfoX, ProblemSolverBtnPatch.challengeDownHitbox.cY, Settings.BLUE_TEXT_COLOR);
            }

        }
    }
}
