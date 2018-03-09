package me.gavin;

import org.junit.Test;

import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void reference() {
        String abc = new String("abc");
        WeakReference<String> abcWeakRef = new WeakReference<>(abc);
        m(abcWeakRef.get());
        String z = abcWeakRef.get();
        abc = null;
        System.out.println("before gc: " + abcWeakRef.get());
        System.gc();
        System.out.println("after gc: " + abcWeakRef.get());
    }

    private void m(String s) {
        String str = s;
        System.out.println("??? : " + str);
    }

    @Test
    public void integer() {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE + 1);
        System.out.println(Integer.MAX_VALUE + 2);
    }

    @Test
    public void soft() {
        ArrayList<String> newArray = new ArrayList<>();
        newArray.add("汽车");
        newArray.add("ab12");
        newArray.add("ab21");
        newArray.add("公安");
        newArray.add("怡");
        newArray.add("张新");
        newArray.add("广州");
        newArray.add("test");
        newArray.add("pp");
        newArray.add("？23");
        newArray.add(".23");

        Collator col = Collator.getInstance(Locale.CHINESE);
        Collections.sort(newArray, col);

        for (String i : newArray) {
            System.out.print(i + " ");
        }
    }

    @Test
    public void soft2() {
        ArrayList<Long> list = new ArrayList<>();
        list.add(0L);
        list.add(100L);
        list.add(30L);
        list.add(50L);
        Collections.sort(list, (o1, o2) -> o1 > o2 ? -1 : 1);
        for (Long t : list) {
            System.out.println(t);
        }
    }

    @Test
    public void test2() {
        Observable.create((ObservableOnSubscribe<String>) e -> {
            System.out.println("subscribing");
            e.onError(new RuntimeException("always fails"));
        }).retryWhen(throwableObservable -> {
            //这里可以发送新的被观察者 Observable
            return throwableObservable.delay(2, TimeUnit.SECONDS).map(throwable -> 1);
        }).subscribe(System.out::println);
    }
}