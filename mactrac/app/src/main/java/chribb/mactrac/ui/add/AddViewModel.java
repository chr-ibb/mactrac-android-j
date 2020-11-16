package chribb.mactrac.ui.add;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;
import java.util.concurrent.Executors;

import chribb.mactrac.data.Favorite;
import chribb.mactrac.data.FavoriteDao;
import chribb.mactrac.data.FavoriteTrie;
import chribb.mactrac.data.Macro;
import chribb.mactrac.data.MacroRepository;


public class AddViewModel extends AndroidViewModel {
    private MacroRepository repo;

    private FavoriteTrie fTrie;
    private Favorite favorite;

    private int day;
    private int count;

    private String name;
    private Integer calories;
    private Integer protein;
    private Integer fat;
    private Integer carbs;


    public AddViewModel(Application application) {
        super(application);
        repo = new MacroRepository(application);
    }

    /* * * Repo Methods * * */
    void insertMacro(Integer day, String food, Integer calories,
                Integer protein, Integer fat, Integer carbs, int count) {
        repo.insert(new Macro(day, food, calories, protein, fat, carbs, count));
    }
    void insertFavorite(Favorite favorite) {
        addToTrie(favorite);
        repo.insertFavorite(favorite);
    }
    void deleteFavorite(Favorite deleteFavorite) {
        deleteFromTrie(deleteFavorite);
        repo.deleteFavorite(deleteFavorite.getName());
    }

    private int countFood(int day) {
        return repo.countFood(day);
    }

    void findCount() {
        //Queries Room for the number of macros already on this day, to use as position.
        Executors.newSingleThreadExecutor().execute(() -> count = countFood(day));
    }

    void getFavorite() {
        assert(name != null);
        if (fTrie.contains(name)) {
            Executors.newSingleThreadExecutor().execute(() -> favorite = repo.getFavorite(name));
        } else {
            favorite = null;
        }
    }

    boolean alreadyFavorite() {
        return favorite != null;
    }

    //TODO what is this? Is it trying to add a Macro or Favorite?
    public void addFood() {
        insertMacro(day, name, calories, protein, fat, carbs, count);
    }

    public void addFavorite(boolean checked) {
        //TODO reconsider whether it increments if you dont overwrite... or what it does
        // maybe only increment when you clicked on the suggestion before? not sure
        if (alreadyFavorite()) {
            if (checked) {
                Favorite toAdd = new Favorite(name, calories, protein, fat, carbs,
                        favorite.getCount() + 1);

                insertFavorite(toAdd);
            } else {
                Favorite toAdd = new Favorite(favorite.getName(), favorite.getCalories(),
                        favorite.getProtein(), favorite.getFat(), favorite.getCarbs(),
                        favorite.getCount() + 1);
                insertFavorite(toAdd);
            }
        } else if (checked) {
            Favorite toAdd = new Favorite(name, calories, protein, fat, carbs,1);
            insertFavorite(toAdd);
        }
    }


    //TODO doc string
    public void setNumbers(String calories, String protein, String fat, String carbs) {
        if (calories.isEmpty()) {
            setCalories(0);
        } else {
            setCalories(Integer.parseInt(calories));
        }

        if (protein.isEmpty()) {
            setProtein(0);
        } else {
            setProtein(Integer.parseInt(protein));
        }

        if (fat.isEmpty()) {
            setFat(0);
        } else {
            setFat(Integer.parseInt(fat));
        }

        if (carbs.isEmpty()) {
            setCarbs(0);
        } else {
            setCarbs(Integer.parseInt(carbs));
        }
    }
    public void setNumbers(Favorite favorite) {
        setCalories(favorite.getCalories());
        setProtein(favorite.getProtein());
        setFat(favorite.getFat());
        setCarbs(favorite.getCarbs());
    }

    /**
     * Only runs once on mainActivity being created. //TODO might have to move to mainActivity resume
     */
    public void loadFavoriteTrie() {
        Executors.newSingleThreadExecutor().execute(() -> fTrie = new FavoriteTrie(repo.loadFavorites()));
    }

    private void addToTrie(Favorite favorite) {
        fTrie.add(favorite);
    }
    public void deleteFromTrie(Favorite favorite) {
        //TODO go through code and find every time you need to call this
        fTrie.delete(favorite);
    }
    public List<Favorite> getSortedFavoritesWithPrefix(String prefix) {
        return fTrie.sortedFavoritesWithPrefix(prefix);
    }

    //TODO the number setters could maybe be private

    /* * * Getters/Setters * * */
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }

}
