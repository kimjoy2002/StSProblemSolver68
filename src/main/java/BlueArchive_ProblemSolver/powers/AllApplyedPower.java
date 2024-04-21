package BlueArchive_ProblemSolver.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface AllApplyedPower {
    public void AllApplyed(AbstractPower power, AbstractCreature target, AbstractCreature source);
}