package tower;

import shapes.Canvas;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TowerC2Test {

    private Tower tower;

    @BeforeAll
    static void disableCanvas() {
        Canvas.setEnabled(false);
    }

    @BeforeEach
    void setUp() {
        tower = new Tower(10, 20);
        tower.makeInvisible();
    }

    @Test
    public void shouldAddCupCorrectly() {
        tower.pushCup(5);
        assertTrue(tower.ok());
        assertNotNull(getCupByNumber(tower, 5));
    }

    @Test
    public void shouldNotAddDuplicateCup() {
        tower.pushCup(5);
        tower.pushCup(5);
        assertFalse(tower.ok());
    }

    @Test
    public void shouldRemoveExistingCup() {
        tower.pushCup(6);
        assertNotNull(getCupByNumber(tower, 6));
        tower.removeCup(6);
        assertTrue(tower.ok());
        assertNull(getCupByNumber(tower, 6));
    }

    @Test
    public void shouldNotRemoveNonExistingCup() {
        tower.pushCup(2);
        tower.removeCup(9);
        assertFalse(tower.ok());
    }

    @Test
    public void shouldAddLidCorrectly() {
        tower.pushLid(3);
        assertTrue(tower.ok());
        assertNotNull(getLidByNumber(tower, 3));
    }

    @Test
    public void shouldNotAddDuplicateLid() {
        tower.pushLid(3);
        tower.pushLid(3);
        assertFalse(tower.ok());
    }

    @Test
    public void shouldRemoveExistingLid() {
        tower.pushLid(4);
        assertNotNull(getLidByNumber(tower, 4));
        tower.removeLid(4);
        assertTrue(tower.ok());
        assertNull(getLidByNumber(tower, 4));
    }

    @Test
    public void shouldNotRemoveNonExistingLid() {
        tower.pushLid(2);
        tower.removeLid(9);
        assertFalse(tower.ok());
    }

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

    @Test
    public void shouldNotSwapWithNonExistingElement() {
        tower.pushCup(1);
        String[] c1 = {"cup", "1"};
        String[] c2 = {"cup", "5"};
        tower.swap(c1, c2);
        assertFalse(tower.ok());
    }

    @Test
    public void shouldCalculateHeight() {
        tower.pushCup(1);
        tower.pushCup(2);
        assertTrue(tower.height() > 0);
    }

    @Test
    public void shouldHaveValidVisualHeight() {
        tower.pushCup(1);
        tower.pushCup(2);
        assertTrue(tower.heightVisual() <= tower.height());
    }

    @Test
    public void shouldCoverAllCupsWithoutLid() {
        tower.pushCup(3);
        tower.pushCup(7);
        tower.pushCup(1);
        assertNull(getLidByNumber(tower, 3));
        assertNull(getLidByNumber(tower, 7));
        assertNull(getLidByNumber(tower, 1));
        tower.cover();
        assertTrue(tower.ok());
        assertNotNull(getLidByNumber(tower, 3));
        assertNotNull(getLidByNumber(tower, 7));
        assertNotNull(getLidByNumber(tower, 1));
        assertEquals(3, getLidByNumber(tower, 3).getNumber());
        assertEquals(7, getLidByNumber(tower, 7).getNumber());
        assertEquals(1, getLidByNumber(tower, 1).getNumber());
    }

    @Test
    public void shouldNotCoverCupsThatAlreadyHaveLid() {
        tower.pushCup(4);
        tower.pushCup(9);
        tower.pushLid(4);
        assertNotNull(getLidByNumber(tower, 4));
        assertNull(getLidByNumber(tower, 9));
        tower.cover();
        assertTrue(tower.ok());
        assertNotNull(getLidByNumber(tower, 4));
        assertEquals(4, getLidByNumber(tower, 4).getNumber());
        assertNotNull(getLidByNumber(tower, 9));
        assertEquals(9, getLidByNumber(tower, 9).getNumber());
    }

    @Test
    public void shouldReturnSwapWhenBeneficialOrderExists() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        String[][] result = tower.swapToReduce();
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    public void shouldReturnNullWhenNoSwapReducesHeight() {
        tower.pushCup(3);
        tower.pushCup(2);
        tower.pushCup(1);
        assertNull(tower.swapToReduce());
    }

    @Test
    public void shouldReturnImpossibleWhenNoSolutionExists() {
        TowerContest contest = new TowerContest();
        assertEquals("impossible", contest.solve(2, 100));
    }
    
    //Pruebas de compañeros
    /**
    * Valida que para n = 4 y h = 11 el resultado debe ser 1 3 7 5
    */
    @Test
    public void accordingCOShouldContestSolveReturn1375(){
        TowerContest towerContest = new TowerContest();
        String result = towerContest.solve(4,11);
        assertEquals("1 3 7 5", result);
    }
        
    /**
    * Valida que para n = 5 y h = 11 el resultado debe ser 9 3 7 5 1
    */
    @Test
    public void accordingCOShouldTrivialSolutionReturn93751(){
        TowerContest towerContest = new TowerContest();
        String result = towerContest.solve(5, 11);
        assertEquals("9 3 7 5 1", result);
    }
    
      /**
     * Valida que para n=4 y h=12 el resultado es 5 3 1 7
     */
    public void testAccordingORShouldSolveN_4H_12_Return5317()
    {
        TowerContest towerContest = new TowerContest();
        String result = towerContest.solve(4, 12);
        assertEquals("5 3 1 7", result);
    }
    
    /**
     * Valida que para n=5 y h=16 el resultado es 7 5 3 1 9
     */
    public void testAccordingORShouldSolveN_5H_16_Return75319()
    {
        TowerContest towerContest = new TowerContest();
        String result = towerContest.solve(5, 16);
        assertEquals("7 5 3 1 9", result);
    }
    
    private Cup getCupByNumber(Tower t, int number) {
        for (Cup c : t.cups) {
            if (c.getNumber() == number) return c;
        }
        return null;
    }

    private Lid getLidByNumber(Tower t, int number) {
        for (Lid l : t.lids) {
            if (l.getNumber() == number) return l;
        }
        return null;
    }
}
