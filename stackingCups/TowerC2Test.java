import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TowerC2Test {

    private Tower tower;

    @BeforeEach
    void setUp() {
        tower = new Tower(10, 20);
        tower.makeInvisible();
    }

    @Test
    public void shouldCoverAllCupsWithoutLid() {
        tower.pushCup(3);
        tower.pushCup(7);
        tower.pushCup(1);

        assertNull(getCupByNumber(tower, 3).getLid());
        assertNull(getCupByNumber(tower, 7).getLid());
        assertNull(getCupByNumber(tower, 1).getLid());

        tower.cover();

        assertTrue(tower.ok());
        assertNotNull(getCupByNumber(tower, 3).getLid());
        assertNotNull(getCupByNumber(tower, 7).getLid());
        assertNotNull(getCupByNumber(tower, 1).getLid());

        assertEquals(3, getCupByNumber(tower, 3).getLid().getNumber());
        assertEquals(7, getCupByNumber(tower, 7).getLid().getNumber());
        assertEquals(1, getCupByNumber(tower, 1).getLid().getNumber());
    }

    @Test
    public void shouldNotCoverCupsThatAlreadyHaveLid() {
        tower.pushCup(4);
        tower.pushCup(9);

        tower.pushLid(4);

        assertNotNull(getCupByNumber(tower, 4).getLid());
        assertNull(getCupByNumber(tower, 9).getLid());

        tower.cover();

        assertTrue(tower.ok());

        Lid lid4 = getCupByNumber(tower, 4).getLid();
        assertNotNull(lid4);
        assertEquals(4, lid4.getNumber());

        assertNotNull(getCupByNumber(tower, 9).getLid());
        assertEquals(9, getCupByNumber(tower, 9).getLid().getNumber());
    }
    private Cup getCupByNumber(Tower t, int number) {
        for (Cup c : t.cups) {
            if (c.getNumber() == number) {
                return c;
            }
        }
        return null;
    }
}