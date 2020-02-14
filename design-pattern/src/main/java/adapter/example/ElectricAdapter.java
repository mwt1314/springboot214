package adapter.example;

//电能适配器
public class ElectricAdapter implements Motor {

    private ElectricMotor emotor;

    public ElectricAdapter() {
        emotor = new ElectricMotor();
    }

    @Override
    public void drive() {
        emotor.electricDrive();
    }
}