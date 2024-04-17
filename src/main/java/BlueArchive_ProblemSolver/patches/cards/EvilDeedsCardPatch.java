package BlueArchive_ProblemSolver.patches.cards;

import BlueArchive_ProblemSolver.cards.EvilDeedsCard;
import BlueArchive_ProblemSolver.cards.onAddToHandCards;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class EvilDeedsCardPatch {
    @SpirePatch(
            clz = CardGroup.class,
            method = "addToHand",
            paramtypez= {
                    AbstractCard.class
            }
    )
    public static class addToHandPatcher {

        public static void Postfix(CardGroup __instance, AbstractCard card)
        {
            if(AbstractDungeon.currMapNode != null &&
                    AbstractDungeon.getCurrRoom() != null &&
                    AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                    __instance.type == CardGroup.CardGroupType.HAND &&
                    card instanceof onAddToHandCards){
                ((onAddToHandCards)card).onAddToHand();
            }
        }
    }
}
