package user;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.time.LocalDate;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
        jdbi.installPlugin(new SqlObjectPlugin());
        long id;
        User imi;
        User petra = new User();
        Optional user;

        try (Handle handle = jdbi.open()) {
            UserDao dao = handle.attach(UserDao.class);
            dao.createTable();
            id = dao.insert(User.builder().username("imi")
                    .password("secret").name("Imre").email("imi@imi.com")
                    .gender(User.Gender.MALE).dob(LocalDate.of(1999,1,4))
                    .enabled(true).build());
            dao.insert(User.builder().username("petra")
                    .password("secret2").name("Petra").email("petra@petra.com")
                    .gender(User.Gender.FEMALE).dob(LocalDate.of(1999,9,12))
                    .enabled(true).build());

            user = dao.findById(id);

            if(user.isPresent()){
                imi = (User)user.get();
            }

            user = dao.findByUsername("petra");

            if(user.isPresent()){
                petra = (User)user.get();
            }

            dao.list().stream().forEach(System.out::println);

            dao.delete(petra);


        }
    }
}
