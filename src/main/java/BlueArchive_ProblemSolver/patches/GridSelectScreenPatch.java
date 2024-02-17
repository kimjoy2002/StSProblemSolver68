package BlueArchive_ProblemSolver.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;

@SpirePatch(
        clz = GridCardSelectScreen.class,
        method = "updateCardPositionsAndHoverLogic"
)
public class GridSelectScreenPatch {
    public static boolean centerGridSelect = false;
    private static boolean save_isJustForConfirming = false;

    public GridSelectScreenPatch() {
    }

    public static void Prefix(GridCardSelectScreen __instance) {
        save_isJustForConfirming = __instance.isJustForConfirming;
        if (centerGridSelect) {
            __instance.isJustForConfirming = true;
        }

    }

    public static void Postfix(GridCardSelectScreen __instance) {
        __instance.isJustForConfirming = save_isJustForConfirming;
    }
}