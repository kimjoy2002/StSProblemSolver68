package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.ChooseDouble;
import BlueArchive_ProblemSolver.cards.ChooseDraw;
import BlueArchive_ProblemSolver.cards.ChooseRabu;
import BlueArchive_ProblemSolver.cards.ChooseSaori;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.ArrayList;

import static BlueArchive_ProblemSolver.actions.FinishAction.getFinishPowerNum;

public class Assault2Action extends AbstractGameAction {

    public Assault2Action() {
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        this.isDone = true;
        if(getFinishPowerNum() == 0) {
            new ChooseDraw().onChoseThisOption();
        } else {
            ArrayList<AbstractCard> optionChoices = new ArrayList();

            optionChoices.add(new ChooseDraw());
            optionChoices.add(new ChooseDouble());
            InputHelper.moveCursorToNeutralPosition();

            this.addToBot(new ChooseOneAction(optionChoices));
        }
    }
}
