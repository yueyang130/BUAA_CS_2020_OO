import org.junit.*;

public class MyPersonTest {
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
        System.out.println("---before test---");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("---afeter test---");
    }

    @Test
    public void getId() {
    }

    @Test
    public void getName() {
    }

    @Test
    public void getCharacter() {
    }

    @Test
    public void getAge() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void isLinked() {
    }

    @Test
    public void addAcquaintance() {
    }

    @Test
    public void queryValue() {
    }

    @Test
    public void getAcquaintanceSum() {
    }

    @Test
    public void compareTo() {
    }
}