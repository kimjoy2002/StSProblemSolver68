package BlueArchive_ProblemSolver.actions;

import BlueArchive_ProblemSolver.cards.ChooseRabu;
import BlueArchive_ProblemSolver.cards.ChooseSaori;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.ArrayList;

public class HireHelmetLeaderAction extends AbstractGameAction {

    public HireHelmetLeaderAction(int amount) {
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        this.isDone = true;
        ArrayList<AbstractCard> optionChoices = new ArrayList();

        optionChoices.add(new ChooseRabu(amount));
        optionChoices.add(new ChooseSaori(amount));
        InputHelper.moveCursorToNeutralPosition();

        this.addToBot(new ChooseOneAction(optionChoices));
    }
}
