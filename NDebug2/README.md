# Application Debugging

This project is a program for creating bugs in production environment.  The aim is to determine if people can identify the issue with an application.

## Usage

DBrootPW="my-secret-pw"
DBuserName="trades"
DBuserBadPW="test"
DBuserPW="trades"
export DBrootPW DBuserName DBuserBadPW DBuserPW

java -jar NDebug2.jar 3 61616 activemq

### Usage documentation

The script requires some environment variables to be configured prior to running;

* DBrootPW
* DBuserName
* DBuserBadPW
* DBuserPW

The script will also take a number of arguments which are dependent on the numerical value supplied as the first argument.

If the first argument is;

3. Then 2 extra parameters are required.
   * Port Number
   * Service Name (as in the Linux or Windows service name, must be exact and is case sensitive)

4. A single parameter is requied.
   * Number of seconds to delay.

7. A single parameter is required.
   * The amount to multiply the price by.

8. A single parameter is required.
   * Number of processes to run to slow the system down.
