package chribb.mactrac.data;

import java.util.ArrayList;
import java.util.List;

public class FavoriteTrie {

    //TODO need method to remove a Favorite
    // as of now, I think the Trie will be stored in the AddViewModel. I'll initiallize the creation
    // of the Trie in mainactivity. ill use an executor.

    private TrieNode root;
    private int size;

    public FavoriteTrie() {
        root = new TrieNode('!');
        size = 0;
    }

    public FavoriteTrie(List<Favorite> favorites) {
        this();
        for (int i = 0; i < favorites.size(); i++) {
            Favorite f = favorites.get(i);
            add(f);
        }
    }

    public void clear() {
        root = new TrieNode('!');
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean contains(String key) {
        TrieNode n = root;
        for(int i = 0; i <= key.length(); i++) {
            if(i == key.length()) {
                return n.isKey;
            }

            TrieNode nextN = n.next[key.charAt(i)];
            if (nextN == null) {
                break;
            }
            n = nextN;
        }
        return false;
    }

    public void add(Favorite favorite) {
        String key = favorite.getName();
        TrieNode n = root;

        int i = 0;
        //progress through Trie to the longest existing prefix of KEY
        while (i < key.length()) {
            TrieNode nextN = n.next[key.charAt(i)];
            if (nextN == null) {
                break;
            }
            n = nextN;
            i++;
        }

        //If the whole word was already in here, set isKey to true and save the favorite.
        //This either overwrites an existing key, or adds a key to a prefix of an existing key,
        //i.e. if you had "hamburger" and then you added "ham."
        if (i == key.length()) {
            if (!n.isKey) {
                size++;
                n.isKey = true;
            }
            n.keyValue = favorite;
            return;
        }

        //if you've still got more of the Key left, keep adding each character to the Trie until
        //you get to the end, where you save it as a Key and save the Favorite.
        while (i < key.length()) {
            TrieNode nextN;
            if (i == key.length() - 1) {
                nextN = new TrieNode(key.charAt(i), favorite);
                size++;
            } else {
                nextN = new TrieNode(key.charAt(i));
            }

            n.next[key.charAt(i)] = nextN;
            n = nextN;
            i++;
        }
    }

    public void delete(Favorite favorite) {
        String key = favorite.getName();
        TrieNode n = root;
        int i = 0;
        while (i < key.length()) {
            TrieNode nextN = n.next[key.charAt(i)];
            if (nextN == null) {
                break;
            }
            n = nextN;
            i++;
        }

        if (i == key.length() && n.isKey) {
            n.isKey = false;
            n.keyValue = null;
            size--;
        }
    }

    public List<Favorite> sortedFavoritesWithPrefix(String prefix) {
        List<Favorite> withPrefix = new ArrayList<>();

        TrieNode n = root;
        for (int i = 0; i < prefix.length(); i++) {
            TrieNode nextN = n.next[prefix.charAt(i)];
            if (nextN == null) {
                return withPrefix;
            }
            n = nextN;
        }
        addKeysWithPrefix(withPrefix, n);
        withPrefix.sort((f1, f2) -> f2.getCount() - f1.getCount()); //lambda for compare()
        return withPrefix;
    }

    private void addKeysWithPrefix(List<Favorite> l, TrieNode n) {
        if (n.isKey) {
            l.add(n.keyValue);
        }
        for (TrieNode nextN : n.next) {
            if (nextN == null) continue;
            addKeysWithPrefix(l, nextN);
        }
    }



    private static class TrieNode {
        char character;
        boolean isKey;
        TrieNode[] next;
        Favorite keyValue;

        TrieNode(Character character, Favorite keyValue) {
            this.character = character;
            this.isKey = true;
            next = new TrieNode[128];
            this.keyValue = keyValue;
        }

        TrieNode(Character character) {
            this.character = character;
            this.isKey = false;
            next = new TrieNode[128];
        }
    }
}
