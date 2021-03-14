import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import org.junit.*;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class MyNetworkTest {
    private Network myNet;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("---before class---");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("---after class---");
    }

    @Before
    public void setUp() throws Exception {
        myNet = new MyNetwork();
        Person p1 = new MyPerson(1, "Yue", new BigInteger("123"), 19);
        Person p2 = new MyPerson(2, "He", new BigInteger("456"), 21);
        try {
            myNet.addPerson(p1);
            myNet.addPerson(p2);
        } catch (EqualPersonIdException e) {
            e.print();
        }

        System.out.println("---before test---");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("---after test---");
    }

    @Test
    public void contains() {
        assertTrue(myNet.contains(1));
        assertFalse(myNet.contains(3));
    }

    @Test
    public void getPerson() {
    }

    @Test(expected = EqualPersonIdException.class)
    public void addPerson() throws EqualPersonIdException {
        Person p1 = new MyPerson(1, "YueYang", new BigInteger("123"), 19);
        myNet.addPerson(p1);
    }

    @Test
    public void addRelation() throws EqualRelationException, PersonIdNotFoundException {
        myNet.addRelation(1, 2, 20);
    }

    @Test
    public void queryValue() {

    }

    @Test
    public void queryConflict() {
    }

    @Test
    public void queryAcquaintanceSum() {
    }

    @Test
    public void compareAge() {
    }

    @Test
    public void compareName() {
    }

    @Test
    public void queryPeopleSum() {
    }

    @Test
    public void queryNameRank() {
    }

    @Test
    public void isCircle() {
    }

}