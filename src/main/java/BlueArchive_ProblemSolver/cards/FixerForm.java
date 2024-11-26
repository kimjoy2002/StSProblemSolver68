package BlueArchive_ProblemSolver.cards;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.ApplyPowerToSpecificAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BerserkPower;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import com.megacrit.cardcrawl.powers.DrawPower;

import java.util.ArrayList;
import java.util.Collections;

import static BlueArchive_ProblemSolver.DefaultMod.makeCardPath;

public class FixerForm extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(FixerForm.class.getSimpleName());
    public static final String IMG = makeCardPath("FixerForm.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Aru.Enums.COLOR_RED;

    private static final int COST = 3;
    private static final int MAGIC = 1;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    private static final int MAGIC2 = 1;
    private static final int UPGRADE_PLUS_MAGIC2 = 1;
    private static final int MAGIC3 = 1;
    private static final int UPGRADE_PLUS_MAGIC3 = 1;


    public FixerForm() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagicNumber = secondMagicNumber = MAGIC2;
        baseThirdMagicNumber = thirdMagicNumber = MAGIC3;
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
        if(cardStrings.EXTENDED_DESCRIPTION != null && cardStrings.EXTENDED_DESCRIPTION.length > 0) {
            if (GameDictionary.keywords.containsKey(cardStrings.EXTENDED_DESCRIPTION[0].toLowerCase())) {
                if (!this.keywords.contains(cardStrings.EXTENDED_DESCRIPTION[0].toLowerCase())) {
                    this.keywords.add(cardStrings.EXTENDED_DESCRIPTION[0].toLowerCase());
                }
            }
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerToSpecificAction(new EnergyupPower(AbstractDungeon.player, upgraded?2:1),  upgraded?2:1, Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_ARU));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerToSpecificAction(new DrawPower(p, this.magicNumber), magicNumber, Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_KAYOKO));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerToSpecificAction(new StrupPower(AbstractDungeon.player, secondMagicNumber), secondMagicNumber, Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_MUTSUKI));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerToSpecificAction(new DexupPower(AbstractDungeon.player, thirdMagicNumber), thirdMagicNumber, Aru.ProblemSolver68Type.PROBLEM_SOLVER_68_HARUKA));
        } else {
            ArrayList<Integer> array_power = new ArrayList();
            array_power.add(0);
            array_power.add(1);
            array_power.add(2);
            array_power.add(3);
            Collections.shuffle(array_power, new java.util.Random(AbstractDungeon.miscRng.randomLong()));

            for(int i = 0; i < 2; i++) {
                switch (array_power.get(i)) {
                    case 0:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyupPower(AbstractDungeon.player,  upgraded?2:1),  upgraded?2:1));
                        break;
                    case 1:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DrawPower(AbstractDungeon.player, this.magicNumber), magicNumber));
                        break;
                    case 2:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrupPower(AbstractDungeon.player, secondMagicNumber), secondMagicNumber));
                        break;
                    case 3:
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexupPower(AbstractDungeon.player, thirdMagicNumber), thirdMagicNumber));
                        break;
                }
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            upgradeSecondMagicNumber(UPGRADE_PLUS_MAGIC2);
            upgradeThirdMagicNumber(UPGRADE_PLUS_MAGIC3);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
