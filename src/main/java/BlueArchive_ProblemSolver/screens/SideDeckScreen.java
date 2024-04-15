package BlueArchive_ProblemSolver.screens;

import BlueArchive_ProblemSolver.patches.EnumPatch;
import BlueArchive_ProblemSolver.save.SideDeckSave;
import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.mainMenu.HorizontalScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.buttons.ConfirmButton;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import com.megacrit.cardcrawl.ui.buttons.SingingBowlButton;
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import de.robojumper.ststwitch.TwitchPanel;
import de.robojumper.ststwitch.TwitchVoteListener;
import de.robojumper.ststwitch.TwitchVoteOption;
import de.robojumper.ststwitch.TwitchVoter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Stream;

import static basemod.BaseMod.getCustomScreen;

public class SideDeckScreen extends CustomScreen implements ScrollBarListener {
    private static final Logger logger = LogManager.getLogger(CardRewardScreen.class.getName());
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final float PAD_X;
    private static final float CARD_TARGET_Y;
    public ArrayList<AbstractCard> rewardGroup;
    public RewardItem rItem = null;
    private boolean skippable = false;
    private String header = "";
    private SkipCardButton skipButton = new SkipCardButton();
    private PeekButton peekButton = new PeekButton();
    private static final float START_X;
    private boolean grabbedScreen = false;
    private float grabStartX = 0.0F;
    private float scrollX;
    private float targetX;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private HorizontalScrollBar scrollBar;
    public ConfirmButton confirmButton;
    private AbstractCard touchCard;
    private boolean isVoting;
    private boolean mayVote;
    private int card_misc;

    public SideDeckScreen() {
        this.scrollX = START_X;
        this.targetX = this.scrollX;
        this.scrollLowerBound = (float) Settings.WIDTH - 300.0F * Settings.xScale;
        this.scrollUpperBound = 2400.0F * Settings.scale;
        this.confirmButton = new ConfirmButton();
        this.touchCard = null;
        this.isVoting = false;
        this.mayVote = false;
        this.scrollBar = new HorizontalScrollBar(this, (float) Settings.WIDTH / 2.0F, 50.0F * Settings.scale + HorizontalScrollBar.TRACK_H / 2.0F, (float) Settings.WIDTH - 256.0F * Settings.scale);
    }


    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return EnumPatch.SIDEDECK_SCREEN;
    }

    private void open(ArrayList<AbstractCard> cards, int card_misc, RewardItem rItem) {
        this.peekButton.hideInstantly();
        this.confirmButton.hideInstantly();
        this.touchCard = null;
        this.skippable = true;
        this.skipButton.show();
        this.rItem = rItem;
        this.card_misc = card_misc;

        AbstractDungeon.topPanel.unhoverHitboxes();
        this.rewardGroup = cards;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = EnumPatch.SIDEDECK_SCREEN;
        this.header = TEXT[6];
        AbstractDungeon.dynamicBanner.appear(header);
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.showBlackScreen();
        this.placeCards((float) Settings.WIDTH / 2.0F, CARD_TARGET_Y);
        Iterator var4 = cards.iterator();

        AbstractCard c;
        while (var4.hasNext()) {
            c = (AbstractCard) var4.next();
            UnlockTracker.markCardAsSeen(c.cardID);
        }

        var4 = cards.iterator();

        while (var4.hasNext()) {
            c = (AbstractCard) var4.next();
            if (c.type == AbstractCard.CardType.POWER && !(Boolean) TipTracker.tips.get("POWER_TIP")) {
                AbstractDungeon.ftue = new FtueTip(AbstractPlayer.LABEL[0], AbstractPlayer.MSG[0], (float) Settings.WIDTH / 2.0F - 500.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F, c);
                AbstractDungeon.ftue.type = FtueTip.TipType.POWER;
                TipTracker.neverShowAgain("POWER_TIP");
                this.skipButton.hide();
                break;
            }
        }

        this.mayVote = true;
        if (AbstractDungeon.topPanel.twitch.isPresent()) {
            this.updateVote();
        }
    }

    @Override
    public void reopen() {
        this.confirmButton.hideInstantly();
        this.touchCard = null;
        AbstractDungeon.screen = EnumPatch.SIDEDECK_SCREEN;
        if (this.skippable) {
            this.skipButton.show();
        } else {
            this.skipButton.hide();
        }

        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.dynamicBanner.appear(this.header);
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.skipButton.screenDisabled = true;
    }


    private void placeCards(float x, float y) {
        int maxPossibleStartingIndex = this.rewardGroup.size() - 4;
        int indexToStartAt = (int)((float)(maxPossibleStartingIndex + 1) * MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.scrollX));
        if (indexToStartAt > maxPossibleStartingIndex) {
            indexToStartAt = maxPossibleStartingIndex;
        }

        AbstractCard c;
        for(Iterator var5 = this.rewardGroup.iterator(); var5.hasNext(); c.current_y = y) {
            c = (AbstractCard)var5.next();
            c.drawScale = 0.75F;
            c.targetDrawScale = 0.75F;
            if (this.rewardGroup.size() > 5) {
                if (this.rewardGroup.indexOf(c) < indexToStartAt) {
                    c.current_x = (float)(-Settings.WIDTH) * 0.25F;
                } else if (this.rewardGroup.indexOf(c) >= indexToStartAt + 4) {
                    c.current_x = (float)Settings.WIDTH * 1.25F;
                } else {
                    c.current_x = x;
                }
            } else {
                c.current_x = x;
            }
        }

    }
    @Override
    public void close() {
        if (AbstractDungeon.topPanel.twitch.isPresent()) {
            this.mayVote = false;
            this.updateVote();
        }

        if (Settings.isControllerMode) {
            InputHelper.moveCursorToNeutralPosition();
        }
    }

    @Override
    public void update() {

        if (Settings.isTouchScreen) {
            this.confirmButton.update();
            if (this.confirmButton.hb.clicked && this.touchCard != null) {
                this.confirmButton.hb.clicked = false;
                this.confirmButton.hb.clickStarted = false;
                this.confirmButton.isDisabled = true;
                this.confirmButton.hide();
                this.touchCard.hb.clicked = false;
                this.skipButton.hide();
                SideDeckSave.addDeck(this.card_misc, this.touchCard);

                this.takeReward();
                AbstractDungeon.closeCurrentScreen();

                this.touchCard = null;
            }
        }

        this.peekButton.update();
        if (!PeekButton.isPeeking) {
            this.skipButton.update();
        }

        this.updateControllerInput();
        if (!PeekButton.isPeeking) {
            if (!this.scrollBar.update()) {
                this.updateScrolling();
            }

            this.cardSelectUpdate();
        }

    }


    private void updateControllerInput() {
        if (Settings.isControllerMode && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.player.viewingRelics) {
            int index = 0;
            boolean anyHovered = false;

            for (Iterator var3 = this.rewardGroup.iterator(); var3.hasNext(); ++index) {
                AbstractCard c = (AbstractCard) var3.next();
                if (c.hb.hovered) {
                    anyHovered = true;
                    break;
                }
            }

            if (!anyHovered) {
                index = 0;
                Gdx.input.setCursorPosition((int) ((AbstractCard) this.rewardGroup.get(index)).hb.cX, Settings.HEIGHT - (int) ((AbstractCard) this.rewardGroup.get(index)).hb.cY);
            } else if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                    ++index;
                    if (index > this.rewardGroup.size() - 1) {
                        index = 0;
                    }

                    Gdx.input.setCursorPosition((int) ((AbstractCard) this.rewardGroup.get(index)).hb.cX, Settings.HEIGHT - (int) ((AbstractCard) this.rewardGroup.get(index)).hb.cY);
                }
            } else {
                --index;
                if (index < 0) {
                    index = this.rewardGroup.size() - 1;
                }

                Gdx.input.setCursorPosition((int) ((AbstractCard) this.rewardGroup.get(index)).hb.cX, Settings.HEIGHT - (int) ((AbstractCard) this.rewardGroup.get(index)).hb.cY);
            }

        }
    }

    private void updateScrolling() {
        int x = InputHelper.mX;
        if (!this.grabbedScreen) {
            if (InputHelper.scrolledDown) {
                this.targetX += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                this.targetX -= Settings.SCROLL_SPEED;
            }

            if (InputHelper.justClickedLeft) {
                this.grabbedScreen = true;
                this.grabStartX = (float) (-x) - this.targetX;
            }
        } else if (InputHelper.isMouseDown) {
            this.targetX = (float) (-x) - this.grabStartX;
        } else {
            this.grabbedScreen = false;
        }

        this.scrollX = MathHelper.scrollSnapLerpSpeed(this.scrollX, this.targetX);
        this.resetScrolling();
        this.updateBarPosition();
    }

    private void resetScrolling() {
        if (this.targetX < this.scrollLowerBound) {
            this.targetX = MathHelper.scrollSnapLerpSpeed(this.targetX, this.scrollLowerBound);
        } else if (this.targetX > this.scrollUpperBound) {
            this.targetX = MathHelper.scrollSnapLerpSpeed(this.targetX, this.scrollUpperBound);
        }

    }

    private void cardSelectUpdate() {
        AbstractCard hoveredCard = null;
        Iterator var2 = this.rewardGroup.iterator();

        while (var2.hasNext()) {
            AbstractCard c = (AbstractCard) var2.next();
            c.update();
            c.updateHoverLogic();
            if (c.hb.justHovered) {
                CardCrawlGame.sound.playV("CARD_OBTAIN", 0.4F);
            }

            if (c.hb.hovered) {
                hoveredCard = c;
            }
        }

        if (hoveredCard != null && InputHelper.justClickedLeft) {
            hoveredCard.hb.clickStarted = true;
        }

        if (hoveredCard != null && (InputHelper.justClickedRight || CInputActionSet.proceed.isJustPressed())) {
            InputHelper.justClickedRight = false;
            CardCrawlGame.cardPopup.open(hoveredCard);
        }

        if (hoveredCard != null && CInputActionSet.select.isJustPressed()) {
            hoveredCard.hb.clicked = true;
        }

        if (hoveredCard != null && hoveredCard.hb.clicked) {
            hoveredCard.hb.clicked = false;
            if (!Settings.isTouchScreen) {
                this.skipButton.hide();
                SideDeckSave.addDeck(this.card_misc, hoveredCard);

                this.takeReward();
                AbstractDungeon.closeCurrentScreen();
            } else if (!this.confirmButton.hb.clicked) {
                this.touchCard = hoveredCard;
                this.confirmButton.show();
                this.confirmButton.isDisabled = false;
            }
        }

        if (InputHelper.justReleasedClickLeft && Settings.isTouchScreen && hoveredCard == null && !this.confirmButton.isDisabled && !this.confirmButton.hb.hovered) {
            this.confirmButton.hb.clickStarted = false;
            this.confirmButton.hide();
            this.touchCard = null;
        }

    }


    private void acquireCard(AbstractCard hoveredCard) {
        this.recordMetrics(hoveredCard);
        InputHelper.justClickedLeft = false;
        AbstractDungeon.effectsQueue.add(new FastCardObtainEffect(hoveredCard, hoveredCard.current_x, hoveredCard.current_y));
    }

    private void recordMetrics(AbstractCard hoveredCard) {
    }

    private void recordMetrics(String pickText) {
    }

    private void takeReward() {
        if (this.rItem != null) {
            AbstractDungeon.combatRewardScreen.rewards.remove(this.rItem);
            AbstractDungeon.combatRewardScreen.positionRewards();
            if (AbstractDungeon.combatRewardScreen.rewards.isEmpty()) {
                AbstractDungeon.combatRewardScreen.hasTakenAll = true;
                AbstractDungeon.overlayMenu.proceedButton.show();
            }
        }

    }

    public void completeVoting(int option) {
        if (this.isVoting) {
            this.isVoting = false;
            if (this.getVoter().isPresent()) {
                TwitchVoter twitchVoter = (TwitchVoter) this.getVoter().get();
                AbstractDungeon.topPanel.twitch.ifPresent((twitchPanel) -> {
                    twitchPanel.connection.sendMessage("Voting on card ended... chose " + twitchVoter.getOptions()[option].displayName);
                });
            }

            while (AbstractDungeon.screen != EnumPatch.SIDEDECK_SCREEN) {
                AbstractDungeon.closeCurrentScreen();
            }

            if (option != 0) {
                if (option < this.rewardGroup.size() + 1) {
                    AbstractDungeon.overlayMenu.cancelButton.hide();
                    this.acquireCard((AbstractCard) this.rewardGroup.get(option - 1));
                }
            }

            this.takeReward();
            AbstractDungeon.closeCurrentScreen();
        }
    }

    private void renderTwitchVotes(SpriteBatch sb) {
        if (this.isVoting) {
            if (this.getVoter().isPresent()) {
                TwitchVoter twitchVoter = (TwitchVoter) this.getVoter().get();
                TwitchVoteOption[] options = twitchVoter.getOptions();
                int voteCountOffset = 1;
                int sum = (Integer) Arrays.stream(options).map((cx) -> {
                    return cx.voteCount;
                }).reduce(0, Integer::sum);

                for (int i = 0; i < this.rewardGroup.size(); ++i) {
                    AbstractCard c = (AbstractCard) this.rewardGroup.get(i);
                    StringBuilder cardVoteText = (new StringBuilder("#")).append(i + voteCountOffset).append(": ").append(options[i + voteCountOffset].voteCount);
                    if (sum > 0) {
                        cardVoteText.append(" (").append(options[i + voteCountOffset].voteCount * 100 / sum).append("%)");
                    }

                    FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, cardVoteText.toString(), c.target_x, c.target_y - 200.0F * Settings.scale, Color.WHITE.cpy());
                }

                StringBuilder skipVoteText = (new StringBuilder("#0: ")).append(options[0].voteCount);
                if (sum > 0) {
                    skipVoteText.append(" (").append(options[0].voteCount * 100 / sum).append("%)");
                }

                FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, skipVoteText.toString(), (float) Settings.WIDTH / 2.0F, 150.0F * Settings.scale, Color.WHITE.cpy());

                FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, TEXT[3] + twitchVoter.getSecondsRemaining() + TEXT[4], (float) Settings.WIDTH / 2.0F, AbstractDungeon.dynamicBanner.y - 70.0F * Settings.scale, Color.WHITE.cpy());
            }

        }
    }


    @Override
    public void render(SpriteBatch sb) {

        this.peekButton.render(sb);
        if (!PeekButton.isPeeking) {
            this.confirmButton.render(sb);
            this.skipButton.render(sb);
            this.renderCardReward(sb);
            if (this.shouldShowScrollBar()) {
                this.scrollBar.render(sb);
            }

            this.renderTwitchVotes(sb);
        }
    }

    private void renderCardReward(SpriteBatch sb) {
        Iterator var6;
        AbstractCard c;
        if (this.rewardGroup.size() > 5) {
            int maxPossibleStartingIndex = this.rewardGroup.size() - 4;
            int indexToStartAt = Math.max(Math.min((int) ((float) (maxPossibleStartingIndex + 1) * MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.scrollX)), maxPossibleStartingIndex), 0);

            for (Iterator var4 = this.rewardGroup.iterator(); var4.hasNext(); c.target_y = (float) Settings.HEIGHT / 2.0F) {
                c = (AbstractCard) var4.next();
                if (this.rewardGroup.indexOf(c) >= indexToStartAt && this.rewardGroup.indexOf(c) < indexToStartAt + 4) {
                    c.target_x = (float) Settings.WIDTH / 2.0F + ((float) (this.rewardGroup.indexOf(c) - indexToStartAt) - 1.5F) * (AbstractCard.IMG_WIDTH + PAD_X);
                } else if (this.rewardGroup.indexOf(c) < indexToStartAt) {
                    c.target_x = (float) (-Settings.WIDTH) * 0.25F;
                } else {
                    c.target_x = (float) Settings.WIDTH * 1.25F;
                }
            }
        } else {
            for (var6 = this.rewardGroup.iterator(); var6.hasNext(); c.target_y = CARD_TARGET_Y) {
                c = (AbstractCard) var6.next();
                c.target_x = (float) Settings.WIDTH / 2.0F + ((float) this.rewardGroup.indexOf(c) - (float) (this.rewardGroup.size() - 1) / 2.0F) * (AbstractCard.IMG_WIDTH + PAD_X);
            }
        }

        var6 = this.rewardGroup.iterator();

        while (var6.hasNext()) {
            c = (AbstractCard) var6.next();
            c.render(sb);
        }

        var6 = this.rewardGroup.iterator();

        while (var6.hasNext()) {
            c = (AbstractCard) var6.next();
            c.renderCardTip(sb);
        }

    }

    @Override
    public void openingSettings() {
        AbstractDungeon.previousScreen = curScreen();
    }



    public void skippedCards() {
        this.recordMetrics("SKIP");
    }

    public void closeFromBowlButton() {
        this.recordMetrics("Singing Bowl");
    }

    private Optional<TwitchVoter> getVoter() {
        return TwitchPanel.getDefaultVoter();
    }

    private void updateVote() {
        if (this.getVoter().isPresent()) {
            TwitchVoter twitchVoter = (TwitchVoter)this.getVoter().get();
            if (this.mayVote && twitchVoter.isVotingConnected() && !this.isVoting) {
                logger.info("Publishing Card Reward Vote");
                this.isVoting = twitchVoter.initiateSimpleNumberVote((String[]) Stream.concat(Stream.of(TEXT[0]), this.rewardGroup.stream().map(AbstractCard::toString)).toArray((x$0) -> {
                    return new String[x$0];
                }), this::completeVoting);
            } else if (this.isVoting && (!this.mayVote || !twitchVoter.isVotingConnected())) {
                twitchVoter.endVoting(true);
                this.isVoting = false;
            }
        }

    }

    public void scrolledUsingBar(float newPercent) {
        this.scrollX = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.targetX = this.scrollX;
        this.updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.scrollX);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    private boolean shouldShowScrollBar() {
        return this.rewardGroup.size() > 5;
    }



    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("CardRewardScreen");
        TEXT = uiStrings.TEXT;
        PAD_X = 40.0F * Settings.xScale;
        CARD_TARGET_Y = (float) Settings.HEIGHT * 0.45F;
        START_X = (float) Settings.WIDTH - 300.0F * Settings.xScale;
        TwitchVoter.registerListener(new TwitchVoteListener() {
            public void onTwitchAvailable() {
                CustomScreen screen = getCustomScreen(EnumPatch.SIDEDECK_SCREEN);
                if (screen != null) {
                    ((SideDeckScreen)screen).updateVote();
                }
            }

            public void onTwitchUnavailable() {
                CustomScreen screen = getCustomScreen(EnumPatch.SIDEDECK_SCREEN);
                if (screen != null) {
                    ((SideDeckScreen)screen).updateVote();
                }
            }
        });
    }
}
