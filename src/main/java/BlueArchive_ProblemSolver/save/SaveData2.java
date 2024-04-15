package BlueArchive_ProblemSolver.save;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class SaveData2 {
    public int side_deck_misc;
    public int upgrades;
    public int misc;
    public String id;

    public SaveData2(int side_deck_misc, AbstractCard card) {
        this.side_deck_misc = side_deck_misc;
        this.id = card.cardID;
        this.upgrades = card.timesUpgraded;
        this.misc = card.misc;

    }
}
