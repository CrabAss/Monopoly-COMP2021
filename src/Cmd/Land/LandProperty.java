package Cmd.Land;

import Cmd.Others.BankruptException;
import Cmd.Others.Property;
import Cmd.Others.Output;
import Cmd.Player.Player;

/**
 * The grid with property.
 */
public class LandProperty extends Land {
    private final Property property;

    /**
     * Initialize landProperty.
     * @param property Initialzation value.
     * @param gridNo Initialzation value.
     */
    public LandProperty(Property property, int gridNo) {
        super(property.getName(), gridNo);
        this.property = property;
    }

    /**
     * @return The property of the grid.
     */
    public Property getProperty() {
        return property;
    }

    @Override
    public void run(Player player) throws BankruptException {
        landOn(player);

        String hint = "0: do nothing; 1: rent " + property.toString() + ".";

        if (property.getBelongs() == null) {
            Output.println("No one owns " + property.getName() + " now.");
            int inp = player.getInput(hint, 2);
            if (inp == 0) {
                Output.println(player + " decides to do nothing.");
            }
            else {
                Output.println(player + " decides to rent " + property.toString() + ".");
                player.addProperty(property);
                player.decMoney(property.getPrice());
                property.setBelongs(player);
            }
        } else {
            if (property.getBelongs() == player) {
                Output.println(getName() + " belongs to " + player + " himself. \nNothing happens. ");
            }
            else {
                Output.println(getName() + " belongs to " + property.getBelongs() + " now.");
                Output.println(player + " has to pay to " + property.getBelongs() + ".");
                player.decMoney(property.getRent());
                property.getBelongs().incMoney(property.getRent());
            }
        }
    }
}
