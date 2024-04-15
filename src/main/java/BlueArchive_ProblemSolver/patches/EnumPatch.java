package BlueArchive_ProblemSolver.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class EnumPatch {
    @SpireEnum
    public static AbstractCard.CardTags TACTICAL_CHALLENGE;

    @SpireEnum
    public static RewardItem.RewardType REWORD_SIDEDECK;


    @SpireEnum
    public static AbstractDungeon.CurrentScreen SIDEDECK_SCREEN;

    @SpireEnum
    public static AbstractDungeon.CurrentScreen SIDEDECK_VIEW_SCREEN;

}
