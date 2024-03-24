package BlueArchive_ProblemSolver.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnApplyedPower {
    public void OnApplyed(AbstractPower power, AbstractCreature target, AbstractCreature source);
}
