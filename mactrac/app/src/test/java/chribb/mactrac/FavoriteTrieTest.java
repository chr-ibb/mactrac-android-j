package chribb.mactrac;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import chribb.mactrac.data.Favorite;
import chribb.mactrac.data.FavoriteTrie;

import static org.junit.Assert.*;


public class FavoriteTrieTest {

    @Test
    public void TestTrie() {
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(new Favorite("TestLunch", 500, 20, 10, 10, 5));
        favorites.add(new Favorite("TestBanana", 100, 1, 0, 20, 3));
        favorites.add(new Favorite("TestSnack", 200, 4, 2, 5, 10));
        favorites.add(new Favorite("TestDinner", 800, 30, 10, 15, 1));
        favorites.add(new Favorite("TestBreakfast", 400, 7, 2, 8, 7));
        favorites.add(new Favorite("TestSandwich", 300, 4, 2, 5, 2));
        favorites.add(new Favorite("TestApple", 80, 1, 0, 15, 9));
        
        List<Favorite> expectedSorted = new ArrayList<>();
        expectedSorted.add(new Favorite("TestSnack", 200, 4, 2, 5, 10));
        expectedSorted.add(new Favorite("TestApple", 80, 1, 0, 15, 9));
        expectedSorted.add(new Favorite("TestBreakfast", 400, 7, 2, 8, 7));
        expectedSorted.add(new Favorite("TestLunch", 500, 20, 10, 10, 5));
        expectedSorted.add(new Favorite("TestBanana", 100, 1, 0, 20, 3));
        expectedSorted.add(new Favorite("TestSandwich", 300, 4, 2, 5, 2));
        expectedSorted.add(new Favorite("TestDinner", 800, 30, 10, 15, 1));
        
        List<Favorite> sorted;

        FavoriteTrie fTrie = new FavoriteTrie(favorites);

        assertEquals(7, fTrie.size());

        sorted = fTrie.sortedFavoritesWithPrefix("Test");

        assertEquals(expectedSorted, sorted);

        sorted = fTrie.sortedFavoritesWithPrefix("TestLu");

        assertEquals(1, sorted.size());

        sorted = fTrie.sortedFavoritesWithPrefix("TestB");

        assertEquals(2, sorted.size());

        List<Favorite> testBExpected = new ArrayList<>();
        testBExpected.add(new Favorite("TestBreakfast", 400, 7, 2, 8, 7));
        testBExpected.add(new Favorite("TestBanana", 100, 1, 0, 20, 3));

        assertEquals(testBExpected, sorted);
    }
}
