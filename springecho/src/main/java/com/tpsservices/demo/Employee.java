public class Employee {

    private long employeeId = -1;
    private String name;
    private String region;
    private double dosh;
	
    public Employee() { }
	
    public Employee(int employeeId, String name, double dosh, String region) {
        this.employeeId = employeeId;
        this.name = name;
        this.dosh = dosh;
        this.region = region;
    }

    // Plus getters, setters, toString()
}
