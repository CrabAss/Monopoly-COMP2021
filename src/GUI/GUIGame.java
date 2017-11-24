package GUI;

import Game.Game;
import Others.Input;
import Player.PlayerAI;
import Player.PlayerUser;

public class GUIGame extends Game {

    GUIGame(){super();}

    public void initGame(int NumberOfplayer, int NumberOfAI) {
        setPlayerNumber(NumberOfplayer);

        for (int i = 1; i <= getPlayerNumber(); i++) {
            if (i <= getPlayerNumber() - NumberOfAI) playerList[i - 1] = new PlayerUser("Player " + i, landList[getSTARTLAND()]);
            else playerList[i - 1] = new PlayerAI("Player " + i, landList[getSTARTLAND()]);
        }
        setRounds(0);
        setPlayerAlive(getPlayerNumber());
    }

}
