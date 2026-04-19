package tower;
import static org.junit.Assert.*;
import org.junit.Test;

public class TowerContestCTest {

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
    @Test
    public void testAccordingORShouldSolveN_4H_12_Return5317()
    {
        TowerContest towerContest = new TowerContest();
        String result = towerContest.solve(4, 12);
        assertEquals("5 3 1 7", result);
    }

    /**
     * Valida que para n=5 y h=16 el resultado es 7 5 3 1 9
     */
    @Test
    public void testAccordingORShouldSolveN_5H_16_Return75319()
    {
        TowerContest towerContest = new TowerContest();
        String result = towerContest.solve(5, 16);
        assertEquals("7 5 3 1 9", result);
    }
}