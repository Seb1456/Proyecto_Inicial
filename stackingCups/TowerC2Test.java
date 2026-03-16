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
    
    /**
     * Este test prueba que las copas sin tapa queden tapadas al utilizar cover 
     */
    @Test
    public void shouldCoverAllCupsWithoutLid() {
        tower.pushCup(3);
        tower.pushCup(7);
        tower.pushCup(1);
    
        assertNull(getLidByCupNumber(tower, 3));
        assertNull(getLidByCupNumber(tower, 7));
        assertNull(getLidByCupNumber(tower, 1));
    
        tower.cover();
    
        assertTrue(tower.ok());
    
        assertNotNull(getLidByCupNumber(tower, 3));
        assertNotNull(getLidByCupNumber(tower, 7));
        assertNotNull(getLidByCupNumber(tower, 1));
    
        assertEquals(3, getLidByCupNumber(tower, 3).getNumber());
        assertEquals(7, getLidByCupNumber(tower, 7).getNumber());
        assertEquals(1, getLidByCupNumber(tower, 1).getNumber());
    }
    
        /**
     * Este test prueba que no se vuelvan a tapar las copas que ya tienen tapa
     */
    @Test
    public void shouldNotCoverCupsThatAlreadyHaveLid() {
        tower.pushCup(4);
        tower.pushCup(9);
    
        tower.pushLid(4);
    
        assertNotNull(getLidByCupNumber(tower, 4));
        assertNull(getLidByCupNumber(tower, 9));
    
        tower.cover();
    
        assertTrue(tower.ok());
    
        Lid lid4 = getLidByCupNumber(tower, 4);
        assertNotNull(lid4);
        assertEquals(4, lid4.getNumber());
    
        assertNotNull(getLidByCupNumber(tower, 9));
        assertEquals(9, getLidByCupNumber(tower, 9).getNumber());
    }
    
    private Cup getCupByNumber(Tower t, int number) {
        for (Cup c : t.cups) {
            if (c.getNumber() == number) {
                return c;
            }
        }
        return null;
    }
    
    private Lid getLidByCupNumber(Tower t, int number) {
    for (Lid l : t.lids) {
        if (l.getNumber() == number) {
            return l;
        }
    }
    return null;
}
}