package ru.mobnius.vote.data.manager;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ru.mobnius.vote.ui.model.FilterItem;
import ru.mobnius.vote.ui.model.PointItem;

import static org.junit.Assert.assertEquals;

public class SearchManagerTest {

    private List<People> mList;
    private String mKey = "test";
    private PeopleSearchManager mPeopleSearchManager;

    @Before
    public void setUp() {
        mList = new ArrayList<>();
        mList.add(new People("Саша", 32, true, 10000));
        mList.add(new People("Кирилл", 29, true, 8000));
        mList.add(new People("Маша", 27, false, 6000));
        mList.add(new People("Саша", 33, true, 12000));
        mList.add(new People("Саша", 35, true, 5000));
        mList.add(new People("Дима", 30, true, 4500));
        mList.add(new People("Игорь", 33, true, 8500));

        mPeopleSearchManager = new PeopleSearchManager(mKey);
        mPeopleSearchManager.addItem(new FilterItem("name", "са"));
        mPeopleSearchManager.addItem(new FilterItem("age", "5"));
    }

    @Test
    public void toFilters() {
        People[] people = mList.toArray(new People[0]);
        People[] results = mPeopleSearchManager.toFilters(people);
        assertEquals(results.length, 3);
    }

    class PeopleSearchManager extends SearchManager<People> {

        public PeopleSearchManager(String key) {
            super(key);
        }

        public PeopleSearchManager(String key, String deSerialize) {
            super(key, deSerialize);
        }

        @Override
        public People[] toFilters(People[] items) {
            List<People> results = new ArrayList<>();
            for(People item : items) {
                // тут специально используем сокращенный вариант
                if(isAppend(item)) {
                    results.add(item);
                }
            }
            return results.toArray(new People[0]);
        }
    }

    class People {
        public People() {}
        public People(String name, int age, boolean male, double money) {
            this.name = name;
            this.age = age;
            this.isMale = male;
            this.money = money;
        }
        public String name;
        public int age;
        public boolean isMale;
        public double money;
    }
}