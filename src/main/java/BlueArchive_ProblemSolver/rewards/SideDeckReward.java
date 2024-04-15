package BlueArchive_ProblemSolver.rewards;

import BlueArchive_ProblemSolver.actions.SideDeckAction;
import BlueArchive_ProblemSolver.patches.EnumPatch;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

import java.util.UUID;

import static BlueArchive_ProblemSolver.DefaultMod.getModID;

public class SideDeckReward extends CustomReward {
    public int card_misc;
    public SideDeckReward(int card_misc) {
        super(ImageMaster.REWARD_CARD_NORMAL, SideDeckAction.TEXT[1], EnumPatch.REWORD_SIDEDECK);
        this.flashTimer = 0.0F;
        this.isDone = false;
        this.ignoreReward = false;
        this.redText = false;
        this.card_misc = card_misc;
        //this.isBoss = AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss;
        this.cards = AbstractDungeon.getRewardCards();
    }

    @Override
    public boolean claimReward() {
        if (AbstractDungeon.player.hasRelic("Question Card")) {
            AbstractDungeon.player.getRelic("Question Card").flash();
        }

        if (AbstractDungeon.player.hasRelic("Busted Crown")) {
            AbstractDungeon.player.getRelic("Busted Crown").flash();
        }

        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
            BaseMod.openCustomScreen(EnumPatch.SIDEDECK_SCREEN, cards, card_misc, this);
            AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        }
        return false;
    }
}
