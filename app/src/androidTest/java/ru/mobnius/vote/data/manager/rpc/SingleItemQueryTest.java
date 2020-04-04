package ru.mobnius.vote.data.manager.rpc;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobnius.vote.data.manager.synchronization.BaseSynchronization;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SingleItemQueryTest {

    @Test
    public void toJsonString() {
        SingleItemQuery item = new SingleItemQuery(new Path("readme.md", ".md"));
        String str = item.toJsonString();
        assertEquals(str, "{\"limit\":"+ BaseSynchronization.MAX_COUNT_IN_QUERY +",\"params\":[{\"extension\":\".md\",\"name\":\"readme.md\"}]}");
    }

    class Path {
        String name;
        String extension;

        public Path(String name, String extension) {
            this.name = name;
            this.extension = extension;
        }
    }
}
