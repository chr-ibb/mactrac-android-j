package chribb.mactrac;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import chribb.mactrac.data.Favorite;
import chribb.mactrac.data.FavoriteTrie;

import static org.junit.Assert.*;


public class FavoriteTrieTest {

    @Test
    public void testSize() {
        List<Favorite> basicFavorites = createBasicTestList();
        FavoriteTrie fTrie = new FavoriteTrie(basicFavorites);

        int expectedSize = basicFavorites.size();
        assertEquals(expectedSize, fTrie.size());

        Favorite aFavorite = basicFavorites.get(3);
        fTrie.delete(aFavorite);
        assertEquals(--expectedSize, fTrie.size());

        fTrie.add(aFavorite);
        assertEquals(++expectedSize, fTrie.size());

    }

    @Test
    public void testOrder() {
        List<Favorite> basicFavorites = createBasicTestList();
        FavoriteTrie fTrie = new FavoriteTrie(basicFavorites);

        boolean isSorted = true;
        for (int i = 1; i < basicFavorites.size(); i++) {
            int thisCount = basicFavorites.get(i).getCount();
            int previousCount = basicFavorites.get(i - 1).getCount();
            if (thisCount > previousCount) {
                isSorted = false;
            }
        }
        assertFalse(isSorted);

        isSorted = true;
        List<Favorite> sorted = fTrie.sortedFavoritesWithPrefix("Test");
        for (int i = 1; i < sorted.size(); i++) {
            int thisCount = sorted.get(i).getCount();
            int previousCount = sorted.get(i - 1).getCount();
            if (thisCount > previousCount) {
                isSorted = false;
            }
        }
        assertTrue(isSorted);
    }



    private List<Favorite> createBasicTestList() {
        List<Favorite> testList = new ArrayList<>();

        Favorite dinner = new Favorite("TestDinner", 0, 0, 0, 0, 1);
        Favorite sandwich = new Favorite("TestSandwich", 0, 0, 0, 0, 2);
        Favorite banana = new Favorite("TestBanana", 0, 0, 0, 0, 3);
        Favorite cookie = new Favorite("TestCookie", 0, 0, 0, 0, 4);
        Favorite lunch = new Favorite( "TestLunch", 0, 0, 0, 0, 5);
        Favorite fish = new Favorite("TestFish", 0, 0, 0 , 0, 6);
        Favorite breakfast = new Favorite("TestBreakfast", 0, 0, 0, 0, 7);
        Favorite chicken = new Favorite("TestChicken", 0, 0, 0, 0, 8);
        Favorite apple = new Favorite("TestApple", 0, 0, 0, 0, 9);
        Favorite snack = new Favorite("TestSnack", 0, 0, 0, 0, 10);

        testList.add(fish);
        testList.add(dinner);
        testList.add(banana);
        testList.add(breakfast);
        testList.add(sandwich);
        testList.add(snack);
        testList.add(cookie);
        testList.add(lunch);
        testList.add(chicken);
        testList.add(apple);

        return testList;
    }
}
