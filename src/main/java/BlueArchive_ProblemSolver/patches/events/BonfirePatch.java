package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.exordium.Cleric;
import com.megacrit.cardcrawl.events.shrines.Bonfire;
import com.megacrit.cardcrawl.events.shrines.FaceTrader;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.Circlet;

public class BonfirePatch {

    private static final EventStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getEventString("BlueArchive_ProblemSolver:BonfireElementals");
    }


    @SpirePatch(
            clz = Bonfire.class,
            method = "setReward",
            paramtypez= {
                    AbstractCard.CardRarity.class
            }
    )
    public static class BonfireSetReward {
        public static SpireReturn Prefix(Bonfire __instance, AbstractCard.CardRarity rarity) {
            if(AbstractDungeon.player instanceof ProblemSolver68) {

                String DIALOG_3 = ReflectionHacks.getPrivate(__instance, Bonfire.class, "DIALOG_3");
                float drawX = ReflectionHacks.getPrivate(__instance, AbstractEvent.class, "drawX");
                float drawY = ReflectionHacks.getPrivate(__instance, AbstractEvent.class, "drawY");
                String dialog = DIALOG_3;
                switch (rarity) {
                    case CURSE:
                        dialog = dialog + __instance.DESCRIPTIONS[3];
                        if (!AbstractDungeon.player.hasRelic("Spirit Poop")) {
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, RelicLibrary.getRelic("Spirit Poop").makeCopy());
                        } else {
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(drawX, drawY, new Circlet());
                        }
                        break;
                    case BASIC:
                        dialog = dialog + __instance.DESCRIPTIONS[4];
                        break;
                    case COMMON:
                    case SPECIAL:
                        dialog = dialog + strings.OPTIONS[0];
                        ProblemSolver68.healAll(3);
                        break;
                    case UNCOMMON:
                        dialog = dialog + strings.OPTIONS[1];
                        ProblemSolver68.healAll(-1);
                        break;
                    case RARE:
                        dialog = dialog + strings.OPTIONS[2];
                        ProblemSolver68.increaseMaxHpAll(5, false);
                        ProblemSolver68.healAll(-1);
                }

                __instance.imageEventText.updateBodyText(dialog);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
