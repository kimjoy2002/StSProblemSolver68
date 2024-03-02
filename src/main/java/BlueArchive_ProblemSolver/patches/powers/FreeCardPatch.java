package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FreeCardPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class FreeCardPatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "freeToPlay"
    )
    public static class loadPlayerSavePatch {
        public static SpireReturn Prefix(AbstractCard __instance) {
            if (AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                    && AbstractDungeon.player.hasPower(FreeCardPower.POWER_ID)){
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }
}
