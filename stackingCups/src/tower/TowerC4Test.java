package tower;

import shapes.Canvas;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TowerC4Test {

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

    // OpenerCup

    /**
     * Verifica que la OpenerCup elimine todas las tapas presentes al ser apilada.
     */
    @Test
    public void openerCupShouldRemoveAllLidsWhenPushed() {
        tower.pushCup(3);
        tower.pushLid(3);
        assertNotNull(getLidByNumber(tower, 3));
        tower.pushCup("opener", 5);
        assertTrue(tower.ok());
        assertNull(getLidByNumber(tower, 3));
    }

    /**
     * Verifica que la OpenerCup elimine múltiples tapas al ser apilada.
     */
    @Test
    public void openerCupShouldRemoveMultipleLids() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushLid(1);
        tower.pushLid(2);
        tower.pushCup("opener", 4);
        assertTrue(tower.ok());
        assertNull(getLidByNumber(tower, 1));
        assertNull(getLidByNumber(tower, 2));
    }

    /**
     * Verifica que la OpenerCup funcione correctamente si no hay tapas.
     */
    @Test
    public void openerCupShouldWorkWithNoLids() {
        tower.pushCup(2);
        tower.pushCup("opener", 4);
        assertTrue(tower.ok());
    }

    // HierarchicalCup

    /**
     * Verifica que la HierarchicalCup quede debajo de las copas más pequeñas.
     */
    @Test
    public void hierarchicalCupShouldSinkBelowSmallerCups() {
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup("hierarchical", 4);
        assertTrue(tower.ok());
        assertEquals(4, tower.cups.get(0).getNumber());
    }

    /**
     * Verifica que la HierarchicalCup no pueda eliminarse si está en el fondo.
     */
    @Test
    public void hierarchicalCupAtBottomCannotBeRemoved() {
        tower.pushCup("hierarchical", 5);
        tower.removeCup(5);
        assertFalse(tower.ok());
        assertNotNull(getCupByNumber(tower, 5));
    }

    /**
     * Verifica que la HierarchicalCup sí pueda eliminarse si no está en el fondo.
     */
    @Test
    public void hierarchicalCupNotAtBottomCanBeRemoved() {
        tower.pushCup("hierarchical", 3);
        tower.pushCup("hierarchical", 5);
        tower.removeCup(3);
        assertTrue(tower.ok());
        assertNull(getCupByNumber(tower, 3));
    }

    //CoverCup

        /**
     * Verifica que la CoverCup genera su propia tapa al ser apilada.
     */
    @Test
    public void coverCupShouldGenerateItsOwnLidWhenPushed() {
        tower.pushCup("cover", 3);
        assertTrue(tower.ok());
        assertNotNull(getLidByNumber(tower, 3));
    }

    /**
     * Verifica que la tapa generada tiene el mismo número que la CoverCup.
     */
    @Test
    public void coverCupLidNumberMatchesCupNumber() {
        tower.pushCup("cover", 5);
        Lid lid = getLidByNumber(tower, 5);
        assertNotNull(lid);
        assertEquals(5, lid.getNumber());
    }

    /**
     * Verifica que la CoverCup no genera una segunda tapa si ya existe una con su número.
     */
    @Test
    public void coverCupShouldNotDuplicateLidIfAlreadyExists() {
        tower.pushLid(4);
        tower.pushCup("cover", 4);
        int count = 0;
        for (Lid l : tower.lids) {
            if (l.getNumber() == 4) count++;
        }
        assertEquals(1, count);
    }

    /**
     * Verifica que múltiples CoverCups generan cada una su propia tapa.
     */
    @Test
    public void multipleCoverCupsShouldGenerateTheirOwnLids() {
        tower.pushCup("cover", 2);
        tower.pushCup("cover", 5);
        assertNotNull(getLidByNumber(tower, 2));
        assertNotNull(getLidByNumber(tower, 5));
    }

    /**
     * Verifica que la CoverCup puede eliminarse normalmente.
     */
    @Test
    public void coverCupCanBeRemoved() {
        tower.pushCup("cover", 3);
        tower.removeCup(3);
        assertTrue(tower.ok());
        assertNull(getCupByNumber(tower, 3));
    }

    /**
     * Verifica que pushCup("cover", n) crea efectivamente una instancia de CoverCup.
     */
    @Test
    public void pushCupCoverShouldCreateCoverCupInstance() {
        tower.pushCup("cover", 4);
        Cup c = getCupByNumber(tower, 4);
        assertNotNull(c);
        assertInstanceOf(CoverCup.class, c);
    }

    /**
     * Verifica que la CoverCup aparece correctamente en la torre después de ser apilada.
     */
    @Test
    public void coverCupShouldExistInTowerAfterPush() {
        tower.pushCup("cover", 6);
        assertTrue(tower.ok());
        assertNotNull(getCupByNumber(tower, 6));
    }

    /**
     * Verifica que la tapa generada por CoverCup puede eliminarse normalmente.
     */
    @Test
    public void lidGeneratedByCoverCupCanBeRemoved() {
        tower.pushCup("cover", 3);
        assertNotNull(getLidByNumber(tower, 3));
        tower.removeLid(3);
        assertTrue(tower.ok());
        assertNull(getLidByNumber(tower, 3));
    }

    /**
     * Verifica que una CoverCup coexiste bien con otras copas y sus tapas.
     */
    @Test
    public void coverCupCoexistsWithOtherCupsAndLids() {
        tower.pushCup(2);
        tower.pushLid(2);
        tower.pushCup("cover", 5);
        assertNotNull(getLidByNumber(tower, 2));
        assertNotNull(getLidByNumber(tower, 5));
        assertTrue(tower.ok());
    }

    // Helpers

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

    // FearfulLid

    /**
     * Verifica que la FearfulLid no entre si su taza compañera no está en la torre.
     */
    @Test
    public void fearfulLidShouldNotEnterWithoutCup() {
        tower.pushLid("fearful", 3);
        assertFalse(tower.ok());
        assertNull(getLidByNumber(tower, 3));
    }

    /**
     * Verifica que la FearfulLid entre correctamente si su taza está presente.
     */
    @Test
    public void fearfulLidShouldEnterWhenCupIsPresent() {
        tower.pushCup(3);
        tower.pushLid("fearful", 3);
        assertTrue(tower.ok());
        assertNotNull(getLidByNumber(tower, 3));
    }

    /**
     * Verifica que la FearfulLid no pueda eliminarse mientras tapa a su taza.
     */
    @Test
    public void fearfulLidCannotBeRemovedWhenCovering() {
        tower.pushCup(3);
        tower.pushLid("fearful", 3);
        tower.cover();
        tower.removeLid(3);
        assertFalse(tower.ok());
        assertNotNull(getLidByNumber(tower, 3));
    }

    /**
     * Verifica que la FearfulLid sí pueda eliminarse si no está tapando su taza.
     */
    @Test
    public void fearfulLidCanBeRemovedWhenNotCovering() {
        tower.pushCup(3);
        tower.pushLid("fearful", 3);
        tower.removeLid(3);
        assertTrue(tower.ok());
        assertNull(getLidByNumber(tower, 3));
    }

    // CrazyLid

    /**
     * Verifica que la CrazyLid pueda agregarse correctamente a la torre.
     */
    @Test
    public void crazyLidShouldBeAddedCorrectly() {
        tower.pushLid("crazy", 2);
        assertTrue(tower.ok());
        assertNotNull(getLidByNumber(tower, 2));
    }

    /**
     * Verifica que la CrazyLid pueda eliminarse normalmente.
     */
    @Test
    public void crazyLidCanBeRemoved() {
        tower.pushLid("crazy", 2);
        tower.removeLid(2);
        assertTrue(tower.ok());
        assertNull(getLidByNumber(tower, 2));
    }

    // Helpers

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
