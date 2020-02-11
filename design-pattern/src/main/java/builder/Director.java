package builder;

public class Director {

    public Robot createRobotByDirecotr(IBuildRobot ibr) {
        ibr.buildBody();
        ibr.buildFoot();
        ibr.buildHand();
        ibr.buildHead();
        return ibr.createRobot();
    }
}