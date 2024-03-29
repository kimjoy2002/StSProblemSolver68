package BlueArchive_ProblemSolver.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.ui.FtueTip;

public class ProblemSolverTutorial extends FtueTip {
    private static final TutorialStrings tutorialStrings;
    public static final String[] TEXT;
    public static final String[] LABEL;
    private Texture img1 = ImageMaster.loadImage("BlueArchive_ProblemSolverResources/images/ui/t1.png");
    private Texture img2 = ImageMaster.loadImage("BlueArchive_ProblemSolverResources/images/ui/t2.png");
    private Texture img3 = ImageMaster.loadImage("BlueArchive_ProblemSolverResources/images/ui/t3.png");
    private Texture img4 = ImageMaster.loadImage("BlueArchive_ProblemSolverResources/images/ui/t4.png");
    private Color screen = Color.valueOf("85000000");
    private float x;
    private float x1;
    private float x2;
    private float x3;
    private float x4;
    private float targetX;
    private float startX;
    private float scrollTimer = 0.0F;
    private int currentSlot = 0;
    private int closeScreen;

    public ProblemSolverTutorial() {
        this.closeScreen = -3;
        AbstractDungeon.player.releaseCard();
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }

        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.FTUE;
        AbstractDungeon.overlayMenu.showBlackScreen();
        this.x = 0.0F;
        this.x1 = 567.0F * Settings.scale;
        this.x2 = this.x1 + (float)Settings.WIDTH;
        this.x3 = this.x2 + (float)Settings.WIDTH;
        this.x4 = this.x3 + (float)Settings.WIDTH;
        AbstractDungeon.overlayMenu.proceedButton.show();
        AbstractDungeon.overlayMenu.proceedButton.setLabel(LABEL[0]);
    }

    public void update() {
        AbstractDungeon.overlayMenu.proceedButton.setLabel(LABEL[0]);
        if (this.currentSlot <= this.closeScreen) {
            AbstractDungeon.overlayMenu.proceedButton.setLabel(LABEL[1]);
        }

        if (this.screen.a != 0.8F) {
            Color var10000 = this.screen;
            var10000.a += Gdx.graphics.getDeltaTime();
            if (this.screen.a > 0.8F) {
                this.screen.a = 0.8F;
            }
        }

        if (AbstractDungeon.overlayMenu.proceedButton.isHovered && InputHelper.justClickedLeft || CInputActionSet.proceed.isJustPressed()) {
            CInputActionSet.proceed.unpress();
            if (this.currentSlot <= this.closeScreen) {
                CardCrawlGame.sound.play("DECK_CLOSE");
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.overlayMenu.proceedButton.hide();
                AbstractDungeon.effectList.clear();
            }

            --this.currentSlot;
            this.startX = this.x;
            this.targetX = (float)(this.currentSlot * Settings.WIDTH);
            this.scrollTimer = 0.3F;
            if (this.currentSlot <= this.closeScreen) {
                AbstractDungeon.overlayMenu.proceedButton.setLabel(LABEL[1]);
            }
        }

        if (this.scrollTimer != 0.0F) {
            this.scrollTimer -= Gdx.graphics.getDeltaTime();
            if (this.scrollTimer < 0.0F) {
                this.scrollTimer = 0.0F;
            }
        }

        this.x = Interpolation.fade.apply(this.targetX, this.startX, this.scrollTimer / 0.3F);
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screen);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
        sb.setColor(Color.WHITE);
        sb.draw(this.img1, this.x + this.x1 - img1.getWidth()/2, (float)Settings.HEIGHT / 2.0F - img1.getHeight()/2, img1.getWidth()/2, img1.getHeight()/2, img1.getWidth(), img1.getHeight(), Settings.scale, Settings.scale, 0.0F, 0, 0, img1.getWidth(), img1.getHeight(), false, false);
        sb.draw(this.img2, this.x + this.x2 - img2.getWidth()/2, (float)Settings.HEIGHT / 2.0F - img2.getHeight()/2, img2.getWidth()/2, img2.getHeight()/2, img2.getWidth(), img2.getHeight(), Settings.scale, Settings.scale, 0.0F, 0, 0, img2.getWidth(), img2.getHeight(), false, false);
        sb.draw(this.img3, this.x + this.x3 - img3.getWidth()/2, (float)Settings.HEIGHT / 2.0F - img3.getHeight()/2, img3.getWidth()/2, img3.getHeight()/2, img3.getWidth(), img3.getHeight(), Settings.scale, Settings.scale, 0.0F, 0, 0, img3.getWidth(), img3.getHeight(), false, false);
        sb.draw(this.img4, this.x + this.x4 - img4.getWidth()/2, (float)Settings.HEIGHT / 2.0F - img4.getHeight()/2, img4.getWidth()/2, img4.getHeight()/2, img4.getWidth(), img4.getHeight(), Settings.scale, Settings.scale, 0.0F, 0, 0, img4.getWidth(), img4.getHeight(), false, false);
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, TEXT[0], this.x + this.x1 + (img1.getWidth()/2+50) * Settings.scale, (float)Settings.HEIGHT / 2.0F - FontHelper.getSmartHeight(FontHelper.panelNameFont, TEXT[0], 700.0F * Settings.scale, 40.0F * Settings.scale) / 2.0F, 700.0F * Settings.scale, 40.0F * Settings.scale, Settings.CREAM_COLOR);
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, TEXT[1], this.x + this.x2 + (img2.getWidth()/2+50) * Settings.scale, (float)Settings.HEIGHT / 2.0F - FontHelper.getSmartHeight(FontHelper.panelNameFont,  TEXT[1], 700.0F * Settings.scale, 40.0F * Settings.scale) / 2.0F, 700.0F * Settings.scale, 40.0F * Settings.scale, Settings.CREAM_COLOR);
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, TEXT[2], this.x + this.x3 + (img3.getWidth()/2+50) * Settings.scale, (float)Settings.HEIGHT / 2.0F - FontHelper.getSmartHeight(FontHelper.panelNameFont,  TEXT[1], 700.0F * Settings.scale, 40.0F * Settings.scale) / 2.0F, 700.0F * Settings.scale, 40.0F * Settings.scale, Settings.CREAM_COLOR);
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, TEXT[3], this.x + this.x4 + (img4.getWidth()/2+50) * Settings.scale, (float)Settings.HEIGHT / 2.0F - FontHelper.getSmartHeight(FontHelper.panelNameFont,  TEXT[1], 700.0F * Settings.scale, 40.0F * Settings.scale) / 2.0F, 700.0F * Settings.scale, 40.0F * Settings.scale, Settings.CREAM_COLOR);
        FontHelper.renderFontCenteredWidth(sb, FontHelper.panelNameFont, LABEL[2], (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F - 360.0F * Settings.scale, Settings.GOLD_COLOR);
        FontHelper.renderFontCenteredWidth(sb, FontHelper.tipBodyFont, LABEL[3] + Integer.toString(Math.abs(this.currentSlot - 1)) + "/" + Math.abs(this.closeScreen - 1) + LABEL[4], (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F - 400.0F * Settings.scale, Settings.CREAM_COLOR);
        AbstractDungeon.overlayMenu.proceedButton.render(sb);
    }

    static {
        tutorialStrings = CardCrawlGame.languagePack.getTutorialString("BlueArchive_ProblemSolver:ProblemSolverTutorial");
        TEXT = tutorialStrings.TEXT;
        LABEL = tutorialStrings.LABEL;
    }
}
