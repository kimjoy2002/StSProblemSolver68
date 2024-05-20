package BlueArchive_ProblemSolver.relics;

import BlueArchive_ProblemSolver.DefaultMod;
import BlueArchive_ProblemSolver.actions.AddCharacterAction;
import BlueArchive_ProblemSolver.actions.ChangeStancOtherPlayereAction;
import BlueArchive_ProblemSolver.actions.IncreaseMaxOrbOtherCharacterAction;
import BlueArchive_ProblemSolver.actions.SolverTalkAction;
import BlueArchive_ProblemSolver.characters.Aru;
import BlueArchive_ProblemSolver.characters.ProblemSolver68;
import BlueArchive_ProblemSolver.powers.OnUsePotionPower;
import BlueArchive_ProblemSolver.util.TextureLoader;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.potions.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

import static BlueArchive_ProblemSolver.DefaultMod.makeRelicOutlinePath;
import static BlueArchive_ProblemSolver.DefaultMod.makeRelicPath;

public class BottleSetRelic extends CustomRelic implements OnUsePotionRelic {

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("BottleSetRelic");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("BottleSetRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("BottleSetRelic.png"));

    public BottleSetRelic() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.CLINK);
    }
    public void OnUsePotion(AbstractPotion potion, AbstractCreature target) {
        if(potion == null) {
            return;
        }
        int potency = (int) ReflectionHacks.getPrivate(potion, AbstractPotion.class, "potency");
        if(AbstractDungeon.player instanceof ProblemSolver68) {
            if(potion instanceof Ambrosia){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ChangeStancOtherPlayereAction("Divinity", ps));
                    }
                }
            }
            else if(potion instanceof AncientPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ApplyPowerAction(ps, ps, new ArtifactPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof BloodPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                            this.addToBot(new HealAction(ps, ps, (int)((float)AbstractDungeon.player.maxHealth * ((float)potency / 100.0F))));
                        } else {
                            AbstractDungeon.player.heal((int)((float)ps.maxHealth * ((float)potency / 100.0F)));
                        }
                    }
                }
            }
            else if(potion instanceof CultistPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        int roll = MathUtils.random(2);
                        if (roll == 0) {
                            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CULTIST_1A"));
                        } else if (roll == 1) {
                            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CULTIST_1B"));
                        } else {
                            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CULTIST_1C"));
                        }
                        AbstractDungeon.actionManager.addToBottom(new SolverTalkAction(ps, Byrd.DIALOG[0], 1.2F, 1.2F));
                        this.addToBot(new ApplyPowerAction(ps, ps, new RitualPower(ps, potency, true), potency));
                    }
                }
            }
            else if(potion instanceof DexterityPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(ps.hb.cX, ps.hb.cY, AbstractGameAction.AttackEffect.SHIELD));
                        this.addToBot(new ApplyPowerAction(ps, ps, new DexterityPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof DuplicationPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ApplyPowerAction(ps, ps, new DuplicationPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof EssenceOfSteel){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ApplyPowerAction(ps, ps, new PlatedArmorPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof FocusPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ApplyPowerAction(ps, ps, new FocusPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof FruitJuice){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (ps != AbstractDungeon.player) {
                        ps.increaseMaxHp(potency, true);
                    }
                }
            }
            else if(potion instanceof GhostInAJar){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ApplyPowerAction(ps, ps, new IntangiblePlayerPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof HeartOfIron){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ApplyPowerAction(ps, ps, new MetallicizePower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof LiquidBronze){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ApplyPowerAction(ps, ps, new ThornsPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof PotionOfCapacity){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new IncreaseMaxOrbOtherCharacterAction(ps, potency));
                    }
                }
            }
            else if(potion instanceof RegenPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ApplyPowerAction(ps, ps, new RegenPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof SpeedPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {

                        this.addToBot(new ApplyPowerAction(ps, ps, new DexterityPower(ps, potency), potency));
                        this.addToBot(new ApplyPowerAction(ps, ps, new LoseDexterityPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof SteroidPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ApplyPowerAction(ps, ps, new StrengthPower(ps, potency), potency));
                        this.addToBot(new ApplyPowerAction(ps, ps, new LoseStrengthPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof StrengthPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && ps != AbstractDungeon.player && ps.currentHealth > 0) {
                        this.addToBot(new ApplyPowerAction(ps, ps, new StrengthPower(ps, potency), potency));
                    }
                }
            }
            else if(potion instanceof BlockPotion){
                flash();
                for(ProblemSolver68 ps : ProblemSolver68.problemSolverPlayer) {
                   if (ps != AbstractDungeon.player && ps.currentHealth > 0) {
                      this.addToBot(new GainBlockAction(ps, ps, potency));
                   }
                }
            }
        }
    }


    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
}
