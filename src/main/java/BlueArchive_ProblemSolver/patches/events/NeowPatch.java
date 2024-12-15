package BlueArchive_ProblemSolver.patches.events;

import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.ShiningLight;
import com.megacrit.cardcrawl.events.shrines.Designer;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import javassist.CtBehavior;

import java.util.ArrayList;

public class NeowPatch {

    private static final CharacterStrings strings;
    static {
        strings = CardCrawlGame.languagePack.getCharacterString("BlueArchive_ProblemSolver:NeowReward");
    }
    @SpirePatch(
            clz = NeowReward.NeowRewardDef.class,
            method = "<ctor>"
    )
    public static class NeowRewardDefPatch {
        public static void Postfix(NeowReward.NeowRewardDef __instance, NeowReward.NeowRewardType type, String desc) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                switch (type) {
                    case TEN_PERCENT_HP_BONUS:
                        __instance.desc = strings.TEXT[0] + (int)((float)AbstractDungeon.player.maxHealth * 0.1F) + " ]";
                        break;
                    case TWENTY_PERCENT_HP_BONUS:
                        __instance.desc = strings.TEXT[1] + (int)((float)AbstractDungeon.player.maxHealth * 0.2F) + " ]";
                        break;
                }
            }
        }
    }


    @SpirePatch(
            clz = NeowReward.NeowRewardDrawbackDef.class,
            method = "<ctor>"
    )
    public static class NeowRewardDrawbackDefPatch {
        public static void Postfix(NeowReward.NeowRewardDrawbackDef __instance, NeowReward.NeowRewardDrawback type, String desc) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                switch (type) {
                    case TEN_PERCENT_HP_LOSS:
                        __instance.desc = strings.TEXT[2]+ (int)((float)AbstractDungeon.player.maxHealth * 0.1F) + strings.TEXT[3];
                        break;
                    case PERCENT_DAMAGE:
                        __instance.desc = strings.TEXT[4] + AbstractDungeon.player.currentHealth / 10 * 3 + NeowReward.TEXT[29] + " ";
                        break;
                }
            }
        }
    }


    @SpirePatch(
            clz = NeowReward.class,
            method = "activate"
    )
    public static class NeowRewardActivate {
        public static void Postfix(NeowReward __instance) {
            if (AbstractDungeon.player instanceof ProblemSolver68) {
                int hp_bonus = (Integer) ReflectionHacks.getPrivate(__instance, NeowReward.class, "hp_bonus");
                switch (__instance.drawback) {
                    case TEN_PERCENT_HP_LOSS:
                        ProblemSolver68.decreaseMaxHpAll(hp_bonus, AbstractDungeon.player);
                        break;
                    case PERCENT_DAMAGE:{
                        AbstractPlayer player = AbstractDungeon.player;
                        for(AbstractPlayer ps : ProblemSolver68.problemSolverPlayer) {
                            if(ps != player) {
                                player = ps;
                                break;
                            }
                        }
                        ProblemSolver68.damageAll(player.currentHealth / 10 * 3, AbstractDungeon.player);
                        break;

                    }
                }

                switch (__instance.type) {
                    case TEN_PERCENT_HP_BONUS:
                        ProblemSolver68.increaseMaxHpAll(hp_bonus, true, AbstractDungeon.player);
                        break;
                    case TWENTY_PERCENT_HP_BONUS:
                        ProblemSolver68.increaseMaxHpAll(hp_bonus * 2, true, AbstractDungeon.player);
                        break;
                }
            }
        }
    }
}
