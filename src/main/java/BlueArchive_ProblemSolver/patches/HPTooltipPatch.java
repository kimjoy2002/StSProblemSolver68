package BlueArchive_ProblemSolver.patches;

import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;

import java.util.Map;
import java.util.TreeMap;

@SpirePatch(
        clz = TopPanel.class,
        method = "updateTips"
)
public class HPTooltipPatch {
    public HPTooltipPatch() {
    }

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn patch(TopPanel __instance) {
        if (AbstractDungeon.player instanceof Aru) {
            float TIP_OFF_X = (Float) ReflectionHacks.getPrivateStatic(TopPanel.class, "TIP_OFF_X");
            float TIP_Y = (Float)ReflectionHacks.getPrivateStatic(TopPanel.class, "TIP_Y");
            String[] LABEL = (String[])ReflectionHacks.getPrivateStatic(TopPanel.class, "LABEL");
            Map<String, Integer> problemSolverHps = new TreeMap();
            Map<String, Integer> problemSolverMaxHps = new TreeMap();
            for (ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                if (ProblemSolver68.isProblemSolver(ps.solverType)) {
                    problemSolverHps.put(ps.name,ps.currentHealth);
                    problemSolverMaxHps.put(ps.name,ps.maxHealth);
                }
            }

            StringBuilder sb = new StringBuilder();
            problemSolverHps.forEach((name, hp) -> {
                if (hp > 0) {
                    sb.append(name);
                    sb.append(": ");
                    sb.append(hp);
                    sb.append("/");
                    sb.append(problemSolverMaxHps.get(name));
                    sb.append(" NL ");
                } else {
                    sb.append(colorifyName(name, 'r'));
                    sb.append(": #r");
                    sb.append(hp);
                    sb.append("/");
                    sb.append(problemSolverMaxHps.get(name));
                    sb.append(" NL ");
                }
            });
            sb.setLength(sb.length() - 4);
            TipHelper.renderGenericTip((float) InputHelper.mX - TIP_OFF_X, TIP_Y, LABEL[3], sb.toString());
            return SpireReturn.Return((Object)null);
        } else {
            return SpireReturn.Continue();
        }
    }

    private static String colorifyName(String s, char color) {
        StringBuilder sb = new StringBuilder();
        String[] var3 = s.split(" ");
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String part = var3[var5];
            sb.append("#");
            sb.append(color);
            sb.append(part);
            sb.append(" ");
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static class Locator extends SpireInsertLocator {
        public Locator() {
        }

        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(TopPanel.class, "hpHb");
            return new int[]{LineFinder.findInOrder(ctBehavior, finalMatcher)[0] + 1};
        }
    }
}