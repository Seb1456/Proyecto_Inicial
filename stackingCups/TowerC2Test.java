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
     * Verifica que una copa se agregue correctamente a la torre
     */
    @Test
    public void shouldAddCupCorrectly() {
        tower.pushCup(5);
        assertTrue(tower.ok());
        assertNotNull(getCupByNumber(tower, 5));
    }
    
    /**
     * Verifica que no se permita agregar una copa con un numero existente
     */
    @Test
    public void shouldNotAddDuplicateCup() {
        tower.pushCup(5);
        tower.pushCup(5);
        assertFalse(tower.ok());
    }
    
    /**
     * Verifica que una copa existente se elimine
     */
    @Test
    public void shouldRemoveExistingCup() {
        tower.pushCup(6);
        assertNotNull(getCupByNumber(tower, 6));
        tower.removeCup(6);
        assertTrue(tower.ok());
        assertNull(getCupByNumber(tower, 6));
    }
    
    /**
     * Verifica que al eliminar una copa no existente no se modifique la torre     */
    @Test
    public void shouldNotRemoveNonExistingCup() {
        tower.pushCup(2);
        tower.removeCup(9);
        
        assertFalse(tower.ok());
    }
    
    /**
     * Verifica que una tapa se agregue
     */
    @Test
    public void shouldAddLidCorrectly() {
        tower.pushLid(3);
    
        assertTrue(tower.ok());
        assertNotNull(getLidByCupNumber(tower, 3));
    }
    
    /**
     * Verifica que no se pueda agregar una tapa con un número ya existente
     */
    @Test
    public void shouldNotAddDuplicateLid() {
        tower.pushLid(3);
        tower.pushLid(3);
        
        assertFalse(tower.ok());
    }

    /**
     * Verifica que una tapa existente se elimine 
     */
    @Test
    public void shouldRemoveExistingLid() {
        tower.pushLid(4);
        assertNotNull(getLidByCupNumber(tower, 4));
        tower.removeLid(4);
        
        assertTrue(tower.ok());
        assertNull(getLidByCupNumber(tower, 4));
    }
    
    /**
     * Verifica que al eliminar una tapa no eexistente no modifique la torre
     */
    @Test
    public void shouldNotRemoveNonExistingLid() {
        tower.pushLid(2);
        tower.removeLid(9);
        
        assertFalse(tower.ok());
    }
    
    /**
     * Verifica que el intercambio entre dos copas sea correcto
     */
    @Test
    public void shouldSwapCupsPositions() {
        tower.pushCup(1);
        tower.pushCup(2);
        String[] c1 = {"cup", "1"};
        String[] c2 = {"cup", "2"};
        tower.swap(c1, c2);
    
        assertTrue(tower.ok());
        assertNotNull(getCupByNumber(tower, 1));
        assertNotNull(getCupByNumber(tower, 2));
    }
    
    /**
     * Verifica que no se realice un intercambio si uno de los objetos no existe 
     */
    @Test
    public void shouldNotSwapWithNonExistingElement() {
        tower.pushCup(1);
        String[] c1 = {"cup", "1"};
        String[] c2 = {"cup", "5"};
        tower.swap(c1, c2);
        assertFalse(tower.ok());
    }
    
    /**
     * Verifica que la altura total de la torre se calcule correctamente
     */
    @Test
    public void shouldCalculateHeight() {
        tower.pushCup(1);
        tower.pushCup(2);
        int height = tower.height();
    
        assertTrue(height > 0);
    }
    
    /**
     * Verifica que la altura visual sea menor o igual a la altura total de la torre
     */
    @Test
    public void shouldHaveValidVisualHeight() {
        tower.pushCup(1);
        tower.pushCup(2);
        int total = tower.height();
        int visual = tower.heightVisual();
    
        assertTrue(visual <= total);
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
    
    /**
     * Verifica que el método solve retorne "impossible"
     */
    @Test
    public void shouldReturnImpossibleWhenNoSolutionExists() {
        TowerContest contest = new TowerContest();
        String result = contest.solve(2, 100);
    
        assertEquals("impossible", result);
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