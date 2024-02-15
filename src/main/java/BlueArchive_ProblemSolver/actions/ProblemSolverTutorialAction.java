package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.ui.ProblemSolverTutorial;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static BlueArchive_ProblemSolver.DefaultMod.*;

public class ProblemSolverTutorialAction extends AbstractGameAction {

    public ProblemSolverTutorialAction() {
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (activeTutorial) {
            AbstractDungeon.ftue = new ProblemSolverTutorial();
            activeTutorial = false;

            try {
                SpireConfig config = new SpireConfig("BlueArchive_ProblemSolver", "BlueArchiveConfig", problemSolverSettings);
                config.setBool(ACTIVE_TUTORIAL, activeTutorial);
                config.save();
            } catch (Exception ignore) {
            }
        }
        this.isDone = true;
    }
}
