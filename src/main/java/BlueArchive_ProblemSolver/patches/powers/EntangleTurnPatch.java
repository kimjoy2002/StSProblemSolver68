package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.powers.EntangleTurnPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EntangleTurnPatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "hasEnoughEnergy"
    )
    public static class hasEnoughEnergyPatch {
        public static SpireReturn Prefix(AbstractCard __instance) {
            if(!AbstractDungeon.actionManager.turnHasEnded) {
                if (AbstractDungeon.player.hasPower(EntangleTurnPower.POWER_ID) && __instance.type == AbstractCard.CardType.ATTACK) {
                    __instance.cantUseMessage = AbstractCard.TEXT[10];
                    return SpireReturn.Return(false);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
