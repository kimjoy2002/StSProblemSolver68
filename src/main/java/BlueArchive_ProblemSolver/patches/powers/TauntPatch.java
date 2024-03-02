package BlueArchive_ProblemSolver.patches.powers;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.FreeCardPower;
import BlueArchive_ProblemSolver.powers.OnEndOverCharacterPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class TauntPatch {

    @SpirePatch(
            clz = AbstractRoom.class,
            method = "endTurn"
    )
    public static class EndTurnPatch {
        public static void Prefix(AbstractRoom __instance) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    for(AbstractPower power_ : ps.powers) {
                        if(power_ instanceof OnEndOverCharacterPower) {
                            ((OnEndOverCharacterPower)power_).atEndOfTurnOverCharacer();
                        }
                    }
                }
            }
        }
    }
}
