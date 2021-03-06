package Cmd;

import Cmd.Others.Dice;
import Cmd.Player.Player;
import Cmd.Player.PlayerUser;
import org.junit.Assert;
import org.junit.Test;

/**
 * test Dice
 */
public class DiceTest {
    private Dice dice = new Dice();
    private Player player = new PlayerUser("Player 0", null);

    /**
     * Test dice
     */
    @Test
    public void test() {
        CmdTest.redirectOutput();
        dice.dice();
        Assert.assertEquals(dice.getX() + dice.getY(), dice.getStep());
        if (dice.getX() != dice.getY())
            Assert.assertFalse(dice.isEqual());
        else
            Assert.assertTrue(dice.isEqual());
        dice.dice(player);
    }
}