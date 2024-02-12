import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate; 
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

//Configuring a Database
@Component
public class MyDB {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void init() {
		jdbcTemplate.execute("create table EMPLOYEES(" +
				 "employeeid int auto_increment primary key, " + 
				 "name varchar, " + 
				 "salary number, " + 
				 "region varchar)");

		jdbcTemplate.update("insert into EMPLOYEES (name, salary, region) " + 
				"values (?, ?, ?)", 
				 new Object[]{"James", 21000, "London"});

	}
}