package foo;

import com.groupon.jtier.daas.postgres.PostgresRule;

import org.junit.Rule;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import java.util.ArrayList;
import java.util.List;

public class TestFoo {

    @Rule
    public PostgresRule postgres = PostgresRule.withNewDatabase();

    interface IFoo<T> {
        int getOne();
        List<T> getStuff(int n);
    }

    static class IntList extends ArrayList<Integer> { }

    interface IBar extends IFoo<Integer> {
        @Override
        IntList getStuff(int n);
    }

    public static abstract class Foo implements IBar {
        @SqlQuery("SELECT 1")
        public abstract int getOne();
    }


    @Test
    public void testFoo() {
        DBI dbi = new DBI(postgres.getTransactionDataSource());
        try {
            dbi.onDemand(Foo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
