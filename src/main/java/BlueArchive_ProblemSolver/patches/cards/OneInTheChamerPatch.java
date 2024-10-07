package BlueArchive_ProblemSolver.patches.cards;

import BlueArchive_ProblemSolver.cards.AbstractDynamicCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.Iterator;

public class OneInTheChamerPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "applyStartOfCombatPreDrawLogic"
    )
    public static class useEnergyPatch {
        public static void Postfix(AbstractPlayer player) {
            for(AbstractCard c :  player.drawPile.group) {
                if (c instanceof AbstractDynamicCard) {
                    ((AbstractDynamicCard)c).atStartOfCombat();
                }
            }
        }
    }

}
