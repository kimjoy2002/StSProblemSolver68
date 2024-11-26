package BlueArchive_ProblemSolver.patches.relics;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.OneTurnConfusionPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.relics.PotionBelt;
import javassist.CtBehavior;

public class PotionBeltPatch {

    @SpirePatch(
            clz = PotionBelt.class,
            method = "onEquip"
    )
    public static class DrawPatch {
        public static void Postfix(PotionBelt __instance) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer){
                    if(ps != AbstractDungeon.player) {
                        ps.potionSlots += 2;
                    }
                }
            }
        }
    }
}
