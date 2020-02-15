package command;

//具体命令
class ConcreteCommand implements Command {

    private Receiver receiver;

    public ConcreteCommand() {
        this.receiver = new Receiver();
    }

    public void execute() {
        this.receiver.action();
    }

}