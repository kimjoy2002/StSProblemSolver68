package BlueArchive_ProblemSolver.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;


public class GameActionManagerPatch {

    public static int deadThisCombat = 0;
    public static int increaseMercenaryMaxHP = 0;

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class ThisCombatClearPatch {
        public static void Postfix(GameActionManager __instance)
        {
            deadThisCombat = 0;
            increaseMercenaryMaxHP = 0;
        }
    }

}
